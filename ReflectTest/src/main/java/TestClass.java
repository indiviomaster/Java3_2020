public class TestClass {
    public TestClass() {
    }

    @BeforeSuite
    void first(){
        System.out.println("Работает BeforeSuit класса "+getClass().getSimpleName() );
    }

    @Test
    void test1(){
        System.out.println("Работает Тест1 приоритет default  класса " +getClass().getSimpleName() );
    }
    @Test
    void test4(){
        System.out.println("Работает Тест4 приоритет default  класса " +getClass().getSimpleName() );
    }

    @Test(priority = 0)
    void test2(){
        System.out.println("Работает Тест2 приоритет 0 класса "+getClass().getSimpleName() );
    }

    @Test(priority = 2)
    void test3(){
        System.out.println("Работает Тест3 приоритет 3 класса "+getClass().getSimpleName() );
    }


    @AfterSuite
    void last(){
        System.out.println("Работает AfterSuit класса "+getClass().getSimpleName() );
    }
}
