package com.mako.patterngeneratorwfc.wfc;

import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

public class Colors {

    private static final Map<String, Integer> map = new HashMap<String, Integer>(){{
        put("_", Color.LTGRAY);
        put("G", Color.GREEN);
        put("C", Color.YELLOW);
        put("S", Color.BLUE);
        put("M", Color.GRAY);
    }};

    public static Integer getValue(String colorStr){
        return map.containsKey(colorStr) ? map.get(colorStr) : map.get("");
    }

    public static String getKey(Integer value){
         for (Map.Entry<String, Integer> entry : map.entrySet()){
             if (entry.getValue().equals(value))
                 return entry.getKey();
         }
         return "_";
    }
}
