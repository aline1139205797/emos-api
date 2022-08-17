package com.example.emos.api.service;

import com.example.emos.api.common.util.PageUtils;
import com.example.emos.api.service.db.pojo.TbUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public interface UserService {
    public HashMap createQrCode();

    public boolean checkQrCode(String code, String uuid);

    public HashMap wechatLogin(String uuid);

    public Set<String> searchUserPermissions(int userId);

    public HashMap searchUserSummary(int userId);

    public HashMap searchById(int userId);

    public ArrayList<HashMap> searchAllUser();

    /**
     * 用户登录
     *
     * @param param 用户对象
     * @return 用户ID
     */
    public Integer login(HashMap param);

    /**
     * 修改用户密码
     *
     * @param param 用户对象
     * @return 修改条数
     */
    public int updatePassWord(HashMap param);

    /**
     * 分页查询用户数据
     *
     * @param param 条数，页码，用户对象
     * @return 分页数据
     */
    public PageUtils searchUserByPage(HashMap param);

    /**
     * 新增用户
     *
     * @param tbUser 用户对象
     * @return 插入数量
     */
    public int addUser(TbUser tbUser);

    /**
     * 修改用户
     *
     * @param tbUser 用户
     * @return 修改数量
     */
    public int updateUser(TbUser tbUser);

    /**
     * 批量删除用户
     *
     * @param ids ID数组
     * @return 删除数量
     */
    public int deleteUsers(Integer[] ids);

    /**
     * 查询用户角色名称
     *
     * @param userId 用户ID
     * @return 角色名称
     */
    public ArrayList<String> searchUserRoles(int userId);
}
