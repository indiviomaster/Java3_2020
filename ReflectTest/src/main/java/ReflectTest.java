
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReflectTest {

    static TestClass instClass;
    public static void main(String[] args) {
        Class cla = TestClass.class;

        int countBeforeSuite = 0;
        int countAfterSuite = 0;
        int countTest = 0;
        try {
            instClass =  (TestClass)cla.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        for (Method method: cla.getDeclaredMethods()){
            if(method.isAnnotationPresent(BeforeSuite.class)){
                countBeforeSuite++;
                if(countBeforeSuite>1)
                {
                    throw new RuntimeException("BeforeSuite не один");
                }else{

                   // System.out.println("Присутствует @BeforeSuite");
                    // System.out.println("с методом:" + method.getReturnType() +" "+ method.getName());
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
        Map<Integer, Method> map = new HashMap<Integer, Method>();

        for (Method method: cla.getDeclaredMethods()){
            if(method.isAnnotationPresent(Test.class)){

                map.putIfAbsent(((Test)method.getAnnotation(Test.class)).priority(), method);

            }

        }
        for(Integer key: map.keySet()){
            //System.out.println(map.get(key).getReturnType()+map.get(key).getName());
            try {
                map.get(key).invoke(instClass);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        for (Method method: cla.getDeclaredMethods()){
            if(method.isAnnotationPresent(AfterSuite.class)){
                countAfterSuite++;
                if(countAfterSuite>1)
                {
                    throw new RuntimeException("AfterSuite не один");
                }else{
                   // System.out.println("Присутствует AfterSuite");
                   // System.out.println("с методом:" + method.getReturnType() +" "+ method.getName());
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
