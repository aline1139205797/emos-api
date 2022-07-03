package com.example.emos.api.service.db.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;


@Mapper
public interface TbRoleDao {
    public ArrayList<HashMap> searchAllRole();
    public HashMap searchById(int id);
}