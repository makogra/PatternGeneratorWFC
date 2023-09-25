package com.mako.patterngeneratorwfc.database.convertors;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

public class ListOfStringConverter {

    private static final String SEPARATOR = "|";

    @TypeConverter
    public static String listToString(List<String> list){
        if (list == null)
            return null;
        if (list.size() == 0)
            return "";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(list.size())
                .append(SEPARATOR);
        for (String string : list){
            stringBuilder.append(string.length())
                    .append(SEPARATOR)
                    .append(string);
        }
        return stringBuilder.toString();
    }

    @TypeConverter
    public static List<String> fromString(String string){
        if (string == null)
            return null;
        List<String> list = new ArrayList<>();

        if (string.equals(""))
            return list;

        int indexFrom = 0;
        int indexTo;
        int size;
        int length;
        String word;

        indexTo = string.indexOf(SEPARATOR);
        size = Integer.parseInt(string.substring(indexFrom, indexTo));
        indexTo++;

        for (int i = 0; i < size; i++) {
            indexFrom = indexTo;
            indexTo = string.indexOf(SEPARATOR, indexFrom);

            length = Integer.parseInt(string.substring(indexFrom, indexTo));

            indexFrom = indexTo + 1;
            indexTo = indexFrom + length;

            if (length == 0)
                word = "";
            else
                word = string.substring(indexFrom, indexTo);
            list.add(word);
        }

        return list;
    }
}
