# Mybatis Demo

## 一、Mybatis怎么跑起来的？
1. 通过Resources工具类将mybatis-config.xml配置文件读入Reader。
2. 再通过SqlSessionFactoryBuilder建造类使用Reader创建SqlSessionFactory工厂对象。在创建SqlSessionFactory对象的过程中，首先解析mybatis-config.xml配置文件，读取配置文件中的 mappers 配置后会读取全部的  Mapper.xml  进行具体方法的解析，在这些解析完成后，  SqlSessionFactory  就包含了所有的属性配置和执行 SQL 的信息。
3. 使用时通过SqlSessionFactory工厂对象获取一个SqlSession。
4. 通过  SqlSession  的  selectList  方法查找到  CountryMapper.xml  中 id=＂selectAll＂的方法，执行 SQL 查询。
5. MyBatis 底层使用 JDBC 执行 SQL，获得查询结果集  ResultSet  后，根据  resultType  的配置将结果映射为 Country 类型的集合，返回查询结果。
6. 这样就得到了最后的查询结果countryList，简单将结果输出到控制台。
7. 最后一定不要忘记关闭  SqlSession  ，否则会因为连接没有关闭导致数据库连接数过多，造成系统崩溃。上面的测试代码

## 二、Mybatis XML的基本用法
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

### 多个接口参数用法
参数的类型可以分为两种：一种是基本类型，另一种是JavaBean。
当参数是一个基本类型的时候，它在 XML 文件中对应的 SQL 语句只会使用一个参数，例如 delete 方法。当参数是一个JavaBean类型的时候，它在 XML 文件中对应的 SQL 语句会有多个参数，例如 insert、update 方法。在实际应用中经常会遇到使用多个参数的情况。

1. 将多个参数合并到一个`JavaBean`中：使用这个JavaBean作为接口方法的参数。这种方法用起来很方便，但并不适合全部的情况，因为不能只为了两三个参数去创建新的JavaBean类，
2. 使用`Map`类型作为参数：使用Map类型作为参数的方法，就是在Map中通过key来映射XML中SQL使用的参数值名字，value用来存放参数值，需要多个参数时，通过Map的key-value方式传递参数值，由于这种方式还需要自己手动创建Map以及对参数进行赋值，其实并不简洁。
3. 使用`@Param`注解：给参数配置`@Param`注解后，MyBatis就会自动将参数封装成`Map`类型，`@Param`注解值会作为Map中的key，因此在SQL部分就可以通过配置的注解值来使用参数。

**疑问**：当只有一个参数（基本类型或拥有TypeHandler配置的类型）的时候，为什么可以不使用注解？
这是因为在这种情况下（除集合和数组外），MyBatis不关心这个参数叫什么名字就会直接把这个唯一的参数值拿来使用。

### Mapper接口动态代理实现原理
为什么Mapper接口没有实现类依然能被正常调用？和XML如何产生关联？
1. 通过Java动态代理创建一个代理类，其中有两个Filed: mapperInterface和sqlsession。
2. 在代理类中重写`invoke`方法，使得当调用一个接口的方法时，会先通过接口的全限定名称和当前调用的方法名的组合得到一个方法id，这个id的值就是映射XML中namespace和具体方法id的组合。
3. 在代理方法中使用sqlSession以命名空间的方式调用方法。

通过这种方式可以将Mapper接口和XML文件中的方法关联起来。这种代理方式和常规代理的不同之处在于，这里没有对某个具体类进行代理，而是通过代理转化成了对其他代码的调用。

## 三、Mybatis注解的基本用法
MyBatis 注解方式就是将SQL语句直接写在接口上。这种方式的优点是，对于需求比较简单的系统，效率较高。缺点是，当SQL有变化时都需要重新编译代码，一般情况下不建议使用注解方式。

在MyBatis注解SQL中，最基本的就是@Select、@Insert、@Update 和@Delete 四种。

### @Select注解

`@Select`注解后一般跟着SELECT的sql语句，注解一般加在Mapper接口类的接口上
```java
@Select({"select id,role_name roleName, enabled, create_by createBy, create_time createTime",
         "from sys_role",
         "where id = #{id}"})
SysRole selectById(Long id);

@Select({"select id,role_name roleName, enabled, create_by createBy, create_time createTime
         from sys_role
         where id = #{id}"})
SysRole selectById(Long id);
```

`@Result`注解对应XML文件中`<result>`元素，参数重写上id=true时对应`<id>`元素

`@Results`注解增加了一个id属性，设置id属性可以通过id属性引用同一个`@Result`配置

```java
@Results(id = "roleResultMap", value = {
    @Result(property = "id", column = "id", id = true),
    @Result(property = "roleName", column = "role_name"),
    @Result(property = "enabled", column = "enabled"),
    @Result(property = "createBy", column = "create_by"),
    @Result(property = "createTime", column = "create_time")
})
@Select("select id,role_name, enabled, create_by, create_time from sys_role where id = #{id}")
SysRole selectById2(Long id);

@ResultMap("roleResultMap")
@Select("select * from sys_role")
List<SysRole> selectAll();
```

### @Insert注解
@Insert注解本身是简单的，但如果需要返回主键的值，情况会变的稍微复杂一些。
#### 不需要返回主键
```java
@Insert({"insert into sys_role(id, role_name, enabled, create_by, create_time)",
        "values(#{id}, #{roleName}, #{enabled}, #{createBy}, #{createTime, jdbcType=TIMESTAMP})"})
int insert(SysRole sysRole);
```
#### 返回自增主键
```java
@Insert({"insert into sys_role(role_name, enabled, create_by, create_time)",
        "values(#{roleName}, #{enabled}, #{createBy}, #{createTime, jdbcType=TIMESTAMP})"})
@Options(useGeneratedKeys = true, keyProperty = "id")
int insertByUseGeneratedKeys(SysRole sysRole);
```
sql语句中少了id属性，注解多了一个`@Options`，并设置了`useGeneratedKeys`和`keyProperty`属性

#### 返回非自增主键

```java
@Insert({"insert into sys_role(role_name, enabled, create_by, create_time)",
        "values(#{roleName}, #{enabled}, #{createBy}, #{createTime, jdbcType=TIMESTAMP})"})
@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", resultType = Long.class, before = false)
int insertByInsertBySelectKeyTest(SysRole sysRole);
```
注解多了`statement`属性。
配置与返回自增主键基本相同，其中`before`为`false`时功能等同于order="AFTER"。

### @Update
```java
@Update({"update sys_role",
        "set role_name = #{roleName},",
        "enabled = #{enabled},",
        "create_by = #{createBy},",
        "create_time = #{createTime, jdbcType=TIMESTAMP}",
        "where id = #{id}"
})
int updateById(SysRole sysRole);
```

### @Delete
```java
@Delete("delete from sys_role where id = #{id}")
int deleteById(Long id);
```

### 四种Provider注解
除了四种简答常用SQL注解外，MyBatis还提供了4种`@Provider`注解

#### @SelectProvider
Provider注解中提供了两个必填属性type和method。
- type配置是一个包含method属性指定方法的类，这个类必须有空构造方法。
- method属性指定了类中的方法值，这个方法就是执行需要执行的SQL语句，返回值是String类型。

```java
// Mapper接口类中
@SelectProvider(type = PrivilegeProvider.class, method = "selectById")
SysPrivilege selectById(Long id);

// type所指PrivilegeProvider类中的method方法
public String selectById(final Long id){
    return new SQL(){
        {
            SELECT("id, privilege_name, privilege_url");
            FROM("sys_privilege");
            WHERE("id = #{id}");
        }
    }.toString();
}
```

#### @InsertProvider

#### @UpdateProvider

#### @DeleteProvider