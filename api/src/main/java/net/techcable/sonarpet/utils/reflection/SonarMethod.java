package net.techcable.sonarpet.utils.reflection;

import lombok.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Primitives;


public final class SonarMethod<T> {
    @Getter
    private final Method handle;
    private final MethodHandle invoker;

    @SneakyThrows(IllegalAccessException.class) // Should not be thrown
    private SonarMethod(Method handle) {
        assert handle != null : "Null handle";
        this.handle = handle;
        handle.setAccessible(true);
        this.invoker = MethodHandles.lookup().unreflect(handle);
    }

    /**
     * Invoke the method
     * <p>Sneakily propagates any exception thrown by the underlying method.</p>
     *
     * @param instance the instance of the object to call the method on
     * @return the value of the field
     * @throws IllegalStateException field is static
     * @throws NullPointerException  if the instance or parameters are static
     * @throws E                     if thrown by the underlying method
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <E extends Throwable> T invokeChecked(Object instance, Object... parameters) throws E {
        return invoke(instance, parameters);
    }

    /**
     * Invoke the method
     * <p>Sneakily propagates any exception thrown by the underlying method.</p>
     *
     * @param instance the instance of the object to call the method on
     * @return the value of the field
     * @throws IllegalStateException field is static
     * @throws NullPointerException  if the instance or parameters are static
     * @throws Throwable             any exception thrown by the underlying method
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public T invoke(Object instance, Object... parameters) {
        Preconditions.checkNotNull(parameters, "Null parameters");
        Object[] args = new Object[parameters.length + 1];
        parameters[0] = Preconditions.checkNotNull(instance, "Null instance");
        System.arraycopy(parameters, 0, args, 1, parameters.length);
        try {
            return (T) invoker.invokeWithArguments(args);
        } catch (ClassCastException | WrongMethodTypeException e) {
            validateArgs(instance, parameters);
            throw new AssertionError("Unknown error", e);
        }
    }


    /**
     * Invoke the method with no arguments
     * <p>Sneakily propagates any exception thrown by the underlying method.</p>
     *
     * @param instance the instance of the object to call the method on
     * @return the value of the field
     * @throws IllegalStateException field is static
     * @throws NullPointerException  if the instance or parameters are static
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public T invoke(Object instance) {
        try {
            return (T) invoker.invoke(instance);
        } catch (ClassCastException | WrongMethodTypeException e) {
            if (!getHandle().getDeclaringClass().isInstance(instance)) {
                throw new IllegalArgumentException("Object is not of expected type. Got " + instance.getClass() + " instead of " + getHandle().getDeclaringClass());
            } else if (getHandle().getParameterCount() != 0) {
                throw new IllegalArgumentException("Was passed no arguments but needed " + getHandle().getParameterCount());
            }
            throw new AssertionError("Unknown error", e);
        }
    }


    /**
     * Invoke the method with the given arguments
     * <p>Sneakily propagates any exception thrown by the underlying method.</p>
     *
     * @param instance the instance of the object to call the method on
     * @param arg1     the first argument to the method
     * @return the value of the field
     * @throws IllegalStateException field is static
     * @throws NullPointerException  if the instance or parameters are static
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public T invoke(Object instance, Object arg1) {
        try {
            return (T) invoker.invoke(instance, arg1);
        } catch (ClassCastException | WrongMethodTypeException e) {
            validateArgs(instance, arg1);
            throw new AssertionError("Unknown error", e);
        }
    }

    /**
     * Invoke the method with the given arguments
     * <p>Sneakily propagates any exception thrown by the underlying method.</p>
     *
     * @param instance the instance of the object to call the method on
     * @param arg1     the first argument to the method
     * @param arg2     the second argument to the method
     * @return the value of the field
     * @throws IllegalStateException field is static
     * @throws NullPointerException  if the instance or parameters are static
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public T invoke(Object instance, Object arg1, Object arg2) {
        try {
            return (T) invoker.invoke(instance, arg1, arg2);
        } catch (ClassCastException | WrongMethodTypeException e) {
            validateArgs(instance, arg1, arg2);
            throw new AssertionError("Unknown error", e);
        }
    }



    /**
     * Invoke the method with the given arguments
     * <p>Sneakily propagates any exception thrown by the underlying method.</p>
     *
     * @param instance the instance of the object to call the method on
     * @param arg1     the first argument to the method
     * @param arg2     the second argument to the method
     * @param arg3     the third argument to the method
     * @return the value of the field
     * @throws IllegalStateException field is static
     * @throws NullPointerException  if the instance or parameters are static
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public T invoke(Object instance, Object arg1, Object arg2, Object arg3) {
        try {
            return (T) invoker.invoke(instance, arg1, arg2, arg3);
        } catch (ClassCastException | WrongMethodTypeException e) {
            validateArgs(instance, arg1, arg2, arg3);
            throw new AssertionError("Unknown error", e);
        }
    }

    private void validateArgs(Object instance, Object... parameters) {
        if (!getHandle().getDeclaringClass().isInstance(instance)) {
            throw new IllegalArgumentException("Object is not of expected type. Got " + instance.getClass() + " instead of " + getHandle().getDeclaringClass());
        } else if (parameters.length != getHandle().getParameterCount()) {
            throw new IllegalArgumentException("Was passed " + parameters.length + " arguments but needed " + getHandle().getParameterCount());
        } else {
            Class<?>[] expectedParameters = getHandle().getParameterTypes();
            for (int i = 0; i < expectedParameters.length; i++) {
                Class<?> expectedParameter = expectedParameters[i];
                if (parameters[i] == null) {
                    if (expectedParameter.isPrimitive())
                        throw new IllegalArgumentException("Was passed null but expected: " + expectedParameter);
                } else {
                    Class<?> parameter = parameters[i].getClass();
                    if (!Primitives.wrap(expectedParameter).isAssignableFrom(Primitives.wrap(parameter))) {
                        throw new IllegalArgumentException("Needed " + expectedParameter.getTypeName() + " but got " + parameter.getTypeName());
                    }
                }
            }
        }
    }

    /**
     * Return if the field is static
     *
     * @return if static
     */
    public boolean isStatic() {
        return Modifier.isStatic(getHandle().getModifiers());
    }

    /**
     * Get the field with the given name and type from the given class
     *
     * @param clazz          the class to get the field from
     * @param name           the name of the field
     * @param parameterTypes the parameter types of the method
     * @return the name
     * @throws NullPointerException     if clazz, name or type is null
     * @throws IllegalArgumentException if a field with the given name doesn't exist
     * @throws IllegalArgumentException if the field doesn't have the expected type
     */
    public static SonarMethod<?> getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        return getMethodWithReturnType(clazz, name, Object.class, parameterTypes);
    }


    /**
     * Get the field with the given name and type from the given class
     *
     * @param clazz          the class to get the field from
     * @param name           the name of the field
     * @param expectedReturnType     the return type of the method
     * @param parameterTypes the parameter types of the method
     * @return the name
     * @throws NullPointerException     if clazz, name or type is null
     * @throws IllegalArgumentException if a field with the given name doesn't exist
     * @throws IllegalArgumentException if the field doesn't have the expected type
     */
    public static <T> SonarMethod<T> getMethodWithReturnType(Class<?> clazz, String name, Class<T> expectedReturnType, Class<?>... parameterTypes) {
        Preconditions.checkNotNull(clazz, "Null class");
        Preconditions.checkNotNull(name, "Null name");
        Preconditions.checkNotNull(expectedReturnType, "Null expected return type type");
        Preconditions.checkNotNull(parameterTypes, "Null parameter types array");
        for (int i = 0; i < parameterTypes.length; i++) {
            Preconditions.checkNotNull(parameterTypes[i], "Null parameter type at index %s", i);
        }
        try {
            Method method = clazz.getDeclaredMethod(name, parameterTypes);
            Class<?> returnType = method.getReturnType();
            if (!Reflection.isLenientlyAssignableFrom(expectedReturnType, returnType)) {
                throw new IllegalArgumentException("Expected return type " + expectedReturnType + " isn't assignable from " + returnType);
            }
            return new SonarMethod<>(method);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("No field named " + name + " in class " + clazz);
        }
    }

    public String toString() {
        return getHandle().getDeclaringClass() + ":" + getHandle().getName();
    }

    /**
     * Get the only method in the class with the given return and parameter types
     * <p>Throws an exception if no fields are found, or if multiple fields are found.</p>
     * <p>Fields may have a subclass of the required type, and primitives are interchangeable with wrappers. Therefore passing Object accepts all fields.</p>
     *
     * @param clazz      the type to find the method in
     * @param returnType the return type of the method to find
     * @param parameters the parameters of the method to find
     * @param <T>        the return type of the field
     * @return the only method with the given return type and paramaters
     * @throws IllegalArgumentException if there is more than one field with the given type
     * @throws IllegalArgumentException if not found
     * @throws NullPointerException     if any args are null
     */
    public static <T> SonarMethod<T> findMethodWithType(Class<?> clazz, Class<T> returnType, Class<?>... parameters) {
        ImmutableList<SonarMethod<T>> methods = findMethodsWithType(clazz, returnType, parameters);
        StringBuilder builder;
        switch (methods.size()) {
            case 1:
                return methods.get(0);
            case 0:
                builder = new StringBuilder("Method in ");
                builder.append(clazz.getTypeName());
                builder.append(" not found with type: ");
                appendSignature(builder, returnType, parameters);
            default:
                builder = new StringBuilder("Multiple fields found in ");
                builder.append(clazz);
                builder.append(" with type ");
                appendSignature(builder, returnType, parameters);
                builder.append(": [");
                for (int i = 0; i < methods.size(); i++) {
                    SonarMethod<T> field = methods.get(i);
                    builder.append(field);
                    if (i + 1 < methods.size()) { // Has more
                        builder.append(", ");
                    }
                }
                builder.append("] ");

        }
        throw new IllegalArgumentException(builder.toString());
    }

    /**
     * Get all the methods in the class with the given return and parameter types
     * <p>Throws an exception if no fields are found, or if multiple fields are found.</p>
     * <p>Fields may have a subclass of the required type, and primitives are interchangeable with wrappers. Therefore passing Object accepts all fields.</p>
     *
     * @param clazz      the type to find the method in
     * @param returnType the return type of the method to find
     * @param parameters the parameters of the method to find
     * @param <T>        the return type of the field
     * @return the only method with the given return type and paramaters
     * @throws IllegalArgumentException if there is more than one field with the given type
     * @throws IllegalArgumentException if not found
     * @throws NullPointerException     if any args are null
     */
    public static <T> ImmutableList<SonarMethod<T>> findMethodsWithType(Class<?> clazz, Class<T> returnType, Class<?>... parameters) {
        Preconditions.checkNotNull(clazz, "Null class");
        Preconditions.checkNotNull(returnType, "Null return type");
        Preconditions.checkNotNull(returnType, "Null parameter array");
        ImmutableList.Builder<SonarMethod<T>> builder = ImmutableList.builder();
        if (returnType.isPrimitive()) {
            returnType = Primitives.wrap(returnType);
        }
        methodLoop:
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getParameterCount() != parameters.length) continue;
            Class<?>[] methodParameters = method.getParameterTypes();
            for (int i = 0; i < methodParameters.length; i++) {
                Class<?> parameter = methodParameters[i];
                Class<?> expectedParameter = Preconditions.checkNotNull(parameters[i], "Null parameter");
                if (expectedParameter.isPrimitive()) {
                    expectedParameter = Primitives.wrap(expectedParameter);
                    parameters[i] = expectedParameter;
                }
                if (parameter.isPrimitive()) {
                    parameter = Primitives.wrap(parameter);
                }
                if (!expectedParameter.isAssignableFrom(parameter)) continue methodLoop;
            }
            Class<?> methodReturnType = method.getReturnType().isPrimitive() ? Primitives.wrap(method.getReturnType()) : method.getReturnType();
            if (returnType.isAssignableFrom(methodReturnType)) {
                builder.add(new SonarMethod<T>(method));
            }
        }
        return builder.build();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && this == o && getHandle().equals(((SonarMethod) o).getHandle());

    }

    @Override
    public int hashCode() {
        return getHandle().hashCode();
    }

    private static void appendSignature(StringBuilder builder, Class<?> returnType, Class<?>[] parameters) {
        if (parameters.length == 0) {
            builder.append("()");
        } else {
            builder.append('(');
            builder.append(parameters[0].getTypeName());
            for (int i = 1; i < parameters.length; i++) {
                builder.append(',');
                builder.append(parameters[0].getTypeName());
            }
            builder.append(')');
        }
        builder.append(returnType.getTypeName());
    }
}
