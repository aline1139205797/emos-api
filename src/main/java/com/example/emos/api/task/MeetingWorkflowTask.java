package com.example.emos.api.task;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.emos.api.exception.EmosException;
import com.example.emos.api.service.db.dao.TbMeetingDao;
import com.example.emos.api.service.db.dao.TbUserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Slf4j
public class MeetingWorkflowTask {
    @Autowired
    private TbUserDao userDao;

    @Autowired
    private TbMeetingDao meetingDao;

    @Value("${emos.receiveNotify}")
    private String receiveNotify;

    @Value("${emos.code}")
    private String code;

    @Value("${emos.tcode}")
    private String tcode;

    @Value("${workflow.url}")
    private String workflow;

    @Async("AsyncTaskExecutor")
    public void startMeetingWorkflow(String uuid, int creatorId, String title, String date, String start, String meetingType) {
        //查询申请人信息
        HashMap info = userDao.searchUserInfo(creatorId);

        JSONObject json = new JSONObject();
        json.set("url", receiveNotify);
        json.set("uuid", uuid);
        json.set("creatorId", creatorId);
        json.set("creatorName", info.get("name").toString());
        json.set("code", code);
        json.set("tcode", tcode);
        json.set("title", title);
        json.set("date", date);
        json.set("start", start);
        json.set("meetingType", meetingType);

        String[] roles = info.get("roles").toString().split("，");
        //查询用户是否为总经理
        if (!ArrayUtil.contains(roles, "总经理")) {
            //查询部门经理ID
            Integer managerId = userDao.searchDeptManagerId(creatorId);
            json.set("managerId", managerId);

            //查询总经理ID
            Integer gmId = userDao.searchGmId();
            json.set("gmId", gmId);

            //查询参会人是否为同一个部门
            boolean bool = meetingDao.searchMeetingMembersInSameDept(uuid);
            json.set("sameDept", bool);
        }

        String url = workflow + "/workflow/startMeetingProcess";
        HttpResponse res = HttpRequest.post(url).header("Content-Type", "application/json").body(json.toString()).execute();
        if (res.getStatus() == 200) {
            json = JSONUtil.parseObj(res.body());
            String instanceId = json.getStr("instanceId");
            HashMap param = new HashMap();
            param.put("instanceId", instanceId);
            param.put("uuid", uuid);

            //更新会议记录instance_id
            int row = meetingDao.updateMeetingInstanceId(param);
            if (row != 1) {
                throw new EmosException("保存会议实例工作流ID失败");
            }
        } else {
            log.error(res.body());
        }
    }

    @Async("AsyncTaskExecutor")
    public void deleteMeetingApplication(String uuid,String instanceId,String reason){
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("uuid",uuid);
        jsonObject.set("instanceId",instanceId);
        jsonObject.set("code", code);
        jsonObject.set("tcode", tcode);
        jsonObject.set("type","会议申请");
        jsonObject.set("reason",reason);

        String url = workflow + "/workflow/deleteProcessById";
        HttpResponse response = HttpRequest.post(url).header("Content-Type", "application/json").body(jsonObject.toString()).execute();
        if (response.getStatus() == 200){
            log.debug("会议删除成功");
        }else{
            log.error(response.body());
            throw new EmosException(response.body());
        }
    }
}
