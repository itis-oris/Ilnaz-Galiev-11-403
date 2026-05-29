package com.itis.oris.skilltrade.repository;

import com.itis.oris.skilltrade.entity.User;

import java.util.List;

public interface UserRepositoryCustom {

    List<User> searchUsers(Long skillId, String city, String usernameFragment, Boolean onlyBlocked);
}
