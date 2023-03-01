import javassist.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 使用javasiste动态地实现代理(动态生成class字节码)
 */

public class ProxyFactory {

    // 要动态生成的接口
    public interface TestService {
        void sayHi(String arg);
    }

    /**
     * 动态代理类实现1
     * 缺点：
     *  1. 代理的接口采用硬编码的方式写死了 class1.addInterface(classPool.get(TestService.class.getName()));
     *  2. 实现的源代码使用"{System.out.println(\"hello:\"+$1);}"的方式加入，没有编译器的编译很容易会出现编写错误
     *  3. 实现的接口的方法，参数类型，返回类型...都是写死了的，如何动态的知道每个不同接口要实现什么方法，返回类型，参数类型?
      */
    public static TestService createProxy() throws Exception {
        // 1. 创建一个类并增加TestService接口
        ClassPool classPool = new ClassPool();
        classPool.appendSystemPath();   //增加当前的classloader才能加载到TestService
        CtClass class1 = classPool.makeClass("TestServiceImpl");
        class1.addInterface(classPool.get(TestService.class.getName()));
        // 2. 创建一个方法并增加到类中
        CtMethod sayHi = CtNewMethod.make(CtClass.voidType, "sayHi", new CtClass[]{classPool.get(String.class.getName())},
                new CtClass[0], "{System.out.println(\"hello:\"+$1);}",
                class1);
        class1.addMethod(sayHi);
        // 3. 实例化这个类并从javassist类变成java类
        Class aClass = classPool.toClass(class1);
        // 强制转换
        return (TestService) aClass.newInstance();
    }

    /**
     * 动态代理实现2
     * 实现：
     *  1. 能代理所有接口及其所有方法
     *  2. 以对象的方法传入源代码
     * 实现思路：
     *  1. 接口的所有方法->反射
     *  2. 对象的方式传入源代码->InvocationHandler接口
     * @param cla 代理的接口的class对象
     * @param handler InvocationHandler接口实现类
     * @param <T> 代理的接口
     * @return 代理的接口实例对象
     */
    public static <T> T createProxy2(Class<T> cla, InvocationHandler handler) throws Exception {
        //1. 创建一个类并加入接口
        int count = 0;
        ClassPool classPool = new ClassPool();
        classPool.appendSystemPath();
        CtClass impl = classPool.makeClass("$proxy" + count++);
        impl.addInterface(classPool.get(cla.getName()));
        //2. 添加属性 handler
        CtField fie = CtField.make("public ProxyFactory.InvocationHandler handler=null;", impl);
        impl.addField(fie);

        // 返回的源代码，范围2种情况，有返回值的和没有返回值的, 主要都是调用属性handler的方法
        String src = "return ($r)this.handler.invoke(\"%s\", $args);";
        String voidsrc = "this.handler.invoke(\"%s\", $args);";

        //3. 遍历所有方法实现
        for (Method method : cla.getMethods()) {
            CtClass returnType = classPool.get(method.getReturnType().getName());
            String name = method.getName();
            CtClass[] parameters = toCtClass(classPool, method.getParameterTypes());
            CtClass[] errors = toCtClass(classPool, method.getExceptionTypes());
            String srcImpl = "";
            if (method.getReturnType().equals(Void.class)) {
                srcImpl = voidsrc;
            } else {
                srcImpl = src;
            }
            CtMethod newMethod = CtNewMethod.make(returnType, name, parameters, errors, String.format(srcImpl, method.getName()), impl);
            impl.addMethod(newMethod);
        }

        //4. 类加载到当前classpool，实例化java对象，注意要将handler放进去
        Class aClass = classPool.toClass(impl);
        Object o = aClass.newInstance();
        aClass.getField("handler").set(o,handler);
        return (T) o;
    }

    // 自定义的InvocationHandler不是jdk中的
    public interface InvocationHandler {
        Object invoke(String methodname, Object args[]);
    }

    // 辅助方法将java的类型转换成javassist类型
    private static CtClass[] toCtClass(ClassPool pool, Class[] classes) {
        return Arrays.stream(classes).map(c -> {
            try {
                return pool.get(c.getName());
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()).toArray(new CtClass[0]);
    }


    /**
     * jdk的动态代理
     */
    public static void jdkProxyTest() {
        ClassLoader classLoader = ProxyFactory.class.getClassLoader();
        Class<?>[] interfaces = new Class[]{
                TestService.class
        };
        java.lang.reflect.InvocationHandler handler = (proxy, method, args) -> {
            System.out.println("hello 鲁班");
            return null;
        };
        TestService testService = (TestService) Proxy.newProxyInstance(classLoader, interfaces, handler);
        testService.sayHi("ddd");
    }



    // 局部测试
    public static void main(String[] args) throws Exception {
//        TestService testService = createProxy();
//        testService.sayHi("鲁班大叔007");

//        TestService proxy2 = createProxy2(TestService.class, new InvocationHandler() {
//
//            @Override
//            public Object invoke(String methodname, Object[] args) {
//                System.out.println("hello: " + args[0]);
//                return null;
//            }
//        });
//
//        proxy2.sayHi("鲁班大叔007");

        jdkProxyTest();
    }
}
