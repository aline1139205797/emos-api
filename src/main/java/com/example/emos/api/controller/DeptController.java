package com.example.emos.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.hutool.json.JSONUtil;
import com.example.emos.api.common.util.PageUtils;
import com.example.emos.api.common.util.R;
import com.example.emos.api.service.DeptService;
import com.example.emos.api.service.db.dao.form.*;
import com.example.emos.api.service.db.pojo.TbDept;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/dept")
@Tag(name = "DeptController", description = "部门Web接口")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @GetMapping("/searchAllDept")
    @Operation(summary = "查询所有部门")
    public R searchAllDept() {
        ArrayList<HashMap> list = deptService.searchAllDept();
        return R.ok().put("list", list);
    }

    @PostMapping("/searchById")
    @Operation(summary = "根据ID查询部门")
    @SaCheckPermission(value = {"ROOT", "DEPT:SELECT"}, mode = SaMode.OR)
    public R searchById(@Valid @RequestBody SearchDeptByIdForm form) {
        HashMap map = deptService.searchById(form.getId());
        return R.ok(map);
    }

    /**
     * 分页查询部门数据
     *
     * @param form 查询部门分页表单
     * @return 消息模型
     */
    @PostMapping("/searchDeptByPage")
    @Operation(summary = "查询部门分页数据")
    @SaCheckPermission(value = {"ROOT", "DEPT:SELECT"}, mode = SaMode.OR)
    public R searchDeptByPage(@Valid @RequestBody SearchDeptByPageForm form) {
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        int start = (form.getPageIndex() - 1) * form.getPageSize();
        param.put("start", start);
        PageUtils page = deptService.searchDeptByPage(param);
        return R.ok().put("page", page);
    }

    /**
     * 插入部门数据
     *
     * @param form 添加部门表单
     * @return 消息模型
     */
    @PostMapping("/insert")
    @Operation(summary = "添加部门")
    @SaCheckPermission(value = {"ROOT", "DEPT:INSERT"}, mode = SaMode.OR)
    public R insert(@Valid @RequestBody InsertDeptForm form) {
        TbDept dept = JSONUtil.parse(form).toBean(TbDept.class);
        int row = deptService.insert(dept);
        return R.ok().put("row", row);
    }

    /**
     * 修改部门数据
     *
     * @param form 更新部门表单
     * @return 消息模型
     */
    @PostMapping("/update")
    @Operation(summary = "修改部门")
    @SaCheckPermission(value = {"ROOT", "DEPT:UPDATE"}, mode = SaMode.OR)
    public R update(@Valid @RequestBody UpdateDeptForm form) {
        TbDept dept = JSONUtil.parse(form).toBean(TbDept.class);
        int row = deptService.update(dept);
        return R.ok().put("row", row);
    }

    /**
     * 批量删除部门
     *
     * @param form 删除部门表单
     * @return 消息模型
     */
    @PostMapping("/delete")
    @Operation(summary = "批量删除部门")
    @SaCheckPermission(value = {"ROOT","DEPT:DELETE"},mode = SaMode.OR)
    public R delete(@Valid @RequestBody DeleteDeptByIdsForm form){
        Integer[] ids = form.getIds();
        int rows = deptService.delete(ids);
        return R.ok().put("rows",rows);
    }

}