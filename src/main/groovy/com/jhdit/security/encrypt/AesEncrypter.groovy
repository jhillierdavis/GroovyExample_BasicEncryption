package com.jhdit.security.encrypt

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import java.security.Key
import java.security.MessageDigest
import java.nio.charset.StandardCharsets

/**
 * Performs string & byte array encryption & decryption using the AES (Advanced Encryption Standard) algorithm.
 */

class AesEncrypter implements Encrypter {
    private static final String algorithm = "AES";
    private static final String SHA_256 = "SHA-256"

    private byte[] keyValue // password as byte array

    AesEncrypter(String password)   {
        assert password
        this.keyValue = generateKeyFromPassword(password)
    }

    AesEncrypter(final String password, final String salt)   {
        this(salt + password)

        assert salt
    }

    private byte[] generateKeyFromPassword(String password) {
        byte[] key = (password).getBytes(StandardCharsets.UTF_8);
        MessageDigest sha = MessageDigest.getInstance(SHA_256);
        key = sha.digest(key);

        // assert key.size() == 256 / 8  // 256 bits or 32 bytes
        key
    }

    /**
     * Performs encryption
     * @param plainText
     * @return encrypted text
     */

    String encrypt(String plainText)    {
        assert plainText

        byte[] encVal = this.encrypt(plainText.getBytes())
        String encryptedValue = Base64.getEncoder().encodeToString(encVal)
        return encryptedValue
    }

    byte[] encrypt(byte[] unencryptedByteArray) {
        assert unencryptedByteArray

        Key key = generateKey()
        Cipher cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        byte[] encVal = cipher.doFinal(unencryptedByteArray)
        return encVal
    }

    /**
     * Performs decryption
     * @param encryptedText
     * @return decrypted text
     */

    String decrypt(String encryptedText)    {
        assert encryptedText

        byte[] decodedValue = Base64.getDecoder().decode(encryptedText)
        byte[] decValue = this.decrypt(decodedValue)

        String decryptedValue = new String(decValue)
        return decryptedValue;
    }

    byte[] decrypt(byte[] encryptedByteArray)   {
        Key key = generateKey()
        Cipher cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, key)
        byte[] decValue = cipher.doFinal(encryptedByteArray)
        return decValue
    }

    /**
     * Generate a secret key for an algorithm e.g. AES
     * @return secret key
     */
    private Key generateKey() {
        Key key = new SecretKeySpec(keyValue, algorithm)
        return key;
    }

}