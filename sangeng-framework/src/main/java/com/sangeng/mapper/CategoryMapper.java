package com.sangeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sangeng.domain.dto.CategoryListDto;
import com.sangeng.domain.entity.Category;
import org.apache.ibatis.annotations.Param;


/**
 * 分类表(Category)表数据库访问层
 *
 * @author makejava
 * @since 2022-02-12 12:12:28
 */
public interface CategoryMapper extends BaseMapper<Category> {

    IPage<Category> pageCategoryList(Page<Category> page, @Param("categoryListDto") CategoryListDto categoryListDto);
}
