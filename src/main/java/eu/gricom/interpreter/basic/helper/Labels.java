package eu.gricom.interpreter.basic.helper;

import java.util.Map;

public class Labels {
    private static Map<String, Integer> _aoLabels;

    public static boolean containsKey(String strKey) {
        return(_aoLabels.containsKey(strKey));
    }

    public int get(String strKey) {
        return (_aoLabels.get(strKey).intValue());
    }
}
