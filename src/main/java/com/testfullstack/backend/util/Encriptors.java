package com.testfullstack.backend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class Encriptors {

    public static final Logger LOG = LoggerFactory.getLogger(Encriptors.class);

    /**
     *
     * It returns a SHA-256 Encriptor
     *
     * @return
     */
    @Bean
    public MessageDigest sha256encriptor()  {
        String algorithm = "SHA-256";
        try{

            return MessageDigest.getInstance(algorithm);

        }catch (NoSuchAlgorithmException e){
            LOG.error(String.format("%s Encriptor does not exist",algorithm));
            throw new RuntimeException(String.format("%s Encriptor does not exist",algorithm));
        }
    }

    /**
     *
     * Creates a hash of the given String
     *
     * @param string
     * @return String SHA256 hash
     */
    public String hashStringToSha256(String string) {

        byte[] hash = sha256encriptor().digest(string.getBytes(StandardCharsets.UTF_8));

        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }


}
