package com.example.emos.api.service;

import com.example.emos.api.common.util.PageUtils;
import com.example.emos.api.service.db.pojo.TbRole;

import java.util.ArrayList;
import java.util.HashMap;

public interface RoleService {
    public ArrayList<HashMap> searchAllRole();

    public HashMap searchById(int id);

    /**
     * 查询角色分页数据
     *
     * @param param
     * @return 角色分页数据
     */
    public PageUtils searchRoleByPage(HashMap param);

    /**
     * 批量删除角色
     * @param ids ID集合
     * @return 操作条数
     */
    public int deleteRolesByIds(Integer[] ids);

    /**
     * 添加角色数据
     * @param role 角色对象
     * @return 添加数量
     */
    public int insertRole(TbRole role);

    /**
     * 修改角色数据
     * @param role 角色对象
     * @return 修改数量
     */
    public int updateRole(TbRole role);
}
