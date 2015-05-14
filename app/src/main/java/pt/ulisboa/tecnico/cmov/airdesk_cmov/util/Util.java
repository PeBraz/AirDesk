package pt.ulisboa.tecnico.cmov.airdesk_cmov.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
public class Util {


    public static String generateSecureRandomString() {

        final byte bytes[] = new byte[64];
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.nextBytes(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes){
            sb.append(String.format("%1$02X", b));
        }
        return sb.toString();
    }
}
