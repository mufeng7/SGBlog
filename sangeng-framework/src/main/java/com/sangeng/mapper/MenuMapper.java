package com.sangeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sangeng.domain.dto.MenuDto;
import com.sangeng.domain.entity.Menu;
import com.sangeng.domain.vo.SimpleMenuVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2022-08-10 16:09:23
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterMenuByUserId(Long userId);

    List<Menu> listMenu(@Param("menuDto") MenuDto menuDto);

    List<SimpleMenuVo> treeSelect();
}
