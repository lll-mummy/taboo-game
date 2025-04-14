package edu.upc.lll.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.upc.lll.dto.request.AuthRequest;
import edu.upc.lll.mapper.UserMapper;
import edu.upc.lll.pojo.User;
import edu.upc.lll.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private  UserMapper userMapper;


    @Override
    public User authenticate(AuthRequest authRequest) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, authRequest.getUsername()));
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }


        if (!user.getPassword().equals(authRequest.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        return user;
    }
}
