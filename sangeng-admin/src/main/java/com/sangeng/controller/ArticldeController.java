package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddArticleDto;
import com.sangeng.domain.dto.ArticleListDto;
import com.sangeng.service.ArticleService;
import com.sangeng.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticldeController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisCache redisCache;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, ArticleListDto articleListDto){
        return articleService.pageArticleList(pageNum,pageSize,articleListDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleById(@PathVariable("id") Long id){
        return articleService.getArticleById(id);
    }

    /**
     * 更新文章
     * @param article
     * @return
     */
    @PutMapping
    public ResponseResult updateArticle(@RequestBody AddArticleDto article){
        return articleService.add(article);
    }

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article){
        return articleService.add(article);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteArticleZById(@PathVariable("id") Long id){
        if (articleService.removeById(id) && redisCache.deleteObject(id.toString())){
            return ResponseResult.okResult();
        }
        return ResponseResult.okResult();
    }
}
