package com.xtl.ebusiness.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CommonMapper{

    /**
     * 查询系统环境变量
     */
    @Select("SELECT paramvalue from t_public_paramvalue WHERE module = 'system' AND paramname='config'")
    String querySystemConfig();


    /**
     * 更新系统环境变量
     */
    @Update("UPDATE t_public_paramvalue SET paramvalue = #{configStr} WHERE module = 'system' AND paramname='config'")
    boolean updateSystemConfig(@Param("configStr") String configStr);

}