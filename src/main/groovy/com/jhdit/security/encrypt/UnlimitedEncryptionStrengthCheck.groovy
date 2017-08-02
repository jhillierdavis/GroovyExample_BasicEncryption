package com.jhdit.security.encrypt

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.crypto.SecretKey

// Make a blank 256 Bit AES Key
final SecretKey secretKey = new SecretKeySpec(new byte[32], "AES")
final Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding")

try	{
    encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey) // Throws an invalid key length exception if you don't have JCE Unlimited strength installed
    println "Success: You have JCE unlimited strength policy files configured!"
} catch (Exception e)	{
    println "Failure: You DO NOT have JCE unlimited strength policy files configured!"
}