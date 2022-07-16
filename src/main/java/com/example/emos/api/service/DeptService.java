package com.example.emos.api.service;

import com.example.emos.api.common.util.PageUtils;
import com.example.emos.api.service.db.pojo.TbDept;

import javax.crypto.interfaces.PBEKey;
import java.util.ArrayList;
import java.util.HashMap;

public interface DeptService {
    public ArrayList<HashMap> searchAllDept();

    public HashMap searchById(int id);

    /**
     * 分页查询部门数据
     *
     * @param param 查询参数
     * @return 分页数据
     */
    public PageUtils searchDeptByPage(HashMap param);

    /**
     * 添加部门数据
     *
     * @param dept 部门对象
     * @return 添加条数
     */
    public int insert(TbDept dept);

    /**
     * 修改部门数据
     *
     * @param dept 部门对象
     * @return 修改条数
     */
    public int update(TbDept dept);

    public int delete(Integer[] ids);
}
