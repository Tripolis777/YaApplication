package com.example.vkaryagin.yaapplication;

/**
 * Created by v.karyagin on 3/22/17.
 */

public interface Callable<T> {
    void done(T value);
    void error(final Response res);
}
