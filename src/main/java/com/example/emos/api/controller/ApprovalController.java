package com.example.emos.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.server.HttpServerResponse;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import com.example.emos.api.common.util.PageUtils;
import com.example.emos.api.common.util.R;
import com.example.emos.api.exception.EmosException;
import com.example.emos.api.service.ApprovalService;
import com.example.emos.api.service.UserService;
import com.example.emos.api.service.db.dao.form.SearchApprovalContentForm;
import com.example.emos.api.service.db.dao.form.SearchTaskByPageForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/approval")
@Slf4j
@Tag(name = "ApprovalController", description = "任务审批WEB接口")
public class ApprovalController {
    @Value("${workflow.url}")
    private String workflow;

    @Value("${emos.code}")
    private String code;

    @Value("${emos.tcode}")
    private String tcode;

    @Autowired
    private UserService userService;

    @Autowired
    private ApprovalService approvalService;

    /**
     * 查询分页任务列表
     *
     * @param form 查询任务分页列表表单
     * @return 分页数据
     */
    @PostMapping("/searchTaskByPage")
    @Operation(summary = "查询分页任务列表")
    @SaCheckPermission(value = {"WORKFLOW:APPROVAL", "FILE:ARCHIVE"}, mode = SaMode.OR)
    public R searchTaskByPage(@Valid @RequestBody SearchTaskByPageForm form) {
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        int userId = StpUtil.getLoginIdAsInt();
        param.put("userId", userId);
        param.put("role", userService.searchUserRoles(userId));
        PageUtils pageUtils = approvalService.searchTaskByPage(param);
        return R.ok().put("page", pageUtils);
    }

    /**
     * 查询审批详情信息
     *
     * @param form 获取审批详细表单
     * @return 审批详情信息
     */
    @PostMapping("/searchApprovalContent")
    @Operation(summary = "查询审批详细信息")
    @SaCheckPermission(value = {"WORKFLOW:APPROVAL", "FILE:ARCHIVE"}, mode = SaMode.OR)
    public R searchApprovalContent(@Valid @RequestBody SearchApprovalContentForm form) {
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        int userId = StpUtil.getLoginIdAsInt();
        ArrayList<String> role = userService.searchUserRoles(userId);
        param.put("userId", userId);
        param.put("role", role);
        HashMap content = approvalService.searchApprovalContent(param);
        return R.ok().put("content", content);
    }

    @GetMapping("/searchApprovalBpmn")
    @Operation(summary = "获取BPMN图")
    @SaCheckPermission(value = {"WORKFLOW:APPROVAL", "FILE:ARCHIVE"}, mode = SaMode.OR)
    public void searchApprovalBpmn(String instanceId, HttpServletResponse response) {
        if (StrUtil.isBlankIfStr(instanceId)) {
            throw new EmosException("instanceId不能为空");
        }
        //请求参数
        HashMap param = new HashMap() {{
            put("instanceId", instanceId);
            put("code", code);
            put("tcode", tcode);
        }};
        //请求地址
        String url = workflow + "/workflow/searchApprovalBpmn";
        //发起请求
        HttpResponse res = HttpRequest.post(url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(param)).execute();
        if (res.getStatus() == 200) {
            try (InputStream in = res.bodyStream();
                 BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
                 OutputStream out = response.getOutputStream();
                 BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out)) {
                IOUtils.copy(bufferedInputStream,bufferedOutputStream);
            } catch (Exception e) {
                log.error("执行异常",e);
            }
        } else {
            log.error(res.body());
            throw new EmosException("获取工作流数据异常");
        }

    }
}
