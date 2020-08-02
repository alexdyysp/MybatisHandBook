package com.alexdyysp.mapper;

import com.alexdyysp.model.Country;
import mybatis.generator.mapper.CountryMapper;
import mybatis.generator.model.CountryExample;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountryMapperTest extends BaseMapperTest{

    @Test
    public void selectAllTest(){
        SqlSession sqlSession = getSqlSession();
        try {
            List<Country> countryList = sqlSession.selectList("mapper.CountryMapper.selectAll");
            countryList.forEach(country->
                System.out.printf("%-2d%3s%4s\n", country.getId(), country.getCountrycode(), country.getCountryname())
            );
        }finally {
            sqlSession.close();
        }
    }

    @Test
    public void selectByGeneratedExampleTest(){
        SqlSession sqlSession = getSqlSession();
        try {
            CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);
            CountryExample countryExample = new CountryExample();

            // 排序规则
            countryExample.setOrderByClause("id desc, countryname asc");
            // 去重
            countryExample.setDistinct(true);
            // 创建条件
            CountryExample.Criteria criteria = countryExample.createCriteria();

            criteria.andIdGreaterThanOrEqualTo(2);  // Id >= 1
            criteria.andIdLessThan(4);  // id < 4
            criteria.andCountrycodeLike("%U%");

            CountryExample.Criteria or = countryExample.or();
            or.andCountrynameEqualTo("中国");

            List<mybatis.generator.model.Country> countryList = countryMapper.selectByExample(countryExample);
            countryList.forEach(country->
                    System.out.printf("%-2d%3s%4s\n", country.getId(), country.getCountrycode(), country.getCountryname())
            );

        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void updateByGeneratedExampleSelectiveTest() {
        // 获取 sqlSession
        SqlSession sqlSession = getSqlSession();
        try {
            // 获取 CountryMapper 接口
            CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);
            //创建 Example 对象
            CountryExample example = new CountryExample();
            //创建条件，只能有一个 createCriteria
            CountryExample.Criteria criteria = example.createCriteria();
            //更新所有 id > 2 的国家
            criteria.andIdGreaterThan(2);
            //创建一个要设置的对象
            mybatis.generator.model.Country country = new mybatis.generator.model.Country();
            //将国家名字设置为 China
            country.setCountryname("China");
            //执行查询
            countryMapper.updateByExampleSelective(country, example);
            //在把符合条件的结果输出查看
            countryMapper.selectByExample(example).forEach(c ->
                    System.out.printf("%-2d%3s%4s\n", country.getId(), country.getCountrycode(), country.getCountryname())
            );
        } finally {
            // 不要忘记关闭 sqlSession
            sqlSession.close();
        }
    }

    @Test
    public void deleteByExampleTest() {
        // 获取 sqlSession
        SqlSession sqlSession = getSqlSession();
        try {
            // 获取 CountryMapper 接口
            CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);
            //创建 Example 对象
            CountryExample example = new CountryExample();
            //创建条件，只能有一个 createCriteria
            CountryExample.Criteria criteria = example.createCriteria();
            //删除所有 id > 2 的国家
            criteria.andIdGreaterThan(2);
            //执行查询
            countryMapper.deleteByExample(example);
            //使用 countByExample 查询符合条件的数量，因为删除了，所以这里应该是 0
            Assert.assertEquals(0, countryMapper.countByExample(example));
        } finally {
            // 不要忘记关闭 sqlSession
            sqlSession.close();
        }
    }


}
