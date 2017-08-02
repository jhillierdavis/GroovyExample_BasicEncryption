package com.jhdit.resources

import spock.lang.Specification

class ResourceSpec extends Specification {

    void "verify classpath resource loading"() {
        given:
/*
            URL url = this.getClass().getResource("/resources/file.txt")
            assert url
            File testResourceFile = new File(url.toURI())
*/
            File testResourceFile =  new File("src/test/resources/file.txt")

        expect:
            testResourceFile.exists()
    }
}
