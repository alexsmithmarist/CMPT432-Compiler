//Scanner to read input
import java.util.Scanner;

public class Compiler_prj1 {
  
   public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    
    //Simple testing of Token class 
    Token test = new Token ("L_Paren", 1, 2);
    System.out.println (test.type);
    System.out.println (test.lineNum);
    System.out.println (test.indexNum);
    
    //Simple testing of Making a token sequence and printing it
    Token[] stream = new Token[1];
    stream[0] = test;
    
    for (int i = 0; i < stream.length; i++) {
      if(stream[i].type == "L_Paren"){
        System.out.println("L_PAREN DETECTED AT " +stream[i].lineNum+ ":" +stream[i].indexNum);  
      }
    }
   }
}