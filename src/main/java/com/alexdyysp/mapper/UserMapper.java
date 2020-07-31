package com.alexdyysp.mapper;

import com.alexdyysp.model.SysRole;
import com.alexdyysp.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    // 根据Id获取用户信息
    SysUser selectById(Long id);

    // 获取全部用户
    List<SysUser> selectAll();

    // 根据用户Id获取用户拥有的所有角色
    List<SysRole> selectRolesByUserId(Long userId);

    // 新增用户
    int insert(SysUser sysUser);

    // 新增用户——使用useGeneratedKeys方式
    int insertByUseGeneratedKeys(SysUser sysUser);

    // 新增用户——使用SelectKey方式
    int insertBySelectKey(SysUser sysUser);

    // 根据主键Id更新用户数据
    int updateById(SysUser sysUser);

    // 根据主键Id删除用户数据
    int deleteById(Long id);

    // 根据主键Id和角色enabled状态获取用户的角色
    List<SysRole> selectRolesByUserIdAndRoleEnabled(@Param("userId")Long userId, @Param("enabled")Integer enabled);
}
