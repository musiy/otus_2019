package com.musiy.loginservice;

import org.eclipse.jetty.security.AbstractLoginService;
import org.eclipse.jetty.util.security.Credential;

import java.util.HashMap;
import java.util.Map;

public class MyLoginService extends AbstractLoginService {

    private Map<String, String> DEFINED_CREDENTIALS = new HashMap<>();

    public MyLoginService() {
        // можно хранить в базе например
        DEFINED_CREDENTIALS.put("admin", "123456");
    }

    @Override
    protected String[] loadRoleInfo(UserPrincipal user) {
        return new String[]{Role.USER.toString()};
    }

    @Override
    protected UserPrincipal loadUserInfo(String username) {
        return new UserPrincipal(username, new Credential() {
            @Override
            public boolean check(Object credentials) {
                if (!(credentials instanceof String)) {
                    throw new RuntimeException("Unknown credential type: " + credentials);
                }
                String password = DEFINED_CREDENTIALS.get(username);
                return credentials.equals(password);
            }
        });
    }
}
