package com.sangeng.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private String roleName;

    private String roleKey;

    private Integer roleSort;

    private String status;

    private String remark;

    private List<Long> menuIds;

    public RoleDto(String roleName, String roleKey, Integer roleSort, String status, String remark) {
        this.roleName = roleName;
        this.roleKey = roleKey;
        this.roleSort = roleSort;
        this.status = status;
        this.remark = remark;
    }
}
