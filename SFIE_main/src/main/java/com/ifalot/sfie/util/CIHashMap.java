package com.ifalot.sfie.util;

import java.util.HashMap;

public class CIHashMap<T> extends HashMap<String, T> {

    @Override
    public T get(Object key) {
        return super.get(((String)key).toLowerCase());
    }

    @Override
    public T put(String key, T value) {
        return super.put(key.toLowerCase(), value);
    }

}
