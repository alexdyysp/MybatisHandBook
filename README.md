# Mybatis Demo

## Mybatis怎么跑起来的？
1. 通过Resources工具类将mybatis-config.xml配置文件读入Reader。
2. 再通过SqlSessionFactoryBuilder建造类使用Reader创建SqlSessionFactory工厂对象。在创建SqlSessionFactory对象的过程中，首先解析mybatis-config.xml配置文件，读取配置文件中的 mappers 配置后会读取全部的  Mapper.xml  进行具体方法的解析，在这些解析完成后，  SqlSessionFactory  就包含了所有的属性配置和执行 SQL 的信息。
3. 使用时通过SqlSessionFactory工厂对象获取一个SqlSession。
4. 通过  SqlSession  的  selectList  方法查找到  CountryMapper.xml  中 id=＂selectAll＂的方法，执行 SQL 查询。
5. MyBatis 底层使用 JDBC 执行 SQL，获得查询结果集  ResultSet  后，根据  resultType  的配置将结果映射为 Country 类型的集合，返回查询结果。
6. 这样就得到了最后的查询结果countryList，简单将结果输出到控制台。
7. 最后一定不要忘记关闭  SqlSession  ，否则会因为连接没有关闭导致数据库连接数过多，造成系统崩溃。上面的测试代码

## Mybatis XML基本用法

