package eu.gricom.basic.parser;

/**
 * Example demonstrating the difference between current GD-BASIC evaluation
 * and standard mathematical precedence
 * 
 * This class shows how expressions would be evaluated differently
 * with the current implementation vs. standard mathematical rules.
 */
public class PrecedenceExample {
    
    /**
     * Entry point that demonstrates differences between GD-BASIC's left-to-right expression evaluation and standard mathematical operator precedence.
     *
     * Runs several example expressions, printing their evaluation results under both systems, and summarizes key distinctions between the two approaches.
     */
    public static void main(String[] args) {
        System.out.println("=== GD-BASIC Mathematical Expression Evaluation ===");
        System.out.println();
        
        // Example 1: Basic arithmetic
        demonstrateExpression("1 + 2 * 3 - 4 / 5");
        
        // Example 2: Exponentiation
        demonstrateExpression("2 ^ 3 + 1");
        
        // Example 3: Complex expression
        demonstrateExpression("10 - 2 * 3 + 4 / 2");
        
        // Example 4: With parentheses (same result in both)
        demonstrateExpression("(1 + 2) * 3");
        
        // Example 5: Unary operators
        demonstrateExpression("-5 + 3");
        demonstrateExpression("--5");
        demonstrateExpression("!true");
        demonstrateExpression("!!true");
        
        System.out.println();
        System.out.println("=== Key Differences ===");
        System.out.println("1. Current GD-BASIC: Left-to-right evaluation");
        System.out.println("2. Standard Math: Operator precedence rules");
        System.out.println("3. Unary Operators: Supported via UnaryOperatorExpression class");
        System.out.println("4. Solution: Use parentheses for explicit precedence");
    }
    
    /**
     * Compares the evaluation of a given expression using both GD-BASIC's left-to-right rules and standard mathematical operator precedence.
     *
     * Prints the expression, then displays its evaluation and result under each system for illustrative purposes.
     *
     * @param expression the arithmetic or logical expression to be evaluated and compared
     */
    private static void demonstrateExpression(String expression) {
        System.out.println("Expression: " + expression);
        System.out.println();
        
        // Current GD-BASIC evaluation (left-to-right)
        System.out.println("Current GD-BASIC (left-to-right):");
        evaluateLeftToRight(expression);
        System.out.println();
        
        // Standard mathematical precedence
        System.out.println("Standard Mathematical Precedence:");
        evaluateWithPrecedence(expression);
        System.out.println();
        
        System.out.println("---");
        System.out.println();
    }
    
    /**
     * Simulates GD-BASIC's left-to-right evaluation of a space-separated arithmetic expression.
     *
     * Parses the expression by applying each operator to the running result in strict left-to-right order, ignoring standard operator precedence. Supports basic arithmetic and exponentiation. Prints each evaluation step and the final result.
     *
     * @param expression A space-separated arithmetic expression (e.g., "2 + 3 * 4").
     */
    private static void evaluateLeftToRight(String expression) {
        // This is a simplified simulation of the current parser behavior
        // In reality, the parser builds an expression tree left-to-right
        
        String[] parts = expression.split(" ");
        double result = Double.parseDouble(parts[0]);
        
        System.out.print("  " + parts[0]);
        
        for (int i = 1; i < parts.length; i += 2) {
            if (i + 1 < parts.length) {
                String operator = parts[i];
                double operand = Double.parseDouble(parts[i + 1]);
                
                System.out.print(" " + operator + " " + operand);
                
                switch (operator) {
                    case "+":
                        result += operand;
                        break;
                    case "-":
                        result -= operand;
                        break;
                    case "*":
                        result *= operand;
                        break;
                    case "/":
                        result /= operand;
                        break;
                    case "^":
                        result = Math.pow(result, operand);
                        break;
                }
                
                System.out.print(" = " + result);
            }
        }
        
        System.out.println();
        System.out.println("  Final Result: " + result);
    }
    
    /**
     * Simulates evaluation of an expression using standard mathematical operator precedence.
     *
     * Prints step-by-step evaluation and the final result for known example expressions, demonstrating how operator precedence affects the outcome compared to left-to-right evaluation.
     *
     * @param expression the expression string to simulate evaluation for
     */
    private static void evaluateWithPrecedence(String expression) {
        // This simulates how the expression would be evaluated with proper precedence
        // In a real implementation, the parser would build the correct expression tree
        
        if (expression.contains("1 + 2 * 3 - 4 / 5")) {
            System.out.println("  1 + 2 * 3 - 4 / 5");
            System.out.println("  = 1 + 6 - 0.8");
            System.out.println("  = 7 - 0.8");
            System.out.println("  = 6.2");
            System.out.println("  Final Result: 6.2");
        } else if (expression.contains("2 ^ 3 + 1")) {
            System.out.println("  2 ^ 3 + 1");
            System.out.println("  = 8 + 1");
            System.out.println("  = 9");
            System.out.println("  Final Result: 9.0");
        } else if (expression.contains("10 - 2 * 3 + 4 / 2")) {
            System.out.println("  10 - 2 * 3 + 4 / 2");
            System.out.println("  = 10 - 6 + 2");
            System.out.println("  = 4 + 2");
            System.out.println("  = 6");
            System.out.println("  Final Result: 6.0");
        } else if (expression.contains("(1 + 2) * 3")) {
            System.out.println("  (1 + 2) * 3");
            System.out.println("  = 3 * 3");
            System.out.println("  = 9");
            System.out.println("  Final Result: 9.0");
        } else if (expression.contains("-5 + 3")) {
            System.out.println("  -5 + 3");
            System.out.println("  = -2");
            System.out.println("  Final Result: -2.0");
        } else if (expression.contains("--5")) {
            System.out.println("  --5");
            System.out.println("  = -(-5)");
            System.out.println("  = 5");
            System.out.println("  Final Result: 5.0");
        } else if (expression.contains("!true")) {
            System.out.println("  !true");
            System.out.println("  = false");
            System.out.println("  Final Result: false");
        } else if (expression.contains("!!true")) {
            System.out.println("  !!true");
            System.out.println("  = !(!true)");
            System.out.println("  = !(false)");
            System.out.println("  = true");
            System.out.println("  Final Result: true");
        }
    }
    
    /**
     * Prints a suggested method hierarchy for implementing a precedence-aware expression parser.
     *
     * Outlines the sequence of parsing methods, each responsible for a specific operator precedence level, to guide the design of a parser that correctly handles standard mathematical and logical operator precedence.
     */
    public static void showImplementation() {
        System.out.println("=== Implementation Strategy ===");
        System.out.println();
        System.out.println("To implement standard precedence, replace the single operator() method");
        System.out.println("with multiple methods, each handling a specific precedence level:");
        System.out.println();
        System.out.println("1. expression() -> logicalOr()");
        System.out.println("2. logicalOr() -> logicalAnd()");
        System.out.println("3. logicalAnd() -> equality()");
        System.out.println("4. equality() -> comparison()");
        System.out.println("5. comparison() -> shift()");
        System.out.println("6. shift() -> addition()");
        System.out.println("7. addition() -> multiplication()");
        System.out.println("8. multiplication() -> exponentiation()");
        System.out.println("9. exponentiation() -> unary()");
        System.out.println("10. unary() -> atomic()");
        System.out.println();
        System.out.println("Each method calls the next higher precedence method");
        System.out.println("and handles its own operators in a while loop.");
    }
} 