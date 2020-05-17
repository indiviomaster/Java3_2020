import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ReflectTest {

    static TestClass instClass;

    public static void start(Class theTestClass) throws RuntimeException {

        int countBeforeSuite = 0;
        int countAfterSuite = 0;

        try {
            instClass = (TestClass) theTestClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        for (Method method: theTestClass.getDeclaredMethods()){
            if(method.isAnnotationPresent(BeforeSuite.class)){
                countBeforeSuite++;
                if(countBeforeSuite>1)
                {
                    throw new RuntimeException("BeforeSuite не один");
                }else{
                   try {
                        method.invoke(instClass);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        List<PriorityTest> listOfTest = new ArrayList<>();

        for (Method method: theTestClass.getDeclaredMethods()){
            if(method.isAnnotationPresent(Test.class)){
                listOfTest.add(new PriorityTest(method,((Test)method.getAnnotation(Test.class)).priority()));
            }
        }
        listOfTest.sort(Comparator.comparing(PriorityTest::getPriority));

        for(PriorityTest pt : listOfTest){

            try {
                pt.getMethod().invoke(instClass);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        for (Method method: theTestClass.getDeclaredMethods()){
            if(method.isAnnotationPresent(AfterSuite.class)){
                countAfterSuite++;
                if(countAfterSuite>1)
                {
                    throw new RuntimeException("AfterSuite не один");
                }else{

                    try {
                        method.invoke(instClass);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
