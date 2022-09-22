package com.sangeng;

import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.entity.UserRole;
import com.sangeng.service.ArticleService;
import com.sangeng.service.UserRoleService;
import com.sangeng.utils.RedisCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
public class MyTest {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisCache redisCache;

    @Test
    public void getArticleTest(){
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.VIEW_COUNT);
        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        //更新到数据库中
        articleService.updateBatchById(articles);
    }

    @Test
    public void t1(){
        Article article = new Article();
        article.setId(1L);
        article.setStatus("1");
        articleService.updateById(article);
        System.out.println("success");
    }

    @Autowired
    private UserRoleService userRoleService;

    @Test
    void printTest(){
        UserRole userRole = userRoleService.getById(14787164048664L);
        Long roleId = userRole.getRoleId();
        System.out.println(roleId);
    }

}
