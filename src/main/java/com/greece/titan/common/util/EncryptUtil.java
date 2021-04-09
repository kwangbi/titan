package com.greece.titan.common.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Key;

public class EncryptUtil {
    private static final Logger logger = LoggerFactory.getLogger(EncryptUtil.class);

    public enum Algorithms {
        AES("AES");
        private final String value;

        public String getValue() {
            return value;
        }

        Algorithms(final String value) {
            this.value = value;
        }
    }

    /**
     * AES 암호화
     *
     * @param message 암호화 대상 문자열
     * @param key     암호화 키
     * @return 암호화된 문자열
     * @throws GeneralSecurityException
     */
    public static String aesEncodeBase64(final String plainString, final String key) throws GeneralSecurityException, IllegalArgumentException {
        final Key aesKey = getKey(Algorithms.AES, key);
        Cipher cipher = null;
        byte[] encrypted = null;

        if (StringUtils.isBlank(plainString) || StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("The value and key can not be empty. value:" + plainString + ", key:" + key);
        }
        try {
            cipher = Cipher.getInstance(Algorithms.AES.getValue());
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            encrypted = cipher.doFinal(plainString.getBytes());
        } catch (final Exception e) {
            logger.warn("Can not encrypt with input value:{} and key:{}.", plainString, key);
            logger.warn("Exception: ", e);
            throw new GeneralSecurityException("Can not encrypt with input value:" + plainString + " and Key:" + key + ".", e);
        }
        final String convertedString = new String(Base64.encodeBase64(encrypted));

        if (StringUtils.isBlank(convertedString)) {
            throw new GeneralSecurityException("Can not encode encrypted string. input value:" + plainString + " and Key:" + key + ".");
        }
        return convertedString;
    }

    /**
     * AES 복호화
     *
     * @param encryptedString 암호화된 문자열
     * @param key             복호화 키
     * @return 복호화된 문자열
     * @throws GeneralSecurityException
     */
    public static String aesDecodeBase64(final String encryptedString, final String key) throws GeneralSecurityException, IllegalArgumentException {
        final Key aesKey = getKey(Algorithms.AES, key);
        Cipher cipher = null;
        String decrypted = null;

        if (StringUtils.isBlank(encryptedString) || StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("The value and key can not be empty. value:" + encryptedString + ", key:" + key);
        }
        try {
            cipher = Cipher.getInstance(Algorithms.AES.getValue());
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            decrypted = new String(cipher.doFinal(Base64.decodeBase64(encryptedString)));
            //decrypted value is null
            if (StringUtils.isBlank(decrypted)) {
                throw new GeneralSecurityException("Can not decrypt with input value:" + encryptedString + " and Key:" + key + ".");
            }
        } catch (final Exception e) {
            logger.warn("Can not decrypt with input value:{} and key:{}.", encryptedString, key);
            logger.warn("Exception: ", e);
            throw new GeneralSecurityException("Can not decrypt with input value:" + encryptedString + " and Key:" + key + ".", e);
        }
        return decrypted;
    }

    /**
     * Key 조회
     *
     * @param Algorithm
     * @param key
     * @return
     */
    public static Key getKey(final Algorithms Algorithm, final String key) {
        return new SecretKeySpec(key.getBytes(), Algorithm.getValue());
    }


}
