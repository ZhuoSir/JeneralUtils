package com.chen.jeneral.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 *
 * Created by sunny on 2017/4/14.
 */
public class ReflectUtils {

    private ReflectUtils() {}

    /**
     * 循环向上转型,获取对象的DeclaredField.
     */
    public static Field getDeclaredField(final Object object, final String fieldName) {
        if (null == object) {
            return null;
        }

        return getDeclaredField(object.getClass(), fieldName);
    }

    /**
     * 循环向上转型,获取类的DeclaredField
     *
     * */
    public static Field getDeclaredField(final Class clazz, final String fieldName) {
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义,继续向上转型
            }
        }

        return null;
    }

    /**
     * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
     */
    public static void setFieldValue(final Object object, final String fieldName, final Object value) {
        Field field = getDeclaredField(object, fieldName);

        if (null == field)
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");

        makeAccessible(field);

        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 强制转换field可访问.
     */
    public static void makeAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers())
                || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

    /**
     * 获取对象中所有的Field，包括父类
     *
     * */
    public static Field[] getAllDeclaredFields(final Object object) {
        if (null == object) {
            return null;
        }

        return getAllDeclaredFields(object.getClass());
    }

    /**
     * 获取类中所有Field，包括父类
     * */
    public static Field[] getAllDeclaredFields(final Class clazz) {
        Field[] fields   = clazz.getDeclaredFields();
        Field[] suFields = new Field[0];

        Class suClazz  = clazz;
        Field[] aField = new Field[0];
        while (null != (aField = getSuperDeclaredFields(suClazz))) {
            suClazz = suClazz.getSuperclass();

            if (Object.class == suClazz)
                break;

            Field[] temp = new Field[suFields.length + aField.length];
            System.arraycopy(suFields, 0, temp, 0, suFields.length);
            System.arraycopy(aField, 0, temp, suFields.length, aField.length);

            suFields = temp;
        }

        Field[] ret = new Field[fields.length + suFields.length];
        System.arraycopy(fields, 0, ret, 0, fields.length);
        System.arraycopy(suFields, 0, ret, fields.length, suFields.length);

        return ret;
    }

    /**
     * 获取父类的Field
     * */
    public static Field[] getSuperDeclaredFields(final Class clazz) {
        if (Object.class == clazz)
            return null;

        Class superClass = clazz.getSuperclass();
        return superClass.getDeclaredFields();
    }

    /**
     * 反射调用方法
     *
     * @param clazzName 类的对象名
     * @param methodName 调用方法名
     * @param parameterTypes 参数类型
     * @param args 方法实参
     *
     * @return 方法返回结果
     * */
    @SuppressWarnings("unchecked")
    public static Object invokeMethod(final String clazzName, final String methodName, Class[] parameterTypes, Object[] args) {
        try {
            Class clazz = Class.forName(clazzName);
            Method theMethod = clazz.getDeclaredMethod(methodName, parameterTypes);
            return theMethod.invoke(clazz.newInstance(), args);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
