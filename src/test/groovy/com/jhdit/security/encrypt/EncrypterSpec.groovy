package com.jhdit.security.encrypt

import spock.lang.Specification


class EncrypterSpec extends Specification {

    def "round trip encryption for plain text string"() {
        setup:
        Encrypter encrypter = new AesEncrypter("testpassword")

        when:
        String msg = "My secret message"
        String encryptedText = encrypter.encrypt(msg)
        String decryptedText = encrypter.decrypt(encryptedText);

        then:
        assert !encryptedText.equals(msg)
        assert !encryptedText.equals(decryptedText)
        assert decryptedText.equals(msg)
    }

    def "ensure different input messages create different encryption output"()    {
        setup:
        Encrypter encrypter = new AesEncrypter("testpassword")

        when:
        String msg = "My secret message"
        String encryptedText = encrypter.encrypt(msg)
        String msg2 = "A different message"
        String encryptedText2 = encrypter.encrypt(msg2)

        then:
        assert !encryptedText.equals(encryptedText2)

    }

    def "ensure different passwords create different encryption output"()   {
        setup:
        Encrypter encrypter = new AesEncrypter("testpassword")
        Encrypter encrypter2 = new AesEncrypter("testpassword2")

        when:
        String msg = "A top secret message"
        String encryptedText = encrypter.encrypt(msg)
        String encryptedText2 = encrypter2.encrypt(msg)

        then:
        assert !encryptedText.equals(encryptedText2)
    }

    def "ensure different instance can decrypt"()   {
        setup:
        String password = "Letme1n!"
        Encrypter encrypter = new AesEncrypter(password)
        Encrypter decrypter = new AesEncrypter(password)

        when:
        String msg = "For your eyes only!"
        String encryptedText = encrypter.encrypt(msg)
        String decryptedText = decrypter.decrypt(encryptedText)

        then:
        assert !decryptedText.equals(encryptedText)
        assert decryptedText.equals(msg)
    }

    def "ensure different salts create different encryption output"() {
        setup:
        Encrypter encrypter = new AesEncrypter("testpassword", "salt")
        Encrypter encrypter2 = new AesEncrypter("testpassword", "sodium chloride")

        when:
        String msg = "A top secret message"
        String encryptedText = encrypter.encrypt(msg)
        String encryptedText2 = encrypter2.encrypt(msg)

        then:
        assert !encryptedText.equals(encryptedText2)
    }
}