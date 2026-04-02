import java.io.*; // Import necessary classes for input/output operations

public class Part1{

    // Checker for valid characters
    private boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }
    //TODO remember after you eat what you wanted to do 
    public void EvaluateThenParse(){
        try {
            if (line == null){
                return;
            }
        }
        
        int[] pos = new int[]{0};
        String result = parseExpression(line, pos);

        catch (IOException e) {
            System.err.println("parse error");
        }
    }

    public static void main(String[] args) throws IOException {
        // Create an instance of Part1 and pass System.in as the input stream
        Part1 evaluator = new Part1(System.in);
        evaluator.EvaluateThenParse();
    }
}