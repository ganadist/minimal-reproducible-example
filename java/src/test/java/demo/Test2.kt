package demo

import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import org.mockito.ArgumentMatchers.anyString

class Test2 {

    @Test
    fun test1() {
        Assert.assertEquals(METHOD1_RETURN, staticMethod1())
        Mockito.mockStatic(Test2::class.java).use {
            it.`when`<String> {
                staticMethod1()
            }.thenReturn(METHOD1_MOCK_RETURN)

            Assert.assertEquals(METHOD1_MOCK_RETURN, staticMethod1())
        }
        Assert.assertEquals(METHOD1_RETURN, staticMethod1())
    }

    @Test
    fun test1_1() {
        Assert.assertEquals(METHOD1_RETURN, staticMethod1())
        Mockito.mockStatic(Test2.Companion::class.java).use {
            it.`when`<String> {
                Test2.Companion.staticMethod1()
            }.thenReturn(METHOD1_MOCK_RETURN)

            Assert.assertEquals(METHOD1_MOCK_RETURN, Test2.Companion.staticMethod1())
        }
        Assert.assertEquals(METHOD1_RETURN, staticMethod1())
    }

    @Test
    fun test2() {
        Assert.assertEquals(METHOD2_RETURN, staticMethod2(""))
        Mockito.mockStatic(Test2::class.java).use {
            it.`when`<String> {
                staticMethod2(anyString())
            }.thenReturn(METHOD2_MOCK_RETURN)

            Assert.assertEquals(METHOD2_MOCK_RETURN, staticMethod2(""))
        }
        Assert.assertEquals(METHOD2_RETURN, staticMethod2(""))
    }

    @Test
    fun test2_1() {
        Assert.assertEquals(METHOD2_RETURN, staticMethod2(""))
        Mockito.mockStatic(Test2.Companion::class.java).use {
            it.`when`<String> {
                Test2.Companion.staticMethod2(anyString())
            }.thenReturn(METHOD2_MOCK_RETURN)

            Assert.assertEquals(METHOD2_MOCK_RETURN, Test2.Companion.staticMethod2(""))
        }
        Assert.assertEquals(METHOD2_RETURN, staticMethod2(""))
    }

    /**
     * Testcase with non-argument static method from java class
     */
    @Test
    fun test3() {
        Assert.assertEquals(METHOD1_RETURN, Test1.staticMethod1())
        Mockito.mockStatic(Test1::class.java).use {
            it.`when`<String> {
                Test1.staticMethod1()
            }.thenReturn(METHOD1_MOCK_RETURN)

            Assert.assertEquals(METHOD1_MOCK_RETURN, Test1.staticMethod1())
        }
        Assert.assertEquals(METHOD1_RETURN, Test1.staticMethod1())
    }

    /**
     * Testcase with 1 argument static method from java class
     */
    @Test
    fun test4() {
        Assert.assertEquals(METHOD2_RETURN, Test1.staticMethod2(""))
        Mockito.mockStatic(Test1::class.java).use {
            it.`when`<String> {
                Test1.staticMethod2(anyString())
            }.thenReturn(METHOD2_MOCK_RETURN)

            Assert.assertEquals(METHOD2_MOCK_RETURN, Test1.staticMethod2(""))
        }
        Assert.assertEquals(METHOD2_RETURN, Test1.staticMethod2(""))
    }

    companion object {
        @JvmStatic
        fun staticMethod1(): String = METHOD1_RETURN

        @JvmStatic
        fun staticMethod2(arg: String): String = METHOD2_RETURN

        private const val METHOD1_RETURN = Test1.METHOD1_RETURN
        private const val METHOD2_RETURN = Test1.METHOD2_RETURN

        private const val METHOD1_MOCK_RETURN = Test1.METHOD1_MOCK_RETURN
        private const val METHOD2_MOCK_RETURN = Test1.METHOD2_MOCK_RETURN
    }
}