package com.sangeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sangeng.domain.dto.TagListDto;
import com.sangeng.domain.entity.Tag;
import org.apache.ibatis.annotations.Param;


/**
 * 标签(Tag)表数据库访问层
 *
 * @author makejava
 * @since 2022-08-09 11:30:55
 */
public interface TagMapper extends BaseMapper<Tag> {

    IPage<Tag> pageTagList(Page<Tag> page, @Param("name1") String name1);
}
