package com.jhdit.security.encrypt

/**
 * Contract for encryption & decryption implementation.
 */

interface Encrypter {

    String encrypt(String plainText)

    String decrypt(String encryptedText)

    byte[] encrypt(byte[] byteArray)

    byte[] decrypt(byte[] byteArray)
}