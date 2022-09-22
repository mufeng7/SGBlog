package com.sangeng.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.CategoryListDto;
import com.sangeng.domain.dto.CategoryStatusDto;
import com.sangeng.domain.dto.RoleStatusDto;
import com.sangeng.domain.entity.Category;
import com.sangeng.domain.entity.Tag;
import com.sangeng.domain.vo.CategoryVo;
import com.sangeng.domain.vo.ExcelCategoryVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.service.CategoryService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<CategoryVo> categoryVos = categoryService.listAllCategory();
        return ResponseResult.okResult(categoryVos);
    }

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto){
        return categoryService.pageCategoryList(pageNum,pageSize,categoryListDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteById(@PathVariable("id") Long id){
        if (categoryService.removeById(id)){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_FAIL);
    }

    @GetMapping("/{id}")
    public ResponseResult getCategory(@PathVariable("id") Long id){
        return ResponseResult.okResult(categoryService.getById(id));
    }

    @PutMapping("")
    public ResponseResult updateCategory(@RequestBody Category category){
        return categoryService.updateCategory(category);
    }

    @PostMapping("")
    public ResponseResult addCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }

    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            List<CategoryVo> categoryVos = categoryService.listAllCategory();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出").doWrite(excelCategoryVos);
        } catch (Exception e) {
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody CategoryStatusDto categoryStatusDto){
        return categoryService.changeStatus(categoryStatusDto);
    }

}
