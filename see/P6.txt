abstract class Bank {
    String name;
    int accountNumber;
    double balance;
    String accountType;

    // Constructor
    Bank(String name, int accountNumber, double balance, String accountType) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
    }

    // Abstract method to calculate interest
    abstract double calcint();

    // Overridden toString method
    @Override
    public String toString() {
        return String.format("\nName: %s\nAccount Number: %d\nBalance: %.2f\nAccount Type: %s\nInterest Earned: %.2f",
                name, accountNumber, balance, accountType, calcint());
    }
}

class CityBank extends Bank {
    CityBank(String name, int accountNumber, double balance, String accountType) {
        super(name, accountNumber, balance, accountType);
    }
    @Override
    double calcint() {
        if (accountType.equalsIgnoreCase("Savings")) {
            return balance * 0.6;
        } else if (accountType.equalsIgnoreCase("Current")) {
            return balance * 0.4;
        }
        return 0;
    }
}

class SBIBank extends Bank {
    SBIBank(String name, int accountNumber, double balance, String accountType) {
        super(name, accountNumber, balance, accountType);
    }
    @Override
    double calcint() {
        if (accountType.equalsIgnoreCase("Savings")) {
            return balance * 0.7;
        } else if (accountType.equalsIgnoreCase("Current")) {
            return balance * 0.5;
        }
        return 0;
    }
}

class CanaraBank extends Bank {
    CanaraBank(String name, int accountNumber, double balance, String accountType) {
        super(name, accountNumber, balance, accountType);
    }
    @Override
    double calcint() {
        if(accountType.equalsIgnoreCase("Savings")) {
            return balance * 0.65;
        } else if(accountType.equalsIgnoreCase("Current")) {
            return balance * 0.45;
        }
        return 0;
    }
}

public class P6 {
    public static void main(String[] args) {
        Bank[] bankAccounts = {
            new CityBank("John", 101, 50000, "Savings"),
            new SBIBank("Jane", 102, 75000, "Current"),
            new CanaraBank("Mike", 103, 60000, "Savings"),
            new CityBank("Sara", 104, 80000, "Current"),
            new SBIBank("David", 105, 95000, "Savings")
        };

        for(Bank account: bankAccounts) {
            System.out.println(account);
        }
    }
}
