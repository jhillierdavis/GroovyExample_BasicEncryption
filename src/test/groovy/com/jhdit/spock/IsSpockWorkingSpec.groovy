package com.jhdit.spock

/**
 * Simple Unit test/specification to confirm Spock test framework is functioning correctly (&amp; for quick reference).
 *
 * See https://code.google.com/p/spock/wiki/HelloSpock
 * and https://code.google.com/p/spock/wiki/SpockBasics
 *
 * Spock blocks:
 *
 * given: preconditions, data fixtures
 * setup: alias for given (JUnit syntax)
 * when: actions that trigger some outcome
 * then: make assertions about the outcome
 * expect: shorthand for when & then
 * where: applies varied inputs
 * and: subdivides other blocks
 * cleanup: post-conditions, housekeeping
 *
 */

import spock.lang.*

class IsSpockWorkingSpec extends Specification {
    // Fields
    def unsharedResource =  "NOT shared between features, recreated each time"
    @Shared def sharedResource = "Too expensive to keep creating"

    // Fixture methods
    def setup() {}          // run before every feature method
    def cleanup() {}        // run after every feature method
    def setupSpec() {}     // run before the first feature method
    def cleanupSpec() {}   // run after the last feature method

    // Feature methods

    /**
     * A 'where' block data table example
     */

    def "length of Spock's and his friends' names"() {
        expect: "matching name length"
        name.size() == length

        where: "character name data"
        name     | length
        "Spock"  | 5
        "Kirk"   | 4
        "Scotty" | 6
    }

    /**
     * Exception handling example (NB: must be in 'then' block)
     */

    def "divide by zero" () {
        when: "silly division"
        1 / 0

        then: "ensure expected exception raised"
        final ArithmeticException e = thrown()
        e.message == "Division by zero"
    }

    /**
     * Show-case use of FailsWith annotation
     */

    @FailsWith(ArithmeticException)
    def "alternative divide by zero" () {
        given: "silly division"
        1 / 0
    }

    /**
     * Test exclusion example
     */

    @Ignore(value = "TODO")
    def "ignore for now"() {
        expect: "will fail for now"
        true == false
    }

    def "test unshared and shared"() {
        expect:
        assert this.unsharedResource == "NOT shared between features, recreated each time"
        assert this.sharedResource == "Too expensive to keep creating"

        cleanup: "Modify for subsequent test"
        this.unsharedResource = "Changed"
        this.sharedResource = "Changed"
    }

    def "retest unshared and shared"() {
        expect: "shared resource modified, assuming feature method that alters it excuted previously"
        assert this.unsharedResource == "NOT shared between features, recreated each time"
        assert this.sharedResource == "Changed"
    }

}
