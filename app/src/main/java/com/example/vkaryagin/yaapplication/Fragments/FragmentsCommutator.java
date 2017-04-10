package com.example.vkaryagin.yaapplication.Fragments;

import android.app.Fragment;

import java.util.HashMap;

/**
 * Created by v.karyagin on 07.04.2017.
 */

public class FragmentsCommutator {
    private HashMap<String, Commutable> fragmentHashMap;

    public FragmentsCommutator () {}

    public void addFragment(String name, Commutable fragment) {
        fragmentHashMap.put(name, fragment);
    }

 //   public void commutate()
}
