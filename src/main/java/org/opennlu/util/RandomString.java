package org.opennlu.util;

import java.util.Random;

/**
 * Created by René Preuß on 4/29/2018.
 */
public class RandomString {

    /**
     * @author https://stackoverflow.com/a/20536597/1849433
     */
    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 32) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }
}
