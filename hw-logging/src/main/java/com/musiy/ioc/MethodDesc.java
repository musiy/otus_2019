package com.musiy.ioc;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class MethodDesc {

    private String name;

    private Collection<Class<?>> parameterTypes;

    public MethodDesc(String name, Collection<Class<?>> parameterTypes) {
        this.name = name;
        this.parameterTypes = parameterTypes;
    }

    public boolean methodSignatureEquals(MethodDesc other) {
        return name.equals(other.name)
                && parameterTypes.equals(other.parameterTypes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodDesc that = (MethodDesc) o;
        return methodSignatureEquals(that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parameterTypes);
    }

    @Override
    public String toString() {
        return "MethodDesc{" +
                "name='" + name + '\'' +
                ", parameterTypes=" + parameterTypes +
                '}';
    }

    public static MethodDesc fromMethod(Method method) {
        return new MethodDesc(method.getName(), Arrays.asList(method.getParameterTypes()));
    }
}
