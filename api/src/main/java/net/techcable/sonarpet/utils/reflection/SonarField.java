package net.techcable.sonarpet.utils.reflection;

import lombok.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Primitives;


public final class SonarField<T> {
    @Getter
    private final Field handle;
    private final MethodHandle getter, setter;

    @SneakyThrows(IllegalAccessException.class) // Should not be thrown
    private SonarField(Field handle) {
        assert handle != null : "Null handle";
        this.handle = handle;
        handle.setAccessible(true);
        this.getter = MethodHandles.lookup().unreflectGetter(handle);
        this.setter = MethodHandles.lookup().unreflectSetter(handle);
    }

    /**
     * Get the value of the field
     *
     * @param instance the instance of the object to get the field from
     * @return the value of the field
     * @throws IllegalStateException field is static
     * @throws NullPointerException  if the field is static
     * @throws NullPointerException
     */
    @SuppressWarnings("unchecked")
    public T getValue(Object instance) {
        try {
            return (T) getter.invoke(instance);
        } catch (Throwable t) {
            if (isStatic()) {
                throw new IllegalStateException("Field is static");
            } else if (instance == null) {
                throw new NullPointerException("Object is null");
            } else if (!getHandle().getDeclaringClass().isInstance(instance)) {
                throw new IllegalArgumentException("Object is not of expected type. Got " + instance.getClass() + " instead of " + getHandle().getType());
            } else {
                throw new AssertionError("getter threw an unexpected exeception", t);
            }

        }
    }


    /**
     * Set the value of the field
     *
     * @param instance the instance of the object to get the field from
     * @param value the value to set
     * @throws IllegalStateException field is static
     * @throws NullPointerException  if the field is static
     * @throws NullPointerException
     */
    @SuppressWarnings("unchecked")
    public void setValue(Object instance, T value) {
        try {
            setter.invoke(instance, value);
        } catch (Throwable t) {
            if (isStatic()) {
                throw new IllegalStateException("Field is static");
            } else if (instance == null) {
                throw new NullPointerException("Object is null");
            } else if (!getHandle().getDeclaringClass().isInstance(instance)) {
                throw new IllegalArgumentException("Object is not of expected type. Got " + instance.getClass() + " instead of " + getHandle().getType());
            } else {
                throw new AssertionError("setter threw an unexpected exeception", t);
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
     * Get the field with the given name from the given class
     *
     * @param clazz the type to get the field from
     * @param name  the name of the field
     * @return the name
     * @throws NullPointerException     if type or name is null
     * @throws IllegalArgumentException if a field with the given name doesn't exist
     */
    public static SonarField<?> getField(Class<?> clazz, String name) {
        return getField(clazz, name, Object.class);
    }


    /**
     * Get the field with the given name and type from the given class
     * <p>Fields may have a subclass of the required type, and therfore passing Object accepts all fields.</p>
     * <p>Primitive field types are interchangeable with the wrapper type, so you can accept any number field by using Number.class</p>
     *
     * @param clazz     the class to get the field from
     * @param name      the name of the field
     * @param expectedType the expected type of the field
     * @return the name
     * @throws NullPointerException     if clazz, name or type is null
     * @throws IllegalArgumentException if a field with the given name doesn't exist
     * @throws IllegalArgumentException if the field doesn't have the expected type
     */
    public static <T> SonarField<T> getField(Class<?> clazz, String name, Class<T> expectedType) {
        Preconditions.checkNotNull(clazz, "Null class");
        Preconditions.checkNotNull(name, "Null name");
        Preconditions.checkNotNull(expectedType, "Null type");
        try {
            Field f = clazz.getDeclaredField(name);
            Class<?> fieldType = f.getType();
            if (!Reflection.isLenientlyAssignableFrom(expectedType, fieldType)) {
                throw new IllegalArgumentException("Expected type " + expectedType + " isn't assignable from " + fieldType);
            }
            return new SonarField<>(f);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("No field named " + name + " in class " + clazz);
        }
    }

    public String toString() {
        return getHandle().getDeclaringClass() + ":" + getHandle().getName();
    }

    /**
     * Get the only field in the class with the given type
     * <p>Throws an exception if no fields are found, or if multiple fields are found.</p>
     *
     * @param clazz the type to find the field in
     * @param fieldType the type of the field
     * @param <T> the type of the field
     * @throws IllegalArgumentException if there is more than one field with the given type
     * @throws IllegalArgumentException if not found
     * @throws NullPointerException if any args are null
     * @return the only field
     */
    public static <T> SonarField<T> findFieldWithType(Class<?> clazz, Class<T> fieldType) {
        ImmutableList<SonarField<T>> fields = findFieldsWithType(clazz, fieldType);
        switch (fields.size()) {
            case 1:
                return fields.get(0);
            case 0:
                throw new IllegalArgumentException("Field in " + clazz + " not found with type " + fieldType);
            default:
                StringBuilder builder = new StringBuilder("Multiple fields found in ");
                builder.append(clazz);
                builder.append(" with type ");
                builder.append(fieldType);
                builder.append(": [");
                for (int i = 0; i < fields.size(); i++) {
                    SonarField<T> field = fields.get(i);
                    builder.append(field);
                    if (i + 1 < fields.size()) { // Has more
                        builder.append(", ");
                    }
                }
                builder.append("] ");
                throw new IllegalArgumentException(builder.toString());
        }
    }

    /**
     * Get the only field in the class with the given type
     * @param clazz the type to find the field in
     * @param fieldType the type of the field
     * @param <T> the type of the field
     * @throws IllegalArgumentException if there is more than one field with the given type
     * @throws IllegalArgumentException if not found
     * @throws NullPointerException if any args are null
     * @return all fields in the class with the given type
     */
    public static <T> ImmutableList<SonarField<T>> findFieldsWithType(Class<?> clazz, Class<T> fieldType) {
        Preconditions.checkNotNull(clazz, "Null class");
        Preconditions.checkNotNull(fieldType, "Null type");
        ImmutableList.Builder<SonarField<T>> builder = ImmutableList.builder();
        for (Field field : clazz.getDeclaredFields()) {
            if (Reflection.isLenientlyAssignableFrom(fieldType, field.getType())) {
                builder.add(new SonarField<T>(field));
            }
        }
        return builder.build();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && this == o && getHandle().equals(((SonarField) o).getHandle());

    }

    @Override
    public int hashCode() {
        return getHandle().hashCode();
    }
}
