package com.musiy.dao;

import java.util.Collection;

public interface UsersDao {

    Collection<UserDto> getUsers();

    void saveUser(UserDto userDto);

    void deleteUser(UserDto userDto);
}
