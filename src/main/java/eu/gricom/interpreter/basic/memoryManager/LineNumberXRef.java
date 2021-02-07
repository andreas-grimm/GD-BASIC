package eu.gricom.interpreter.basic.memoryManager;

import eu.gricom.interpreter.basic.error.RuntimeException;
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
public class LineNumberXRef {
    private final Logger _oLogger = new Logger(this.getClass().getName());
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
        _oLogger.debug("-putStatementNumber-> iTokenNumber [" + iTokenNumber + "] iStatementNumber ["
                + iStatementNumber + "]");
        _aoStatementNumbers.put(iTokenNumber, iStatementNumber);
    }

    /**
     * get the statement number of the line number searched.
     *
     * @param iTokenNumber token number
     * @return BASIC source code line number
     * @throws RuntimeException a requested token number was not found
     */
    public final int getLineNumberFromToken(final int iTokenNumber) throws RuntimeException {
        for (Map.Entry<Integer, Integer> oLine : _aoLineNumbers.entrySet()) {
            if (oLine.getValue().equals(iTokenNumber)) {
                _oLogger.debug("-getLineNumberFromToken-> Token No. [" + iTokenNumber + "] ---> Line No. ["
                        + oLine.getKey() + "]");
                return oLine.getKey();
            }
        }

        throw new RuntimeException("getLineNumberFromToken: Token No. [" + iTokenNumber + "] not found");
    }

    /**
     * get the statement number of the line number searched.
     *
     * @param iStatement statement number
     * @return token number
     * @throws RuntimeException a requested token number was not found
     */
    public final int getTokenFromStatement(final int iStatement) throws RuntimeException {
        for (Map.Entry<Integer, Integer> oToken : _aoStatementNumbers.entrySet()) {
            if (oToken.getValue().equals(iStatement)) {
                _oLogger.debug("-getTokenFromStatement-> Statement No. [" + iStatement + "] ---> Token No. "
                        + "[" + oToken.getKey() + "]");
                return oToken.getKey();
            }
        }

        throw new RuntimeException("getTokenFromStatement-> Statement No. [" + iStatement + "] not found");
    }

    /**
     * get the statement number of the line number searched.
     *
     * @param iLineNumber BASIC source code line number
     * @return statement number
     * @throws RuntimeException a requested statement number was not found
     */
    public final int getStatementFromLineNumber(final int iLineNumber) throws RuntimeException {

        if (_aoLineNumbers.containsKey(iLineNumber)) {
            int iTokenNumber = _aoLineNumbers.get(iLineNumber);

            if (_aoStatementNumbers.containsKey(iTokenNumber)) {
                _oLogger.debug("-getStatementFromLineNumber-> Line No. [" + iLineNumber + "] ---> Statement ["
                        + _aoStatementNumbers.get(iTokenNumber) + "]");
                return _aoStatementNumbers.get(iTokenNumber);
            }
        }

        throw new RuntimeException("getStatementFromLineNumber: Line No. [" + iLineNumber + "] not found");
    }

    /**
     * get the statement number of the token number searched.
     *
     * @param iTokenNumber token number
     * @return statement number
     * @throws RuntimeException a requested statement number was not found
     */
    public final int getStatementFromToken(final int iTokenNumber) throws RuntimeException {

        if (_aoStatementNumbers.containsKey(iTokenNumber)) {
            _oLogger.debug("-getStatementFromToken-> Token No. [" + iTokenNumber + "] ---> Statement ["
                    + _aoStatementNumbers.get(iTokenNumber) + "]");
            return _aoStatementNumbers.get(iTokenNumber);
        }

        throw new RuntimeException("getStatementFromToken-> Token No. [" + iTokenNumber + "] not found");
    }

    /**
     * get the statement number of the line number searched.
     *
     * @param iLineNumber BASIC source code line number
     * @return next following BASIC source code number
     */
    public final int getNextLineNumber(final int iLineNumber) {
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
        _oLogger.debug("-getNextLineNumber-> Line No. [" + iLineNumber + "] ---> next Line No. ["
                + iNextHigherStatement + "]");

        return iNextHigherStatement;
    }

    /**
     * verify that a requested statement number is in the XRef table.
     *
     * @param iStatementNumber statement number
     * @return true if the statement is in the memory management
     */
    public final boolean contains(final int iStatementNumber) {

        return _aoLineNumbers.containsKey(iStatementNumber);
    }
}
