package com.alexdyysp.mapper;

import com.alexdyysp.model.SysRole;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

public class RoleMapperTest extends BaseMapperTest{

    @Test
    public void selectByIdTest(){
        SqlSession sqlSession = getSqlSession();
        try {
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
            SysRole sysRole = roleMapper.selectById(1L);

            Assert.assertNotNull(sysRole);
            Assert.assertEquals("管理员", sysRole.getRoleName());
        }finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectById2(){
        //获取 sqlSession
        SqlSession sqlSession = getSqlSession();
        try {
            //获取 RoleMapper 接口
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
            //调用 selectById 方法，查询 id = 1 的角色
            SysRole role = roleMapper.selectById2(1L);
            //role 不为空
            Assert.assertNotNull(role);
            //roleName = 管理员
            Assert.assertEquals("管理员", role.getRoleName());
        } finally {
            //不要忘记关闭 sqlSession
            sqlSession.close();
        }
    }
}
