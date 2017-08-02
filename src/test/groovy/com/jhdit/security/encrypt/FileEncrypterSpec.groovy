package com.jhdit.security.encrypt

// import org.apache.commons.logging.LogFactory
// import org.springframework.core.io.ClassPathResource
// import org.springframework.core.io.Resource
import spock.lang.Specification
import spock.lang.*

class FileEncrypterSpec extends Specification {
    private static final String TEST_RESOURCE_DIR = 'src/test/resources/'
    private static final String TEST_PLAIN_TEXT_FILE =  TEST_RESOURCE_DIR + 'file.txt'
    private static final String TEST_BINARY_FILE = TEST_RESOURCE_DIR + 'image.png'
    private static final String TEST_DESTINATION_DIR = "/tmp/"
    private static final String TEST_ENCRYPTION_EXTENSION = "encrypted"

    // def log = LogFactory.getLog(getClass())

    def "round-trip encryption/decryption for plain text file"()   {
        setup:
            File input =  new File(TEST_PLAIN_TEXT_FILE)
            assert input.exists()
            Encrypter encrypter = new AesEncrypter("testpassword")
            FileEncryptor fileEncrypter = new FileEncryptor(encrypter)

        when:
            File encryptedFile = fileEncrypter.createEncryptedFile(input, TEST_DESTINATION_DIR, TEST_ENCRYPTION_EXTENSION)
            File decryptedFile = fileEncrypter.createDecryptedFile(encryptedFile, TEST_DESTINATION_DIR, TEST_ENCRYPTION_EXTENSION)

        then:
            assert input.size() != encryptedFile.size()
            assert decryptedFile.size() != encryptedFile.size()
            assert decryptedFile.size() == input.size()
            assert input.text == decryptedFile.text
    }

    def "round-trip encryption/decryption for binary file"()   {
        setup:
            File input =  new File(TEST_BINARY_FILE)
            Encrypter encrypter = new AesEncrypter("Letme1n!")
            FileEncryptor fileEncrypter = new FileEncryptor(encrypter)

        when:
            File encryptedFile = fileEncrypter.createEncryptedFile(input, TEST_DESTINATION_DIR, TEST_ENCRYPTION_EXTENSION)
            File decryptedFile = fileEncrypter.createDecryptedFile(encryptedFile, TEST_DESTINATION_DIR, TEST_ENCRYPTION_EXTENSION)

        then:
            assert input.size() != encryptedFile.size()
            assert decryptedFile.size() != encryptedFile.size()
            assert decryptedFile.size() == input.size()
            assert input.text == decryptedFile.text
    }

    def "check decryption fails with different password"()   {
        setup:
            File input =  new File(TEST_BINARY_FILE)
            Encrypter encrypter = new AesEncrypter("Letme1n!")
            FileEncryptor fileEncrypter = new FileEncryptor(encrypter)
            File encryptedFile = fileEncrypter.createEncryptedFile(input, TEST_DESTINATION_DIR, TEST_ENCRYPTION_EXTENSION)

        when: "decryption is attempted it fails"
            Encrypter decrypter = new AesEncrypter("Different!")
            FileEncryptor fileDecrypter = new FileEncryptor(decrypter)
            fileDecrypter.createDecryptedFile(encryptedFile, TEST_DESTINATION_DIR, TEST_ENCRYPTION_EXTENSION)

        then: "the exception is caught & can be verified"
            final javax.crypto.BadPaddingException e = thrown()
            e.message == "Given final block not properly padded"
    }


}