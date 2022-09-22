package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.TagListDto;
import com.sangeng.domain.entity.LoginUser;
import com.sangeng.domain.entity.Tag;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.domain.vo.TagVo;
import com.sangeng.mapper.TagMapper;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sangeng.service.TagService;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2022-08-09 11:30:58
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, String name) {
        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page);
        IPage<Tag> tagIPage = tagMapper.pageTagList(page,name);
        PageVo pageVo = new PageVo(tagIPage.getRecords(),tagIPage.getTotal());
          return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(Tag tag) {
        tag.setCreateTime(new Date());
        tag.setUpdateTime(new Date());
        Long id = SecurityUtils.getLoginUser().getUser().getId();
        tag.setCreateBy(id);
        tag.setUpdateBy(id);
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateTag(Tag tag) {
        TagMapper tagMapper = getBaseMapper();
        Long id = SecurityUtils.getLoginUser().getUser().getId();
        tag.setUpdateBy(id);
        tag.setUpdateTime(new Date());
        tagMapper.updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId, Tag::getName);
        List<Tag> tags = list(queryWrapper);
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(tags,TagVo.class);
        return ResponseResult.okResult(tagVos);

    }

    @Override
    public ResponseResult deletesTag(String ids) {
        String[] idList = ids.split(",");
        List<Long> list = new ArrayList<>();
        for (String s : idList) {
            list.add(Long.parseLong(s));
        }
        removeByIds(list);
        return ResponseResult.okResult();
    }
}
