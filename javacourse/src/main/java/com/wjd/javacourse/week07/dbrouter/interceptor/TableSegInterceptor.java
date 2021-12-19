package com.wjd.javacourse.week07.dbrouter.interceptor;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import com.alibaba.druid.util.JdbcConstants;
import com.wjd.javacourse.week07.dbrouter.annotation.TableSeg;
import com.wjd.javacourse.week07.dbrouter.enums.TableShardDisposeType;
import com.wjd.javacourse.week07.dbrouter.enums.TableShardStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
@Slf4j
@Component
public class TableSegInterceptor implements Interceptor {

    private final static String BOUNDSQL_SQL_NAME = "delegate.boundSql.sql";

    private final static String BOUNDSQL_NAME = "delegate.boundSql";

    private final static String MAPPEDSTATEMENT_NAME = "delegate.mappedStatement";

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();

    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

    private final static ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

            //全局操作对象
            MetaObject metaObject = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY,
                    DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);

            //获取原始sql
            String originalSql = (String) metaObject.getValue(BOUNDSQL_SQL_NAME);

            //这两个对象都是获取mapper的参数的
            MappedStatement mappedStatement = (MappedStatement) metaObject.getValue(MAPPEDSTATEMENT_NAME);
            BoundSql boundSql = (BoundSql) metaObject.getValue(BOUNDSQL_NAME);

            if (StringUtils.isNotEmpty(originalSql)) {
                String id = mappedStatement.getId();
                String className = id.substring(0, id.lastIndexOf("."));
                Class<?> classObj = Class.forName(className);
                TableSeg tableSeg = classObj.getAnnotation(TableSeg.class);

                if (tableSeg != null) {
                    Map<String, Object> parameter = getParameterFromMappedStatement(mappedStatement, boundSql);

                    String regex = ".*_[0-9].*";
                    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                    Matcher matcher = pattern.matcher(originalSql);
                    if (!matcher.matches()) {
                        shardTable(metaObject, parameter, tableSeg, originalSql);
                    }

                }
            }
        } catch (Exception e) {
            log.error("TableSegInterceptor:" + e.getMessage());
        }

        return invocation.proceed();
    }

    /**
     * 获取参数
     *
     * @param ms
     * @param boundSql
     * @return
     */
    private Map<String, Object> getParameterFromMappedStatement(MappedStatement ms, BoundSql boundSql) {

        Map<String, Object> paramMap;

        Object parameterObject = boundSql.getParameterObject();

        if (parameterObject == null) {
            paramMap = new HashMap<String, Object>();
        } else {

            if (parameterObject instanceof Map) {
                Object param1 = ((Map) parameterObject).get("param1");
                if (param1 != null) {
                    parameterObject = param1;
                } else {
                    paramMap = new HashMap<String, Object>();
                    paramMap.putAll((Map) parameterObject);
                }
            }

            paramMap = new HashMap<String, Object>();
            boolean hasTypeHandler = ms.getConfiguration().getTypeHandlerRegistry()
                    .hasTypeHandler(parameterObject.getClass());

            MetaObject metaObject = SystemMetaObject.forObject(parameterObject);
            if (!hasTypeHandler) {
                for (String name : metaObject.getGetterNames()) {
                    paramMap.put(name, metaObject.getValue(name));
                }
            }
            //下面这段方法，主要解决一个常见类型的参数时的问题
            if (boundSql.getParameterMappings() != null && boundSql.getParameterMappings().size() > 0) {
                for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
                    String name = parameterMapping.getProperty();
                    if (paramMap.get(name) == null) {
                        if (hasTypeHandler || parameterMapping.getJavaType().equals(parameterObject.getClass())) {
                            paramMap.put(name, parameterObject);
                            break;
                        }
                    }
                }
            }
        }

        return paramMap;
    }

    /**
     * 分表操作
     *
     * @param metaObject
     * @param tableSeg
     * @param originalSql
     * @throws Exception
     */
    private void shardTable(MetaObject metaObject, Map<String, Object> parameter,
                            TableSeg tableSeg, String originalSql) throws Exception {
        MySqlStatementParser parser = new MySqlStatementParser(originalSql);
        SQLStatement statement = parser.parseStatement();

        StringBuilder newSql = new StringBuilder();
        SQLASTOutputVisitor visitor = SQLUtils.createOutputVisitor(newSql, JdbcConstants.MYSQL);

        Map<String, String> oldTableNewTableNameMap = getShardTableName(tableSeg, parameter);

        if (!oldTableNewTableNameMap.isEmpty()) {
            for (Map.Entry<String, String> entry : oldTableNewTableNameMap.entrySet()) {
                // 增加旧标明和新表名映射关系
                visitor.addTableMapping(entry.getKey(), entry.getValue());
            }
        }

        statement.accept(visitor);
        //重新赋值新sql生效
        metaObject.setValue(BOUNDSQL_SQL_NAME, newSql.toString());
    }

    /**
     * 构造分表表名映射
     *
     * @param seg
     * @param parameter
     * @return
     */
    private Map<String, String> getShardTableName(TableSeg seg, Map<String, Object> parameter) throws Exception {

        TableShardStrategy tableShardStrategy = seg.shardBy();

        // 分表code
        String memberIdStr = null;
        if (tableShardStrategy.getShardCode() != null) {
            Object memberIdObjc = parameter.get(tableShardStrategy.getShardCode());
            if (memberIdObjc != null) {
                memberIdStr = memberIdObjc.toString();
            }
        }
        String memberIdBackupStr = null;
        if (tableShardStrategy.getSencondShardCode() != null) {
            Object memberIdBackupObjc = parameter.get(tableShardStrategy.getSencondShardCode());
            if (memberIdBackupObjc != null) {
                memberIdBackupStr = memberIdBackupObjc.toString();
            }
        }


        if (tableShardStrategy.getStrategy() == TableShardDisposeType.SingleParamSingleTail.getCode()
                || tableShardStrategy.getStrategy() == TableShardDisposeType.MutiParamSingleTail.getCode()
                || tableShardStrategy.getStrategy() == TableShardDisposeType.SingleParamMutiTail.getCode()
                || tableShardStrategy.getStrategy() == TableShardDisposeType.MutiParamMutiTail.getCode()) {
            if (memberIdStr == null && memberIdBackupStr == null) {
                throw new Exception("生成分表所需要的字段未添加");
            }

        }


        // 分表表名，可以针对多种类型做分表
        String[] toShardTableList = tableShardStrategy.getShardTableList();

        // 新老表名map，key:老表名  value：新表名
        Map<String, String> oldTableNewTableNameMap = new HashMap<>();
        String suffix = "";
        for (String toShardTable : toShardTableList) {
            if (tableShardStrategy.getStrategy() == TableShardDisposeType.MutiParamSingleTail.getCode()
                    || tableShardStrategy.getStrategy() == TableShardDisposeType.MutiParamMutiTail.getCode()) {
                if (memberIdStr != null) {
                    //取模
                    Long memberId = Long.valueOf(memberIdStr);
                    suffix = String.valueOf(memberId % seg.shardNum());
                } else if (memberIdBackupStr != null
                        && StringUtils.isNotBlank(memberIdBackupStr)
                        && memberIdBackupStr.length() >= 21) {
                    suffix = memberIdBackupStr.substring(2, 4);
                }
            } else if (tableShardStrategy.getStrategy() == TableShardDisposeType.SingleParamSingleTail.getCode()
                    || tableShardStrategy.getStrategy() == TableShardDisposeType.SingleParamMutiTail.getCode()) {
                if (memberIdStr != null) {
                    Long memberId = Long.valueOf(memberIdStr);
                    suffix = String.valueOf(memberId % seg.shardNum());
                }
            } else if (tableShardStrategy.getStrategy() == TableShardDisposeType.Date.getCode()) {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("yyyyMM");
                suffix = now.format(datePattern);
            }

            if (tableShardStrategy.getStrategy() == TableShardDisposeType.SingleParamMutiTail.getCode()
                    || tableShardStrategy.getStrategy() == TableShardDisposeType.MutiParamMutiTail.getCode()) {
                DecimalFormat df = new DecimalFormat("00");
                suffix = df.format(Long.valueOf(suffix));
            }

            StringBuilder shardTableName = new StringBuilder();
            //添加后缀
            oldTableNewTableNameMap.put(toShardTable, shardTableName.append(toShardTable).append("_").append(suffix).toString());
        }

        return oldTableNewTableNameMap;
    }


    @Override
    public Object plugin(Object target) {
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }

    public static void main(String[] args) {
        String content = "I am noob " +
                "from runoob. asd_asd_01 com.";

        String content1 = "I am noob " +
                "from runoob. asd_asd_1 com.";

        String content2 = "I am noob " +
                "from runoob. asd_asd_202101 com.";

        String content3 = "I am noob " +
                "from runoob. asd_asd_ acom.";

        String regex = ".*_[0-9].*";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);


        Matcher matcher = pattern.matcher(content);
        log.info("isMatch:" + matcher.matches());

        Matcher matcher1 = pattern.matcher(content1);
        log.info("isMatch1:" + matcher1.matches());

        Matcher matcher2 = pattern.matcher(content2);
        log.info("isMatch2:" + matcher2.matches());

        Matcher matcher3 = pattern.matcher(content3);
        log.info("isMatch3:" + matcher3.matches());

        String content4 = "select orders.uid,orders.channel_id channelId,user_apply_orders_06.sn\\n\" +\n" +
                "                \"        from (select uid,channel_id,sn from user_apply_orders_00 where status = '100'\\n\" +\n" +
                "                \"\\t\\t\\t\\tunion select uid,channel_id,sn from user_apply_orders_01 where status = '100'\\n\" +\n" +
                "                \"\\t\\t\\t\\tunion select uid,channel_id,sn from user_apply_orders_02 where status = '100'\\n\" +\n" +
                "                \"\\t\\t\\t\\tunion select uid,channel_id,sn from user_apply_orders_03 where status = '100'\\n\" +\n" +
                "                \"\\t\\t\\t\\tunion select uid,channel_id,sn from user_apply_orders_04 where status = '100'\\n\" +\n" +
                "                \"\\t\\t\\t\\tunion select uid,channel_id,sn from user_apply_orders_05 where status = '100'\\n\" +\n" +
                "                \"\\t\\t\\t\\tunion select uid,channel_id,sn from user_apply_orders_06 where status = '100'\\n\" +\n" +
                "                \"\\t\\t\\t\\tunion select uid,channel_id,sn from user_apply_orders_07 where status = '100'\\n\" +\n" +
                "                \"\\t\\t\\t\\tunion select uid,channel_id,sn from user_apply_orders_08 where status = '100'\\n\" +\n" +
                "                \"\\t\\t\\t\\tunion select uid,channel_id,sn from user_apply_orders_09 where status = '100') orders,order_status_flow_record flow\\n\" +\n" +
                "                \"\\twhere orders.sn = flow.sn and flow.status = '100' \\n\" +\n" +
                "                \"\\t  and flow.ctime between ? and ?";

        Matcher matcher4 = pattern.matcher(content4);

        log.info("isMatch4:" + matcher4.matches());


    }
}