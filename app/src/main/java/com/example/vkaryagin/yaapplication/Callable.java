package com.example.vkaryagin.yaapplication;

/**
 * Callback интерфейс для ассинхронных запросов. Определяет 2 функции. {@link #done(Object)} вызывается,
 * когда считается, что объект инициализирован, {@link #error(Response)}  когда что-то пошло не так.
 */
public interface Callable<T> {
    void done(T value);
    void error(final Response res);
}
