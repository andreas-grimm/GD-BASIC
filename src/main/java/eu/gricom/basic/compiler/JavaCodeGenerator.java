package eu.gricom.basic.compiler;

import eu.gricom.basic.memoryManager.LineNumberXRef;
import eu.gricom.basic.statements.AssignStatement;
import eu.gricom.basic.statements.ColonStatement;
import eu.gricom.basic.statements.DoStatement;
import eu.gricom.basic.statements.EndStatement;
import eu.gricom.basic.statements.EndWhileStatement;
import eu.gricom.basic.statements.Expression;
import eu.gricom.basic.statements.ForStatement;
import eu.gricom.basic.statements.GosubStatement;
import eu.gricom.basic.statements.GotoStatement;
import eu.gricom.basic.statements.IfThenStatement;
import eu.gricom.basic.statements.NextStatement;
import eu.gricom.basic.statements.OnGosubStatement;
import eu.gricom.basic.statements.OnGotoStatement;
import eu.gricom.basic.statements.OperatorExpression;
import eu.gricom.basic.statements.PragmaStatement;
import eu.gricom.basic.statements.PrintStatement;
import eu.gricom.basic.statements.RemStatement;
import eu.gricom.basic.statements.ReturnStatement;
import eu.gricom.basic.statements.Statement;
import eu.gricom.basic.statements.UnaryOperatorExpression;
import eu.gricom.basic.statements.UntilStatement;
import eu.gricom.basic.statements.VariableExpression;
import eu.gricom.basic.statements.WhileStatement;
import eu.gricom.basic.functions.Function;
import eu.gricom.basic.tokenizer.BasicTokenType;
import eu.gricom.basic.variableTypes.BcdValue;
import eu.gricom.basic.variableTypes.BooleanValue;
import eu.gricom.basic.variableTypes.IntegerValue;
import eu.gricom.basic.variableTypes.LongValue;
import eu.gricom.basic.variableTypes.RealValue;
import eu.gricom.basic.variableTypes.StringValue;
import eu.gricom.basic.variableTypes.Value;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * JavaCodeGenerator turns the parsed statements of a BASIC program into the source code of one Java class.
 *
 * <p>How it works, in plain words:</p>
 * <ul>
 *   <li>First the statements are put in a flat, numbered list of <b>units</b>. A block IF (IF ... THEN ... END-IF)
 *       is split into its parts: the test, the THEN statements, a jump over the ELSE part, and the ELSE statements.</li>
 *   <li>The generated class has one <code>switch</code> with one <code>case</code> per unit. The variable
 *       <code>iPc</code> is the number of the unit to run next. This is the same idea as the interpreter's
 *       program pointer.</li>
 *   <li>Variables are kept in the interpreter's own <code>VariableManagement</code> class, so the type rules
 *       (for example <code>A%</code> is an integer) are exactly the same.</li>
 *   <li>Arithmetic, comparisons and built-in functions call the same <code>Value</code> methods and the same
 *       static <code>execute</code> methods that the interpreter calls.</li>
 *   <li>GOSUB, ON GOSUB, FOR, WHILE and DO use the interpreter's shared <code>Stack</code> class in the same order,
 *       so the compiled program behaves the same way as the interpreter.</li>
 * </ul>
 *
 * <p>Anything the compiler does not support yet makes {@link #generate()} throw a {@link CompileException}.</p>
 */
public final class JavaCodeGenerator {

    /** What kind of step a unit is. */
    private enum Kind {
        /** A normal BASIC statement. */
        STATEMENT,
        /** The condition test of a block IF. */
        IF_TEST,
        /** The jump that skips the ELSE part after the THEN part has run. */
        JUMP
    }

    /** One step of the generated program. */
    private static final class Unit {
        private final Statement _oStatement;
        private final int _iLine;
        private final Kind _eKind;
        /** For IF_TEST: the unit to run when the condition is false. For JUMP: the unit after the block. */
        private int _iTarget = -1;

        Unit(final Statement oStatement, final int iLine, final Kind eKind) {
            _oStatement = oStatement;
            _iLine = iLine;
            _eKind = eKind;
        }
    }

    private static final Map<BasicTokenType, String> ONE_PARAMETER_FUNCTIONS = Map.ofEntries(
            Map.entry(BasicTokenType.ABS, "Abs"),
            Map.entry(BasicTokenType.ASC, "Asc"),
            Map.entry(BasicTokenType.ATN, "Atn"),
            Map.entry(BasicTokenType.CDBL, "Cdbl"),
            Map.entry(BasicTokenType.CHR, "Chr"),
            Map.entry(BasicTokenType.CINT, "Cint"),
            Map.entry(BasicTokenType.COS, "Cos"),
            Map.entry(BasicTokenType.EXP, "Exp"),
            Map.entry(BasicTokenType.LEN, "Len"),
            Map.entry(BasicTokenType.LOG, "Log"),
            Map.entry(BasicTokenType.LOG10, "Log10"),
            Map.entry(BasicTokenType.LOWER, "Lower"),
            Map.entry(BasicTokenType.SIN, "Sin"),
            Map.entry(BasicTokenType.SQR, "Sqr"),
            Map.entry(BasicTokenType.STR, "Str"),
            Map.entry(BasicTokenType.TAN, "Tan"),
            Map.entry(BasicTokenType.UPPER, "Upper"),
            Map.entry(BasicTokenType.VAL, "Val"));

    private static final Map<BasicTokenType, String> TWO_PARAMETER_FUNCTIONS = Map.of(
            BasicTokenType.INSTR, "Instr",
            BasicTokenType.LEFT, "Left",
            BasicTokenType.RIGHT, "Right");

    private static final Map<BasicTokenType, String> THREE_PARAMETER_FUNCTIONS = Map.of(
            BasicTokenType.MID, "Mid");

    private static final Map<BasicTokenType, String> NO_PARAMETER_FUNCTIONS = Map.of(
            BasicTokenType.MEM, "Mem",
            BasicTokenType.RND, "Rnd",
            BasicTokenType.TIME, "Time");

    /** Maps a binary operator token to the name of the Value method that does the work. */
    private static final Map<BasicTokenType, String> BINARY_OPERATORS = Map.ofEntries(
            Map.entry(BasicTokenType.PLUS, "plus"),
            Map.entry(BasicTokenType.MINUS, "minus"),
            Map.entry(BasicTokenType.MULTIPLY, "multiply"),
            Map.entry(BasicTokenType.DIVIDE, "divide"),
            Map.entry(BasicTokenType.POWER, "power"),
            Map.entry(BasicTokenType.AND, "and"),
            Map.entry(BasicTokenType.OR, "or"),
            Map.entry(BasicTokenType.COMPARE_EQUAL, "equals"),
            Map.entry(BasicTokenType.COMPARE_NOT_EQUAL, "notEqual"),
            Map.entry(BasicTokenType.SMALLER, "smallerThan"),
            Map.entry(BasicTokenType.SMALLER_EQUAL, "smallerEqualThan"),
            Map.entry(BasicTokenType.GREATER, "largerThan"),
            Map.entry(BasicTokenType.GREATER_EQUAL, "largerEqualThan"),
            Map.entry(BasicTokenType.MODULO, "modulo"));

    /** The same operators written as text, used by expressions that the parser builds from text. */
    private static final Map<String, String> TEXT_OPERATORS = Map.ofEntries(
            Map.entry("+", "plus"),
            Map.entry("-", "minus"),
            Map.entry("*", "multiply"),
            Map.entry("/", "divide"),
            Map.entry("^", "power"),
            Map.entry("&", "and"),
            Map.entry("AND", "and"),
            Map.entry("|", "or"),
            Map.entry("OR", "or"),
            Map.entry("=", "equals"),
            Map.entry("==", "equals"),
            Map.entry("!=", "notEqual"),
            Map.entry("<", "smallerThan"),
            Map.entry("<=", "smallerEqualThan"),
            Map.entry(">", "largerThan"),
            Map.entry(">=", "largerEqualThan"),
            Map.entry("%", "modulo"));

    private static final String STATEMENT_PACKAGE = "eu.gricom.basic.statements.";
    private static final String FUNCTIONS_PACKAGE = "eu.gricom.basic.functions.";
    private static final String PC = "iPc";
    /** Number of units per generated method. Keeps each method far below the JVM size limit. */
    private static final int PART_SIZE = 100;

    private final List<Statement> _aoStatements;
    private final String _strClassName;
    private final List<Unit> _aoUnits = new ArrayList<>();

    /**
     * Creates the generator and splits the program into units.
     *
     * @param aoStatements the parsed statements, in program order
     * @param strClassName the name of the Java class to generate (must match the file name)
     * @throws CompileException if a line number of a statement cannot be found
     */
    public JavaCodeGenerator(final List<Statement> aoStatements, final String strClassName)
            throws CompileException {
        _aoStatements = aoStatements;
        _strClassName = strClassName;
        flatten(aoStatements);
    }

    /**
     * Generates the Java source code of the whole class.
     *
     * @return the Java source code as text
     * @throws CompileException if the program uses something the compiler does not support yet
     */
    public String generate() throws CompileException {
        if (_aoStatements.isEmpty()) {
            throw new CompileException("Compiler: the program is empty, there is nothing to compile");
        }
        StringBuilder oSource = new StringBuilder();
        oSource.append("// Generated by the GD-BASIC compiler from a BASIC program.\n");
        oSource.append("// Do not edit by hand: change the BASIC program and compile it again.\n");
        oSource.append("import eu.gricom.basic.memoryManager.Stack;\n");
        oSource.append("import eu.gricom.basic.memoryManager.VariableManagement;\n");
        oSource.append("import eu.gricom.basic.statements.OperatorExpression;\n");
        oSource.append("import eu.gricom.basic.statements.UnaryOperatorExpression;\n");
        oSource.append("import eu.gricom.basic.statements.WhileStatement;\n");
        oSource.append("import eu.gricom.basic.variableTypes.BcdValue;\n");
        oSource.append("import eu.gricom.basic.variableTypes.BooleanValue;\n");
        oSource.append("import eu.gricom.basic.variableTypes.IntegerValue;\n");
        oSource.append("import eu.gricom.basic.variableTypes.LongValue;\n");
        oSource.append("import eu.gricom.basic.variableTypes.RealValue;\n");
        oSource.append("import eu.gricom.basic.variableTypes.StringValue;\n");
        oSource.append("import eu.gricom.basic.variableTypes.Value;\n");
        oSource.append("\n");
        oSource.append("public final class ").append(_strClassName).append(" {\n");
        oSource.append("\n");
        oSource.append("    private final VariableManagement _vars = new VariableManagement();\n");
        oSource.append("    private final Stack _stack = new Stack();\n");
        oSource.append("    private final boolean[] _forStarted = new boolean[").append(_aoUnits.size())
                .append("];\n");
        oSource.append("\n");
        oSource.append("    public static void main(final String[] args) throws Exception {\n");
        // Same log level setting as the interpreter's main method, so the compiled program stays quiet
        oSource.append("        new eu.gricom.basic.helper.Logger(\"").append(_strClassName).append("\")")
                .append(".setLogLevel(eu.gricom.basic.helper.EnvParam.getString(\"log_level\"));\n");
        oSource.append("        new ").append(_strClassName).append("().run();\n");
        oSource.append("    }\n");
        oSource.append("\n");
        oSource.append("    private Value var(final String strName) throws Exception {\n");
        oSource.append("        if (!_vars.mapContainsKey(strName)) {\n");
        oSource.append("            throw new RuntimeException(\"Unknown variable <\" + strName + \">\");\n");
        oSource.append("        }\n");
        oSource.append("        return _vars.getMap(strName);\n");
        oSource.append("    }\n");
        oSource.append("\n");
        // The cases are split into parts of PART_SIZE units. Java limits the size of one method (64 KB),
        // so a long program would not compile as one big method.
        int iParts = (_aoUnits.size() + PART_SIZE - 1) / PART_SIZE;
        oSource.append("    private void run() throws Exception {\n");
        oSource.append("        int ").append(PC).append(" = 0;\n");
        oSource.append("        while (").append(PC).append(" >= 0) {\n");
        oSource.append("            switch (").append(PC).append(" / ").append(PART_SIZE).append(") {\n");
        for (int iPart = 0; iPart < iParts; iPart++) {
            oSource.append("                case ").append(iPart).append(": ").append(PC).append(" = part")
                    .append(iPart).append("(").append(PC).append("); break;\n");
        }
        oSource.append("                default: ").append(PC).append(" = -1; break;\n");
        oSource.append("            }\n");
        oSource.append("        }\n");
        oSource.append("    }\n");
        for (int iPart = 0; iPart < iParts; iPart++) {
            int iFirst = iPart * PART_SIZE;
            int iLast = Math.min(iFirst + PART_SIZE, _aoUnits.size());
            oSource.append("\n");
            oSource.append("    private int part").append(iPart).append("(int ").append(PC)
                    .append(") throws Exception {\n");
            oSource.append("        while (").append(PC).append(" >= ").append(iFirst).append(" && ")
                    .append(PC).append(" < ").append(iLast).append(") {\n");
            oSource.append("            switch (").append(PC).append(") {\n");
            for (int iIndex = iFirst; iIndex < iLast; iIndex++) {
                oSource.append(caseBlock(iIndex, _aoUnits.get(iIndex)));
            }
            oSource.append("            }\n");
            oSource.append("        }\n");
            oSource.append("        return ").append(PC).append(";\n");
            oSource.append("    }\n");
        }
        oSource.append("}\n");
        return oSource.toString();
    }

    /** Puts the statements into the flat list of units. A block IF is split into several units. */
    private void flatten(final List<Statement> aoStatements) throws CompileException {
        for (Statement oStatement : aoStatements) {
            if (oStatement instanceof IfThenStatement oIf && oIf.hasBlockStatements()) {
                flattenBlockIf(oIf);
            } else {
                _aoUnits.add(new Unit(oStatement, lineOf(oStatement), Kind.STATEMENT));
            }
        }
    }

    /**
     * A block IF becomes: the test, the THEN statements, a jump over the ELSE part (only if there is an ELSE),
     * and the ELSE statements. When the test is false, execution goes to the ELSE part or after the block.
     */
    private void flattenBlockIf(final IfThenStatement oIf) throws CompileException {
        Unit oTest = new Unit(oIf, lineOf(oIf), Kind.IF_TEST);
        _aoUnits.add(oTest);
        flatten(oIf.getIfBlockStatements());
        List<Statement> aoElse = oIf.getElseBlockStatements();
        Unit oJump = null;
        if (aoElse != null && !aoElse.isEmpty()) {
            oJump = new Unit(null, oTest._iLine, Kind.JUMP);
            _aoUnits.add(oJump);
        }
        oTest._iTarget = _aoUnits.size();
        if (aoElse != null) {
            flatten(aoElse);
        }
        if (oJump != null) {
            oJump._iTarget = _aoUnits.size();
        }
    }

    private int lineOf(final Statement oStatement) throws CompileException {
        try {
            return new LineNumberXRef().getLineNumberFromToken(oStatement.getTokenNumber());
        } catch (Exception eException) {
            throw new CompileException("Compiler: no BASIC line number found for a statement");
        }
    }

    /** Builds the switch case for one unit. */
    private String caseBlock(final int iIndex, final Unit oUnit) throws CompileException {
        String strBody = unitBody(iIndex, oUnit);
        StringBuilder oBlock = new StringBuilder();
        oBlock.append("                case ").append(iIndex).append(": {\n");
        oBlock.append("                    // BASIC line ").append(oUnit._iLine).append("\n");
        for (String strLine : strBody.split("\n")) {
            oBlock.append("                    ").append(strLine).append("\n");
        }
        // END returns from run(), so a break after it would be unreachable code, which Java rejects
        if (!strBody.endsWith("return;")) {
            oBlock.append("                    break;\n");
        }
        oBlock.append("                }\n");
        return oBlock.toString();
    }

    /** Returns the Java code that runs one unit and sets the next unit to run. */
    private String unitBody(final int iIndex, final Unit oUnit) throws CompileException {
        String strNext = String.valueOf(iIndex + 1);
        if (oUnit._eKind == Kind.JUMP) {
            return setPc(String.valueOf(oUnit._iTarget));
        }
        if (oUnit._eKind == Kind.IF_TEST) {
            IfThenStatement oIf = (IfThenStatement) oUnit._oStatement;
            return "if (((BooleanValue) " + expression(oIf.getCondition(), iIndex) + ").isTrue()) {\n"
                    + "    " + setPc(strNext) + "\n"
                    + "} else {\n"
                    + "    " + setPc(String.valueOf(oUnit._iTarget)) + "\n"
                    + "}";
        }
        return statementBody(iIndex, oUnit, strNext);
    }

    /** Returns the Java code for one BASIC statement. */
    private String statementBody(final int iIndex, final Unit oUnit, final String strNext)
            throws CompileException {
        Statement oStatement = oUnit._oStatement;
        if (oStatement instanceof RemStatement || oStatement instanceof ColonStatement) {
            return setPc(strNext);
        }
        if (oStatement instanceof EndStatement) {
            // -1 is "no next unit": the loop in run() stops
            return setPc("-1");
        }
        if (oStatement instanceof AssignStatement oAssign) {
            String strKey = oAssign.getKey();
            if (strKey.contains("(")) {
                throw unsupported(oUnit, "arrays (DIM and array assignment)");
            }
            return "_vars.putMap(" + javaString(strKey) + ", " + expression(oAssign.getExpression(), iIndex) + ");\n"
                    + setPc(strNext);
        }
        if (oStatement instanceof PrintStatement oPrint) {
            return printBody(iIndex, oUnit, oPrint) + setPc(strNext);
        }
        if (oStatement instanceof PragmaStatement oPragma) {
            return "new " + STATEMENT_PACKAGE + "PragmaStatement(" + oPragma.getTokenNumber() + ", "
                    + javaString(oPragma.getParameter()) + ", " + javaString(oPragma.getValue()) + ").execute();\n"
                    + setPc(strNext);
        }
        if (oStatement instanceof GotoStatement oGoto) {
            return setPc(String.valueOf(indexOfTarget(oUnit, oGoto.getTarget())));
        }
        if (oStatement instanceof GosubStatement oGosub) {
            // The return address is the first unit after this line. It is pushed now, so RETURN only pops it.
            return "_stack.push(new IntegerValue(" + indexAfterLine(oUnit._iLine) + "));\n"
                    + setPc(String.valueOf(indexOfTarget(oUnit, oGosub.getTarget())));
        }
        if (oStatement instanceof OnGotoStatement oOnGoto) {
            return onBody(oUnit, oOnGoto.getSelectExpression(), oOnGoto.getTargetLineNumbers(), false, strNext);
        }
        if (oStatement instanceof OnGosubStatement oOnGosub) {
            return onBody(oUnit, oOnGosub.getSelectExpression(), oOnGosub.getTargetLineNumbers(), true, strNext);
        }
        if (oStatement instanceof ReturnStatement || oStatement instanceof NextStatement
                || oStatement instanceof EndWhileStatement) {
            return setPc("((IntegerValue) _stack.pop()).toInt()");
        }
        if (oStatement instanceof IfThenStatement oIf) {
            return singleLineIf(oUnit, oIf, strNext);
        }
        if (oStatement instanceof ForStatement oFor) {
            return forBody(iIndex, oUnit, oFor, strNext);
        }
        if (oStatement instanceof WhileStatement oWhile) {
            return "if (WhileStatement.isConditionTrue(" + expression(oWhile.getCondition(), iIndex) + ")) {\n"
                    + "    _stack.push(new IntegerValue(" + iIndex + "));\n"
                    + "    " + setPc(strNext) + "\n"
                    + "} else {\n"
                    + "    " + setPc(String.valueOf(indexAfterLine(oWhile.getEndWhileLine()))) + "\n"
                    + "}";
        }
        if (oStatement instanceof DoStatement) {
            return "_stack.push(new IntegerValue(" + iIndex + "));\n" + setPc(strNext);
        }
        if (oStatement instanceof UntilStatement oUntil) {
            // Like the interpreter: the DO address is taken from the stack first, then the condition is tested.
            return "int iDoStatement = ((IntegerValue) _stack.pop()).toInt();\n"
                    + "if (!WhileStatement.isConditionTrue(" + expression(oUntil.getCondition(), iIndex) + ")) {\n"
                    + "    " + setPc("iDoStatement") + "\n"
                    + "} else {\n"
                    + "    " + setPc(strNext) + "\n"
                    + "}";
        }
        throw unsupported(oUnit, statementName(oStatement));
    }

    /** The PRINT statement writes each value, then a new line if the statement ends with one. */
    private String printBody(final int iIndex, final Unit oUnit, final PrintStatement oPrint)
            throws CompileException {
        StringBuilder oCode = new StringBuilder();
        if (oPrint.getExpressions() != null) {
            for (Expression oExpression : oPrint.getExpressions()) {
                oCode.append("System.out.print(").append(expression(oExpression, iIndex)).append(");\n");
            }
        }
        if (oPrint.isCRLF()) {
            oCode.append("System.out.println();\n");
        }
        return oCode.toString();
    }

    /** ON n GOTO / ON n GOSUB: a switch on the selected number. Out of range continues with the next unit. */
    private String onBody(final Unit oUnit, final Expression oSelect, final List<String> astrTargets,
                          final boolean bGosub, final String strNext) throws CompileException {
        int iIndex = _aoUnits.indexOf(oUnit);
        StringBuilder oCode = new StringBuilder();
        oCode.append("int iSelect = (int) (").append(expression(oSelect, iIndex)).append(").toReal();\n");
        if (bGosub) {
            oCode.append("if (iSelect >= 1 && iSelect <= ").append(astrTargets.size()).append(") {\n");
            oCode.append("    _stack.push(new IntegerValue(").append(indexAfterLine(oUnit._iLine)).append("));\n");
            oCode.append("}\n");
        }
        oCode.append("switch (iSelect) {\n");
        for (int iPosition = 0; iPosition < astrTargets.size(); iPosition++) {
            oCode.append("    case ").append(iPosition + 1).append(": ")
                    .append(setPc(String.valueOf(indexOfTarget(oUnit, astrTargets.get(iPosition))))).append(" break;\n");
        }
        oCode.append("    default: ").append(setPc(strNext)).append(" break;\n");
        oCode.append("}");
        return oCode.toString();
    }

    /** A single-line IF: when the condition is true, jump to the target line; otherwise continue. */
    private String singleLineIf(final Unit oUnit, final IfThenStatement oIf, final String strNext)
            throws CompileException {
        if (oIf.getTargetLineNumber() == 0) {
            throw unsupported(oUnit, "IF with a label (only line numbers are supported)");
        }
        return "if (((BooleanValue) " + expression(oIf.getCondition(), _aoUnits.indexOf(oUnit)) + ").isTrue()) {\n"
                + "    " + setPc(String.valueOf(indexOfLine(oUnit, oIf.getTargetLineNumber()))) + "\n"
                + "} else {\n"
                + "    " + setPc(strNext) + "\n"
                + "}";
    }

    /** FOR works like ForStatement.execute() in the interpreter, including the rounding of the counter. */
    private String forBody(final int iIndex, final Unit oUnit, final ForStatement oFor, final String strNext)
            throws CompileException {
        String strName = oFor.getName();
        if (strName.contains("(")) {
            throw unsupported(oUnit, "arrays used as FOR variables");
        }
        String strKey = javaString(strName);
        String strStep = expression(oFor.getStepSize(), iIndex);
        String strEnd = expression(oFor.getEndValue(), iIndex);
        String strStart = expression(oFor.getStartValue(), iIndex);
        String strExit = String.valueOf(indexAfterLine(oFor.getEndForStatement()));
        return "boolean bCountingDown = (" + strStep + ").toReal() < 0;\n"
                + "if (_forStarted[" + iIndex + "]) {\n"
                + "    double dEndValue = (" + strEnd + ").toReal();\n"
                + "    double dStepSize = (" + strStep + ").toReal();\n"
                + "    double dCounter = _vars.getMap(" + strKey + ").toReal();\n"
                + "    if (dCounter + dStepSize <= dEndValue && !bCountingDown"
                + " || dCounter + dStepSize >= dEndValue && bCountingDown) {\n"
                + "        _vars.putMap(" + strKey + ", new RealValue(RealValue.round(dCounter + dStepSize, 2)));\n"
                + "        _stack.push(new IntegerValue(" + iIndex + "));\n"
                + "        " + setPc(strNext) + "\n"
                + "    } else {\n"
                + "        _forStarted[" + iIndex + "] = false;\n"
                + "        " + setPc(strExit) + "\n"
                + "    }\n"
                + "} else {\n"
                + "    _stack.push(new IntegerValue(" + iIndex + "));\n"
                + "    _vars.putMap(" + strKey + ", " + strStart + ");\n"
                + "    _forStarted[" + iIndex + "] = true;\n"
                + "    " + setPc(strNext) + "\n"
                + "}";
    }

    /** Builds a Java expression (of type Value) for a BASIC expression. */
    private String expression(final Expression oExpression, final int iIndex) throws CompileException {
        if (oExpression instanceof RealValue oReal) {
            return "new RealValue(" + Double.toString(oReal.toReal()) + ")";
        }
        if (oExpression instanceof IntegerValue oInteger) {
            return "new IntegerValue(" + oInteger.toInt() + ")";
        }
        if (oExpression instanceof LongValue oLong) {
            return "new LongValue(" + oLong.toLong().longValue() + "L)";
        }
        if (oExpression instanceof StringValue oString) {
            return "new StringValue(" + javaString(oString.toString()) + ")";
        }
        if (oExpression instanceof BooleanValue oBoolean) {
            return "new BooleanValue(" + oBoolean.isTrue() + ")";
        }
        if (oExpression instanceof BcdValue oBcd) {
            return "new BcdValue(new java.math.BigDecimal(" + javaString(oBcd.getBigDecimal().toPlainString())
                    + "))";
        }
        if (oExpression instanceof VariableExpression oVariable) {
            if (oVariable.getName().contains("(")) {
                throw unsupportedExpression("arrays");
            }
            return "var(" + javaString(oVariable.getName()) + ")";
        }
        if (oExpression instanceof OperatorExpression oOperator) {
            return binary(iIndex, oOperator);
        }
        if (oExpression instanceof UnaryOperatorExpression oUnary) {
            return unary(oUnary);
        }
        if (oExpression instanceof Function oFunction) {
            return function(oFunction);
        }
        throw unsupportedExpression(oExpression.getClass().getSimpleName());
    }

    private String binary(final int iIndex, final OperatorExpression oOperator) throws CompileException {
        String strLeft = expression(oOperator.getLeftOperand(), iIndex);
        String strRight = expression(oOperator.getRightOperand(), iIndex);
        BasicTokenType oType = oOperator.getTokenType();
        if (oType == BasicTokenType.SHIFT_LEFT) {
            return "OperatorExpression.integerShiftLeft(" + strLeft + ", " + strRight + ")";
        }
        if (oType == BasicTokenType.SHIFT_RIGHT) {
            return "OperatorExpression.integerShiftRight(" + strLeft + ", " + strRight + ")";
        }
        String strMethod = oType == null ? null : BINARY_OPERATORS.get(oType);
        if (oType == null && oOperator.getOperator() != null) {
            String strText = oOperator.getOperator();
            if (">>".equals(strText)) {
                return "OperatorExpression.integerShiftRight(" + strLeft + ", " + strRight + ")";
            }
            if ("<<".equals(strText)) {
                return "OperatorExpression.integerShiftLeft(" + strLeft + ", " + strRight + ")";
            }
            strMethod = TEXT_OPERATORS.get(strText);
        }
        if (strMethod == null) {
            throw unsupportedExpression("operator " + oOperator.getOperator());
        }
        return "(" + strLeft + ")." + strMethod + "(" + strRight + ")";
    }

    private String unary(final UnaryOperatorExpression oUnary) throws CompileException {
        String strOperand = expression(oUnary.getOperand(), _aoUnits.size());
        BasicTokenType oType = oUnary.getTokenType();
        if (oType == BasicTokenType.PLUS) {
            return strOperand;
        }
        if (oType == BasicTokenType.MINUS) {
            return "UnaryOperatorExpression.evaluateUnaryMinus(" + strOperand + ")";
        }
        if (oType == BasicTokenType.NOT) {
            return "UnaryOperatorExpression.evaluateUnaryNot(" + strOperand + ")";
        }
        throw unsupportedExpression("unary operator " + oType);
    }

    private String function(final Function oFunction) throws CompileException {
        BasicTokenType oType = oFunction.getTokenType();
        int iExpected;
        String strClass;
        if (ONE_PARAMETER_FUNCTIONS.containsKey(oType)) {
            strClass = ONE_PARAMETER_FUNCTIONS.get(oType);
            iExpected = 1;
        } else if (TWO_PARAMETER_FUNCTIONS.containsKey(oType)) {
            strClass = TWO_PARAMETER_FUNCTIONS.get(oType);
            iExpected = 2;
        } else if (THREE_PARAMETER_FUNCTIONS.containsKey(oType)) {
            strClass = THREE_PARAMETER_FUNCTIONS.get(oType);
            iExpected = 3;
        } else if (NO_PARAMETER_FUNCTIONS.containsKey(oType)) {
            strClass = NO_PARAMETER_FUNCTIONS.get(oType);
            iExpected = 0;
        } else {
            throw unsupportedExpression("function " + oType);
        }
        Expression[] aoParameters = {oFunction.getFirstParam(), oFunction.getSecondParam(),
            oFunction.getThirdParam()};
        StringBuilder oArguments = new StringBuilder();
        for (int iParameter = 0; iParameter < iExpected; iParameter++) {
            if (aoParameters[iParameter] == null) {
                throw unsupportedExpression("function " + oType + " with the wrong number of parameters");
            }
            if (iParameter > 0) {
                oArguments.append(", ");
            }
            oArguments.append(expression(aoParameters[iParameter], _aoUnits.size()));
        }
        return FUNCTIONS_PACKAGE + strClass + ".execute(" + oArguments + ")";
    }

    private static String setPc(final String strTarget) {
        return PC + " = " + strTarget + ";";
    }

    /** Index of the first unit of a BASIC line number. */
    private int indexOfLine(final Unit oUnit, final int iLine) throws CompileException {
        for (int iIndex = 0; iIndex < _aoUnits.size(); iIndex++) {
            if (_aoUnits.get(iIndex)._iLine == iLine) {
                return iIndex;
            }
        }
        throw new CompileException("Statement not supported by the compiler yet (BASIC line " + oUnit._iLine
                + "): the line " + iLine + " does not exist in the program");
    }

    private int indexOfTarget(final Unit oUnit, final String strTarget) throws CompileException {
        try {
            return indexOfLine(oUnit, Integer.parseInt(strTarget.trim()));
        } catch (NumberFormatException eException) {
            throw unsupported(oUnit, "jump to a label (only line numbers are supported)");
        }
    }

    /** Index of the first unit after the given BASIC line. Returns the unit count if there is none. */
    private int indexAfterLine(final int iLine) {
        for (int iIndex = 0; iIndex < _aoUnits.size(); iIndex++) {
            if (_aoUnits.get(iIndex)._iLine > iLine) {
                return iIndex;
            }
        }
        return _aoUnits.size();
    }

    private CompileException unsupported(final Unit oUnit, final String strWhat) {
        return new CompileException("Statement not supported by the compiler yet (BASIC line "
                + oUnit._iLine + "): " + strWhat);
    }

    private CompileException unsupportedExpression(final String strWhat) {
        return new CompileException("Expression not supported by the compiler yet: " + strWhat);
    }

    private static String statementName(final Statement oStatement) {
        String strName = oStatement.getClass().getSimpleName();
        return strName.endsWith("Statement") ? strName.substring(0, strName.length() - "Statement".length())
                : strName;
    }

    /**
     * Turns text into a Java string literal, with escapes for quotes, back-slashes and special characters.
     *
     * @param strText the text
     * @return the literal, including the surrounding quotes
     */
    static String javaString(final String strText) {
        StringBuilder oLiteral = new StringBuilder("\"");
        for (char cChar : strText.toCharArray()) {
            switch (cChar) {
                case '\\' -> oLiteral.append("\\\\");
                case '"' -> oLiteral.append("\\\"");
                case '\n' -> oLiteral.append("\\n");
                case '\r' -> oLiteral.append("\\r");
                case '\t' -> oLiteral.append("\\t");
                default -> {
                    if (cChar < 0x20 || cChar > 0x7e) {
                        oLiteral.append(String.format("\\u%04x", (int) cChar));
                    } else {
                        oLiteral.append(cChar);
                    }
                }
            }
        }
        return oLiteral.append('"').toString();
    }
}
