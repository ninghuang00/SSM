package cn.edu.art.ssm.service;

import cn.edu.art.ssm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by huangning on 2017/12/5.
 */
public class TestServiceTest extends BaseTest {
    @Autowired
    TestService service;
    @Test
    public void getAll() throws Exception {

        System.out.println("++++++++++++=============" + service.getAll().get(0).getTestname());
    }

}