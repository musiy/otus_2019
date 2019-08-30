package me.musiy.repositories;

import me.musiy.userspace.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}