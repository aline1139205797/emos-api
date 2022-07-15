package com.example.emos.api.service.db.dao;

import com.example.emos.api.service.db.pojo.TbRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;


@Mapper
public interface TbRoleDao {
    public ArrayList<HashMap> searchAllRole();
    public HashMap searchById(int id);

    /**
     * 查询角色分页数据
     * @param param
     * @return 角色分页数据
     */
    public ArrayList<HashMap> searchRoleByPage(HashMap param);

    /**
     * 查询角色分页数据总条目
     * @param param
     * @return 总条目
     */
    public long searchRoleCount(HashMap param);

    /**
     * 批量删除角色
     * @param ids ID集合
     * @return 操作条数
     */
    public int deleteByIds(Integer[] ids);

    /**
     * 校验角色是否有关联用户
     * @param ids ID集合
     * @return boolean
     */
    public boolean searchCanDelete(Integer[] ids);

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