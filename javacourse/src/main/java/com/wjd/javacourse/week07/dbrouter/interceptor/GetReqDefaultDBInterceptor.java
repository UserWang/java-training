package com.wjd.javacourse.week07.dbrouter.interceptor;

import com.wjd.javacourse.week07.dbrouter.DbContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class GetReqDefaultDBInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetReqDefaultDBInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        try {
            RequestAttributes ra = RequestContextHolder.currentRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            HttpServletRequest request = sra.getRequest();
            String method = request.getMethod();
            if("GET".equalsIgnoreCase(method)){
                DbContextHolder.setDbType(DbContextHolder.DbType.SLAVE);
            }
        } catch (IllegalStateException e) {
            LOGGER.info("GetReqDefaultDBInterceptor.preHandle() error,get请求读从库失败");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        DbContextHolder.clearDbType();
    }
}
