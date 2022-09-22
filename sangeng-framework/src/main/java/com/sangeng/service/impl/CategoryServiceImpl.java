package com.sangeng.service.impl;

import com.alibaba.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.CategoryListDto;
import com.sangeng.domain.dto.CategoryStatusDto;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.entity.Category;
import com.sangeng.domain.vo.CategoryVo;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.mapper.CategoryMapper;
import com.sangeng.service.ArticleService;
import com.sangeng.service.CategoryService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2022-02-12 12:12:31
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus,SystemConstants.NORMAL);
        List<Category> list = list(queryWrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return categoryVos;
    }

    @Override
    public ResponseResult getCategoryList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(queryWrapper);
        Set<Long> idList = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());

        List<Category> categories = listByIds(idList);
        categories = categories.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());

        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult pageCategoryList(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto) {
        Page<Category> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page);
        IPage<Category> categoryIPage = categoryMapper.pageCategoryList(page,categoryListDto);
        PageVo pageVo = new PageVo(categoryIPage.getRecords(),categoryIPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult updateCategory(Category category) {
        Long id = SecurityUtils.getLoginUser().getUser().getId();
        category.setUpdateBy(id);
        category.setUpdateTime(new Date());
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addCategory(Category category) {
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());
        Long id = SecurityUtils.getLoginUser().getUser().getId();
        category.setCreateBy(id);
        category.setUpdateBy(id);
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeStatus(CategoryStatusDto categoryStatusDto) {
        Category category = getById(categoryStatusDto.getCategoryId());
        category.setStatus(category.getStatus());
        updateById(category);
        return ResponseResult.okResult();
    }
}
