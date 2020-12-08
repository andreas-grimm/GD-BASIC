package eu.gricom.interpreter.basic.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Logger.java
 * <p>
 * Description:
 * <p>
 * Light weight logger object as part of the infrastructure. This logger works similar to the log4j logger, but does provide less overhead.
 * <p>
 * (c) = 2004,..,2016 by Andreas Grimm, Den Haag, The Netherlands
 * <p>
 * This software is protected according to Dutch and International Law. Certain components of
 * this package might have been released under terms of conditions of the General Public License
 * of the GNU organization. Those modules are named with name and version number under the
 * following URL: http://www.gricom.nl/public_released
 * <p>
 * Modules and versions not named on this location are not released and cannot be used without
 * written consent of the author. General terms and conditions of the given URL apply. This
 * notice cannot not be used as evidence of public release.
 * <p>
 * Created in 2003
 * <p>
 * Modification Log:
 * $Log: Logger.java,v $
 * Revision 1.1  2009/04/21 19:18:20  Andreas
 * Initial check in on new directory
 * Committed on the Free edition of March Hare Software CVSNT Server.
 * Upgrade to CVS Suite for more features and support:
 * http://march-hare.com/cvsnt/
 *
 * Revision 1.1.2.1  2007/11/24 10:00:45  agrimm
 * Checkpoint November 24, 2007
 *
 * Revision 1.1  2007/11/10 21:10:00  agrimm
 * Windows Version
 * Java 1.5
 * Eclipse 3.x Europa
 *
 * <p>
 * @author $Author: Andreas $
 * @version $Id: Logger.java,v 1.1 2009/04/21 19:18:20 Andreas Exp $
 *
 */

public class Logger {

  private static PrintWriter           _oLogFile           = null;
  private String                       _strClassName       = this.getClass().getName();
  private static String                _strLogLevel        = "trace|debug|info|warning";

    /**
   * Constructor of the Logger class.
   * 
   * @param strClassName - name of the class that instantiates the logger
   */
  public Logger(final String strClassName) {
      if (strClassName != null) {
          _strClassName = new String(strClassName);
      }
  }

  /**
   * Sets the log file name and opens an empty log file.
   *   
   * @param strLogFileBaseName - name of the log file
   */
  public final void setFileName(final String strLogFileBaseName) {
    boolean bFileNameOK              = false;
    Time    oRightNow                = new Time();
    File    oLogFile                 = null;
    String  strFileName              = "";
    int     iSeqNo                   = 0;
    
    _oLogFile           = null;

    if (strLogFileBaseName == null) {
    // using an empty base name does not make sense - we stop here
        return;
    }

    while (!bFileNameOK) {
      // build up a hopefully unique filename

      strFileName = strLogFileBaseName + "." + oRightNow.getDate() + ".";
      strFileName += Integer.toString(iSeqNo);
      strFileName += ".log";

      // check to see if there is one already.
      // If so, increment the SeqNo and try again
      oLogFile = new File(strFileName);
      if (oLogFile.exists()) {
      // File exists. Try again with new seq. no.
        iSeqNo++;

      // Don't loop here!
        if (iSeqNo > 99999) {
          error("Cannot open log file.  Last tried with: <" + strLogFileBaseName + ">");
          break;
        }
      } else {
        bFileNameOK = true;
      }
    }

  // what a pretty filename! try opening it...
    if (bFileNameOK) {
      try {
         Time oTime = new Time();
         debug("Log File set to: <" + strFileName + ">");
         _oLogFile = new PrintWriter(new FileOutputStream(strFileName));
         _oLogFile.println(oTime.getDateTime() + "[eu.gricom.utilities.Logger] +++ initiated log file +++");
         _oLogFile.flush();
      } catch (FileNotFoundException err) {
          error("File generation problem: " + err);
          _oLogFile = null;
      }
    }
  }

  /**
   * Terminates the use of the logger.
   * 
   * Closes the log files and resets the log file name.
   */
  public final void endLogger() {
    if (_oLogFile != null) {
      info("logger terminates");
      
      _oLogFile.flush();
      _oLogFile.close();
      _oLogFile = null;
    }
  }
  
  /**
   * Stores trace message.
   * 
   * The message is either stored locally (_bUseLog4J is false) or is using the Log4J package
   * 
   * @param strMessage - content of the message
   */
  public final void trace(final String strMessage) {
      if (_strLogLevel.toLowerCase().contains("trace")) {
          
          // if Log4J is not to be used - use local foot print...
          storeLogMessage("trace", strMessage);
      }
  }
  
  /**
   * Stores debug message.
   * 
   * The message is either stored locally (_bUseLog4J is false) or is using the Log4J package
   * 
   * @param strMessage - content of the message
   */
  public final void debug(final String strMessage) {
      if (_strLogLevel.toLowerCase().contains("debug")) {
          
          // if Log4J is not to be used - use local foot print...
          storeLogMessage("debug", strMessage);
      }
  }
  
  
  /**
   * Stores info message.
   * 
   * The message is either stored locally (_bUseLog4J is false) or is using the Log4J package
   * 
   * @param strMessage - content of the message
   */
  public final void info(final String strMessage) {
      if (_strLogLevel.toLowerCase().contains("info")) {
          
          // if Log4J is not to be used - use local foot print...
          storeLogMessage("info", strMessage);
      }
  }
  
  /**
   * Stores warning message.
   * 
   * The message is either stored locally (_bUseLog4J is false) or is using the Log4J package
   * 
   * @param strMessage - content of the message
   */
  public final void warning(final String strMessage) {
      if (_strLogLevel.toLowerCase().contains("warning")) {
          
          // if Log4J is not to be used - use local foot print...
          storeLogMessage("warning", strMessage);
      }
  }
  
  /**
   * Stores error message.
   * 
   * The message is either stored locally (_bUseLog4J is false) or is using the Log4J package
   * 
   * @param strMessage - content of the message
   */
  public final void error(final String strMessage) {

      // if Log4J is not to be used - use local foot print...
      storeErrorMessage(strMessage);
  }

  /**
   * Local store of error messages.
   * 
   * @param strMessage - content of the message
   */
  private void storeErrorMessage(final String strMessage) {
      Time oTime = new Time();
      String strTime = new String(oTime.getDateTime());

      // if a log file is open -> use the log file, otherwise use default console output
      if (_oLogFile != null) {
          _oLogFile.println(strTime + " " + _strClassName + " [ERROR] " + strMessage);
          _oLogFile.flush();
      } else {
          System.out.println();
          System.err.println(strTime + " " + _strClassName + " [ERROR] " + strMessage);
      }
  }
  
    /**
     * Stores the log message.
     * 
     * this is the internal handling... is not supposed to be exposed.
     * 
     * @param strMessageType - type of the message - see log level
     * @param strMessage - content of the message
     */
    private void storeLogMessage(final String strMessageType, final String strMessage) {
        Time         oTime               = new Time();
        String       strTime             = new String(oTime.getDateTime());
        
        // if error line is ok - then print it, otherwise log error message
        if (_oLogFile != null) {
          _oLogFile.println(strTime + " " + _strClassName + " [" + strMessageType.toUpperCase() + "] " + strMessage);
          _oLogFile.flush();
        } else {
        // I/O error writing to log file
            System.out.println(strTime + " " + _strClassName + " [" + strMessageType.toUpperCase() + "] " + strMessage);
        }
    }
  
  /**
   * Changes the name of the log file.
   * 
   * @param strFileName - name of the new log file
   * @return - true, if change successful
   */
  public final boolean swapLogFile(final String strFileName) {
      if (strFileName != null) {
         _oLogFile.flush();
         _oLogFile.close();
            
         setFileName(strFileName);
      } else {
          _oLogFile = null;
      }
      
      return _oLogFile != null;
  }

  /**
   * Sets the Log Level.
   * 
   * @param strLogLevel - defines the log level: trace|debug|info|warning 
   */
  public final void setLogLevel(final String strLogLevel) {
    _strLogLevel = strLogLevel;
  }
}
