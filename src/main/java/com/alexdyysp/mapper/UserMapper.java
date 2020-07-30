package com.alexdyysp.mapper;

import com.alexdyysp.model.SysUser;

import java.util.List;

public interface UserMapper {

    SysUser selectById(Long id);

    List<SysUser> selectAll();
}
