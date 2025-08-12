package mod.seanld.rawinput.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 反射工具类，用于兼容Java 12+的严格反射限制
 * Based on GTNHLib's Fields utility class
 */
public class Fields {

    private static final Map<String, Field> fieldCache = new ConcurrentHashMap<>();
    private static final Map<String, Method> methodCache = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Boolean> classAvailableCache = new ConcurrentHashMap<>();

    /**
     * 获取字段，自动处理Java 12+的访问限制
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        String key = clazz.getName() + "#" + fieldName;
        Field field = fieldCache.get(key);
        if (field != null) {
            return field;
        }

        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            
            // 尝试绕过Java 12+的反射限制
            bypassFieldAccess(field);
            
            fieldCache.put(key, field);
            return field;
        } catch (Exception e) {
            throw new RuntimeException("Failed to access field: " + fieldName + " in class: " + clazz.getName(), e);
        }
    }

    /**
     * 获取字段值
     */
    public static <T> T getField(Object instance, String fieldName) {
        try {
            Field field = getField(instance.getClass(), fieldName);
            return (T) field.get(instance);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get field value: " + fieldName, e);
        }
    }

    /**
     * 设置字段值
     */
    public static void setField(Object instance, String fieldName, Object value) {
        try {
            Field field = getField(instance.getClass(), fieldName);
            field.set(instance, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field value: " + fieldName, e);
        }
    }

    /**
     * 绕过Java 12+的反射访问限制
     */
    private static void bypassFieldAccess(Field field) {
        try {
            // 尝试使用Unsafe来绕过访问限制
            Class<?> unsafeClass = getUnsafeClass();
            if (unsafeClass != null) {
                Object unsafe = getUnsafeInstance(unsafeClass);
                if (unsafe != null) {
                    // 获取fieldOffset
                    Method objectFieldOffset = unsafeClass.getDeclaredMethod("objectFieldOffset", Field.class);
                    objectFieldOffset.setAccessible(true);
                    // 如果能获取到offset，说明可以访问
                }
            }
        } catch (Exception e) {
            // 在Java 12+中可能会失败，这是正常的
        }

        try {
            // 尝试使用反射来设置accessible
            Method setAccessible = Field.class.getDeclaredMethod("setAccessible", boolean.class);
            setAccessible.setAccessible(true);
            setAccessible.invoke(field, true);
        } catch (Exception e) {
            // 在Java 17+中可能会失败
        }
    }

    /**
     * 获取Unsafe类
     */
    private static Class<?> getUnsafeClass() {
        try {
            return Class.forName("sun.misc.Unsafe");
        } catch (Exception e) {
            try {
                return Class.forName("jdk.internal.misc.Unsafe");
            } catch (Exception ex) {
                return null;
            }
        }
    }

    /**
     * 获取Unsafe实例
     */
    private static Object getUnsafeInstance(Class<?> unsafeClass) {
        try {
            Field theUnsafe = unsafeClass.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return theUnsafe.get(null);
        } catch (Exception e) {
            try {
                Field theUnsafe = unsafeClass.getDeclaredField("INSTANCE");
                theUnsafe.setAccessible(true);
                return theUnsafe.get(null);
            } catch (Exception ex) {
                return null;
            }
        }
    }

    /**
     * 检查类是否可用
     */
    public static boolean isClassAvailable(String className) {
        return classAvailableCache.computeIfAbsent(className, cls -> {
            try {
                Class.forName(className);
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        });
    }

    /**
     * 获取方法
     */
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        String key = clazz.getName() + "#" + methodName + "#" + Arrays.toString(parameterTypes);
        Method method = methodCache.get(key);
        if (method != null) {
            return method;
        }

        try {
            method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            methodCache.put(key, method);
            return method;
        } catch (Exception e) {
            throw new RuntimeException("Failed to access method: " + methodName + " in class: " + clazz.getName(), e);
        }
    }

    /**
     * 调用方法
     */
    public static <T> T invokeMethod(Object instance, String methodName, Object... args) {
        try {
            Class<?>[] paramTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                paramTypes[i] = args[i].getClass();
            }
            Method method = getMethod(instance.getClass(), methodName, paramTypes);
            return (T) method.invoke(instance, args);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke method: " + methodName, e);
        }
    }
}