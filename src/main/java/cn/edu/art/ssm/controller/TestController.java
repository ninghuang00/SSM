package cn.edu.art.ssm.controller;

import cn.edu.art.ssm.model.Test;
import cn.edu.art.ssm.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangning on 2017/12/5.
 */
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
