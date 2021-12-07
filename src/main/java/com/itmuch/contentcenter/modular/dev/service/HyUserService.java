package com.itmuch.contentcenter.modular.dev.service;

import com.itmuch.contentcenter.modular.dev.model.HyUser;
import com.itmuch.contentcenter.modular.dev.model.dto.UserDTO;

public interface HyUserService {

    HyUser modifyUser(Integer id, UserDTO userDTO);
}
