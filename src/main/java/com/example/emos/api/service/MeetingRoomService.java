package com.example.emos.api.service;

import com.example.emos.api.common.util.PageUtils;
import com.example.emos.api.service.db.pojo.TbMeetingRoom;

import java.util.ArrayList;
import java.util.HashMap;

public interface MeetingRoomService {
    public ArrayList<HashMap> searchAllMeetingRoom();

    public HashMap searchById(int id);

    public ArrayList<String> searchFreeMeetingRoom(HashMap param);

    /**
     * 分页查询会议室信息
     *
     * @param param 查询参数
     * @return 分页信息
     */
    public PageUtils SearchMeetingRoomByPage(HashMap param);

    /**
     * 新增会议室记录
     *
     * @param meetingRoom 会议室对象
     * @return 新增条数
     */
    public int insert(TbMeetingRoom meetingRoom);

    public int update(TbMeetingRoom meetingRoom);

    public int deleteByIds(Integer[] ids);
}
