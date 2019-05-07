package com.musiy.aop_log.internal;

import com.musiy.ioc.aop.AopMethodInterceptor;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Выполняет логирование.
 * todo здесь можно было бы подключить org.slf4j, для упрощения не делаем..
 */
public class LogHandler implements AopMethodInterceptor {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm:ss");

    @Override
    public void invoke(Object proxy, Object origin, Method method, Object[] args) {

        LocalDateTime localDateTime = LocalDateTime.now();
        String time = dateTimeFormatter.format(localDateTime);

        StringBuilder sb = new StringBuilder();

        sb.append(time).append(" - ");
        sb.append(origin.getClass().getName()).append("::").append(method.getName());
        sb.append(" - [");
        boolean isFirst = true;
        for (int i = 0; i < method.getParameterTypes().length; i++) {
            if (!isFirst) {
                sb.append(", ");
            }

            isFirst = false;
            sb.append(method.getParameterTypes()[i]);
            sb.append(": ");
            sb.append(args[i]);
        }
        sb.append("]");
        System.out.println(sb.toString());
    }
}
