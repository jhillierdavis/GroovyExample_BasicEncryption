package com.jhdit.security.encrypt

/**
 * File encryptor / decryptor which takes a specific Encrypter (e.g. AesEncrypter) at construction.
 * @See com.jhdit.security.encryption.Encryptor
 */

class FileEncryptor {
    private Encrypter encryptor

    FileEncryptor(Encrypter encryptor) {
        assert encryptor

        this.encryptor = encryptor
    }

    byte[] encrypt(InputStream inputStream)    {
        assert inputStream

        return encryptor.encrypt(inputStream.getBytes())
    }

    byte[] decrypt(InputStream inputStream)    {
        assert inputStream

        return encryptor.decrypt(inputStream.getBytes())
    }

    File createEncryptedFile(File fileToEncrypt, String destinationDirectory, String extension)  {
        assert fileToEncrypt && fileToEncrypt.exists()
        assert extension

        File destinationDir = getDestinationDir(destinationDirectory)

        String encryptedFilename = destinationDir.getAbsolutePath() + "/" + fileToEncrypt.getName() + "." + extension
        // log.info "encryptedFilename: " + encryptedFilename

        FileInputStream fis = new FileInputStream(fileToEncrypt)
        return createEncryptedFileFromInputStream(fis, encryptedFilename)
    }

    File createDecryptedFile(File fileToDecrypt, String destinationDirectory, String extension)  {
        assert fileToDecrypt && fileToDecrypt.exists()

        File destinationDir = getDestinationDir(destinationDirectory)

        String decryptionFilename = this.getDecryptedFilename(fileToDecrypt, destinationDir, extension)
        FileInputStream fis = new FileInputStream(fileToDecrypt)
        return this.createDecryptedFileFromStream(fis, decryptionFilename)
    }

    private File getDestinationDir(String destinationDirectory) {
        File destinationDir = new File(destinationDirectory)
        assert destinationDir.exists()
        assert destinationDir.isDirectory()
        destinationDir
    }

    private File createEncryptedFileFromInputStream(InputStream is, String encryptedFilename) {
        assert is
        assert encryptedFilename

        File encryptedFile = new File(encryptedFilename)
        FileOutputStream fos = new FileOutputStream(encryptedFile)

        fos.write this.encryptor.encrypt(is.getBytes())

        return encryptedFile
    }

    private File createDecryptedFileFromStream(InputStream is, String decryptedFilename)  {
        assert is
        assert decryptedFilename

//        log.info "decryptedFilename: " + decryptedFilename

        File decryptedFile = new File(decryptedFilename)
        FileOutputStream fos = new FileOutputStream(decryptedFile)


        fos.write this.encryptor.decrypt(is.getBytes())

        return decryptedFile
    }

    private String getDecryptedFilename(File fileToDecrypt, File destinationDir, String extension)   {
        String decryptedFilename = destinationDir.getAbsolutePath() + "/" + fileToDecrypt.getName()
        decryptedFilename = stripFileExtension(decryptedFilename, extension)
//        log.info "decryptedFilename: " + decryptedFilename
        return decryptedFilename
    }

    private String stripFileExtension(String decryptedFilename, String extension) {
        assert decryptedFilename
        assert extension

        if (!decryptedFilename.endsWith(extension)) {
            return
        }

        int length = decryptedFilename.size()
        return decryptedFilename.subSequence(0, length - (1 + extension.size()))
    }
}