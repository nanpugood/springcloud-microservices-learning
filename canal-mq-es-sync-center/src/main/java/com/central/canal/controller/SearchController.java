package com.central.canal.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.central.canal.service.SearchService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description ES查询测试
 * @author weicl
 * @date 2021年3月12日
 */
@Slf4j
@RestController
@RequestMapping("/esSearch")
public class SearchController {
	
	@Autowired	
	private SearchService searchService;


	/**
	 * 高级查询操作--包括排序、筛选等
	 * @return
	 */
	@GetMapping(value = "/search/hightSearchIndex")
    public Object hightSearchIndex(@RequestParam String keyword,@RequestParam String fieldName) {
		
		Map<String,String> map = new HashMap<String,String>();
//		map.put("keyword","商品");		
//		map.put("fieldNames","name");
		
		map.put("keyword",keyword);		
		map.put("fieldNames",fieldName);
		map.put("indexName","canal-user");
		map.put("pageNo","1");
		map.put("pageSize","11");
		
        return searchService.hightSearchIndex(map);
    }
	
	
	
	/**
	 * 常见查询操作
	 * @return
	 */
	@GetMapping(value = "/search/searchManyIndex")
    public Object searchManyIndex() {
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("keyword","商品");		
		map.put("fieldNames","name");
		map.put("indexName","canal-user");
		map.put("pageNo","1");
		map.put("pageSize","11");
		
        return searchService.searchManyIndex(map);
    }
	
	
}
