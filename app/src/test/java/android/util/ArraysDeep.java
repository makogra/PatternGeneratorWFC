package android.util;

import java.util.List;

public class ArraysDeep {

    public static boolean deepEqual(List<List<List<Integer>>> o1, List<List<List<Integer>>> o2) {
        if (o1.size() != o2.size())
            return false;
        for (int i = 0; i < o1.size(); i++) {
            if (o1.get(i).size() != o2.get(i).size())
                return false;
            for (int j = 0; j < o1.get(i).size(); j++) {
                if (o1.get(i).get(j).size() != o2.get(i).get(j).size())
                    return false;
                for (int k = 0; k < o1.get(i).get(j).size(); k++) {
                    if (!o1.get(i).get(j).get(k).equals(o2.get(i).get(j).get(k)))
                        return false;
                }
            }
        }
        return true;
    }

    public static boolean deepEqual(Integer[][][] o1, Integer[][][] o2){
        if (o1.length != o2.length)
            return false;
        for (int i = 0; i < o1.length; i++) {
            if (o1[i].length != o2[i].length)
                return false;
            for (int j = 0; j < o1[i].length; j++) {
                if (o1[i][j].length != o2[i][j].length)
                    return false;
                for (int k = 0; k < o1[i][j].length; k++) {
                    if (!o1[i][j][k].equals(o2[i][j][k]))
                        return false;
                }
            }
        }
        return true;
    }

    public static boolean deepEqualIntArr(List<Integer[][]> o1, List<Integer[][]> o2){
        if (o1.size() != o2.size())
            return false;
        for (int i = 0; i < o1.size(); i++) {
            if (o1.get(i).length != o2.get(i).length)
                return false;
            for (int j = 0; j < o1.get(i).length; j++) {
                if (o1.get(i)[j].length != o2.get(i)[j].length)
                    return false;
                for (int k = 0; k < o1.get(i)[j].length; k++) {
                    if (!o1.get(i)[j][k].equals(o2.get(i)[j][k]))
                        return false;
                }
            }
        }
        return true;
    }
}
