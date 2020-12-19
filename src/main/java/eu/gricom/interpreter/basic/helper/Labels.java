package eu.gricom.interpreter.basic.helper;

import java.util.Map;

/**
 * Label.java
 * <p>
 * Description:
 * <p>
 * The Label structure contained a list of the different labels found in the source code. This structure is now part
 * of the memory management function and has been deprecated.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, Den Haag, The Netherlands
 */
@Deprecated
public class Labels {
    private static Map<String, Integer> _aoLabels;

    /**
     * Verification that the label has been found and added in the list of labels.
     *
     * @param strKey - key for the selection of the Label.
     * @return true if the key exists in the list of labels.
     */
    public static boolean containsKey(final String strKey) {

        return (_aoLabels.containsKey(strKey));
    }

    /**
     * Getter function for the Label object.
     *
     * @param strKey - key for the selection of the Label
     * @return line number of the label
     */
    public final int get(final String strKey) {

        return (_aoLabels.get(strKey).intValue());
    }
}
