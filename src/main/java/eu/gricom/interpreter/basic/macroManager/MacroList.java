package eu.gricom.interpreter.basic.macroManager;

import eu.gricom.interpreter.basic.helper.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class MacroList {

    private final transient Logger _oLogger = new Logger(this.getClass().getName());
    private static Map<String, String> _aoMacroList = new HashMap<>();
    private static Map<String, String> _aoParameterList = new HashMap<>();

    public MacroList() {
    }

    public void add(String strName, Vector<String> vstrParameters, String strFunction) {
        _aoMacroList.put(strName, strFunction);
        //convert Vector to String
        String strParameters = vstrParameters.toString();
        _aoParameterList.put(strName, strParameters);
    }

    public String containsMacro(String strLine) {
        for (Map.Entry<String, String> oMacro : _aoMacroList.entrySet()) {
            String strMacroName = oMacro.getKey();

            // a macro name ends with a '('
            strMacroName += "(";

            if (strLine.contains(strMacroName)) {
                return strMacroName;
            }
        }

        return null;
    }

    public String getFunction(String strParameterList, String strMacroName) {
        String strWorkParameterList = strParameterList;
        Vector<String> vstrParametersFromSource = new Vector<>();
        Vector<String> vstrParametersFromDEF = new Vector<>();

        // Build the parameter list
        vstrParametersFromSource = toVector(strParameterList);

        _oLogger.debug("Macros found: " + strMacroName);
        _oLogger.debug("Parameters: " + vstrParametersFromSource.size());
        _oLogger.debug("Parameters: " + vstrParametersFromSource);

        for (Map.Entry<String, String> oMacro : _aoMacroList.entrySet()) {
            String strKey = oMacro.getKey();
            String strValue = oMacro.getValue();

            if (strMacroName.contains(strKey)) {
                // Generate a vector with all parameters to be changed
                for (Map.Entry<String, String> oParameters : _aoParameterList.entrySet()) {
                    String strMacroKey = oParameters.getKey();

                    if (strMacroName.contains(strMacroKey)) {
                        vstrParametersFromDEF = toVector(oParameters.getValue());
                        _oLogger.debug("Matched Parameters: " + vstrParametersFromDEF);
                    }
                }

                // Loop through the vectors and find the values to replace
                if (vstrParametersFromDEF.size() != vstrParametersFromSource.size()) {
                    _oLogger.error("Incorrect number of parameters in macro call. Expected: " + vstrParametersFromDEF.size() + ", provided: " + vstrParametersFromSource.size());
                }

                for (int iCounter = 0; iCounter < vstrParametersFromDEF.size(); iCounter++) {
                    String strReplacement = vstrParametersFromSource.get(iCounter);
                    String strOriginalValue = vstrParametersFromDEF.get(iCounter);

                    _oLogger.debug("Replace: <" + strOriginalValue + "> with <" + strReplacement + ">");

                    strValue = strValue.replace(strOriginalValue, strReplacement);
                }

                return strValue;
            }
        }

        return new String();
    }

    public void print() {
        _oLogger.debug("List of Macros:");
        _oLogger.debug("Macros: " + _aoMacroList.toString());
        _oLogger.debug("Parameters: " + _aoParameterList.toString());
    }

    private Vector<String> toVector (String strStringCodedVector) {
        Vector<String> vstrVectorFromString = new Vector();
        String strValue = new String();

        if (strStringCodedVector.indexOf("[") >= 0) {
            strStringCodedVector = strStringCodedVector.substring(strStringCodedVector.indexOf("[") + 1);
        }

        if (strStringCodedVector.indexOf("]") >= 0) {
            strStringCodedVector = strStringCodedVector.substring(0, strStringCodedVector.indexOf("]"));
        }

        int iCommaFound = strStringCodedVector.indexOf(",");

        while (iCommaFound > 0) {
            String strFirstParameter = strStringCodedVector.substring(0, iCommaFound);
            strStringCodedVector = strStringCodedVector.substring(iCommaFound + 1).trim();
            vstrVectorFromString.add(strFirstParameter);
            iCommaFound = strStringCodedVector.indexOf(",");
        }

        vstrVectorFromString.add(strStringCodedVector);

        return vstrVectorFromString;
    }
}
