package com.federicoioan.alternativeschool.service.IService;

import com.federicoioan.alternativeschool.model.User;
import com.federicoioan.alternativeschool.model.dto.NewPasswordDto;

import java.util.List;

public interface UserService {

    List<User> findUsers();

    User findUser(Long id);

    User updatePwd(Long userId, NewPasswordDto password);

    User deleteUser(Long id);

}
