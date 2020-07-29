package com.deng.order.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.deng.order.common.entity.TxLogES;
import com.deng.order.common.entity.EsEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * elasticsearch配置
 */
@Component
@RefreshScope
public class ElasticsearchConfig {
	@Value("${elasticsearch.ip}")
    public String host;
    @Value("${elasticsearch.port}")
    public int port;
    @Value("${elasticsearch.scheme}")
    public String scheme;
	@Value("${elasticsearch.username}")
	public String username;
	@Value("${elasticsearch.password}")
	public String password;
	 public static final String INDEX_NAME = "book-index";


	    public static RestHighLevelClient highLevelClient = null;
	    @Autowired
	    private ObjectMapper objectMapper;

	    @PostConstruct
	    public void init() {
	        try {
	        	if (highLevelClient != null) {
	        		highLevelClient.close();
	            }
				final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
				credentialsProvider.setCredentials(AuthScope.ANY,
						new UsernamePasswordCredentials(username, password));
	        	highLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, scheme))
						.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
							public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
								httpClientBuilder.disableAuthCaching();
								return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
							}
						}));
	            if (this.indexExist(INDEX_NAME)) {
	                return;
	            }
	            CreateIndexRequest request = new CreateIndexRequest(INDEX_NAME);
	            request.settings(Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 2));
	            request.source(objectMapper.writeValueAsString(new TxLogES()), XContentType.JSON);
	            CreateIndexResponse res = highLevelClient.indices().create(request, RequestOptions.DEFAULT);
	            if (!res.isAcknowledged()) {
	                throw new RuntimeException("初始化失败");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.exit(0);
	        }
	    }

	    /**
	     * Description: 判断某个index是否存在
	     *
	     * @param index index名
	     * @return boolean
	     * @author fanxb
	     * @date 2019/7/24 14:57
	     */
	    public boolean indexExist(String index) throws Exception {
	        GetIndexRequest request = new GetIndexRequest(index);
	        request.local(false);
	        request.humanReadable(true);
	        request.includeDefaults(false);
	        return highLevelClient.indices().exists(request, RequestOptions.DEFAULT);
	    }

	    /**
	     * Description: 插入/更新一条记录
	     *
	     * @param index  index
	     * @param entity 对象
	     * @author fanxb
	     * @date 2019/7/24 15:02
	     */
	    public void insertOrUpdateOne(String index, EsEntity<?> entity) {
	        IndexRequest request = new IndexRequest(index);
	        request.id(entity.getId());
	        request.source(JSON.toJSONString(entity.getData()), XContentType.JSON);
	        try {
	        	highLevelClient.index(request, RequestOptions.DEFAULT);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }

	    /**
	     * Description: 批量插入数据
	     *
	     * @param index index
	     * @param list  带插入列表
	     * @author fanxb
	     * @date 2019/7/24 17:38
	     */
	    public void insertBatch(String index, List<EsEntity> list) {
	        BulkRequest request = new BulkRequest();
	        list.forEach(item -> request.add(new IndexRequest(index).id(item.getId())
	                .source(JSON.toJSONString(item.getData()), XContentType.JSON)));
	        try {
	        	highLevelClient.bulk(request, RequestOptions.DEFAULT);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }

	    /**
	     * Description: 批量删除
	     *
	     * @param index  index
	     * @param idList 待删除列表
	     * @author fanxb
	     * @date 2019/7/25 14:24
	     */
	    public <T> void deleteBatch(String index, Collection<T> idList) {
	        BulkRequest request = new BulkRequest();
	        idList.forEach(item -> request.add(new DeleteRequest(index, item.toString())));
	        try {
	        	highLevelClient.bulk(request, RequestOptions.DEFAULT);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }

	    /**
	     * Description: 搜索
	     *
	     * @param index   index
	     * @param builder 查询参数
	     * @param c       结果类对象
	     * @return java.util.ArrayList
	     * @author fanxb
	     * @date 2019/7/25 13:46
	     */
	    public <T> List<T> search(String index, SearchSourceBuilder builder, Class<T> c) {
	        SearchRequest request = new SearchRequest(index);
	        request.source(builder);
	        try {
	            SearchResponse response = highLevelClient.search(request, RequestOptions.DEFAULT);
	            SearchHit[] hits = response.getHits().getHits();
	            List<T> res = new ArrayList<>(hits.length);
	            for (SearchHit hit : hits) {
	                res.add(JSON.parseObject(hit.getSourceAsString(), c));
	            }
	            return res;
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }

	    /**
	     * Description: 删除index
	     *
	     * @param index index
	     * @return void
	     * @author fanxb
	     * @date 2019/7/26 11:30
	     */
	    public void deleteIndex(String index) {
	        try {
	        	highLevelClient.indices().delete(new DeleteIndexRequest(index), RequestOptions.DEFAULT);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }

	    /**
	     * Description: delete by query
	     *
	     * @param index   index
	     * @param builder builder
	     * @author fanxb
	     * @date 2019/7/26 15:16
	     */
	    public void deleteByQuery(String index, QueryBuilder builder) {
	        DeleteByQueryRequest request = new DeleteByQueryRequest(index);
	        request.setQuery(builder);
	        //设置批量操作数量,最大为10000
	        request.setBatchSize(10000);
	        request.setConflicts("proceed");
	        try {
	        	highLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }


}
