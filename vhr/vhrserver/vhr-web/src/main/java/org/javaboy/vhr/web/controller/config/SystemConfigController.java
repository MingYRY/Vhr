package org.javaboy.vhr.web.controller.config;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.javaboy.vhr.web.model.Hr;
import org.javaboy.vhr.web.model.Menu;
import org.javaboy.vhr.web.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单处理
 */
@Api(tags = "菜单栏获取操作")
@RestController
@RequestMapping("/system/config")
public class SystemConfigController {
    @Autowired
    MenuService menuService;

    @ApiOperation(value = "根据Hr的Id来获取菜单")
    @GetMapping("/menu")
    public List<Menu> getMenusByHrId() {
        Integer id = ((Hr) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return menuService.getMenusByHrId(id);
    }
}