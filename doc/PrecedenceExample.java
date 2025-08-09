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
     * Demonstrates the difference in evaluation between current GD-BASIC
     * and standard mathematical precedence
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
     * Demonstrates how an expression would be evaluated in both systems
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
     * Simulates current GD-BASIC left-to-right evaluation
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
     * Simulates standard mathematical precedence evaluation
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
     * Shows how to implement the precedence-aware parser
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