package com.example.emos.api.service.db.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@Mapper
public interface TbUserDao {
    public Set<String> searchUserPermissions(int userId);

    public HashMap searchById(int userId);

    public Integer searchIdByOpenId(String openId);

    public HashMap searchUserSummary(int userId);

    public HashMap searchUserInfo(int userId);

    public Integer searchDeptManagerId(int id);

    public Integer searchGmId();

    public ArrayList<HashMap> searchAllUser();

    /**
     * 用户登录
     * @param param 用户对象
     * @return 用户ID
     */
    public Integer login(HashMap param);

    /**
     * 修改密码
     * @param param 用户对象
     * @return 修改条数
     */
    public int updatePassWord(HashMap param);

}