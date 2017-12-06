package cn.edu.art.ssm.service;

import cn.edu.art.ssm.dao.TestMapper;
import cn.edu.art.ssm.model.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by huangning on 2017/12/5.
 */
public class TestService {
    @Autowired
    private TestMapper mapper;

    public List<Test> getAll() {
        return mapper.getAll();
    }


}
