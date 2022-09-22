package com.sangeng.domain.vo;

import com.sangeng.domain.entity.Role;
import com.sangeng.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleVo {
    private List<Long> roleIds;

    private User user;

    private List<Role> roles;
}
