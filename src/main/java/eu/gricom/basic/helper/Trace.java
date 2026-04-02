package eu.gricom.basic.helper;

/**
 * Trace.java
 * <p>
 * Description: The Trace class provides execution tracing capabilities for debugging BASIC programs. When enabled
 * through the @PRAGMA directive or TRON command, it prints the current BASIC line number during execution, helping
 * developers follow program flow.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
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
