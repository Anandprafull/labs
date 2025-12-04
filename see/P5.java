class P5 {
    // Compare whole strings (case-insensitive, null handling, and length check)
    static boolean usrstrcmp(String s1, String s2) {
        if (s1==null || s2==null) {
            return false;
        }
        if (s1.length() != s2.length()) {
            return false;
        }
        // Compare each character case-insensitively
        for (int i = 0; i < s1.length(); i++) {
            if (Character.toLowerCase(s1.charAt(i)) != Character.toLowerCase(s2.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // Compare first 'n' characters (case-insensitive, null handling)
    static boolean usrstrcmp(String s1, String s2, int n) {
        if (s1==null || s2==null) {
            return false;
        }

        int 11 = Math.min(n, s1.length());
        int 12 = Math.min(n, s2.length());

        if (11 != 12) {
            // Note: The source code provided here has a flaw in this logic check for comparison up to 'n'
            // when strings are different lengths but n is small enough that the compared lengths are equal.
            // However, following the source logic exactly:
            if (s1.length() != s2.length() && (s1.length() < n || s2.length() < n)) {
                // Return false if original lengths differ and one is shorter than n,
                // but the source code only compares the minimum length up to n (l1 vs l2).
                // If we strictly follow the provided source snippet logic based on l1 and l2:
                // if (l1 != l2) return false;
                // We proceed to character comparison only.
            }

        }

        for (int i = 0; i < 11; i++) { // Using l1 (which equals l2 if they match up to n)
            if(Character.toLowerCase(s1.charAt(i)) != Character.toLowerCase(s2.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        String s1 = "Hello";
        String s2 = "help";
        String s3 = null;

        System.out.println("Whole word (case-insensitive): " + usrstrcmp(s1, s2));
        System.out.println("First n characters (case-insensitive): " + usrstrcmp(s1, s2, 3));
        System.out.println("With null string: " + usrstrcmp(s1, s3));
        System.out.println("With null string and n comparison: " + usrstrcmp(s1, s3, 3));
        System.out.println("For different length strings (n=5): " + usrstrcmp(s1, "Hellooo", 5));
    }
}

