package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddArticleDto;
import com.sangeng.domain.dto.ArticleListDto;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.entity.ArticleTag;
import com.sangeng.domain.entity.Category;
import com.sangeng.domain.vo.ArticleDetailVo;
import com.sangeng.domain.vo.ArticleListVo;
import com.sangeng.domain.vo.HotArticleVo;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.mapper.ArticleMapper;
import com.sangeng.service.ArticleService;
import com.sangeng.service.ArticleTagService;
import com.sangeng.service.CategoryService;
import com.sangeng.service.TagService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;


    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleService articleService;




    @Override
    public ResponseResult hotArticleList() {
        updateViewCount();
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);


        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.VIEW_COUNT);
        queryWrapper.orderByDesc(Article::getViewCount);
        Page<Article> page=new Page(SystemConstants.PAGE_NUM,SystemConstants.PAGE_SIZE);
        page(page,queryWrapper);
        List<Article> articles=page.getRecords();
//        List<HotArticleVo> articleVos=new ArrayList<>();
//        for (Article article:articles){
//            HotArticleVo vo=new HotArticleVo();
//            BeanUtils.copyProperties(article,vo);
//            articleVos.add(vo);
//        }

        List<HotArticleVo> vs = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(vs);
    }


    /**
     * 更新浏览量
     */
    private void updateViewCount(){
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.VIEW_COUNT);
        List<Article> articles=viewCountMap.entrySet()
                .stream()
                .map(entry-> new Article(Long.valueOf(entry.getKey()),Long.valueOf(entry.getValue())))
                .collect(Collectors.toList());

        articleService.updateBatchById(articles);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);

        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);
        lambdaQueryWrapper.orderByDesc(Article::getCreateTime);


        Page<Article> page=new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);

        List<Article> articles=page.getRecords();
        //stream流的查找
//        articles.stream()
//                .map(new Function<Article, Article>() {
//                    @Override
//                    public Article apply(Article article) {
//
//                        return article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());
//
//                    }
//                });
        //for循环得查找
//        for (Article article : articles) {
//            Category category=categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }

        articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());

        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        for (ArticleListVo articleListVo : articleListVos) {
            Integer viewCount = redisCache.getCacheMapValue(SystemConstants.VIEW_COUNT, articleListVo.getId().toString());
            articleListVo.setViewCount(viewCount.longValue());
        }
        PageVo pageVo=new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        Article article = getById(id);
        Integer viewCount=redisCache.getCacheMapValue(SystemConstants.VIEW_COUNT,id.toString());
        article.setViewCount(viewCount.longValue());

        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        Long categoryId = articleDetailVo.getCategoryId();
        Category category=categoryService.getById(categoryId);
        if (category!=null){
            articleDetailVo.setCategoryName(category.getName());
        }

        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue(SystemConstants.VIEW_COUNT,id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult pageArticleList(Integer pageNum, Integer pageSize, ArticleListDto articleListDto) {
        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page);
        IPage<Article> articleIPage = articleMapper.pageArticleList(page,articleListDto);
        PageVo pageVo = new PageVo(articleIPage.getRecords(),articleIPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    @Transactional
    public ResponseResult add(AddArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto,Article.class);
        //TODO 更新redis上的数据
//        Map<String, Integer> cacheMap = redisCache.getCacheMap(SystemConstants.VIEW_COUNT);
//        cacheMap.put(article.getId().toString(),0);
//        redisCache.setCacheObject(SystemConstants.VIEW_COUNT,cacheMap);

        save(article);
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(),tagId))
                .collect(Collectors.toList());

        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult getArticleById(Long id) {
        Article article = getById(id);
        AddArticleDto addArticleDto = BeanCopyUtils.copyBean(article, AddArticleDto.class);
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> articleTags = articleTagService.list(queryWrapper);
        List<Long> collect = articleTags.stream()
                .map(articleTag -> articleTag.getTagId())
                .collect(Collectors.toList());
        addArticleDto.setTags(collect);
        return ResponseResult.okResult(addArticleDto);


    }
}
