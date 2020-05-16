public class TestClass {
    public TestClass() {
    }

    Class testClass;
    static void start(Class testClass){

    }

    @BeforeSuite
    void first(){
        System.out.println("Работает BeforeSuit");
    }

    @Test
    void test1(){
        System.out.println("Работает Тест1 приоритет default 1");
    }

    @Test(priority = 0)
    void test2(){
        System.out.println("Работает Тест2 приоритет 0");
    }

    @Test(priority = 2)
    void test3(){
        System.out.println("Работает Тест1 приоритет 3");
    }


    @AfterSuite
    void last(){
        System.out.println("Работает AfterSuit");
    }
}
