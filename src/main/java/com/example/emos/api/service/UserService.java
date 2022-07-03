package com.example.emos.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public interface UserService {
    public HashMap createQrCode();

    public boolean checkQrCode(String code,String uuid);

    public HashMap wechatLogin(String uuid);

    public Set<String> searchUserPermissions(int userId);

    public HashMap searchUserSummary(int userId);

    public HashMap searchById(int userId);

    public ArrayList<HashMap> searchAllUser();

    /**
     * 用户登录
     * @param param 用户对象
     * @return 用户ID
     */
    public Integer login(HashMap param);

    /**
     * 修改用户密码
     * @param param 用户对象
     * @return 修改条数
     */
    public int updatePassWord(HashMap param);
}
