package com.central.canal.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import com.central.canal.page.Page;
import com.central.canal.repository.UserRepository;
import com.central.canal.service.SearchService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService{
	
	@Resource
    private ElasticsearchTemplate elasticsearchTemplate;
	@Autowired
	private UserRepository userRepository;

	@Override
	public Object searchManyIndex(Map<String,String> map) {
		
		String keyword = map.get("keyword");
		String fieldNames = map.get("fieldNames");
		String indexName = map.get("indexName");
		int pageNo = Integer.parseInt(map.get("pageNo"));
		int pageSize = Integer.parseInt(map.get("pageSize"));
		
		 // 构造查询条件,使用标准分词器.
        QueryBuilder matchQuery = createQueryBuilder(keyword,fieldNames);

        // 设置高亮,使用默认的highlighter高亮器
        HighlightBuilder highlightBuilder = createHighlightBuilder(fieldNames);

        // 设置查询字段
        SearchResponse response = elasticsearchTemplate.getClient().prepareSearch(indexName)
                .setQuery(matchQuery)
                .highlighter(highlightBuilder)
                .setFrom((pageNo-1) * pageSize)
                .setSize(pageNo * pageSize) // 设置一次返回的文档数量，最大值：10000
                .get();

        // 返回搜索结果
        SearchHits hits = response.getHits();

        Long totalCount = hits.getTotalHits();
        Page<Map<String, Object>> page = new Page<>(pageNo,pageSize,totalCount.intValue());
        page.setList(getHitList(hits));
        return page;
		
	}
	
	
	  /**
     * 处理高亮结果
     * @auther: zhoudong
     * @date: 2018/12/18 10:48
     */
    private List<Map<String,Object>> getHitList(SearchHits hits){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map;
        for(SearchHit searchHit : hits){
            map = new HashMap<>();
            // 处理源数据
            map.put("source",searchHit.getSourceAsMap());
            // 处理高亮数据
            Map<String,Object> hitMap = new HashMap<>();
            searchHit.getHighlightFields().forEach((k,v) -> {
                String hight = "";
                for(Text text : v.getFragments()) hight += text.string();
                hitMap.put(v.getName(),hight);
            });
            map.put("highlight",hitMap);
            list.add(map);
        }
        return list;
    }
	
    /**
     * 构造高亮器
     * @auther: zhoudong
     * @date: 2018/12/18 10:44
     */
    private HighlightBuilder createHighlightBuilder(String... fieldNames){
        // 设置高亮,使用默认的highlighter高亮器
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                // .field("productName")
                .preTags("<span style='color:red'>")
                .postTags("</span>");

        // 设置高亮字段
        for (String fieldName: fieldNames) highlightBuilder.field(fieldName);

        return highlightBuilder;
    }

	private QueryBuilder createQueryBuilder(String keyword, String... fieldNames){
	    // 构造查询条件,使用标准分词器.
	    return QueryBuilders.multiMatchQuery(keyword,fieldNames)   // matchQuery(),单字段搜索
	                .analyzer("ik_max_word")
	                .operator(Operator.OR);
	}


	@Override
	public Object hightSearchIndex(Map<String, String> map) {
		int pageNum = Integer.parseInt(map.get("pageNo"));
		int pageSize = Integer.parseInt(map.get("pageSize"));
		String keywords = map.get("keyword");
		String describle = map.get("describle");
		
		int sort = 2;
		
		
		Pageable pageable = PageRequest.of(pageNum-1, pageSize);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //分页
        nativeSearchQueryBuilder.withPageable(pageable);
        //跨索引查询！！！！ 特别注意，跨索引情况下，只能使用template查询，不能使用resposite。对应字段需根据特定业务定义
        //nativeSearchQueryBuilder.withIndices("canal-role","canal-user");
        //跨索引使用同配符
        nativeSearchQueryBuilder.withIndices("canal-*");
        //nativeSearchQueryBuilder.withIndices("canal-user");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //模糊查询--分词查询
        //boolQueryBuilder.must(QueryBuilders.matchQuery("name", keywords));
        //精准查询
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("name", keywords));
        
        //将查询条件加入builder
        nativeSearchQueryBuilder.withFilter(boolQueryBuilder);
		
        //搜索权重逻辑
        if (StringUtils.isEmpty(keywords)) {
            nativeSearchQueryBuilder.withQuery(QueryBuilders.matchAllQuery());
        } else {
            List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctionBuilders = new ArrayList<>();
            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("name", keywords),
                    ScoreFunctionBuilders.weightFactorFunction(10)));
            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("remark", keywords),
                    ScoreFunctionBuilders.weightFactorFunction(5)));
            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("age", keywords),
                    ScoreFunctionBuilders.weightFactorFunction(2)));
            FunctionScoreQueryBuilder.FilterFunctionBuilder[] builders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[filterFunctionBuilders.size()];
            filterFunctionBuilders.toArray(builders);
            FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(builders)
                    .scoreMode(FunctionScoreQuery.ScoreMode.SUM)//scoreMode为定义上面计算结果的一个处理：比如这里的计算和，还有取平均值。
                    .setMinScore(2);
            nativeSearchQueryBuilder.withQuery(functionScoreQueryBuilder);
        }
        
        if(sort==1){
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC));
        }else if(sort==2){
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("name").order(SortOrder.DESC));
        }else{
            //按相关度
            nativeSearchQueryBuilder.withSort(SortBuilders.scoreSort().order(SortOrder.DESC));
        }
        
        
        nativeSearchQueryBuilder.withSort(SortBuilders.scoreSort().order(SortOrder.DESC));
        
        NativeSearchQuery searchQuery = nativeSearchQueryBuilder.build();
        
        //使用respsitory查询示例，对于多个索引情况，如上所示：respository不适合
        //Object obj = userRepository.search(searchQuery);
        
        return elasticsearchTemplate.query(searchQuery, response -> {
            SearchHits hits = response.getHits();
            List<String> result = new ArrayList<>();
            Arrays.stream(hits.getHits()).forEach(h -> {
                Map<String, Object> source = h.getSourceAsMap();
                //{name=商品哈哈, remark=手机好看, id=2, age=33}
                String resultId = String.valueOf(source.getOrDefault("id","0"));
                String name = String.valueOf(source.getOrDefault("name","0"));
                result.add(resultId+"_"+name);
                //String resultId = String.valueOf(source.getOrDefault("id", null));
                log.info("id结果为:{}",resultId);
            });
            return result;
        });
//        
//        log.info("DSL:{}", searchQuery.getQuery().toString());
//        return productRepository.search(searchQuery);
		
	}
	
	
}
