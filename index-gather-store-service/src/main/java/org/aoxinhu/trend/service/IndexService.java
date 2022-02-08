package org.aoxinhu.trend.service;
 
import org.aoxinhu.trend.pojo.Index;
import org.aoxinhu.trend.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
@CacheConfig(cacheNames="indexes")
public class IndexService {
    private List<Index> indexes;
    @Autowired 
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "third_part_not_connected")
    @Cacheable(key="'all_codes'")
    public List<Index> fetch_indexes_from_third_part(){
        List<Map<?,?>> temp= restTemplate.getForObject("http://127.0.0.1:8090/indexes/codes.json",List.class);
        return map2Index(temp);
    }

    private List<Index> map2Index(List<Map<?,?>> temp) {
        List<Index> indexes = new ArrayList<>();
        for (Map map : temp) {
            String code = map.get("code").toString();
            String name = map.get("name").toString();
            Index index= new Index();
            index.setCode(code);
            index.setName(name);
            indexes.add(index);
        }
        return indexes;
    }

    public List<Index> third_part_not_connected(){
        System.out.println("third_part_not_connected()");
        Index index= new Index();
        index.setCode("000000");
        index.setName("invalid index code");
        return CollectionUtil.toList(index);
    }

    /**
     * refresh data in redis
     */
    @HystrixCommand(fallbackMethod = "third_part_not_connected")
    public List<Index> fresh() {
        /* re-fetch data and store into field indexes */
        indexes =fetch_indexes_from_third_part();
        IndexService indexService = SpringContextUtil.getBean(IndexService.class);
        /* clear old data in redis */
        indexService.remove();
        /* store new data into redis */
        return indexService.store();
    }

    /**
     * store data into redis
     */
    @Cacheable(key="'all_codes'")
    public List<Index> store(){
        return indexes;
    }

    /**
     * get data from redis
     */
    @Cacheable(key="'all_codes'")
    public List<Index> get(){
        return CollUtil.toList();
    }

    /**
     * remove all data in redis
     */
    @CacheEvict(allEntries=true)
	public void remove(){
	}
 
}