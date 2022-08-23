package com.example.emos.api.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.example.emos.api.common.util.PageUtils;
import com.example.emos.api.exception.EmosException;
import com.example.emos.api.service.MeetingService;
import com.example.emos.api.service.db.dao.TbMeetingDao;
import com.example.emos.api.service.db.pojo.TbMeeting;
import com.example.emos.api.task.MeetingWorkflowTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@Slf4j
public class MeetingServiceImpl implements MeetingService {
    @Autowired
    private TbMeetingDao meetingDao;

    @Autowired
    private MeetingWorkflowTask meetingWorkflowTask;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询线下会议分页数据
     *
     * @param param 查询线下会议分页表单
     * @return 分页数据
     */
    @Override
    public PageUtils searchOfflineMeetingByPage(HashMap param) {
        ArrayList<HashMap> list = meetingDao.searchOfflineMeetingByPage(param);
        long count = meetingDao.searchOfflineMeetingCount();
        int page = (Integer) param.get("page");
        int length = (Integer) param.get("length");
        for (HashMap map : list) {
            String meeting = (String) map.get("meeting");
            if (meeting != null && meeting.length() > 0) {
                map.replace("meeting", JSONUtil.parseArray(meeting));
            }
        }
        PageUtils pageUtils = new PageUtils(list, count, page, length);
        return pageUtils;
    }

    /**
     * 添加会议
     *
     * @param meeting 会议对象
     * @return 添加条数
     */
    @Override
    public int insertMeeting(TbMeeting meeting) {
        int row = meetingDao.insert(meeting);
        if (row != 1) {
            throw new EmosException("会议添加失败");
        }
        meetingWorkflowTask.startMeetingWorkflow(meeting.getUuid(), meeting.getCreatorId(), meeting.getTitle(), meeting.getDate(), meeting.getStart() + ":00", meeting.getType() == 1 ? "线上会议" : "线下会议");
        return row;
    }

    /**
     * 查询周日历列表
     *
     * @param param 查询参数
     * @return 会议列表
     */
    @Override
    public ArrayList<HashMap> searchOfflineMeetingInWeek(HashMap param) {
        return meetingDao.searchOfflineMeetingInWeek(param);
    }

    /**
     * 查询会议详细信息
     *
     * @param status 会议状态
     * @param id     会议ID
     * @return 详细信息
     */
    @Override
    public HashMap searchMeetingInfo(short status, long id) {
        HashMap map;
        if (status == 4 || status == 5) {
            map = meetingDao.searchCurrentMeetingInfo(id);
        } else {
            map = meetingDao.searchMeetingInfo(id);
        }
        return map;
    }

    /**
     * 删除会议申请
     *
     * @param param 参数列表
     * @return 操作条目
     */
    @Override
    public int deleteMeetingApplication(HashMap param) {
        Long id = MapUtil.getLong(param, "id");
        String uuid = MapUtil.getStr(param, "uuid");
        String instanceId = MapUtil.getStr(param, "instanceId");
        //查询会议详情 判断会议是否满足开始前20分钟
        HashMap meeting = meetingDao.searchMeetingById(param);
        String date = MapUtil.getStr(meeting, "date");
        String start = MapUtil.getStr(meeting, "start");
        int status = MapUtil.getInt(meeting, "status");
        DateTime dateTime = DateUtil.parse(date + " " + start);
        DateTime now = new DateTime();
        boolean isCreator = Boolean.parseBoolean(MapUtil.getStr(meeting, "isCreator"));
        //判断距离会议开始时间
        if (now.isAfterOrEquals(dateTime.offset(DateField.MINUTE, -20))) {
            throw new EmosException("距离会议开始不足20分钟,不能删除");
        }
        //判断是否为申请人
        if (!isCreator) {
            throw new EmosException("只有会议申请人才能删除");
        }
        //判断会议状态
        if (status == 1 || status == 3) {
            int row = meetingDao.deleteMeetingApplication(param);
            if (row == 1) {
                meetingWorkflowTask.deleteMeetingApplication(uuid, instanceId, param.get("reason").toString());
            }
            return row;
        } else {
            throw new EmosException("只能删除待审批和未开始的会议");
        }
    }

    /**
     * 分页查询线上会议列表
     *
     * @param param 参数列表
     * @return 分页数据
     */
    @Override
    public PageUtils searchOnlineMeetingByPage(HashMap param) {
        ArrayList<HashMap> dataList = meetingDao.searchOnlineMeetingByPage(param);
        long count = meetingDao.searchOnlineMeetingCount(param);
        PageUtils page = new PageUtils(dataList, count, MapUtil.getInt(param, "page"), MapUtil.getInt(param, "length"));
        return page;
    }

    /**
     * 获取roomId
     *
     * @param uuid
     * @return roomId
     */
    @Override
    public Long searchRomeIdByUUID(String uuid) {
        if (redisTemplate.hasKey(uuid)){
            Object temp = redisTemplate.opsForValue().get(uuid);
            Long roomId = Long.parseLong(temp.toString());
            return roomId;
        }
        return null;
    }
}
