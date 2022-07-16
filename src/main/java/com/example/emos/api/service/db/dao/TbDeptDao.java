package com.example.emos.api.service.db.dao;

import com.example.emos.api.service.db.pojo.TbDept;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.mongodb.core.aggregation.BucketOperation;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface TbDeptDao {
    public ArrayList<HashMap> searchAllDept();
    public HashMap searchById(int id);

    /**
     * 分页查询部门数据
     * @param param 查询参数
     * @return 部门数据
     */
    public ArrayList<HashMap> searchDeptByPage(HashMap param);

    /**
     * 查询部门记录总数量
     * @param param 查询参数
     * @return 部门记录总数
     */
    public long searchDeptCount(HashMap param);

    /**
     * 添加部门数据
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

    /**
     * 批量删除部门
     *
     * @param ids ID集合
     * @return 删除条数
     */
    public int delete(Integer[] ids);

    /**
     * 查询部门是否关联用户
     *
     * @param ids ID集合
     * @return 关联状态
     */
    public boolean searchCanDelete(Integer[] ids);

}