package com.alexdyysp.mapper;

import com.alexdyysp.model.SysRole;
import com.alexdyysp.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    // 根据Id获取用户信息
    SysUser selectById(Long id);

    // 获取全部用户
    List<SysUser> selectAll();

    // 根据用户名查询
    SysUser selectByUserName(String userName);

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

    // 根据动态条件查询用户信息
    List<SysUser> selectByUserNameOrUserEmail(SysUser sysUser);

    // 根据主键和动态条件更新用户信息
    int updateByIdSelective(SysUser sysUser);

    // 根据用户Id或用户名查询
    SysUser selectByIdOrUserName(SysUser sysUser);

    // 根据用户id集合查询
    List<SysUser> selectByIdList(List<Long> idList);

    // 批量插入用户信息
    int insertListByForEach(List<SysUser> userList);

    // 通过Map更新列
    int updateByMapForEach(Map<String, Object> map);

    // 通过用户Id高级查询用户信息和用户权限信息
    SysUser selectUserAndRoleById(Long Id);
    SysUser selectUserAndRoleById2(Long Id);  // resultType
    SysUser selectUserAndRoleById3(Long Id);  // resultType association

    SysUser selectUserAndRoleByIdSelect(Long Id);

    // 获取所有用户和对应所有角色
    List<SysUser> selectAllUserAndRoles();

    // 通过嵌套获取指定用户信息和权限
    SysUser selectAllUserAndRolesSelect(Long id);

}
