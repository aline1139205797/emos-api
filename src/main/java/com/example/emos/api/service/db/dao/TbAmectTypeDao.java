package com.example.emos.api.service.db.dao;

import com.example.emos.api.service.db.pojo.TbAmectType;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface TbAmectTypeDao {
    public ArrayList<TbAmectType> searchAllAmectType();
}
