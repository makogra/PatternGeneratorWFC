package com.mako.patterngeneratorwfc.database.convertors;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ListOfStringConverterTest {

    private static final String SEPARATOR = "|";

    @Test
    public void listToString() {
        List<String> list = new ArrayList<String>(){{
            add("a");
            add("A");
            add("|");
            add("");
            add("123");
            add("qwepoirtyzxcmnasdlkj");
            add("asd|asd");
            add("|WWW|WWW|");
            add("......");
            add("\\n\\r\\tasd");
            add("AEWASDQADVWRW");
        }};

        String string = "11|1|a1|A1||0|3|12320|qwepoirtyzxcmnasdlkj7|asd|asd9||WWW|WWW|6|......9|\\n\\r\\tasd13|AEWASDQADVWRW";

        assertEquals(list, ListOfStringConverter.fromString(string));
    }

    @Test
    public void fromString() {
        List<String> list = new ArrayList<String>(){{
            add("a");
            add("A");
            add("|");
            add("");
            add("123");
            add("qwepoirtyzxcmnasdlkj");
            add("asd|asd");
            add("|WWW|WWW|");
            add("......");
            add("\\n\\r\\tasd");
            add("AEWASDQADVWRW");
        }};

        String string = "11|1|a1|A1||0|3|12320|qwepoirtyzxcmnasdlkj7|asd|asd9||WWW|WWW|6|......9|\\n\\r\\tasd13|AEWASDQADVWRW";

        assertEquals(string, ListOfStringConverter.listToString(list));
    }

    @Test
    public void NullListToString_returnNull(){
        assertNull(ListOfStringConverter.listToString(null));
    }

    @Test
    public void NullFromString_ReturnNull(){
        assertNull(ListOfStringConverter.fromString(null));
    }

    @Test
    public void EmptyListToString_correct(){
        assertEquals("", ListOfStringConverter.listToString(new ArrayList<>()));
    }

    @Test
    public void EmptyFromString_correct(){
        assertEquals(new ArrayList<>(), ListOfStringConverter.fromString(""));
    }
}