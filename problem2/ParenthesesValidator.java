public class ParenthesesValidator {
    public static boolean isParenthesesBalanced(String str) {
        int count = 0;
        for (char ch : str.toCharArray()) {
            count += (ch == '(') ? 1 : (ch == ')') ? -1 : 0;
            if (count < 0) return false;
        }
        return count == 0;
    }

    public static void main(String[] args) {
        String str = "(())(()";
        System.out.println(isParenthesesBalanced(str));  // It should print: true
    }
}