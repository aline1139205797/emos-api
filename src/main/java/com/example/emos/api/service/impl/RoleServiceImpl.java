package com.example.emos.api.service.impl;

import com.example.emos.api.common.util.PageUtils;
import com.example.emos.api.service.db.dao.TbRoleDao;
import com.example.emos.api.service.RoleService;
import com.example.emos.api.service.db.pojo.TbRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private TbRoleDao roleDao;

    @Override
    public ArrayList<HashMap> searchAllRole() {
        ArrayList<HashMap> list = roleDao.searchAllRole();
        return list;
    }

    @Override
    public HashMap searchById(int id) {
        HashMap map = roleDao.searchById(id);
        return map;
    }

    @Override
    public PageUtils searchRoleByPage(HashMap param) {
        ArrayList<HashMap> roleList = roleDao.searchRoleByPage(param);
        long pageCount = roleDao.searchRoleCount(param);
        int start = (Integer) param.get("start");
        int length = (Integer) param.get("length");
        PageUtils pageUtils = new PageUtils(roleList, pageCount, start, length);
        return pageUtils;
    }

    /**
     * 批量删除角色
     * @param ids ID集合
     * @return 操作条数
     */
    @Override
    public int deleteRolesByIds(Integer[] ids) throws Exception {
        if (!roleDao.searchCanDelete(ids)) {
            throw new Exception("无法删除关联角色的用户");
        }
        int rows = roleDao.deleteByIds(ids);
        return rows;
    }

    /**
     * 添加角色数据
     * @param role 角色对象
     * @return 添加数量
     */
    @Override
    public int insertRole(TbRole role) {
        int row = roleDao.insertRole(role);
        return row;
    }

    /**
     * 修改角色数据
     * @param role 角色对象
     * @return 修改数量
     */
    @Override
    public int updateRole(TbRole role) {
        int row = roleDao.updateRole(role);
        return row;
    }
}
