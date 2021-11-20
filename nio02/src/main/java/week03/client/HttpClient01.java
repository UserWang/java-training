package week03.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/11/13
 */
@Slf4j
public class HttpClient01 {

    public static void main(String[] args) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8801");
        HttpResponse response = client.execute(httpGet);
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity httpEntity = response.getEntity();
            String data = EntityUtils.toString(httpEntity, "UTF-8");
            log.info("code:{}",response.getStatusLine().getStatusCode());
            log.info("data:{}",data);
        }else {
            log.info("系统异常，状态码：{}",response.getStatusLine().getStatusCode());
        }
    }
}
