package com.example.vkaryagin.yaapplication.Fragments;

import android.content.ContentValues;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Stack;

/**
 * Created by v.karyagin on 07.04.2017.
 */

public class FragmentsCommutator {
    private static HashMap<String, Stack<Bundle>> fragmentHashMap;
    private static FragmentsCommutator instance;

    public static FragmentsCommutator getInstance() {
        if (instance == null) {
            instance = new FragmentsCommutator();
        }
        return instance;
    }

    private FragmentsCommutator () {
        fragmentHashMap = new HashMap<>();
    }

    public void addData(String name, Bundle data) {
        Stack<Bundle> queue = fragmentHashMap.get(name);
        if (queue == null) {
            queue = new Stack<>();
            fragmentHashMap.put(name, queue);
        }
        queue.add(data);
    }

    public Stack<Bundle> getData(String name) {
         return fragmentHashMap.remove(name);
    }
}
