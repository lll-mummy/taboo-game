package edu.upc.lll.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.upc.lll.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
