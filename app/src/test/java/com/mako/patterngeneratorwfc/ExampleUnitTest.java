package com.mako.patterngeneratorwfc;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        int[][] testIntArr = new int[][]{
                {1,2},
                {3,4},
                {5,6}
        };

        System.out.println(Arrays.deepToString(testIntArr));

        assertEquals(4, 2 + 2);
    }

    @Test
    public void isEmptyStringEqualsToNull(){
        String stringEmpty = "";
        String stringNull = null;

        assertEquals(stringEmpty, stringNull);
    }

    @Test
    public void workingWithString(){
        String separator = "|";
        String[] strings = new String[]{"abcd", "f", "qwertyuiopasdfghjkl"};
        StringBuilder sb = new StringBuilder();
        sb.append(strings.length)
                .append(separator);
        for (String s : strings) {
            sb.append(s.length())
                    .append(separator)
                    .append(s);
        }
        String string = sb.toString();
        int indexFrom = 0;
        int indexTo;
        String size;
        int length;
        String word;

        indexTo = string.indexOf(separator);
        size = string.substring(indexFrom, indexTo);
        indexTo++;
        //indexFrom = string.indexOf(separator) + 1;

        assertEquals(3, strings.length);
        assertEquals("3", size);

        for (String s : strings) {
            indexFrom = indexTo;
            indexTo = string.indexOf(separator, indexFrom);
            length = Integer.parseInt(string.substring(indexFrom, indexTo));

            assertEquals(s.length(), length);

            indexFrom = indexTo + 1;
            indexTo = indexFrom + length;

            word = string.substring(indexFrom, indexTo);

            assertEquals(s, word);

        }

        /*
        indexTo = string.indexOf(separator, indexFrom);
        subString = string.substring(indexFrom, indexTo);

        assertEquals("4", subString);

        indexFrom = indexTo + 1;
        indexTo = string.indexOf(separator, indexFrom);
        subString = string.substring(indexFrom);

        assertEquals("abcd", subString);*/
    }

    @Test
    public void StringLength(){
        String s = "\\n\\r\\tasd";
        System.out.println(s.length());

        assertEquals(9, s.length());
    }

    @Test
    public void ArraysDeepToString(){

        int[][] arr = new int[][]{
                {12,1,4,7},
                {54,7,8,9}
        };

        assertEquals("[]", Arrays.deepToString(arr));
    }

    @Test
    public void StringShrink(){
        String s = "123890";
        String shrink = s.substring(1,s.length()-1);
        String sAfterShrink = "2389";
        assertEquals(sAfterShrink, shrink);
    }

    @Test
    public void ReplaceAll(){
        String s = "[12, 1, 4, 7], [54, 7, 8, 9]";
        String separator = "|";
        String s2 = s.replaceAll("], \\[", separator);
        String s3 = s2.replaceAll("[\\[\\] ]", "");

        assertEquals("[12, 1, 4, 7|54, 7, 8, 9]", s2);

        assertEquals("12,1,4,7|54,7,8,9", s3);

    }


}