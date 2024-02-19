public class ScientificCalculator extends Calculator implements ScientificOperations{
    @Override
    double add(double a, double b) {
        return (a + b);
    }

    @Override
    double subtract(double a, double b) {
        return (a - b);
    }

    @Override
    double multiply(double a, double b) {
        return (a * b);
    }

    @Override
    double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return (a / b);
    }

    @Override
    public double sin(double angle) {
        return (Math.sin(Math.toRadians(angle)));
    }

    @Override
    public double cos(double angle) {
        return (Math.cos(Math.toRadians(angle)));
    }

    @Override
    public double tan(double angle) {
        return (Math.tan(Math.toRadians(angle)));
    }

    @Override
    public double sinh(double angle) {
        return (Math.sinh(angle));
    }

    @Override
    public double cosh(double angle) {
        return (Math.cosh(angle));
    }

    @Override
    public double tanh(double angle) {
        return (Math.tanh(angle));
    }

    @Override
    public double log(double a) {
        return (Math.log10(a));
    }

    @Override
    public double power(double base, double exponent) {
        return (Math.pow(base, exponent));
    }

    @Override
    public double factorial(double a) {
        if (a == 0) {
            return 1;
        } else {
            return a * factorial(a-1);
        }
    }

    @Override
    public double modulo(double a, double b) {
        double modulus = a%b;
        return modulus;
    }

    @Override
    public double sqrt(double a) {
        return (Math.sqrt(a));
    }

    @Override
    public double cbrt(double a) {
        return (Math.cbrt(a));
    }

    @Override
    public double pi(double a) {
        return (Math.PI * a);
    }

    @Override
    public double inverse(double a) {
        return (1/a);
    }
}
