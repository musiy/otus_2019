package me.musiy.dao;

import me.musiy.repositories.UserRepository;
import me.musiy.userspace.DtoEntity;
import me.musiy.userspace.User;
import org.springframework.stereotype.Service;

@Service
public class DbServiceImpl implements DbService {

    private UserRepository userRepository;

    public DbServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public <T extends DtoEntity> T saveOrUpdate(T obj) {
        if (obj.getClass() == User.class) {
            return (T) userRepository.save((User) obj);
        }
        throw new IllegalArgumentException("Unknown type to handle: " + obj.getClass());
    }

}
