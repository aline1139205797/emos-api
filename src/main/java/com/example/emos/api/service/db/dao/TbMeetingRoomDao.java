package com.example.emos.api.service.db.dao;

import com.example.emos.api.service.db.pojo.TbMeetingRoom;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface TbMeetingRoomDao {
    public ArrayList<HashMap> searchAllMeetingRoom();
    
    public HashMap searchById(int id);
    
    public ArrayList<String> searchFreeMeetingRoom(HashMap param);

    /**
     * 分页查询会议室信息
     *
     * @param param 查询参数
     * @return 分页数据
     */
    public ArrayList<HashMap> SearchMeetingRoomByPage(HashMap param);

    /**
     * 查询会议室信息总条数
     *
     * @param param 查询参数
     * @return 数据总条数
     */
    public long SearchMeetingRoomCount(HashMap param);

    /**
     * 新增会议室记录
     *
     * @param meetingRoom 会议室对象
     * @return 新增条数
     */
    public int insert(TbMeetingRoom meetingRoom);

    public int update(TbMeetingRoom meetingRoom);

    public boolean searchCanDelete(Integer[] ids);

    public int deleteByIds(Integer[] ids);
}
