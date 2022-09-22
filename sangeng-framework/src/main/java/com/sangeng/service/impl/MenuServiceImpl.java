package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.MenuDto;
import com.sangeng.domain.entity.Menu;
import com.sangeng.domain.vo.SimpleMenuVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.mapper.MenuMapper;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.stereotype.Service;
import com.sangeng.service.MenuService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-08-10 16:09:25
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<String> selectPermsByUserId(Long id) {
        if (SecurityUtils.isAdmin()){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType,SystemConstants.MENU,SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);

            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        if (SecurityUtils.isAdmin()){
            menus = menuMapper.selectAllRouterMenu();
        }else {
            menus = menuMapper.selectRouterMenuByUserId(userId);
        }
        List<Menu> menuTree = buildMenuTree(menus,0l);
        return menuTree;
    }

    @Override
    public ResponseResult getMenuById(Long id) {
        Menu menu = getById(id);
        return ResponseResult.okResult(menu);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        if (menu.getParentId() == menu.getId()){
            return ResponseResult.errorResult(AppHttpCodeEnum.MENU_ID_ERROR);
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long id) {
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<Menu> listMenu(MenuDto menuDto) {
        List<Menu> menus = menuMapper.listMenu(menuDto);
        return ResponseResult.okResult(menus);
    }

    @Override
    public ResponseResult treeSelect() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,SystemConstants.SECOND_CATALOG);
        List<Menu> menus = list(queryWrapper);
        List<SimpleMenuVo> simpleMenuVos = toSimpleMenuVoList(menus);
        for (SimpleMenuVo simpleMenuVo : simpleMenuVos) {
            simpleMenuVo.setChildren(getMenuVoChildren(simpleMenuVo));
        }
        //List<SimpleMenuVo> simpleMenuVos = menuMapper.treeSelect();
        return new ResponseResult().ok(simpleMenuVos);
    }

    private List<SimpleMenuVo> toSimpleMenuVoList(List<Menu> menus) {
        List<SimpleMenuVo> simpleMenuVos = menus.stream()
                .map(menu -> new SimpleMenuVo(menu.getId(), menu.getMenuName(), menu.getParentId()))
                .collect(Collectors.toList());
        return simpleMenuVos;
    }

    private List<SimpleMenuVo> getMenuVoChildren(SimpleMenuVo simpleMenuVo) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,simpleMenuVo.getId());
        List<Menu> menuList = list(queryWrapper);
        List<SimpleMenuVo> simpleMenuVos = toSimpleMenuVoList(menuList);
        return simpleMenuVos;
    }

    private List<Menu> buildMenuTree(List<Menu> menus,Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu,menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m ->m.setChildren(getChildren(m,menus)))   //第三层的查询，递归调用
                .collect(Collectors.toList());
        return childrenList;
    }
}
