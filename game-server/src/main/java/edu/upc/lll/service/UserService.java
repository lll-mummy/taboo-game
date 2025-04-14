package edu.upc.lll.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.upc.lll.dto.request.AuthRequest;
import edu.upc.lll.pojo.User;

import java.util.Optional;

public interface UserService extends IService<User> {
    User authenticate(AuthRequest authRequest);
}
