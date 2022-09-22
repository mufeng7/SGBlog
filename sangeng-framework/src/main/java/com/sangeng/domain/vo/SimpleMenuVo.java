package com.sangeng.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleMenuVo {
    private Long id;

    //菜单名称
    private String label;

    private Long parentId;

    List<SimpleMenuVo> children;

    public SimpleMenuVo(Long id, String label, Long parentId) {
        this.id = id;
        this.label = label;
        this.parentId = parentId;
    }
}
