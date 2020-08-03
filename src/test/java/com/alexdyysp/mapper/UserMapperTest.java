package com.alexdyysp.mapper;

import com.alexdyysp.model.SysPrivilege;
import com.alexdyysp.model.SysRole;
import com.alexdyysp.model.SysUser;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import javax.jws.soap.SOAPBinding;
import java.util.*;

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

            sysUser.setUserName("test2");
            sysUser.setUserEmail("");
            result = userMapper.insertByUseGeneratedKeys(sysUser);
            Assert.assertEquals(1, result);
            sysUser = userMapper.selectByUserName("test2");
            Assert.assertEquals(sysUser.getUserEmail(),"test@mybatis.tk");
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

    @Test
    public void updateByIdTest(){
        SqlSession sqlSession = getSqlSession();
        try{
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser sysUser = userMapper.selectById(1L);

            Assert.assertEquals("admin", sysUser.getUserName());
            sysUser.setUserName("admin_test");
            sysUser.setUserEmail("test@mybatis.tk");

            int result = userMapper.updateById(sysUser);

            Assert.assertEquals(1, result);
            Assert.assertNotNull("admin_test", sysUser.getUserName());

        }finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void deleteByIdTest() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            //从数据库查询 1 个 user 对象，根据 id = 1 查询
            SysUser user1 = userMapper.selectById(1L);
            //现在还能查询出 user 对象
            Assert.assertNotNull(user1);
            //调用方法删除
            Assert.assertEquals(1, userMapper.deleteById(1L));
            //再次查询，这时应该没有值，为 null
            Assert.assertNull(userMapper.selectById(1L));

            //使用 SysUser 参数再做一遍测试，根据 id = 1001  查询
            SysUser user2 = userMapper.selectById(1001L);
            //现在还能查询出 user 对象
            Assert.assertNotNull(user2);
            //调用方法删除，注意这里使用参数为 user2
            Assert.assertEquals(1, userMapper.deleteById(user2.getId()));
            //再次查询，这时应该没有值，为 null
            Assert.assertNull(userMapper.selectById(1001L));
            //使用 SysUser 参数再做一遍测试
        } finally {
            //为了不影响数据库中的数据导致其他测试失败，这里选择回滚
            //由于默认的 sqlSessionFactory.openSession() 是不自动提交的，
            //因此不手动执行 commit 也不会提交到数据库
            sqlSession.rollback();
            //不要忘记关闭 sqlSession
            sqlSession.close();
        }
    }

    @Test
    public void selectRolesByUserIdAndRoleEnabledTest(){
        SqlSession sqlSession = getSqlSession();
        try{
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            List<SysRole> userList = userMapper.selectRolesByUserIdAndRoleEnabled(1L, 1);

            Assert.assertNotNull(userList);
            Assert.assertTrue(userList.size()>0);
        }finally {
            sqlSession.close();
        }
    }

    @Test
    public void selectByUserNameOrUserEmailTest(){
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser querySysUser = new SysUser();

            // 只查userName
            querySysUser.setUserName("ad");
            List<SysUser> sysUserList = userMapper.selectByUserNameOrUserEmail(querySysUser);
            Assert.assertTrue(sysUserList.size()>0);

            // 只查userEmail
            querySysUser = new SysUser();
            querySysUser.setUserEmail("test@mybatis.tk");
            sysUserList = userMapper.selectByUserNameOrUserEmail(querySysUser);
            Assert.assertTrue(sysUserList.size()>0);

            // 同时查询userName 和 userEmail
            querySysUser = new SysUser();
            querySysUser.setUserName("ad");
            querySysUser.setUserEmail("admin@mybatis.tk");
            sysUserList = userMapper.selectByUserNameOrUserEmail(querySysUser);
            Assert.assertTrue(sysUserList.size()>0);
        }finally {
            sqlSession.close();
        }
    }

    @Test
    public void updateByIdSelectiveTest(){
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            //从数据库查询 1 个 user 对象
            SysUser user = new SysUser();
            //更新 id = 1 的用户
            user.setId(1L);
            //修改邮箱
            user.setUserEmail("test@mybatis.tk");
            //将新建的对象插入数据库中，特别注意，这里的返回值result是执行的SQL影响的行数
            int result = userMapper.updateByIdSelective(user);
            //只更新 1 条数据
            Assert.assertEquals(1, result);
            //根据当前 id 查询修改后的数据
            user = userMapper.selectById(1L);
            //修改后的名字保持不变，但是邮箱变成了新的
            Assert.assertEquals("admin", user.getUserName());
            Assert.assertEquals("test@mybatis.tk", user.getUserEmail());
        } finally {
            //为了不影响数据库中的数据导致其他测试失败，这里选择回滚
            sqlSession.rollback();
            //不要忘记关闭 sqlSession
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByIdOrUserName(){
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            //只查询用户名时
            SysUser query = new SysUser();
            query.setId(1L);
            query.setUserName("admin");
            SysUser user = userMapper.selectByIdOrUserName(query);
            Assert.assertNotNull(user);
            //当没有 id 时
            query.setId(null);
            user = userMapper.selectByIdOrUserName(query);
            Assert.assertNotNull(user);
            //当 id 和 name 都为空时
            query.setUserName(null);
            user = userMapper.selectByIdOrUserName(query);
            Assert.assertNull(user);
        } finally {
            //不要忘记关闭 sqlSession
            sqlSession.close();
        }
    }

    @Test
    public void selectByIdListTest(){
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<Long> idList = new ArrayList<Long>();
            idList.add(1L);
            idList.add(1001L);

            List<SysUser> userList = userMapper.selectByIdList(idList);

            Assert.assertEquals(2, userList.size());

        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void insertListTest(){
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            //创建一个 user 对象
            List<SysUser> userList = new ArrayList<SysUser>();
            for(int i = 0; i < 2; i++){
                SysUser user = new SysUser();
                user.setUserName("test" + i);
                user.setUserPassword("123456");
                user.setUserEmail("test@mybatis.tk");
                userList.add(user);
            }
            //将新建的对象批量插入数据库中，特别注意，这里的返回值 result 是执行的 SQL 影响的行数
            int result = userMapper.insertListByForEach(userList);
            Assert.assertEquals(2, result);
            for(SysUser user : userList){
                System.out.println(user.getId());
            }
        } finally {
            //为了不影响数据库中的数据导致其他测试失败，这里选择回滚
            sqlSession.rollback();
            //不要忘记关闭 sqlSession
            sqlSession.close();
        }
    }

    @Test
    public void updateByMapTest(){
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", 1L);
            map.put("user_email", "test@mybatis.tk");
            map.put("user_password", "12345678");

            userMapper.updateByMapForEach(map);
            SysUser sysUser = userMapper.selectById(1L);

            Assert.assertEquals("test@mybatis.tk", sysUser.getUserEmail());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void selectUserAndRoleByIdTest(){
        SqlSession sqlSession = getSqlSession();
        try{
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser sysUser = userMapper.selectUserAndRoleById(1001L);
            SysUser sysUser2 = userMapper.selectUserAndRoleById2(1001L);
            SysUser sysUser3 = userMapper.selectUserAndRoleById3(1001L);

            Assert.assertNotNull(sysUser);
            Assert.assertNotNull(sysUser.getSysRole());
            Assert.assertNotNull(sysUser2);
            Assert.assertNotNull(sysUser2.getSysRole());
            Assert.assertNotNull(sysUser3);
            Assert.assertNotNull(sysUser3.getSysRole());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void selectUserAndRoleByIdSelectTest(){
        SqlSession sqlSession = getSqlSession();
        try{
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            SysUser sysUser = userMapper.selectUserAndRoleByIdSelect(1001L);
            Assert.assertNotNull(sysUser);
            System.out.println("lazy call getsysRole()");
            Assert.assertNotNull(sysUser.getSysRole());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectAllUserAndRoles() {
        SqlSession sqlSession = getSqlSession();
        try {
            //获取 UserMapper 接口
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<SysUser> userList = userMapper.selectAllUserAndRoles();
            System.out.println("用户数：" + userList.size());
            for (SysUser user : userList) {
                System.out.println("用户名：" + user.getUserName());
                for (SysRole role : user.getRoleList()) {
                    System.out.println("角色名：" + role.getRoleName());
                    for (SysPrivilege privilege : role.getPrivilegeList()) {
                        System.out.println("权限名：" + privilege.getPrivilegeName());
                    }
                }
            }
        } finally {
            //不要忘记关闭 sqlSession
            sqlSession.close();
        }
    }

    @Test
    public void selectAllUserAndRolesSelectTest(){
        //获取 sqlSession
        SqlSession sqlSession = getSqlSession();
        try {
            //获取 UserMapper 接口
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser user = userMapper.selectAllUserAndRolesSelect(1L);
            System.out.println("用户名：" + user.getUserName());
            for(SysRole role: user.getRoleList()){
                System.out.println("角色名：" + role.getRoleName());
                for(SysPrivilege privilege : role.getPrivilegeList()){
                    System.out.println("权限名：" + privilege.getPrivilegeName());
                }
            }
        } finally {
            //不要忘记关闭 sqlSession
            sqlSession.close();
        }
    }

}
