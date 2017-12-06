## SSM框架搭建流程
### 1.在maven框架中选择web的框架
### 2.自动生成项目后在`src/main/`路径下新建自己的包
### 3. 在`resources`目录下新建各种配置文件
* 首先是数据库配置文件`db.properties`
* 开启mybatis逆向工程,用来生成dao的mapper和model
在`pom.xml`中添加mybatis.jar的依赖以及如下插件:
```
<dependencies>
    ...
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.2.8</version>
    </dependency>
    ...
</dependencies>

<build>
    <finalName>artedu</finalName>

    <plugins>
        <!--mybatis逆向工程插件-->
        <plugin>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-maven-plugin</artifactId>
            <version>1.3.2</version>
        </plugin>
    </plugins>
</build>
```
需要用到的数据库驱动jar放在resources目录下,记得在配置文件db.properties里面改路径,配置好以后再`Run/Debug Configuration`中新建maven配置,在command line中添加`mybatis-generator:generate -e`,然后运行即可.
 
PS:也可以配置好generatorConfig.xml后,打开终端,cd到该文件目录下,执行下面的命令:(好像行不通)
```
java -jar mybatis-generator-core-1.3.2.jar -configfile generatorConfig.xml -overwrite
```
PPS:在`pom.xml`文件中还要配置一下内容,不然`*mapper.xml`文件将无法再编译的时候拷贝到`classes`文件夹
```
<build>
        ...
        <!--很重要要要是没有配置的话,mapper文件夹下的.xml文件编译的时候就不会被拷贝到classes中-->
        <resources>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>
        </resources>
        ...
    </build>
```
* 然后配置`spring`文件夹中的两个个文件
    * 第一个是`applicationContext.xml`文件
    1. spring-dao部分
      
        1. 引入`db.properties`
        2. 配置数据库连接池的bean
        3. 配置SQLSessionFactory对象  
        配置中引入`mybatis-config.xml`
        4. 配置dao接口包,动态实现dao接口,注入到spring容器中
    2. spring-service部分  
        
        1. 扫描service包所有注解 @Service
        2. 配置事务管理器，把事务管理交由spring来完成
        3. 配置基于注解的声明式事务，可以直接在方法上@Transaction

    * 然后是`spring-web.xml`文件  
    
        1. 开启SpringMVC注解模式，可以使用@RequestMapping，@PathVariable，@ResponseBody等
        2. 对静态资源处理，如js，css，jpg等
        3. 配置jsp 显示ViewResolver，例如在controller中某个方法返回一个string类型的"login"，实际上会返回"/WEB-INF/login.jsp"
        4. 扫描web层 @Controller  
        
     
* 然后在`dao`和`model`路径下创建文件测试数据库连接  
可以使用mybatis逆向工程插件直接将数据库中的表转换成类文件

### 3. 注意事项
1. 测试springmvc的时候要注意由于配置问题,浏览器访问时要加.action的问题  
2. 注意使用el表达式时,tomcat要6.0以上,`web.xml`中的schema要2.5或以上

### 4. 测试流程总结
1. 测试dao层  
首先创建mysql数据库和表,然后插入数据
```
create database artedu;
use artedu;
create table(
testid varchar(10) primary key,
testname varchar(20)
);
insert into test values('000','mending');
```
然后使用mybatis逆向工程创建model和mapper(或者手动编写)
* `model/Test.java`  
```
public class Test {
       private String testid;
   
       private String testname;
   
       public Test(String testid, String testname) {
           this.testid = testid;
           this.testname = testname;
       }
   
       public Test() {
           super();
       }
   
       public String getTestid() {
           return testid;
       }
   
       public void setTestid(String testid) {
           this.testid = testid == null ? null : testid.trim();
       }
   
       public String getTestname() {
           return testname;
       }
   
       public void setTestname(String testname) {
           this.testname = testname == null ? null : testname.trim();
       }
   }
```

* `dao/TestMapper.java`
```
public interface TestMapper {
    List<Test> getAll();
}
```
* `dao/TestMaper.xml`
```
<?xml version="1.0" encoding="UTF-8"?>
   <!DOCTYPE mapper
           PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   <mapper namespace="cn.edu.art.ssm.dao.TestMapper">
     <!-- 目的：为dao接口方法提供sql语句配置 -->
   
     <select id="getAll" resultType="Test"  >
       select
       *
       from test
     </select>
   
   </mapper>
   
   
```
* 测试文件夹中新建`BaseTest.java`,加载spring,mybatis等的配置文件
```
/**
 * 配置spring和junit整合，junit启动时加载springIOC容器 spring-test,junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring配置文件
@ContextConfiguration({ "classpath:spring/applicationContext.xml", "classpath:spring/spring-mvc.xml" })
public class BaseTest {

}
```
之后创建`TestMapper.java`的测试文件`TestMapperTest.java`继承`BaseTest.java`
```
public class TestMapperTest extends BaseTest{
       @Autowired
       private TestMapper mapper;
       @Test
       public void queryAll() throws Exception {
           System.out.println("11");
           List<cn.edu.art.ssm.model.Test> list = mapper.getAll();
           System.out.println("22");
           for (cn.edu.art.ssm.model.Test test : list) {
               System.out.println(test.getTestid() + ":" + test.getTestname());
           }
       }
   
   }
```
2. 测试service层
* 在`service`中创建`TestService.java`文件
```
public class TestService {
       @Autowired
       private TestMapper mapper;
   
       public List<Test> getAll() {
           return mapper.getAll();
       }
   
   
   }
```
然后创建测试文件`TestServiceTest.java`
```
public class TestServiceTest extends BaseTest {
    @Autowired
    TestService service;
    @Test
    public void getAll() throws Exception {

        System.out.println("++++++++++++=============" + service.getAll().get(0).getTestname());
    }

}
```

3. 测试controller层
* 在`controller`中创建`TestController.java`
```
@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TestService service;
    //之后再浏览器中访问的连接就是localhost:8080/项目名/test/testJsp.action
    @RequestMapping("/testJsp")
    public ModelAndView showJsp() {
        List<Test> tests = service.getAll();
        System.out.println("=========================" + tests.get(0).getTestname() + "===================");
        Test test = tests.get(0);
        Map<String, Object> model = new HashMap<>();
        model.put("test", test);
        //返回视图和数据,返回"testJsp"就相当于跳转到"/WEB-INF/jsp/testJsp.jsp",model保存请求的数据,在JSP中可以使用el表达式取出
        return new ModelAndView("testJsp", model);
    }
}

```
在`webapp/WEB-INF/jsp`中新建`testJsp.jsp`
```
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>测试</title>
</head>
<body>
just a test <br>
name is ${test.testname} <br>
id is ${test.testid}


</table>
</body>
</html>

```
然后在`Edit Configuration`中添加tomcat服务器,配置`deployment`中添加项目的explored形式,`application context`填写`/项目名`,  
然后`server`中,`on frame`选`update classes and resources`,`on update action`选`redeploy`,这样配置修改Jsp文件不需要重新启动tomcat,  
配置完成后启动tomcat服务器,在浏览器中访问链接,localhost:8080/artedu/test/testJsp.action  



    

