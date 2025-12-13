package com.myproduct.myservice

import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Test suite for all unit test cases of [MyStatusObserver].
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    MyStatusObserverTest::class,
    MyStatusObserverNotifyPlayStatusTest::class,
    MyStatusObserverNotifyErrorTest::class
)
class MyStatusObserverTestSuite {
    // empty class for holding annotations above
}

