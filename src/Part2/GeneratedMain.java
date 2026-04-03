public class Main {
    public static boolean isPrefix(String a, String b) {
        return b.startsWith(a);
    }
    public static boolean isSuffix(String a, String b) {
        return b.endsWith(a);
    }
    public static String check(String a, String b) {
        return (isPrefix(a, b) ? "prefix-match" : (isSuffix(a, b) ? "suffix-match" : "no-match"));
    }
    public static void main(String[] args) {
        System.out.println(check("he", "hello"));
        System.out.println(check("lo", "hello"));
        System.out.println(check("xy", "hello"));
    }
}
