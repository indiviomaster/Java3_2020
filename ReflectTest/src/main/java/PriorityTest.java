import java.lang.reflect.Method;

public class PriorityTest {

    public PriorityTest(Method method, int priority) {
        this.method = method;
        this.priority = priority;
    }

    Method method;
    int priority;

    public Method getMethod() {
        return method;
    }

    public int getPriority() {
        return priority;
    }

}
