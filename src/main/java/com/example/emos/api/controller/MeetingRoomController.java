package com.example.emos.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.hutool.json.JSONUtil;
import com.example.emos.api.common.util.PageUtils;
import com.example.emos.api.common.util.R;
import com.example.emos.api.service.db.dao.form.*;
import com.example.emos.api.service.MeetingRoomService;
import com.example.emos.api.service.db.pojo.TbMeetingRoom;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/meeting_room")
@Tag(name = "MeetingRoomController", description = "会议管理Web接口")
public class MeetingRoomController {
    @Autowired
    private MeetingRoomService meetingRoomService;

    @GetMapping("/searchAllMeetingRoom")
    @Operation(summary = "查询所有会议室")
    @SaCheckLogin
    public R searchAllMeetingRoom() {
        ArrayList<HashMap> list = meetingRoomService.searchAllMeetingRoom();
        return R.ok().put("list", list);
    }

    @PostMapping("/searchById")
    @Operation(summary = "根据ID查找会议室")
    @SaCheckPermission(value = {"ROOT", "MEETING_ROOM:SELECT"}, mode = SaMode.OR)
    public R searchById(@Valid @RequestBody SearchMeetingRoomByIdForm form) {
        HashMap map = meetingRoomService.searchById(form.getId());
        return R.ok(map);
    }

    @PostMapping("/searchFreeMeetingRoom")
    @Operation(summary = "查询空闲会议室")
    @SaCheckLogin
    public R searchFreeMeetingRoom(@Valid @RequestBody SearchFreeMeetingRoomForm form) {
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        ArrayList<String> list = meetingRoomService.searchFreeMeetingRoom(param);
        return R.ok().put("list", list);
    }

    /**
     * 分页查询会议室信息
     *
     * @param form 查询会议室分页表单
     * @return 消息模型
     */
    @PostMapping("/SearchMeetingRoomByPage")
    @Operation(summary = "分页查询会议室信息")
    @SaCheckLogin
    public R SearchMeetingRoomByPage(@Valid @RequestBody SearchMeetingRoomByPageForm form) {
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        PageUtils page = meetingRoomService.SearchMeetingRoomByPage(param);
        return R.ok().put("page", page);
    }

    /**
     * 新增会议室记录
     *
     * @param form 添加会议室表单
     * @return 消息模型
     */
    @PostMapping("/insert")
    @Operation(summary = "新增会议室")
    @SaCheckPermission(value = {"ROOT", "MEETING_ROOM:INSERT"}, mode = SaMode.OR)
    public R insert(@Valid @RequestBody InsertMeetingRoomForm form) {
        TbMeetingRoom meetingRoom = JSONUtil.parse(form).toBean(TbMeetingRoom.class);
        int row = meetingRoomService.insert(meetingRoom);
        return R.ok().put("row", row);
    }

    @PostMapping("/update")
    @Operation(summary = "修改会议室信息")
    @SaCheckPermission(value = {"ROOT", "MEETING_ROOM:UPDATE"}, mode = SaMode.OR)
    public R update(@Valid @RequestBody UpdateMeetingRoomForm form) {
        TbMeetingRoom meetingRoom = JSONUtil.parse(form).toBean(TbMeetingRoom.class);
        int row = meetingRoomService.update(meetingRoom);
        return R.ok().put("row",row);
    }

    @PostMapping("/deleteByIds")
    @Operation(summary = "删除会议室信息")
    @SaCheckPermission(value = {"ROOT","MEETING_ROOM:DELETE"},mode = SaMode.OR)
    public R delete(@Valid @RequestBody DeleteMeetingRoomByIdsForm form){
        Integer[] ids = form.getIds();
        int rows =  meetingRoomService.deleteByIds(ids);
        return R.ok().put("rows",rows);
    }
}