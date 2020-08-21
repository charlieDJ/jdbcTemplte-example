package com.example.template.function;

@FunctionalInterface
public interface SupplierWrapper<T, E extends Throwable> {

    T get() throws E;

    static <T, E extends Throwable> T wrap(SupplierWrapper<T, E> s) {
        try {
            return s.get();
        } catch (Throwable ex) {
            return null;
        }
    }

}
