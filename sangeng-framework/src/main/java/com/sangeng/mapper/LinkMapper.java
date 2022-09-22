package com.sangeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sangeng.domain.dto.LinkListDto;
import com.sangeng.domain.entity.Link;
import org.apache.ibatis.annotations.Param;


/**
 * 友链(Link)表数据库访问层
 *
 * @author makejava
 * @since 2022-02-13 10:30:28
 */
public interface LinkMapper extends BaseMapper<Link> {

    IPage<Link> pageLinkList(Page<Link> page,@Param("linkListDto") LinkListDto linkListDto);
}
