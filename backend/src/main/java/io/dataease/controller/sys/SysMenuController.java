package io.dataease.controller.sys;


import io.dataease.plugins.common.base.domain.SysMenu;
import io.dataease.commons.utils.BeanUtils;

import io.dataease.controller.handler.annotation.I18n;
import io.dataease.controller.sys.base.BaseGridRequest;
import io.dataease.controller.sys.request.MenuCreateRequest;
import io.dataease.controller.sys.request.MenuDeleteRequest;
import io.dataease.controller.sys.response.MenuNodeResponse;
import io.dataease.controller.sys.response.MenuTreeNode;
import io.dataease.service.sys.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@ApiIgnore
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：菜单管理")
@RequestMapping("/api/menu")
public class SysMenuController {

    @Resource
    private MenuService menuService;

    @ApiOperation("查询根节点菜单")
    @I18n
    @PostMapping("/childNodes/{pid}")
    public List<MenuNodeResponse> childNodes(@PathVariable("pid") Long pid){
        List<SysMenu> nodes = menuService.nodesByPid(pid);
        return menuService.convert(nodes);
    }

    @ApiOperation("搜索菜单树")
    @I18n
    @PostMapping("/search")
    public List<MenuNodeResponse> search(@RequestBody BaseGridRequest request) {
        List<SysMenu> nodes = menuService.nodesTreeByCondition(request);
        return nodes.stream().map(node -> {
            MenuNodeResponse menuNodeResponse = BeanUtils.copyBean(new MenuNodeResponse(), node);
            menuNodeResponse.setHasChildren(node.getSubCount() > 0);
            menuNodeResponse.setTop(node.getPid().equals(MenuService.MENU_ROOT_PID));
            return menuNodeResponse;
        }).collect(Collectors.toList());
    }



    @ApiOperation("新增菜单")
    @PostMapping("/create")
    public void create(@RequestBody MenuCreateRequest request){
        menuService.add(request);
    }

    @ApiOperation("删除菜单")
    @PostMapping("/delete")
    public void delete(@RequestBody MenuDeleteRequest request){
        menuService.delete(request);
    }

    @ApiOperation("更新菜单")
    @PostMapping("/update")
    public void update(@RequestBody MenuCreateRequest menu){
        menuService.update(menu);
    }



    @PostMapping("/childMenus/{pid}")
    public Set<Long> childMenus(@PathVariable Long pid){
        List<MenuNodeResponse> children = menuService.children(pid);
        Set<Long> sets = children.stream().map(MenuNodeResponse::getMenuId).collect(Collectors.toSet());
        sets.add(pid);
        return sets;
    }
    @PostMapping("/nodesByMenuId/{menuId}")
    public List<MenuTreeNode> nodesByMenuId(@PathVariable("menuId") Long menuId) {
        return  menuService.searchTree(menuId);
    }




}
