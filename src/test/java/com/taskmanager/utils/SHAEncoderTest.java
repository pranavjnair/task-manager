package com.taskmanager.utils;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SHAEncoderTest {

    @Test
    void test1() {
        String encodedString = SHAEncoder.encodeString("password");
        System.out.println(encodedString);
    }

    @Test
    void test2() {
        String encodedString1 = SHAEncoder.encodeString("password");
        String encodedString2 = SHAEncoder.encodeString("password");
        System.out.println(encodedString1);
        System.out.println(encodedString2);
    }

    @Test
    void test3() {
        String encodedString1 = SHAEncoder.encodeString("password");
        String encodedString2 = SHAEncoder.encodeString("other_password");
        System.out.println(encodedString1);
        System.out.println(encodedString2);
    }
}