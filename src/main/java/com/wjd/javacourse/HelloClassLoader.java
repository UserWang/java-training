package com.wjd.javacourse;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/11/6
 */
public class HelloClassLoader extends ClassLoader{

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("Hello.xlass");
        try {
            int length = inputStream.available();
            byte[] byteArray = new byte[length];
            inputStream.read(byteArray);
            byte[] bytes = decode(byteArray);
            return defineClass(name,bytes,0,bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] decode(byte[] byteArray){
        byte[] bytes = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            bytes[i] = (byte)(255 - byteArray[i]);
        }
        return bytes;
    }

    public static void main(String[] args) throws Exception {
        ClassLoader classLoader = new HelloClassLoader();
        Class<?> klass = classLoader.loadClass("Hello");
        for (Method method: klass.getDeclaredMethods()) {
            System.out.println(method);
        }

        Object obj = klass.getDeclaredConstructor().newInstance();
        Method method = klass.getMethod("hello");
        method.invoke(obj);

    }
}
