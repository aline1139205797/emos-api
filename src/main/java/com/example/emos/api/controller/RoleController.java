package com.example.emos.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.hutool.json.JSONUtil;
import com.example.emos.api.common.util.PageUtils;
import com.example.emos.api.common.util.R;
import com.example.emos.api.service.db.dao.form.*;
import com.example.emos.api.service.RoleService;
import com.example.emos.api.service.db.pojo.TbRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/role")
@Tag(name = "RoleController", description = "角色Web接口")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/searchAllRole")
    @Operation(summary = "查询所有角色")
    public R searchAllRole() {
        ArrayList<HashMap> list = roleService.searchAllRole();
        return R.ok().put("list", list);
    }

    @PostMapping("/searchById")
    @Operation(summary = "根据ID查询角色")
    @SaCheckPermission(value = {"ROOT", "ROLE:SELECT"}, mode = SaMode.OR)
    public R searchById(@Valid @RequestBody SearchRoleByIdForm form) {
        HashMap map = roleService.searchById(form.getId());
        return R.ok(map);
    }

    /**
     * 查询角色分页数据
     *
     * @param form 查询角色分页表单
     * @return 角色分页数据
     */
    @PostMapping("/searchRoleByPage")
    @Operation(summary = "获取角色分页数据")
    @SaCheckPermission(value = {"ROOT", "ROLE:SELECT"}, mode = SaMode.OR)
    public R searchRoleByPage(@Valid @RequestBody SearchRoleByPageForm form) {
        int page = form.getPage();
        int length = form.getLength();
        int start = (page - 1) * length;
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        param.put("start", start);
        PageUtils pageUtils = roleService.searchRoleByPage(param);
        return R.ok().put("page", pageUtils);
    }

    /**
     * 批量删除角色
     * @param form 删除角色表单
     * @return 消息模型
     */
    @PostMapping("/deleteRolesByIds")
    @Operation(summary = "批量删除角色")
    @SaCheckPermission(value = {"ROOT", "ROLE:DELETE"}, mode = SaMode.OR)
    public R deleteRolesByIds(@Valid @RequestBody DeleteRoleByIdsForm form){
        int rows = roleService.deleteRolesByIds(form.getIds());
        return R.ok().put("rows", rows);
    }

    /**
     * 添加角色数据
     * @param form 添加角色数据表单
     * @return 消息模型
     */
    @PostMapping("/insertRole")
    @Operation(summary = "添加角色数据")
    @SaCheckPermission(value = {"ROOT","ROLE:INSERT"}, mode = SaMode.OR)
    public R insertRole(@Valid @RequestBody InsertRoleForm form){
        TbRole role =  JSONUtil.parse(form).toBean(TbRole.class);
        int row = roleService.insertRole(role);
        return R.ok().put("rows",row);
    }

    /**
     * 修改角色数据
     * @param form 修改角色数据表单
     * @return 消息模型
     */
    @PostMapping("/updateRole")
    @Operation(summary = "修改角色数据")
    @SaCheckPermission(value = {"ROOT","ROLE:UPDATE"},mode = SaMode.OR)
    public R updateRole(@Valid @RequestBody UpdateRoleForm form){
        TbRole role = JSONUtil.parse(form).toBean(TbRole.class);
        int row =  roleService.updateRole(role);
        return  R.ok().put("rows",row);
    }
}
