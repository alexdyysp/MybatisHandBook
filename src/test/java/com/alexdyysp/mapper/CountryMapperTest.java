package com.alexdyysp.mapper;

import com.alexdyysp.model.Country;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class CountryMapperTest extends BaseMapperTest{

    @Test
    public void testSelectAll(){
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
}
