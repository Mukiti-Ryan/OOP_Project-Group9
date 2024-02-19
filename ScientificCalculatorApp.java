import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ScientificCalculatorApp extends JFrame {
    private JTextField displayField;
    private List<String> history;

    public ScientificCalculatorApp() {
        super("Scientific Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        displayField = new JTextField();
        displayField.setEditable(false);
        add(displayField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(7,5));
        add(buttonPanel, BorderLayout.CENTER);

        String[] operations = {
                "7", "8", "9", "/", "^",
                "4", "5", "6", "*", "√",
                "1", "2", "3", "-", "log",
                "0", ".", "C", "+", "="
        };

        for (String operation : operations) {
            JButton button = new JButton(operation);
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }

        String[] scientificOperations = {
                "sin", "cos", "tan", "%", "!",
                "sinh", "cosh", "tanh", "(", ")",
                "3√", "1/x", "π"
        };

        for (String operation : scientificOperations) {
            JButton button = new JButton(operation);
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }

        history = new ArrayList<>();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            switch (command) {
                case "=":
                    try {
                        String expression = displayField.getText();
                        ScientificCalculator calculator = new ScientificCalculator();
                        double result = evaluateExpression(expression);
                        displayField.setText(Double.toString(result));
                        history.add(expression + "=" +result);
                    } catch (Exception ex) {
                        displayField.setText("Error: " + ex.getMessage());
                    }
                    break;
                case "C":
                    displayField.setText("");
                    break;
                default:
                    displayField.setText(displayField.getText() + command);
                    break;
            }
        }

        private List<String> infixToPostfix(String[] infixExpression) {
            List<String> postfixExpression = new ArrayList<>();
            Stack<String> operatorStack = new Stack<>();

            for (String token : infixExpression) {
                if (token.matches("-?\\d+(\\.\\d+)?")) {
                    postfixExpression.add(token);
                } else if (isFunction(token)) {
                    operatorStack.push(token);
                } else if (token.equals("(")) {
                    operatorStack.push(token);
                } else if (token.equals(")")) {
                    while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                        postfixExpression.add(operatorStack.pop());
                    }
                    operatorStack.pop();
                } else {
                    while (!operatorStack.isEmpty() && precedence(token) <= precedence(operatorStack.peek())) {
                        postfixExpression.add(operatorStack.pop());
                    }
                    operatorStack.push(token);
                }
            }

            while (!operatorStack.isEmpty()) {
                postfixExpression.add(operatorStack.pop());
            }
            return postfixExpression;
        }

        private double evaluateExpression(String expression) {
            String[] tokens = expression.split("(?=[-+*/^!%()])|(?<=[-+*/^!%()])");

            ScientificCalculator calculator = new ScientificCalculator();
            List<String> postfixExpression = infixToPostfix(tokens);
            Stack<Double> stack = new Stack<>();

            for (String token : postfixExpression) {
                if (isFunction(token)) {
                    if (token.equals("sin") || token.equals("cos") || token.equals("tan") || token.equals("sinh") || token.equals("cosh") || token.equals("tanh") || token.equals("log") || token.equals("!") || token.equals("π")) {
                        double argument = stack.pop();
                        switch (token) {
                            case "sin":
                                stack.push(calculator.sin(argument));
                                break;
                            case "cos":
                                stack.push(calculator.cos(argument));
                                break;
                            case "tan":
                                stack.push(calculator.tan(argument));
                                break;
                            case "sinh":
                                stack.push(calculator.sinh(argument));
                                break;
                            case "cosh":
                                stack.push(calculator.cosh(argument));
                                break;
                            case "tanh":
                                stack.push(calculator.tanh(argument));
                                break;
                            case "log":
                                stack.push(calculator.log(argument));
                                break;
                            case "!":
                                stack.push(calculator.factorial((int) argument));
                                break;
                            case "π":
                                stack.push(calculator.pi(argument));
                                break;
                        }
                    }
                } else if (token.matches("-?\\d+(\\.\\d+)?")) {
                    stack.push(Double.parseDouble(token));
                } else {
                    switch (token) {
                        case "+":
                            stack.push(calculator.add(stack.pop(), stack.pop()));
                            break;
                        case "-":
                            double b = stack.pop();
                            double a = stack.pop();
                            stack.push(calculator.subtract(a, b));
                            break;
                        case "*":
                            stack.push(calculator.multiply(stack.pop(), stack.pop()));
                            break;
                        case "/":
                            double divisor = stack.pop();
                            double dividend = stack.pop();
                            stack.push(calculator.divide(dividend, divisor));
                            break;
                        case "^":
                            double exponent = stack.pop();
                            double base = stack.pop();
                            stack.push(calculator.power(base, exponent));;
                            break;
                        case "%":
                            double mod = stack.pop();
                            double num = stack.pop();
                            stack.push(calculator.modulo(num, mod));
                            break;
                        case "√":
                            stack.push(calculator.sqrt(stack.pop()));
                            break;
                        case "3√":
                            stack.push(calculator.cbrt(stack.pop()));
                            break;
                        case "1/x":
                            stack.push(calculator.inverse(stack.pop()));
                            break;
                    }
                }
            }

//            if (stack.size() != 1) {
//                throw new IllegalArgumentException("Invalid expression");
//            }
            return stack.pop();
        }

        private boolean isFunction(String token) {
            return (token.equals("sin") || token.equals("cos") || token.equals("tan") || token.equals("sinh") || token.equals("cosh") || token.equals("tanh") || token.equals("log") || token.equals("!") || token.equals("π"));
        }

        private int precedence(String operator) {
            switch (operator) {
                case "+", "-" -> {
                    return 1;
                }
                case "*", "/", "%" -> {
                    return 2;
                }
                case "^", "sin", "cos", "tan", "sinh", "cosh", "tanh", "log", "!", "√", "3√", "1/x" -> {
                    return 3;
                }
                default -> {
                    return 0;
                }
            }
        }
    }
}
