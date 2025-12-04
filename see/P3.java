    private double real, imag;

    // Constructor to initialize real and imaginary parts
    Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    // Method to add two complex numbers
    Complex add(Complex c) {
        return new Complex(this.real + c.real, this.imag + c.imag);
    }

    // Method to subtract two complex numbers
    Complex sub(Complex c) {
        return new Complex(this.real - c.real, this.imag - c.imag);
    }

    // Method to multiply two complex numbers
    Complex multiply(Complex c) {
        double realPart = this.real * c.real - this.imag * c.imag;
        double imagPart = this.real * c.imag + this.imag * c.real;
        return new Complex(realPart, imagPart);
    }

    // Method to divide two complex numbers
    Complex divide(Complex c) {
        double denominator = c.real * c.real + c.imag * c.imag;
        if(denominator == 0) {
            throw new ArithmeticException("Cannot divide by zero.");
        }
        double realPart = (this.real * c.real + this.imag * c.imag) / denominator;
        double imagPart = (this.imag * c.real - this.real * c.imag) / denominator;
        return new Complex(realPart, imagPart);
    }

    // Method to compare two complex numbers
    boolean comp(Complex c) {
        return Double.compare(this.real, c.real) == 0 && Double.compare(this.imag, c.imag) == 0;
    }

    // Override toString()
    @Override
    public String toString() {
        return this.real + " + " + this.imag + "i";
    }

    public static void main(String[] args) {
        Complex n1 = new Complex(2, 3);
        Complex n2 = new Complex (1, 1);

        Complex sum = n1.add(n2);
        Complex diff = n1.sub(n2);
        Complex product = n1.multiply(n2);
        Complex quotient = n1.divide(n2);

        System.out.println("Sum: " + sum);
        System.out.println("Difference: " + diff);
        System.out.println("Product: " + product);
        System.out.println("Quotient: " + quotient);
        System.out.println("Are n1 and n2 equal? " + n1.comp(n2));
    }
}
