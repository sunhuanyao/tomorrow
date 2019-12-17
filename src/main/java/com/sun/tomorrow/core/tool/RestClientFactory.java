package com.sun.tomorrow.core.tool;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sun.tomorrow.core.util.SafeGson;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @Author roger sun
 * @Date 2019/12/17 18:14
 */
public class RestClientFactory {

    private RestClient client;

    private String url;

    public RestClientFactory(String prefixUrl, String suffixUrl){
        this.client = getClient(prefixUrl, -1, "https");
        this.url = suffixUrl;
    }
    public RestClientFactory(String prefixUrl, String suffixUrl, int port){
        this.client = getClient(prefixUrl, port, "https");
        this.url = suffixUrl;
    }
    public RestClientFactory(String prefixUrl, String suffixUrl, boolean ssl){
        this.client = getClient(prefixUrl, -1, ssl? "https":"http");
        this.url = suffixUrl;
    }
    public RestClientFactory(String prefixUrl, String suffixUrl, int port, boolean ssl){
        this.client = getClient(prefixUrl, port, ssl? "https":"http");
        this.url = suffixUrl;
    }

    private RestClient getClient(String url, int port, String ssl){

        RestClientBuilder clientBuilder;

        clientBuilder = RestClient.builder(
                new HttpHost(url, port, ssl)
        );
        return clientBuilder.build();

    }


    public Object postData(String data) throws Exception{
        Request r = new Request(
                "POST",
                url
        );

        r.setJsonEntity(data);
        Response response = this.client.performRequest(r);

        InputStream in = response.getEntity().getContent();
        this.client.close();
        return readJsonStream(in);
    }
    private Gson gson = new Gson();
    public Object readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "utf-8"));

        Object message = gson.fromJson(reader, Map.class);
        reader.close();
        return message;
    }

    public Map<String, Object> parseResponse(String result) throws Exception{
        return SafeGson.fromJsonToMap(result);
    }


    public RestClient getClient() {
        return client;
    }

    public void setClient(RestClient client) {
        this.client = client;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
