import java.util.Scanner;


public class BodmasApp {

    public static void main(String args[]) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter an expression :");
        String input = scanner.nextLine().trim();

        try {

            if (input.indexOf(" ") > 0) {
                input = input.replaceAll("\\s", "");
                StringBuilder expressionBuilder = new StringBuilder(input);

                //solve bracket first
                while (expressionBuilder.toString().contains("(") && expressionBuilder.toString().contains(")")) {

                    int initialOpeningBracketIdx = findOpeningBracket(expressionBuilder.toString());
                    int initialClosingBracketIdx = findClosingBracket(expressionBuilder.toString());

                    //find nested bracket
                    String nestedExpression = expressionBuilder.substring(initialOpeningBracketIdx, initialClosingBracketIdx + 1);

                    // perform calculation
                    double result = calculate(nestedExpression);
                    //result builder
                    expressionBuilder.replace(initialOpeningBracketIdx, initialClosingBracketIdx + 1, String.valueOf(result));

                }

                //calculate final result
                char[] operatorArray = fetchOperatorOrder();
                String result = calculateSumByOperatorOrder(expressionBuilder.toString(), operatorArray);
                System.out.printf("Result for \"%s\" :  %s%n", input, result);
            } else {
                throw new Exception("Error ! Expression must be seperated by spaces.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double calculate(String sum) {

        char[] operatorOrder = fetchOperatorOrder();
        double result = 0.0;

        String resultStr = calculateSumByOperatorOrder(sum, operatorOrder);
        int openingBracketIdx = findOpeningBracket(resultStr);
        int closingBracketIdx = findClosingBracket(resultStr);
        resultStr = resultStr.substring(openingBracketIdx + 1, closingBracketIdx);

        result = Double.parseDouble(resultStr);

        return result;
    }

    private static int findOpeningBracket(String userInput) {
        return userInput.lastIndexOf("(");
    }

    private static int findClosingBracket(String userInput) {
        return userInput.indexOf(")");
    }

    //find left operand
    private static String findX(char[] expression, int operatorIndex) {

        StringBuilder x = new StringBuilder();

        while (operatorIndex > 0) {

            char c = expression[operatorIndex - 1];

            if (Character.isDigit(c) || c == '.') {
                x.append(c);
            } else {
                break;
            }

            operatorIndex--;
        }

        x.reverse();

        return x.toString();
    }

    //find right operand
    private static String findY(char[] expression, int operatorIndex) {
        StringBuilder y = new StringBuilder();

        for (int o = operatorIndex + 1; o < expression.length; o++) {
            if (Character.isDigit(expression[o]) || expression[o] == '.') {
                y.append(expression[o]);
            } else {
                break;
            }
        }
        return y.toString();

    }

    private static String calculateSumByOperatorOrder(String expression, char[] operatorOrder) {

        int operatorOrderIdx = 0;
        int currentOperatorIdx = 0;
        String resultExpression = expression;
        StringBuilder expressionBuilder = new StringBuilder();

        double result = 0.0;

        if (operatorOrder.length == 0) {
            return resultExpression;
        }

        if (expression.contains(String.valueOf(operatorOrder[operatorOrderIdx]))) {
            currentOperatorIdx = expression.indexOf(operatorOrder[operatorOrderIdx]);

            char[] expressionAry = expression.toCharArray();

            String a = findX(expressionAry, currentOperatorIdx);
            String b = findY(expressionAry, currentOperatorIdx);

            Double x = Double.parseDouble(a);
            Double y = Double.parseDouble(b);

            switch (operatorOrder[operatorOrderIdx]) {
                case '*':
                    result = x * y;
                    expressionBuilder.append(a).append("*").append(b);
                    break;
                case '/':
                    result = x / y;
                    expressionBuilder.append(a).append("/").append(b);
                    break;
                case '+':
                    result = x + y;
                    expressionBuilder.append(a).append("+").append(b);
                    break;
                case '-':
                    result = x - y;
                    expressionBuilder.append(a).append("-").append(b);
                    break;
            }
            resultExpression = expression.replace(expressionBuilder.toString(), String.valueOf(result));

        }

        // if result not containing the current operator , shift to next operator
        if (!resultExpression.contains(String.valueOf(operatorOrder[operatorOrderIdx]))) {
            StringBuilder operatorBuilder = new StringBuilder();
            operatorBuilder.append(operatorOrder);
            operatorBuilder.deleteCharAt(0);
            operatorOrder = operatorBuilder.toString().toCharArray();
        }

        return calculateSumByOperatorOrder(resultExpression, operatorOrder);
    }

    private static char[] fetchOperatorOrder() {
        return new char[]{'/', '*', '+', '-'};
    }

}
