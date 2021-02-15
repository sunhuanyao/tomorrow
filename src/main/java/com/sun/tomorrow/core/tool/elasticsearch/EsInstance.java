package com.sun.tomorrow.core.tool.elasticsearch;

import com.sun.tomorrow.core.tool.config.JsonConfigReader;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;

import java.io.IOException;

@Getter
public class EsInstance {

    private RestHighLevelClient client;

    private EsInstance() {
        init();
    }

    public static EsInstance getInstance () {
        return Holder.instance;
    }

    private void init() {
        EsEntity esEntity = JsonConfigReader.getInstance().getObject(EsEntity.class, "elasticsearch");
        this.client =  new RestHighLevelClient(RestClient.builder(new HttpHost(esEntity.host, esEntity.port, esEntity.ssl)));
    }

    private static class Holder {
        public static final EsInstance instance = new EsInstance();
    }

    @Getter
    @Setter
    @ToString
    public static class EsEntity {
        private String host;
        private int port;
        private String ssl;
    }

    public static boolean isIndexExists(RestHighLevelClient client, String indexName) throws IOException {
        GetIndexRequest indexRequest = new GetIndexRequest(indexName);
        return client.indices().exists(indexRequest, RequestOptions.DEFAULT);
    }

    public static void main(String[] args) throws IOException {
        System.out.println(getInstance().getClient().indices().exists(new GetIndexRequest("test_1"), RequestOptions.DEFAULT));
    }


}
