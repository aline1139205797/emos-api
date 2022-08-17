package com.example.emos.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateRange;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.emos.api.common.util.PageUtils;
import com.example.emos.api.common.util.R;
import com.example.emos.api.service.MeetingService;
import com.example.emos.api.service.db.dao.form.*;
import com.example.emos.api.service.db.pojo.TbMeeting;
import com.example.emos.api.task.MeetingWorkflowTask;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/meeting")
@Tag(name = "MeetingController", description = "会议Web接口")
@Slf4j
public class MeetingController {
    @Autowired
    MeetingService meetingService;

    /**
     * 查询线下会议分页数据
     *
     * @param form 查询线下会议分页表单
     * @return 消息模型
     */
    @PostMapping("/searchOfflineMeetingByPage")
    @Operation(summary = "查询线下会议分页数据")
    @SaCheckLogin
    public R searchOfflineMeetingByPage(@Valid @RequestBody SearchOfflineMeetingByPageForm form) {
        int start = (form.getPage() - 1) * form.getLength();
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        param.put("start", start);
        param.put("userId", StpUtil.getLoginId());
        PageUtils pageUtils = meetingService.searchOfflineMeetingByPage(param);
        return R.ok().put("page", pageUtils);
    }

    /**
     * 添加会议日程
     *
     * @param form 添加会议表单
     * @return 消息模型
     */
    @PostMapping("insertMeeting")
    @Operation(summary = "添加会议")
    @SaCheckPermission(value = {"MEETING:INSERT", "ROOT"}, mode = SaMode.OR)
    public R insertMeeting(@Valid @RequestBody InsertMeetingForm form) {
        DateTime start = DateUtil.parse(form.getDate() + " " + form.getStart());
        DateTime end = DateUtil.parse(form.getDate() + " " + form.getEnd());
        if (start.isAfterOrEquals(end)) {
            return R.error("结束时间必须大于开始时间");
        } else if (new DateTime().isAfterOrEquals(start)) {
            return R.error("开始时间不能早于当前时间");
        }
        TbMeeting tbMeeting = JSONUtil.parse(form).toBean(TbMeeting.class);
        tbMeeting.setStatus((short) 1);
        tbMeeting.setCreatorId(StpUtil.getLoginIdAsInt());
        tbMeeting.setUuid(UUID.randomUUID().toString(true));
        int row = meetingService.insertMeeting(tbMeeting);
        return R.ok().put("row", row);
    }

    /**
     * 接收工作流通知
     *
     * @param form 接收工作流表单
     * @return 消息模型
     */
    @PostMapping("/recieveNotify")
    @Operation(summary = "接收工作流通知")
    public R recieveNotify(@Valid @RequestBody RecieveNotifyForm form) {
        if (form.getResult().equals("同意")) {
            log.debug(form.getUuid() + "的会议审批通过");
        } else {
            log.debug(form.getUuid() + "的会议审批不通过");
        }
        return R.ok();
    }

    /**
     * 查询周日历会议列表
     *
     * @param form 查询某个会议室一周会议表单
     * @return 周日历会议列表
     */
    @PostMapping("/searchOfflineMeetingInWeek")
    @Operation(summary = "查询周日历会议列表")
    @SaCheckLogin
    public R searchOfflineMeetingInWeek(@Valid @RequestBody SearchOfflineMeetingInWeekForm form) {
        String date = form.getDate();
        DateTime startDate, endDate;
        if (date != null && date != "" && date.length() > 0) {
            startDate = DateUtil.parseDate(date);
            endDate = startDate.offsetNew(DateField.DAY_OF_WEEK, 6);
        } else {
            startDate = DateUtil.beginOfWeek(new Date());
            endDate = DateUtil.endOfWeek(new Date());
        }
        HashMap param = new HashMap() {{
            put("place", form.getName());
            put("startDate", startDate.toDateStr());
            put("endDate", endDate.toDateStr());
            put("mold", form.getMold());
            put("userId", StpUtil.getLoginIdAsLong());
        }};
        ArrayList<HashMap> list = meetingService.searchOfflineMeetingInWeek(param);

        //周日历表头文字
        DateRange range = DateUtil.range(startDate, endDate, DateField.DAY_OF_WEEK);
        ArrayList days = new ArrayList();
        range.forEach(item -> {
            JSONObject obj = new JSONObject();
            obj.set("date", item.toString("MM/dd"));
            obj.set("day", item.dayOfWeekEnum().toChinese("周"));
            days.add(obj);
        });
        return R.ok().put("list", list).put("days", days);
    }

    /**
     * 查询会议室纤细信息
     *
     * @param form 查询会议信息表单
     * @return 会议详细信息
     */
    @PostMapping("searchMeetingInfo")
    @Operation(summary = "查询会议室详细信息")
    @SaCheckLogin
    public R searchMeetingInfo(@Valid @RequestBody SearchMeetingInfoForm form) {
        HashMap obj = meetingService.searchMeetingInfo(form.getStatus(), form.getId());
        return R.ok(obj);
    }

    /**
     * 删除会议申请
     *
     * @param form 删除会议申请表单
     * @return
     */
    @PostMapping("/DeleteMeetingApplicationForm")
    @Operation(summary = "删除会议申请")
    @SaCheckLogin
    public R deleteMeetingApplication(@Valid @RequestBody DeleteMeetingApplicationForm form) {
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        param.put("userId", StpUtil.getLoginIdAsLong());
        param.put("creatorId", StpUtil.getLoginIdAsLong());
        int row = meetingService.deleteMeetingApplication(param);
        return R.ok().put("row", row);
    }

    /**
     * 分页查询线上会议列表
     *
     * @param form 查询线上会议分页数据表单
     * @return 消息模型
     */
    @PostMapping("/searchOnlineMeetingByPage")
    @Operation(summary = "分页查询线上会议列表")
    @SaCheckLogin
    public R searchOnlineMeetingByPage(@Valid @RequestBody SearchOnlineMeetingByPageForm form) {
        int start = (form.getPage() - 1) * form.getLength();
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        param.put("start", start);
        param.put("userId", StpUtil.getLoginId());
        PageUtils page = meetingService.searchOnlineMeetingByPage(param);
        return R.ok().put("page", page);
    }
}
