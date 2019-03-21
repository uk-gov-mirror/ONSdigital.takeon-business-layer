package uk.gov.ons.collection.service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class CallAlgorithmia {

    private final String algorithim;
    private final String payload;
    private HttpPost postMethod;
    private StringEntity body;
    CloseableHttpClient client = HttpClients.createDefault();

    public CallAlgorithmia(String uri, String content){
        algorithim = uri;
        payload = content;
        body = new StringEntity(payload, ContentType.APPLICATION_JSON );
        postMethod = new HttpPost(algorithim);

    }

    public String callService() {
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
}
