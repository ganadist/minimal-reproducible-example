package demo;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyString;

public class Test1 {
    static final String METHOD1_RETURN = "Hello";
    static final String METHOD2_RETURN = "World";

    static final String METHOD1_MOCK_RETURN = "Bye";
    static final String METHOD2_MOCK_RETURN = "Earth";

    /**
     * Testcase with non-argument static method
     */
    @Test
    public void test1() {
        Assert.assertEquals(METHOD1_RETURN, staticMethod1());
        try(MockedStatic<Test1> mocked = Mockito.mockStatic(Test1.class)) {
            mocked.when(
                    () -> staticMethod1()
            ).thenReturn(METHOD1_MOCK_RETURN);
            Assert.assertEquals(METHOD1_MOCK_RETURN, staticMethod1());
        }
        Assert.assertEquals(METHOD1_RETURN, staticMethod1());
    }

    /**
     * Testcase with 1 argument static method
     */
    @Test
    public void test2() {
        Assert.assertEquals(METHOD2_RETURN, staticMethod2(""));
        try(MockedStatic<Test1> mocked = Mockito.mockStatic(Test1.class)) {
            mocked.when(
                    () -> staticMethod2(anyString())
            ).thenReturn(METHOD2_MOCK_RETURN);
            Assert.assertEquals(METHOD2_MOCK_RETURN, staticMethod2(""));
        }
        Assert.assertEquals(METHOD2_RETURN, staticMethod2(""));
    }

    /**
     * Testcase with non-argument static method from kotlin class
     */
    @Test
    public void test3() {
        Assert.assertEquals(METHOD1_RETURN, Test2.staticMethod1());
        try(MockedStatic<Test2> mocked = Mockito.mockStatic(Test2.class)) {
            mocked.when(
                    () -> Test2.staticMethod1()
            ).thenReturn(METHOD1_MOCK_RETURN);
            Assert.assertEquals(METHOD1_MOCK_RETURN, Test2.staticMethod1());
        }
        Assert.assertEquals(METHOD1_RETURN, Test2.staticMethod1());
    }

    /**
     * Testcase with 1 argument static method from kotlin class
     */
    @Test
    public void test4() {
        Assert.assertEquals(METHOD2_RETURN, Test2.staticMethod2(""));
        try(MockedStatic<Test2> mocked = Mockito.mockStatic(Test2.class)) {
            mocked.when(
                    () -> Test2.staticMethod2(anyString())
            ).thenReturn(METHOD2_MOCK_RETURN);
            Assert.assertEquals(METHOD2_MOCK_RETURN, Test2.staticMethod2(""));
        }
        Assert.assertEquals(METHOD2_RETURN, Test2.staticMethod2(""));
    }


    static String staticMethod1() {
        return METHOD1_RETURN;
    }


    static String staticMethod2(String arg) {
        return METHOD2_RETURN;
    }
}
