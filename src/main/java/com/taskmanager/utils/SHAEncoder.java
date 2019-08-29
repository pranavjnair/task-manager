package com.taskmanager.utils;

import com.taskmanager.exception.AppException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Encodes SHA-256
 */
public class SHAEncoder {

    /**
     * Uses a string to encode a SHA-256 for a password
     *
     * @param stringToEncode - string that will become encoded
     * @return - string that is encoded with SHA-256 but with hex string
     */
    public static String encodeString(String stringToEncode) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                    stringToEncode.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (Exception e) {
            throw new AppException("Unable to encode password.", e);
        }
    }

    /**
     * Changes the hash into hex format
     *
     * @param hash - bytes that need to be changed
     * @return - hashed string that is readable
     */
    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
