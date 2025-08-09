package eu.gricom.basic.helper;

/**
 * Trace.java
 *
 * Description:
 *
 * The trace object provides a simple static method to print the BASIC line number of the statement given as a
 * parameter. It is toggled by the @PRAGMA command or the BASIC TRON and TROFF command.
 *
 * (c) = 2021 by Andreas Grimm, Den Haag, The Netherlands
 *
 */
public class Trace {

  private static boolean _bTraceLevel = false;
  private int _iLastLine = -1;

    /**
     * Constructor of the Logger class.
     *
     * @param strTraceLevel - name of the class that instantiates the logger
     */
  public Trace(final boolean strTraceLevel) {
      _bTraceLevel = strTraceLevel;
  }

  /**
   * Prints the line number of the BASIC command.
   * 
   * @param iSourceLine - BASIC line number to be printed
   */
  public void trace(final int iSourceLine) {

      if (_bTraceLevel && iSourceLine != _iLastLine) {
          System.out.print("[" + ConsoleColors.YELLOW + iSourceLine + ConsoleColors.RESET + "]");
          _iLastLine = iSourceLine;
      }
  }
}
