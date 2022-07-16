package com.example.emos.api.service.impl;

import com.example.emos.api.common.util.PageUtils;
import com.example.emos.api.exception.EmosException;
import com.example.emos.api.service.db.dao.TbDeptDao;
import com.example.emos.api.service.DeptService;
import com.example.emos.api.service.db.pojo.TbDept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private TbDeptDao deptDao;

    @Override
    public ArrayList<HashMap> searchAllDept() {
        ArrayList<HashMap> list = deptDao.searchAllDept();
        return list;
    }

    @Override
    public HashMap searchById(int id) {
        HashMap map = deptDao.searchById(id);
        return map;
    }

    /**
     * 分页查询部门数据
     * @param param 查询参数
     * @return 分页数据
     */
    @Override
    public PageUtils searchDeptByPage(HashMap param) {
        ArrayList<HashMap> deptList =  deptDao.searchDeptByPage(param);
        long deptCount = deptDao.searchDeptCount(param);
        int pageIndex = (Integer) param.get("pageIndex");
        int pageSize = (Integer) param.get("pageSize");
        PageUtils page = new PageUtils(deptList,deptCount,pageIndex,pageSize);
        return page;
    }

    /**
     * 添加部门数据
     * @param dept 部门对象
     * @return 添加条数
     */
    @Override
    public int insert(TbDept dept) {
        int row = deptDao.insert(dept);
        return row;
    }

    /**
     * 修改部门数据
     *
     * @param dept 部门对象
     * @return 修改条数
     */
    @Override
    public int update(TbDept dept) {
        int row = deptDao.update(dept);
        return row;
    }

    /**
     * 批量删除部门
     *
     * @param ids ID集合
     * @return 删除条数
     */
    @Override
    public int delete(Integer[] ids) {
        if (!deptDao.searchCanDelete(ids)){
            throw new EmosException("无法删除关联用户的部门");
        }
        int rows = deptDao.delete(ids);
        return rows;
    }
}
