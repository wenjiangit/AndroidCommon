package com.wenjian.commonskill.retrofit;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Description: NetResponseTest
 * Date: 2018/7/27
 *
 * @author jian.wen@ubtrobot.com
 */
public class NetResponseTest {


    @Test
    public void test_generic_return_type() {
        Class<MyClass> myClass = MyClass.class;
        try {
            Method method = myClass.getDeclaredMethod("getList");
            Type genericReturnType = method.getGenericReturnType();
            Class<?> returnType = method.getReturnType();
            System.out.println("genericReturnType: " + genericReturnType);
            System.out.println("returnType: " + returnType);

            if (genericReturnType instanceof ParameterizedType) {
                Type[] arguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
                System.out.println(arguments[0]);
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void test_param_type() throws NoSuchMethodException {
        MyClass myClass = new MyClass();
        Method method = myClass.getClass().getDeclaredMethod("setList", List.class, int.class);
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        for (Type parameterType : genericParameterTypes) {
            if (parameterType instanceof ParameterizedType) {
                ParameterizedType parameterType1 = (ParameterizedType) parameterType;
                Type[] arguments = parameterType1.getActualTypeArguments();
                System.out.println("ActualTypeArgument: " + arguments[0]);
                System.out.println("ownerType: " + parameterType1.getOwnerType());
                System.out.println("rawType: " + parameterType1.getRawType());

            } else {
                System.out.println(parameterType);
            }
        }
    }

    @Test
    public void test() {
        Request request = proxy(new MyClass());
        request.request();
    }

    class MyClass implements Request{

        private List<String> mList = new ArrayList<>();

        public List<String> getList() {
            return mList;
        }

        public void setList(List<String> list,int size) {
            mList = list;
        }

        @Net
        @Override
        public void request(){
            System.out.println("请求网络");

        }
    }

    public static Request proxy(final MyClass obj) {
        Objects.requireNonNull(obj);
        Method[] methods = obj.getClass().getDeclaredMethods();
        boolean needProxy = false;
        for (Method method : methods) {
            Net net = method.getAnnotation(Net.class);
            if (net != null) {
                needProxy = true;
                break;
            }
        }
        if (needProxy) {
            Object instance = Proxy.newProxyInstance(obj.getClass().getClassLoader(), new Class[]{Request.class}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                    System.out.println("只能在子线程运行该方法");
                    System.out.println(proxy);

                    return method.invoke(obj,args);
                }
            });
            return (Request) instance;
        }
        return obj;
    }

    interface Request{
        void request();
    }

}