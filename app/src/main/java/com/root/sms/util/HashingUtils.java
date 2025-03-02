package com.root.sms.util;

import android.util.Log;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingUtils {

    public static String getMd5HashedString(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashText = new StringBuilder(no.toString(16));
            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }
            return hashText.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e("HashingUtils", "getMd5HashedString: ", e);
            return null;
        }
    }

}
