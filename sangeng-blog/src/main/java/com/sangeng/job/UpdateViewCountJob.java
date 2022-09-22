package com.sangeng.job;

import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.entity.Article;
import com.sangeng.mapper.ArticleMapper;
import com.sangeng.service.ArticleService;
import com.sangeng.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;



    //@Scheduled(cron = "0/5 * * * * ?")    //五秒更新一次
    @Scheduled(cron = "0 */2 * * * ?")  //两分钟更新一次
    public void updateViewCount(){
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.VIEW_COUNT);
        List<Article> articles=viewCountMap.entrySet()
                        .stream()
                                .map(entry-> new Article(Long.valueOf(entry.getKey()),Long.valueOf(entry.getValue())))
                                        .collect(Collectors.toList());

        articleService.updateBatchById(articles);
    }


}
