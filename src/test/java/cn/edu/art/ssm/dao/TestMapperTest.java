package cn.edu.art.ssm.dao;

import cn.edu.art.ssm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by huangning on 2017/12/5.
 */
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