interface Compute {
    double convert(double v);
}

class GB2B implements Compute {
    // Converts Gigabytes to Bytes
    public double convert(double gb) {
        return gb * 1073741824;
    }

    // Overloaded method: Converts Kilobytes to Bytes
    public double convert(int kb) {
        return kb * 1024;
    }
}

class E2R implements Compute {
    // Converts Euros to Rupees
    public double convert(double euro) {
        return euro * 90.85;
    }

    // Overloaded method: Converts USD to INR
    public double convert(double usd, boolean isUSD) {
        return usd * 82.75;
    }
}

public class P9 {
    public static void main(String[] args) {
        Compute fc = new GB2B();
        Compute sc = new E2R();

        System.out.println("Conversions:");
        System.out.println("2 GB = " + fc.convert(2) + " Bytes.");
        System.out.println("50 Euros = " + sc.convert(50) + " Rupees.");

        // Testing overloaded methods
        GB2B gb2b = new GB2B();
        E2R e2r = new E2R();

        System.out.println("500 Kilobytes = " + gb2b.convert(500) + " Bytes.");
        System.out.println("100 USD = " + e2r.convert(100, true) + " INR.");
    }
}
