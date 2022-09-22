package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.LinkListDto;
import com.sangeng.domain.entity.Link;
import com.sangeng.domain.vo.LinkVo;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.mapper.LinkMapper;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sangeng.service.LinkService;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2022-02-13 10:30:30
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Autowired
    private LinkMapper linkMapper;

    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.Link_STATUS_NORMAL);
        List<Link> links=list(queryWrapper);

        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult<PageVo> pageLinkList(Integer pageNum, Integer pageSize, LinkListDto linkListDto) {
        Page<Link> page = new Page<>(pageNum,pageSize);
        page(page);
        IPage<Link> linkIPage = linkMapper.pageLinkList(page,linkListDto);
        PageVo pageVo = new PageVo(linkIPage.getRecords(),linkIPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addLink(Link link) {
        Long id = SecurityUtils.getLoginUser().getUser().getId();
        link.setCreateBy(id);
        link.setUpdateBy(id);
        link.setCreateTime(new Date());
        link.setUpdateTime(new Date());
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateLink(Link link) {
        LinkMapper linkMapper = getBaseMapper();
        Long id = SecurityUtils.getLoginUser().getUser().getId();
        link.setUpdateBy(id);
        link.setUpdateTime(new Date());
        linkMapper.updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeLinkStatus(LinkListDto linkListDto) {
        LinkMapper linkMapper = getBaseMapper();
        Link link = linkMapper.selectById(linkListDto.getId());
        link.setStatus(linkListDto.getStatus());
        linkMapper.updateById(link);
        return ResponseResult.okResult();
    }


}
