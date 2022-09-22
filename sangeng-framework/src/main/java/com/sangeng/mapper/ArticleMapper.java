package com.sangeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sangeng.domain.dto.ArticleListDto;
import com.sangeng.domain.entity.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {
    void updateArt(Article article);

    IPage<Article> pageArticleList(Page<Article> page, @Param("articleListDto") ArticleListDto articleListDto);
}
