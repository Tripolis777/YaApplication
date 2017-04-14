package com.example.vkaryagin.yaapplication.Fragments;

import android.os.Bundle;

import com.example.vkaryagin.yaapplication.Database.YaAppDBOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by v.karyagin on 07.04.2017.
 */

public class FragmentsCommutator {
    private static HashMap<String, List<Bundle>> fragmentHashMap;
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
        List<Bundle> queue = fragmentHashMap.get(name);
        if (queue == null) {
            queue = new ArrayList<>();
            fragmentHashMap.put(name, queue);
        }
        queue.add(0, data);
    }

    public List<Bundle> getData(String name) {
         return fragmentHashMap.remove(name);
    }
}
