package com.example.emos.api.service.db.dao;

import com.example.emos.api.service.db.pojo.TbUser;
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

    /**
     * 分页查询用户数据
     * @param param 条数，页码，用户对象
     * @return 分页数据
     */
    public  ArrayList<HashMap> searchUserByPage(HashMap param);

    /**
     * 分页查询用户数据总条数
     * @param param 用户对象
     * @return 总条数
     */
    public  long searchUserByPageCount(HashMap param);

    /**
     * 新增用户
     * @param user 用户对象
     * @return 插入数量
     */
    public int addUser(TbUser user);

    /**
     * 修改用户
     * @param user 用户
     * @return 修改数量
     */
    public int updateUser(TbUser user);

}