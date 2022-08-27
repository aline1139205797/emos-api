package com.example.emos.api.service;

import com.example.emos.api.common.util.PageUtils;
import com.example.emos.api.service.db.pojo.TbMeeting;

import java.util.ArrayList;
import java.util.HashMap;

public interface MeetingService {

    /**
     * 查询线下会议分页数据
     *
     * @param param 查询线下会议分页表单
     * @return 分页数据
     */
    public PageUtils searchOfflineMeetingByPage(HashMap param);

    /**
     * 添加会议
     * @param meeting 会议对象
     * @return 添加条数
     */
    public int insertMeeting(TbMeeting meeting);

    /**
     * 查询周日历列表
     *
     * @param param 查询参数
     * @return 会议列表
     */
    public ArrayList<HashMap> searchOfflineMeetingInWeek(HashMap param);

    /**
     * 查询会议详细信息
     *
     * @param status 会议状态
     * @param id 会议ID
     * @return 详细信息
     */
    public HashMap searchMeetingInfo(short status,long id);

    /**
     * 删除会议申请
     *
     * @param param 参数列表
     * @return 操作条目
     */
    public int deleteMeetingApplication(HashMap param);

    /**
     * 分页查询线上会议列表
     *
     * @param param 参数列表
     * @return 分页数据
     */
    public PageUtils searchOnlineMeetingByPage(HashMap param);

    /**
     * 根据UUID获取ROOMID
     *
     * @param uuid
     * @return ROOMID
     */
    public Long searchRomeIdByUUID(String uuid);

    /**
     * 查询视频墙人员
     *
     * @param param 查询参数
     * @return 参会人员
     */
    public ArrayList<HashMap> searchOnlineMeetingMembers(HashMap param);

    /**
     * 查询用户是否可以签到
     *
     * @param param 查询参数
     * @return
     */
    public boolean searchCanCheckinMeeting(HashMap param);

    /**
     * 用户签到
     *
     * @param param 请求参数
     * @return 修改条数
     */
    public int updateMeetingPresent(HashMap param);
}
