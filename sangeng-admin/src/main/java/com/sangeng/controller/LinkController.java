package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.LinkListDto;
import com.sangeng.domain.entity.Link;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, LinkListDto linkListDto){
        return linkService.pageLinkList(pageNum,pageSize,linkListDto);
    }

    @PostMapping("")
    public ResponseResult addLink(@RequestBody Link link){
        return linkService.addLink(link);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable("id") Long id){
        if (linkService.removeById(id)){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @GetMapping("/{id}")
    public ResponseResult getLink(@PathVariable("id") Long id){
        return ResponseResult.okResult(linkService.getById(id));
    }

    @PutMapping("")
    public ResponseResult updateLink(@RequestBody Link link){
        return linkService.updateLink(link);
    }

    @PutMapping("/changeLinkStatus")
    public ResponseResult changeLinkStatus(@RequestBody LinkListDto linkListDto){
        return linkService.changeLinkStatus(linkListDto);
    }
}
