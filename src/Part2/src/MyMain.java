package Part2.src;

import java.io.*;

public class MyMain {
    public static void main(String[] args) throws Exception {
        Lexer lexer = new Lexer(new InputStreamReader(System.in));
        parser p = new parser(lexer);

        Object result = p.parse().value;

        System.out.println(result);
    }
}