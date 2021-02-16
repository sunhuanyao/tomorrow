package com.sun.tomorrow.core.tool.elasticsearch;

import com.sun.tomorrow.core.tool.config.JsonConfigReader;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.rest.RestStatus;

import java.io.IOException;
import java.util.*;

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

    public static boolean insert(RestHighLevelClient client, String index, Map<String, Object> data) throws IOException {
        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.source(data);
        IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
        return response.status().equals(RestStatus.CREATED);
    }

    public static String update(RestHighLevelClient client, String index, String id, Map<String, Object> updateData) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(index, id);
        updateRequest.doc(updateData);
        updateRequest.upsert(new IndexRequest(index).source(updateData));
        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
//        return updateResponse.getResult().name().equals(DocWriteResponse.Result.UPDATED.name());
        return updateResponse.getResult().name();
    }

    public static void main(String[] args) throws IOException {
//        System.out.println(getInstance().getClient().indices().exists(new GetIndexRequest("test_1"), RequestOptions.DEFAULT));
        Map<String, Object> map = new HashMap<>();
        map.put("id", UUID.randomUUID().toString());
        map.put("ip", "1.1.1.2");
        map.put("createTime", System.currentTimeMillis());
        map.put("content", "123");
        map.put("updateTime", System.currentTimeMillis());
        System.out.println(update(getInstance().getClient(), "test_1", "RYVtpXcBogu28grejFr4", map));
    }


}
