package com.alexdyysp.mapper;

import com.alexdyysp.model.SysRole;
import com.alexdyysp.model.SysUser;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
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

    @Test
    public void selectRolesByUserId(){
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<SysRole> sysRoleList = userMapper.selectRolesByUserId(1L);

            Assert.assertNotNull(sysRoleList);
            Assert.assertTrue(sysRoleList.size()>0);
            sysRoleList.forEach(r -> System.out.println(r.toString()));
        }finally {
            sqlSession.close();
        }

    }

    @Test
    public void insertTest(){
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser sysUser = new SysUser("test1", "123456", "test@mybatis.tk",
                    "test info", new byte[]{1,2,3}, new Date());
            userMapper.insert(sysUser);

            // 将新建的对象插入数据库中，特别注意，这里的返回值 result 是执行的 SQL 影响的行数
            int result = userMapper.insert(sysUser);
            // 只插入 1 条数据
            Assert.assertEquals(1, result);
            // id 为 null，我们没有给 id 赋值，并且没有配置回写 id 的值
            Assert.assertNull(sysUser.getId());

        }finally {
            // 回滚一下保护数据库
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void insertByUseGeneratedKeysTest(){
        SqlSession sqlSession = getSqlSession();
        try{
            UserMapper userMapper =sqlSession.getMapper(UserMapper.class);
            SysUser sysUser = new SysUser("test1", "123456", "test@mybatis.tk",
                    "test info", new byte[]{1,2,3}, new Date());
            int result = userMapper.insertByUseGeneratedKeys(sysUser);
            Assert.assertEquals(1, result);
            Assert.assertNotNull(sysUser.getId());
        }finally {
            // 回滚
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void insertBySelectKeyTest(){
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser sysUser = new SysUser("test1", "123456", "test@mybatis.tk",
                    "test info", new byte[]{1,2,3}, new Date());
            int result = userMapper.insertByUseGeneratedKeys(sysUser);
            Assert.assertEquals(1, result);
            Assert.assertNotNull(sysUser.getId());
        }finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }
}
