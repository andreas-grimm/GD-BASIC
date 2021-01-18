package eu.gricom.interpreter.basic.statements;

import eu.gricom.interpreter.basic.helper.Logger;

import java.util.HashMap;
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
public class LineNumberStatement {
//    private final Logger _oLogger = new Logger(this.getClass().getName());
    private static Map<Integer, Integer> _aoLineNumbers = new HashMap<>();
    private static Map<Integer, Integer> _aoStatementNumbers = new HashMap<>();

    /**
     * add a line number destination in the memory management.
     *
     * @param iLineNumber - name of the label
     * @param iTokenNumber - statement number
     */
    public final void putLineNumber(final int iLineNumber, final int iTokenNumber) {

        _aoLineNumbers.put(iLineNumber, iTokenNumber);
    }

    /**
     * add a line number destination in the memory management.
     *
     * @param iTokenNumber - name of the label
     * @param iStatementNumber - statement number
     */
    public final void putStatementNumber(final int iTokenNumber, final int iStatementNumber) {
//        _oLogger.debug("-putStatementNumber-> iTokenNumber [" + iTokenNumber + "] iStatementNumber [" +
//        iStatementNumber + "]");
        _aoStatementNumbers.put(iTokenNumber, iStatementNumber);
    }

    /**
     * get the statement number of the line number searched.
     *
     * @param iStatementNumber - line number
     * @return - statement number
     */
    public final int getLineNumber(final int iStatementNumber) {
//        _oLogger.debug("-getTokenNumber-> Statement No. [" + iStatementNumber + "]");
        for (Map.Entry<Integer, Integer> oLine : _aoLineNumbers.entrySet()) {
            if (oLine.getValue().equals(iStatementNumber)) {
//                _oLogger.debug("-getTokenNumber-> found [" + oLine.getKey() + "]");
                return (oLine.getKey());
            }
        }
        return (0);
    }

    /**
     * get the statement number of the line number searched.
     *
     * @param iLineNumber - line number
     * @return - statement number
     */
    public final int getStatement(final int iLineNumber) {

        if (_aoLineNumbers.containsKey(iLineNumber)) {
            int iTokenNumber = _aoLineNumbers.get(iLineNumber);

            if (_aoStatementNumbers.containsKey(iTokenNumber)) {
//                _oLogger.debug("-getStatement-> Source Code Line No. [" + iLineNumber + "] jumps to [" +
//                _aoStatementNumbers.get(iTokenNumber) + "]");
                return (_aoStatementNumbers.get(iTokenNumber));
            }
        }

        return (0);
    }

    /**
     * get the statement number of the line number searched.
     *
     * @param iLineNumber - line number
     * @return - statement number
     */
    public final int getNextStatement(final int iLineNumber) {
        int iNextHigherStatement = 0;

        for (Map.Entry<Integer, Integer> oLine : _aoLineNumbers.entrySet()) {

            if (oLine.getKey() > iLineNumber) {
                int iNewNextHigher = oLine.getKey();

                if (iNextHigherStatement == 0) {
                    iNextHigherStatement = iNewNextHigher;
                }

                if (iNewNextHigher < iNextHigherStatement) {
                    iNextHigherStatement = iNewNextHigher;
                }
            }
        }

        return (iNextHigherStatement);
    }

    /**
     * verify that the Label is stored.
     *
     * @param iStatementNumber - Line number
     * @return - true if label is in the memory management
     */
    public final boolean contains(final int iStatementNumber) {

        return (_aoLineNumbers.containsKey(iStatementNumber));
    }
}
