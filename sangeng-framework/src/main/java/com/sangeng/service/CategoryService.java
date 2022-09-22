package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.CategoryListDto;
import com.sangeng.domain.dto.CategoryStatusDto;
import com.sangeng.domain.entity.Category;
import com.sangeng.domain.vo.CategoryVo;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-02-12 12:12:30
 */
public interface CategoryService extends IService<Category> {


    List<CategoryVo> listAllCategory();

    ResponseResult getCategoryList();

    ResponseResult pageCategoryList(Integer pageNum, Integer pageSize,CategoryListDto categoryListDto);

    ResponseResult updateCategory(Category category);

    ResponseResult addCategory(Category category);

    ResponseResult changeStatus(CategoryStatusDto categoryStatusDto);
}

