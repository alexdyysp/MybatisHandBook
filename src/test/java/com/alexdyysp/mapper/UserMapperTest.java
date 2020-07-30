package com.alexdyysp.mapper;

import com.alexdyysp.model.SysUser;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class UserMapperTest extends BaseMapperTest{

    @Test
    public void selectByidTest(){
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser sysUser = userMapper.selectById(1L);

            Assert.assertNotNull(sysUser);
            Assert.assertEquals("admin", sysUser.getUserName());
        }finally {
            sqlSession.close();
        }
    }

    @Test
    public void selectAllTest(){
        SqlSession sqlSession = getSqlSession();
        try{
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<SysUser> sysUserList = userMapper.selectAll();

            Assert.assertNotNull(sysUserList);
            Assert.assertTrue(sysUserList.size()>0);
        }finally {
            sqlSession.close();
        }
    }
}
