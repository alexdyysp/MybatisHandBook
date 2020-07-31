package com.alexdyysp.mapper;

import com.alexdyysp.model.SysRole;
import com.alexdyysp.model.SysUser;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

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
    public void selectById2Test(){
        //获取 sqlSession
        SqlSession sqlSession = getSqlSession();
        try {
            //获取 RoleMapper 接口
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
            //调用 selectById 方法，查询 id = 1 的角色
            SysRole role = roleMapper.selectById2(1L);

            Assert.assertNotNull(role);
            Assert.assertEquals("管理员", role.getRoleName());
        } finally {
            //不要忘记关闭 sqlSession
            sqlSession.close();
        }
    }

    @Test
    public void insertTest(){
        SqlSession sqlSession = getSqlSession();
        try {
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
            SysRole sysRole = new SysRole("testuser", 1, "1", new Date());

            // 将新建的对象插入数据库中，特别注意，这里的返回值 result 是执行的 SQL 影响的行数
            int result = roleMapper.insert(sysRole);
            // 只插入 1 条数据
            Assert.assertEquals(1, result);
            // id 为 null，我们没有给 id 赋值，并且没有配置回写 id 的值
            Assert.assertNull(sysRole.getId());

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
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
            SysRole sysRole = new SysRole("testuser", 1, "1", new Date());

            // 将新建的对象插入数据库中，特别注意，这里的返回值 result 是执行的 SQL 影响的行数
            int result = roleMapper.insertByUseGeneratedKeys(sysRole);

            Assert.assertEquals(1, result);
            Assert.assertNotNull(sysRole.getId());
            System.out.println(sysRole.getId());
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
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
            SysRole sysRole = new SysRole("testuser", 1, "1", new Date());

            int result = roleMapper.insertByUseGeneratedKeys(sysRole);
            Assert.assertEquals(1, result);
            Assert.assertNotNull(sysRole.getId());
            System.out.println(sysRole.getId());
        }finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testUpdateById(){
        //获取 sqlSession
        SqlSession sqlSession = getSqlSession();
        try {
            //获取 RoleMapper 接口
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
            //由于数据库数据 enable 都是 1，所以我们给其中一个角色的 enable 赋值为 0
            SysRole role = roleMapper.selectById(2L);
            Assert.assertEquals(role.getEnabled(), role.getEnabled());
            role.setEnabled(0);
            roleMapper.updateById(role);
        } finally {
            sqlSession.rollback();
            //不要忘记关闭 sqlSession
            sqlSession.close();
        }
    }

    @Test
    public void deleteByIdTest() {
        SqlSession sqlSession = getSqlSession();
        try {
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
            //从数据库查询 1 个 SysRole 对象，根据 id = 1 查询
            SysRole role = roleMapper.selectById(1L);
            //现在还能查询出 SysRole 对象
            Assert.assertNotNull(role);
            //调用方法删除
            Assert.assertEquals(1, roleMapper.deleteById(1L));
            //再次查询，这时应该没有值，为 null
            Assert.assertNull(roleMapper.selectById(1L));

        } finally {
            sqlSession.rollback();
            //不要忘记关闭 sqlSession
            sqlSession.close();
        }
    }

}
