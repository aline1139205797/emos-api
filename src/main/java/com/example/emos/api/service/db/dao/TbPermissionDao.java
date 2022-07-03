package com.example.emos.api.service.db.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface TbPermissionDao {
    public ArrayList<HashMap> searchAllPermission();
}