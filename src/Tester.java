import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Tester {
    public static void start(Class testClass) {
        boolean flagBeforeSuite = false, flagAfterSuite = false;
        Method beforeSuite = null, afterSuite = null;
        List<Method> list = new ArrayList<>();
        Method[] methods = testClass.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getAnnotation(BeforeSuite.class) != null) {
                if(!flagBeforeSuite) {
                    flagBeforeSuite = true;
                    beforeSuite = methods[i];
                    continue;
                } else {
                    throw new RuntimeException("Exceeded the number of annotations " + BeforeSuite.class.getName());
                }
            }
            if (methods[i].getAnnotation(AfterSuite.class) != null) {
                if(!flagAfterSuite) {
                    flagAfterSuite = true;
                    afterSuite = methods[i];
                    continue;
                } else {
                    throw new RuntimeException("Exceeded the number of annotations " + AfterSuite.class.getName());
                }
            }
            if (methods[i].getAnnotation(Test.class) != null) {
                list.add(methods[i]);
            }
        }

        if (flagBeforeSuite) {
            try {
                beforeSuite.invoke(testClass.getConstructor().newInstance(), null);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        for (int i = list.size() - 1; i > 0 ; i--) {
            for (int j = 0; j < i; j++) {
                if (list.get(j).getAnnotation(Test.class).value() > list.get(j + 1).getAnnotation(Test.class).value()) {
                    Method tmp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, tmp);
                }
            }
        }

        for (int i = 0; i < list.size(); i++) {
            try {
                list.get(i).invoke(testClass.getConstructor().newInstance(), null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        if (flagAfterSuite) {
            try {
                afterSuite.invoke(testClass.getConstructor().newInstance(), null);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public static void start(String nameClass) {
        try {
            start(Class.forName(nameClass));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
