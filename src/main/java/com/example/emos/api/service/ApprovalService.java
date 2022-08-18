package com.example.emos.api.service;

import com.example.emos.api.common.util.PageUtils;

import java.util.HashMap;

public interface ApprovalService {
    /**
     * 查询分页任务列表
     *
     * @param param 参数列表
     * @return 分页数据
     */
    public PageUtils searchTaskByPage(HashMap param);

    /**
     * 查询审批详情信息
     *
     * @param param 参数列表
     * @return 审批详情信息
     */
    public HashMap searchApprovalContent(HashMap param);
}
