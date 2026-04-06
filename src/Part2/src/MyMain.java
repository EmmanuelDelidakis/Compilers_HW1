package Part2.src;

import java.io.*;
import java_cup.runtime.*;

public class MyMain {
    public static void main(String[] args) throws Exception {
        Lexer lexer = new Lexer(new InputStreamReader(System.in));
        parser p = new parser(lexer);

        Object result = p.parse().value;

        // Output the generated Java code to stdout
        if (result != null) {
            System.out.print(result.toString());
        }
    }
}