package me.musiy.services;

import me.musiy.userspace.DtoEntity;
import me.musiy.userspace.User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DtoFactoryImpl implements DtoFactory {

    @Override
    public DtoEntity entity(String name, Map<String, String> values) {
        if ("user".equals(name)) {
            User user = new User();
            user.setName(values.get("fio"));
            user.setAge(Integer.valueOf(values.get("age")));
            return user;
        } else {
            throw new IllegalArgumentException("unknown entity");
        }
    }
}
