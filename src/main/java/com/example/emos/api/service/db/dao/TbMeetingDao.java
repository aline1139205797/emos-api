package com.example.emos.api.service.db.dao;

import com.example.emos.api.service.db.pojo.TbMeeting;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Mapper
public interface TbMeetingDao {
    public boolean searchMeetingMembersInSameDept(String uuid);

    public HashMap searchMeetingById(HashMap param);

    /**
     * 获取线下会议日程表分页数据
     *
     * @param param 查询参数
     * @return 分页数据
     */
    public ArrayList<HashMap> searchOfflineMeetingByPage(HashMap param);

    /**
     * 获取线下会议室总条目
     *
     * @return 线下会议室总条目
     */
    public long searchOfflineMeetingCount();

    /**
     * 修改会议记录工作流ID
     *
     * @param param 参数列表
     * @return 修改条数
     */
    public int updateMeetingInstanceId(HashMap param);

    /**
     * 添加会议
     *
     * @param meeting 会议对象
     * @return 新增条数
     */
    public int insert(TbMeeting meeting);

    /**
     * 查询周日历列表
     *
     * @param param 查询参数
     * @return 会议列表
     */
    public ArrayList<HashMap> searchOfflineMeetingInWeek(HashMap param);

    /**
     * 查询会议详细信息(未开始状态)
     *
     * @param id
     * @return 会议详细信息
     */
    public HashMap searchMeetingInfo(long id);

    /**
     * 查询会议详细信息(开始状态)
     *
     * @param id
     * @return 会议详细信息
     */
    public HashMap searchCurrentMeetingInfo(long id);

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
     * @param param 查询参数
     * @return 分页数据
     */
    public ArrayList<HashMap> searchOnlineMeetingByPage(HashMap param);

    /**
     * 查询线上会议列表总条目
     *
     * @param param 查询参数
     * @return 总条目
     */
    public long searchOnlineMeetingCount(HashMap param);

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
    public long searchCanCheckinMeeting(HashMap param);

    /**
     * 用户签到
     *
     * @param param 请求参数
     * @return 修改条数
     */
    public int updateMeetingPresent(HashMap param);
}