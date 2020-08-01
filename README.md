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

## MyBatis动态SQL
MyBatis的强大特性之一便是它的动态SQL。使用过JDBC或其他类似框架的人都会知道，根据不同条件拼接SQL语句时不仅不能忘了必要的空格，还要注意省略掉列名列表最后的逗号，处理方式麻烦且凌乱。<br>
MyBatis 的动态 SQL 则能让我们摆脱这种痛苦。在 MyBatis 3 之前的版本中，使用动态 SQL 需要学习和了解非常多的标签。<br>
现在 MyBatis 采用了功能强大的OGNL(Object-Graph Navigation Language)表达式语言消除了许多其他标签。<br>
以下是MyBatis的动态SQL在XML中支持的几种标签。
- if
- choose(when/oterwise)
- trim(where/set)
- foreach
- bind

### if用法
`<if>`标签通常用于WHERE语句中，通过判断参数值来决定是否使用某个查询条件，它也经常用于UPDATE语句中判断是否更新某一个字段，还可以在INSERT语句中用来判断是否插入某个字段的值。

#### WHERE中使用`<if>`
if标签有一个必填的属性test: test的属性值是一个符合OGNL要求的判断表达式，表达式的结果可以是true或false，除此之外所有的非0值都为true，只有0为false。为了方便理解，在表达式中，建议只用true或false作为结果。
- 判断条件`property！=null`或`property==null`：适用于任何类型的字段，用于判断属性值是否为空。
- 判断条件`property！=''`或`property==''`：仅适用于String类型的字段，用于判断是否为空字符串。
- and和or：当有多个判断条件时，使用`and`或`or`进行连接，嵌套的判断可以使用小括号分组，`and`相当于Java中的与`&&`，`or`相当于Java中的或`||`。上面两个条件的属性类型都是String，对字符串的判断和Java中的判断类似，首先需要判断字段是否为 null，然后再去判断是否为空（在 OGNL 表达式中，这两个判断的顺序不会影响判断的结果，也不会有空指针异常）。

#### UPDATE中使用`<if>`
现在要实现这样一个需求：只更新有变化的字段。需要注意，更新的时候不能将原来有值但没有发生变化的字段更新为空或 null。通过`<if>`标签可以实现这种动态列更新。这里要结合业务层的逻辑判断，确保最终产生的SQL语句没有语法错误。
需要注意的有两点：
1. 每个`<if>`元素里面 SQL 语句后面的逗号；
2. `where`关键字前面的`id=＃{id}`这个条件。

以下两种情况可以帮助大家理解为什么需要关注这两点。
1. 全部的查询条件都是 `null` 或者空。则 `set` 关键字后面没有内容，直接是 `where` 关键字，不符合 SQL 语句规范。
2. 查询条件只有一个不是 `null` 也不是空。如果有 `id=＃{id}` 这个条件，则合规。

#### INSERT中使用`<if>`
在数据库表中插入数据的时候，如果某一列的参数值不为空，就使用传入的值，如果传入参数为空，就使用数据库中的默认值（通常是空），而不使用传入的空值。使用`<if>`就可以实现这种动态插入列的功能。

在 INSERT 中使用时要注意，若在列的部分增加 `<if>` 条件，则 values 的部分也要增加相同的 `<if>` 条件，必须保证上下可以互相对应，完全匹配。

### choose用法
上一节的 if 标签提供了基本的条件判断，但是它无法实现  if...else  、  if...else...  的逻辑，要想实现这样的逻辑，就需要用到  choose when otherwise  标签。<br>
choose 元素中包含 when 和  otherwise  两个标签，一个 choose 中至少有一个 when，有 0 个或者 1 个  otherwise  。在已有的 sys_user 表中，除了主键 id 外，我们认为 user_name（用户名）也是唯一的，所有的用户名都不可以重复。

现在进行如下查询：当参数 id 有值的时候优先使用 id 查询，当 id 没有值时就去判断用户名是否有值，如果有值就用用户名查询，如果用户名也没有值，就使 SQL 查询无结果。

### where/set/trim用法
> 这三个标签解决了类似的问题，where和set都属于trim的一种具体用法

#### where用法
>`<where>`标签的作用：如果该标签包含的元素中有返回值，就插入一个 where；如果 where 后面的字符串是以 AND 和 OR 开头的，就将它们剔除。

当 if 条件都不满足的时候，where 元素中没有内容，所以在 SQL 中不会出现 where，也就不存在 SQL 拼接错误的问题。如果 if 条件满足，where 元素的内容就是以 and 开头的条件，where 会自动去掉开头的 and，这也能保证 where 条件正确。和需要写入` where 1=1 `这样的条件相比，这种情况下生成的 SQL 更干净、更贴切。

```xml
<select id="selectByUserNameOrUserEmail" resultType="com.alexdyysp.model.SysUser">
    select id,
        user_name userName,
        user_password userPassword,
        user_email userEmail,
        user_info userInfo,
        head_img headImg,
        create_time createTime
    from sys_user
    <where>
        <!--<if test="@com.alexdyysp.utils.StringUtil@isNotEmpty(userName)">-->
        <if test="userName != null and userName != ''">
            and user_name like concat('%', #{userName}, '%')
        </if>
        <if test="userEmail != null and userEmail != ''">
            and user_email = #{userEmail}
        </if>
    </where>
</select>
```
#### set用法
>set 标签的作用：如果该标签包含的元素中有返回值，就插入一个 set；如果 set 后面的字符串是以逗号结尾的，就将这个逗号剔除。

在 set 标签的用法中，SQL 后面的逗号没有问题了，但是如果 set 元素中没有内容，照样会出现 SQL 错误，所以为了避免错误产生，类似 `id=＃{id}` 这样必然存在的赋值仍然有保留的必要。从这一点来看，set 标签并没有解决全部的问题，使用时仍然需要注意。
```xml
<update id="updateByIdSelective">
    update sys_user
    <set>
        <if test="userName != null and username != ''">
            user_name = #{username},
        </if>
        <if test="userPassword != null and userPassword != ''">
            user_password = #{userPassword},
        </if>
        <if test="userEmail != null and userEmail != ''">
            user_email = #{userEmail},
        </if>
        <if test="userInfo != null and userInfo != ''">
            user_info = #{userInfo},
        </if>
        <if test="headImg != null">
            head_img = #{headImg, jdbcType=BLOB},
        </if>
        <if test="createTime != null">
            create_time = #{createTime, jdbcType=TIMESTAMP},
        </if>
        id = #{id},
    </set>
    where id = #{id}
</update>
```

### trim用法
where 和 set 标签的功能都可以用 trim 标签来实现，并且在底层就是通过  TrimSqlNode  实现的。where 标签对应 trim 的实现如下。 
```xml
<trim prefix="WHERE" prefixOverrides="AND | OR ">
...
</trim>
```
**Notice**: 这里的 AND 和 OR 后面的空格不能省略，为了避免匹配到 andes、orders 等单词。

实际的 prefixeOverrides  包含“AND”、“OR”、“AND\n”、“OR\n”、“AND\r”、“OR\r”、“AND\t”、“OR\t”，不仅仅是上面提到的两个带空格的前缀。set 标签对应的 trim 实现如下。 

trim 标签有如下属性。

- `prefix`：当 trim 元素内包含内容时，会给内容增加 prefix 指定的前缀；
- `prefixOverrides`：当 trim 元素内包含内容时，会把内容中匹配的前缀字符串去掉；
- `suffix`：当 trim 元素内包含内容时，会给内容增加 suffix 指定的后缀；
- `suffixOverrides`：当 trim 元素内包含内容时，会把内容中匹配的后缀字符串去掉。

### foreach用法
>SQL 语句中有时会使用 IN 关键字，例如 id in （1，2，3）。可以使用 ${ids}方式直接获取值，但这种写法不能防止 SQL 注入，想避免 SQL 注入就需要用＃{}的方式，这时就要配合使用 foreach 标签来满足需求。
>
>foreach 可以对数组、Map 或实现了  Iterable  接口（如 List、Set）的对象进行遍历。数组在处理时会转换为 List 对象，因此 foreach 遍历的对象可以分为两大类：Iterable 类型和 Map 类型。这两种类型在遍历循环时情况不一样。 
#### foreach实现动态SELECT
>foreach 实现 in 集合foreach 实现 in 集合（或数组）是最简单和常用的一种情况。

foreach 包含以下属性：
- collection：必填，值为要迭代循环的属性名。这个属性值的情况有很多。
- item：变量名，值为从迭代对象中取出的每一个值。
- index：索引的属性名，在集合数组情况下值为当前索引值，当迭代循环的对象是 Map 类型时，这个值为 Map 的 key（键值）。
- open：整个循环内容开头的字符串。
- close：整个循环内容结尾的字符串。
- separator：每次循环的分隔符。

collection的属性要如何设置呢？MyBatis是分四种类型处理这种类型的参数的。
1. 只有一个数组参数或集合参数: 默认情况下的 DefaultSqlSession 处理逻辑。 当参数类型为集合的时候，默认会转换为 Map 类型，并添加一个 key 为  collection  的值，如果参数类型是 List 集合，那么就继续添加一个 key 为 list 的值，这样，当 `collection=＂list＂` 时，就能得到这个集合，并对它进行循环操作。当参数类型为数组的时候，也会转换成 Map 类型，默认的 key 为 array,设置foreach标签中的collection属性值为array。上面提到的是数组或集合类型的参数默认的名字。推荐使用@Param 来指定参数的名字，这时  collection  就设置为通过@Param 注解指定的名字。
2. 有多个参数: 当有多个参数的时候，要使用 `@Param` 注解给每个参数指定一个名字，否则在 SQL 中使用参数时就会不方便，因此将  collection  设置为 `@Param` 注解指定的名字即可。
3. 参数是 Map 类型：使用 Map 和使用 `@Param` 注解方式类似，将 collection 指定为对应 Map 中的 key 即可。如果要循环所传入的 Map，推荐使用@Param 注解指定名字，此时可将 collection 设置为指定的名字，如果不想指定名字，就使用默认值 `_parameter`。
4. 参数是一个对象：这种情况下指定为对象的属性名即可。当使用对象内多层嵌套的对象时，使用`属性.属性`（集合和数组可以使用下标取值）的方式可以指定深层的属性值。

#### foreach实现动态INSERT
如果数据库支持批量插入，就可以通过 foreach 来实现。批量插入是 SQL-92 新增的特性，目前支持的数据库有 DB2、SQL Server 2008 及以上版本、PostgreSQL 8.2 及以上版本、MySQL、SQLite 3.7.11 及以上版本、H2。<br>
批量插入的语法如下：
```xml
INSERT INTO tablename (column-a, [column-b], ...])
VALUES ('value-1a', ['value-1b', ...]),
VALUES ('value-2a', ['value-2b', ...]),
```
从处理部分看，后面是一个值的循环，可以通过 foreach 实现循环插入。往往第一个值是主键，后面循环非主键。

如果要在MySql中实现批量插入返回自增主键值，需要增加 `useGeneratedKeys="true"` 和 `keyProperty="id"`。

#### foreach实现动态UPDATE
当参数类型是 Map 时，foreach 如何实现动态 UPDATE。当参数是 Map 类型的时候，foreach 标签的 index 属性值对应的不是索引值，而是 Map 中的 key，利用这个 key 可以实现动态 UPDATE。可通过指定的列名和对应的值去更新数据。

### bind用法
bind 标签可以使用 OGNL 表达式创建一个变量并将其绑定到上下文中。 使用 concat 函数连接字符串，在 MySQL 中，这个函数支持多个参数，但在 Oracle 中只支持两个参数。由于不同数据库之间的语法差异，如果更换数据库，有些 SQL 语句可能就需要重写。针对这种情况，可以使用 bind 标签来避免由于更换数据库带来的一些麻烦。

bind 标签的两个属性都是必选项，name 为绑定到上下文的变量名，value 为 OGNL 表达式。创建一个 bind 标签的变量后，就可以在下面直接使用，使用 bind 拼接字符串不仅可以避免因更换数据库而修改 SQL，也能预防 SQL 注入。

```xml
<if test="userName" != null and userName !=''">
    <bind name="userNameLike" value="'%' + userName + '%'"/>
    and user-name like #{userNameLike}
<if>
```

### OGNL用法
>在 MyBatis 的动态 SQL 和 ${}形式的参数中都用到了 OGNL 表达式，所以我们有必要了解一下 OGNL 的简单用法。

MyBatis 常用的 OGNL 表达式如下：
1. `e1 or e2`
2. `e1 and e2`
3. `e1 == e2` 或 `e1 eq e2`
4. `e1 != e2` 或 `e1 neq e2`
5. `e1 lt e2`：小于
6. `e1 lte e2`：小于等于，其他表示为 `gt`（大于）、`gte`（大于等于）
7. `e1+e2`、`e1 ＊ e2`、`e1/e2`、`e1-e2`、`e1%e2`
8. `!e` 或 `not e`：非，取反
9. `e.method(args)`：调用对象方法
10. `e.property`：对象属性值
11. `e1[e2]`：按索引取值（List、数组和 Map）
12. `@class@method(args)`：调用类的静态方法
13. `@class@field`：调用类的静态字段值

## 四、MyBatis代码生成器
>当数据库表的字段比较少的时候，写起来还能接受，一旦字段过多或者需要在很多个表中写这些基本方法时，就会很麻烦，不仅需要很大的代码量，而且字段过多时很容易出现错乱。尤其在新开始一个项目时，如果有几十个甚至上百个表需要从头编写，这将会带来很大的工作量，这样的工作除了能让我们反复熟练这些基本方法外，完全就是重复的体力劳动。作为一个优秀的程序员，“懒”是很重要的优点。我们不仅要会写代码，还要会利用（或自己实现）工具生成代码。
>
>MyBatis 的开发团队提供了一个很强大的代码生成器 —— MyBatis Generator(MBG)。
>
>MBG 通过丰富的配置可以生成不同类型的代码，代码包含了数据库表对应的实体类、Mapper 接口类、  Mapper XML  文件和 Example 对象等，这些代码文件中几乎包含了全部的单表操作方法，使用 MBG 可以极大程度上方便我们使用 MyBatis，还可以减少很多重复操作。

### XML配置详解
