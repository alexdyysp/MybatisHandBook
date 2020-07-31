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
>曾经的Mybatis2.0时代，需要将命名空间参数存进Map中，很不方便。现在的Mybatis3.0时代支持动态代理，可以直接接口调用。<br>

1. 使用<mapper>根标签的namespace属性：当Mapper接口于XML文件关联，命名空间namespace的值就需要配置成接口的全限定名称。Mybatis内部就是通过这个将接口和XML关联起来的。

2. 在mybatis-config.xml中的<mappers>配置所有的mapper：要么一一列举全部映射文件，要么通过package名映射。   

### Select用法
XML中的`<select>`标签的id属性值和定义的接口方法名是一样的。MyBatis就是通过这种方式将接口方法和XML中定义的SQL语句关联到一起的，如果接口方法没有和XML中的id属性值相对应，启动程序便会报错。映射XML和接口的命名需要符合如下规则。
- 当只使用 XML 而不使用接口的时候，namespace的值可以设置为任意不重复的名称。
- 标签的 id 属性值在任何时候都不能出现英文句号“.”，并且同一个命名空间下不能出现重复的id。
- 因为接口方法是可以重载的，所以接口中可以出现多个同名但参数不同的方法，但是XML中id的值不能重复，因而接口中的所有同名方法会对应着XML中的同一个id的方法。最常见的用法就是，同名方法中其中一个方法增加一个RowBound类型的参数用于实现分页查询。

XML中一些标签和属性的作用:
- `<select＞`：映射查询语句使用的标签。
- id：命名空间中的唯一标识符，可用来代表这条语句。
- resultMap：用于设置返回值的类型和映射关系。
- select 标签中的`select ＊ from sys_user where id=＃{id}`是查询语句。
-`＃{id}`: MyBatis的SQL中使用预编译参数的一种方式，大括号中的 id 是传入的参数名。

Notice：在上面的 select 中，使用resultMap设置返回值的类型，这里的userMap就是上面＜resultMap＞中的id属性值，通过id引用需要的＜resultMap＞。

### Insert用法
insert 要简单很多。只有让它返回主键值时，由于不同数据库的主键生成方式不同，这种情况下会有一些复杂。

`<insert>`标签下包含如下元素：
- id：命名空间中的唯一标识符，可用来代表这条语句。
- parameterType：即将传入的语句参数的完全限定类名或别名。这个属性是可选的，因为MyBatis可以推断出传入语句的具体参数，因此不建议配置该属性。
- flushCache：默认值为 true，任何时候只要语句被调用，都会清空一级缓存和二级缓存。
- timeout：设置在抛出异常之前，驱动程序等待数据库返回请求结果的秒数。
- statementType：对于STATEMENT、PREPARED、CALLABLE，MyBatis会分别使用对应的Statement、PreparedStatement、CallableStatement，默认值为PREPARED。
- useGeneratedKeys：默认值为false。如果设置为true，MyBatis会使用JDBC的getGeneratedKeys方法来取出由数据库内部生成的主键。
- keyProperty：MyBatis通过getGeneratedKeys获取主键值后将要赋值的属性名。如果希望得到多个数据库自动生成的列，属性值也可以是以逗号分隔的属性名称列表。
- keyColumn：仅对INSERT和UPDATE有用。通过生成的键值设置表中的列名。当主键列不是表中的第一列时需要设置。如果希望得到多个生成的列，也可以是逗号分隔的属性名称列表。
- databaseId：如果配置了databaseIdProvider，MyBatis会加载所有的不带databaseId的或匹配当前databaseId的语句。如果同时存在带databaseId和不带databaseId的语句，后者会被忽略。

**Notice**：为了防止类型错误，对于一些特殊的数据类型，建议指定具体的jdbcType值。例如headImg指定BLOB类型，createTime指定TIMESTAMP类型。

### Update用法


### Delete用法
