package com.example.emos.api.service.impl;

import com.example.emos.api.common.util.PageUtils;
import com.example.emos.api.exception.EmosException;
import com.example.emos.api.service.db.dao.TbMeetingRoomDao;
import com.example.emos.api.service.MeetingRoomService;
import com.example.emos.api.service.db.pojo.TbMeetingRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class MeetingRoomServiceImpl implements MeetingRoomService {
    @Autowired
    private TbMeetingRoomDao meetingRoomDao;

    @Override
    public ArrayList<HashMap> searchAllMeetingRoom() {
        ArrayList<HashMap> list = meetingRoomDao.searchAllMeetingRoom();
        return list;
    }

    @Override
    public HashMap searchById(int id) {
        HashMap map = meetingRoomDao.searchById(id);
        return map;
    }

    @Override
    public ArrayList<String> searchFreeMeetingRoom(HashMap param) {
        ArrayList<String> list = meetingRoomDao.searchFreeMeetingRoom(param);
        return list;
    }

    /**
     * 分页查询会议室信息
     *
     * @param param 查询参数
     * @return 分页信息
     */
    @Override
    public PageUtils SearchMeetingRoomByPage(HashMap param) {
        int pageIndex = (Integer) param.get("pageIndex");
        int pageSize = (Integer) param.get("pageSize");
        int start = (pageIndex - 1) * pageSize;
        param.put("start", start);
        ArrayList<HashMap> list = meetingRoomDao.SearchMeetingRoomByPage(param);
        long pageCount = meetingRoomDao.SearchMeetingRoomCount(param);
        PageUtils page = new PageUtils(list, pageCount, pageIndex, pageSize);
        return page;
    }

    /**
     * 新增会议室记录
     *
     * @param meetingRoom 会议室对象
     * @return 新增条数
     */
    @Override
    public int insert(TbMeetingRoom meetingRoom) {
        int row = meetingRoomDao.insert(meetingRoom);
        return row;
    }

    @Override
    public int update(TbMeetingRoom meetingRoom) {
        int row = meetingRoomDao.update(meetingRoom);
        return row;
    }

    @Override
    public int deleteByIds(Integer[] ids) {
        if(!meetingRoomDao.searchCanDelete(ids)){
            throw new EmosException("不能删除有关联的数据");
        }
        int rows = meetingRoomDao.deleteByIds(ids);
        return rows;
    }
}