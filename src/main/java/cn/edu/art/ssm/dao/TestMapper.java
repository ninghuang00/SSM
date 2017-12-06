package cn.edu.art.ssm.dao;

import cn.edu.art.ssm.model.Test;
import cn.edu.art.ssm.model.TestExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TestMapper {
    List<Test> getAll();
}