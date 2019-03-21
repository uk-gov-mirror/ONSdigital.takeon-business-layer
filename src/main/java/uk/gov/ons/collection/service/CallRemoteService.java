package uk.gov.ons.collection.service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class CallRemoteService {

    private final String algorithim;
    private final String payload;
    private HttpPost postMethod;
    private StringEntity body;
    CloseableHttpClient client = HttpClients.createDefault();
    CloseableHttpResponse response;

    public CallRemoteService(String uri, String content){
        algorithim = uri;
        payload = content;
        body = new StringEntity(payload, ContentType.APPLICATION_JSON );
        postMethod = new HttpPost(algorithim);

    }

    public String callAlgoService() {
        postMethod.setEntity(body);
        postMethod.setHeader("Authorization", System.getenv("ALGO_AUTH"));
        System.out.println(System.getenv("ALGO_AUTH"));
        try {
            CloseableHttpResponse response = client.execute(postMethod);
            return response.toString();
        } catch (IOException e) {
            return e.toString();
        }
    }

    public void callLambda(){
        postMethod.setEntity(body);
        postMethod.setHeader("Content-Type", "application/json");
        System.out.println(System.getenv("ALGO_AUTH"));
        try {
            response = client.execute(postMethod);
        } catch (IOException e) {

        }
    }

    public String getResponse(){
        try {
            return EntityUtils.toString(response.getEntity());
        }
        catch (Exception e){

        }
        return "";
    }
}
