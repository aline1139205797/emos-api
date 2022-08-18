package com.example.emos.api.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.emos.api.common.util.PageUtils;
import com.example.emos.api.exception.EmosException;
import com.example.emos.api.service.ApprovalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@Slf4j
public class ApprovalServiceImpl implements ApprovalService {
    @Value("${workflow.url}")
    private String workflow;

    @Value("${emos.code}")
    private String code;

    @Value("${emos.tcode}")
    private String tcode;

    /**
     * 查询分页任务列表
     *
     * @param param 参数列表
     * @return 分页数据
     */
    @Override
    public PageUtils searchTaskByPage(HashMap param) {
        param.put("code", code);
        param.put("tcode", tcode);
        String url = workflow + "/workflow/searchTaskByPage";
        HttpResponse resp = HttpRequest.post(url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(param)).execute();
        if (resp.getStatus() == 200) {
            JSONObject json = JSONUtil.parseObj(resp.body());
            JSONObject page = json.getJSONObject("page");
            ArrayList list = page.get("list", ArrayList.class);
            Long totalCount = page.getLong("totalCount");
            int pageIndex = page.getInt("pageIndex");
            int pageSize = page.getInt("pageSize");
            PageUtils pageUtils = new PageUtils(list, totalCount, pageIndex, pageSize);
            return pageUtils;
        } else {
            log.error(resp.body());
            throw new EmosException("获取工作流数据异常");
        }
    }

    /**
     * 查询审批详情信息
     *
     * @param param 参数列表
     * @return 审批详情信息
     */
    @Override
    public HashMap searchApprovalContent(HashMap param) {
        param.put("code",code);
        param.put("tcode",tcode);
        String url = workflow + "/workflow/searchApprovalContent";
        HttpResponse res = HttpRequest.post(url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(param)).execute();
        if(res.getStatus() == 200){
            JSONObject jsonObject = JSONUtil.parseObj(res.body());
            HashMap content = jsonObject.get("content",HashMap.class);
            return content;
        }else{
            log.error(res.body());
            throw new EmosException("获取工作流数据异常");
        }
    }
}
