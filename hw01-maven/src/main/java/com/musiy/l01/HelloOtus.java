package com.musiy.l01;

import com.google.common.collect.ImmutableSet;

/**
 * Sample application. Home work #1.
 */
public class HelloOtus {

    private static final ImmutableSet<String> COLOR_NAMES = ImmutableSet.of(
            "red",
            "orange",
            "yellow",
            "green",
            "blue",
            "purple");

    public static void main(String[] args) {

        COLOR_NAMES.forEach(System.out::println);
    }
}
