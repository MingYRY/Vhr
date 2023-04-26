package org.javaboy.vhr.web.controller.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.javaboy.vhr.web.model.Hr;
import org.javaboy.vhr.common.api.RespBean;
import org.javaboy.vhr.web.model.Role;
import org.javaboy.vhr.web.service.HrService;
import org.javaboy.vhr.web.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Hr管理Controller模块
 */
@Api(tags = "Hr管理")
@RestController
@RequestMapping("/system/hr")
public class HrController {
    @Autowired
    HrService hrService;
    @Autowired
    RoleService roleService;

    @ApiOperation(value = "获取用户列表")
    @ApiImplicitParam(paramType = "path", name = "keywords", value = "Hr对应的名字的关键词", example = "root")
    @GetMapping("/")
    public List<Hr> getAllHrs(String keywords) {
        return hrService.getAllHrs(keywords);
    }

    @PutMapping("/")
    @ApiOperation(value = "更新用户", notes = "根据Hr对象来更新Hr")
    public RespBean updateHr(@RequestBody Hr hr) {
        if (hrService.updateHr(hr) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @ApiOperation(value = "获取当前Hr对应的角色")
    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PutMapping("/role")
    @ApiOperation(value = "更新当前Hr对应的角色")
    public RespBean updateHrRole(@ApiParam("hr的id") Integer hrid, @ApiParam("Hr修改对应的角色ids") Integer[] rids) {
        if (hrService.updateHrRole(hrid, rids)) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @ApiOperation(value = "删除Hr", notes = "根据url的id来指定删除对象")
    @DeleteMapping("/{id}")
    public RespBean deleteHrById(@PathVariable Integer id) {
        if (hrService.deleteHrById(id) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }
}
