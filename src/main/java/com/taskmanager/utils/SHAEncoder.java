package com.taskmanager.utils;

import com.taskmanager.exception.AppException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SHAEncoder {

    public static String encodeString(String stringToEncode) {
        try {
            byte[] salt = getSalt();
            return getSHA256SecurePassword(stringToEncode, salt);
        } catch (Exception e) {
            throw new AppException("Unable to encode password.", e);
        }
    }

    private static String getSHA256SecurePassword(String stringToEncode, byte[] salt) throws NoSuchAlgorithmException {
        String generatedPassword;
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(salt);
        byte[] bytes = messageDigest.digest(stringToEncode.getBytes());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            stringBuilder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        generatedPassword = stringBuilder.toString();
        return generatedPassword;
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }

}
