import java.io.*; // Import necessary classes for input/output operations

public class Part1{

    // BufferedReader to read input from the provided InputStream
    private BufferedReader reader;

    // Constructor that takes an InputStream and initializes the BufferedReader
    public Part1(InputStream in) {
        this.reader = new BufferedReader(new InputStreamReader(in));
    }

    // Method for checking for valid characters
    private boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    // Parses an expression according to the grammar and returns the evaluated result as a string
    private String parseExpression(String line, int[] pos) {
        String term = parseTerm(line, pos);
        if (term == null) {
            return null;
        }
        return parseExpressionPrime(line, pos, term);
    }

    // This method will parse a term in the expression and return the result as a string
    private String parseTerm(String line, int[] pos) {
        String term = parseFactor(line, pos);
        if (term == null){
            return null;
        }
        return parseTermPrime(line, pos, term);
    }
    
    // This method will parse the remaining part of the expression after the first term and return the result as a string
    private String parseExpressionPrime(String line, int[] pos, String term){
        while (pos[0] < line.length() && line.charAt(pos[0]) == '/')  {
            pos[0]++; // Move past the '/' character
            String nextTerm = parseTerm(line, pos);
            if (nextTerm == null) {
                return null; // If the next term is invalid, return null
            }

            // Check if the next term ends with the current term and if it does, remove the current term from the end of the next term
            if (nextTerm.endsWith(nextTerm)){
                term = term.substring(0, term.length() - nextTerm.length());
            }
        }
        return term;
    }

    // This method will parse the remaining part of the term after the first factor and return the result as a string
    private String parseTermPrime(String line, int[] pos, String term){
        while (pos[0] + 1 < line.length() && line.charAt(pos[0]) == '*' && line.charAt(pos[0] + 1) == '*') {
            pos[0] += 2; // Move past the '**' characters
            String nextTerm = parseFactor(line, pos);
            if (nextTerm == null) {
                return null; // If the next factor is invalid, return null
            }
            term = term + nextTerm + nextTerm; // Concatenate the current term with the next factor
        }
        return term;
    }

    // This method will parse a factor in the expression and return the result as a string
    private String parseFactor(String line, int[] pos) {
        if (pos[0] >= line.length()) {
            return null;
        }

        // Check if the current character is an opening parenthesis
        if (line.charAt(pos[0]) == '(') {
            pos[0]++;
            String result = parseExpression(line, pos);
            // Check if the result is null or if we have reached the end of the line or if the current character is not a closing parenthesis
            if (result == null) {
                return null;
            }

            // Skip any whitespace characters after parsing the expression inside the parentheses
            while (pos[0] < line.length() && Character.isWhitespace(line.charAt(pos[0]))) {
                pos[0]++;
            }
            
            // Check if the current character is a closing parenthesis and move the position forward if it is
            if (pos[0] < line.length() && line.charAt(pos[0]) == ')') {
                pos[0]++; 
                return result;
            }
            return null;
        } 
        // Check if the current character is a letter (indicating a variable)
        else if (isLetter(line.charAt(pos[0]))) {
            StringBuilder var = new StringBuilder();
            // Loop to read the variable name while the current character is a letter
            while (pos[0] < line.length() && isLetter(line.charAt(pos[0]))) {
                var.append(line.charAt(pos[0]));
                pos[0]++;
            }
            return var.toString();
        }
        return null;
    }

 
    // Method to read input, parse the expression, and evaluate it 
    public void parseAndEvaluate(){
        try {
            // Read a line of input from the user
            String line = reader.readLine();

            // Check if the line is null (end of input) and return if it is
            if (line == null){
                return;
            }

            line = line.trim(); 
            
            // Check if the line is empty after trimming and return if it is
            if (line.isEmpty()) {
                return;
            }
        
            // Initialize the position array to keep track of the current position in the input string
            int[] pos = new int[]{0};
            String result = parseExpression(line, pos);

            // Skip trailing whitespace after parsing the expression
            while (pos[0] < line.length() && Character.isWhitespace(line.charAt(pos[0]))) {
                pos[0]++;
            }
            
            // Check if the result is not null and if we have reached the end of the line after parsing
            if (result != null && pos[0] == line.length()) {
                System.out.println(result);
            } 
            else {
                System.err.println("parse error");
            }
        }
        // Catch any IOException that may occur during input reading and print a parse error message
        catch (IOException e) {
            System.err.println("parse error");
        }
    }

    // Main method to run the program
    public static void main(String[] args) throws IOException {
        // Create an instance of Part1 and pass System.in as the input stream
        Part1 evaluator = new Part1(System.in);
        evaluator.parseAndEvaluate();
    }
}