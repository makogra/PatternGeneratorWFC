package com.mako.patterngeneratorwfc.database.convertors;

import androidx.room.TypeConverter;

import java.util.Arrays;

public class NestedIntArrayConvertor {

    @TypeConverter
    public static int[][] fromString(String string){
        if (string == null)
            return null;
        if (string.equals("") || string.equals("[]") || string.equals("[[]]"))
            return new int[][]{};
        // Remove first and last characters, that represent boarder of whole arr - [...]
        String str = string.substring(1,string.length()-1);

        String separator = "|";
        str = str.replaceAll("], \\[", separator);
        str = str.replaceAll("[\\[\\] ]", "");

        String[] stringArr = str.split("\\|");
        int[][] result = new int[stringArr.length][];
        int[] helperInt;
        String[] helperString;
        for (int i = 0; i < stringArr.length; i++) {
            helperString = stringArr[i].split(",");
            helperInt = new int[helperString.length];
            for (int j = 0; j < helperString.length; j++) {
                helperInt[j] = Integer.parseInt(helperString[j]);
            }
            result[i] = helperInt;
        }
        return result;
    }

    @TypeConverter
    public static String intArrToString(int[][] intArr){
        return intArr == null ? null : Arrays.deepToString(intArr);
    }
}
