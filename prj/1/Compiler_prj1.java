//Scanner to read input
import java.util.Scanner;
import java.util.ArrayList;

public class Compiler_prj1 {
  
  public static void main(String[] args) {
    
    Scanner input = new Scanner(System.in);
    String line;
    int lineNum = 0;
    int indexNum = 0;
    int tokenNum = 0;
    ArrayList<Token> list = new ArrayList<Token>();
    
    //Simple testing of Making a token sequence and printing it
    
    while(input.hasNext()){
      line = input.nextLine();
      lineNum = lineNum + 1;
      for(int i = 0; i != line.length(); i++){
        indexNum = indexNum + 1;
        
        //INSERT MORE EFFICIENT CHECKING CODE HERE!!!!
        
        if(line.charAt(i) == '('){
          list.add(new Token("L_Paren", lineNum, indexNum));
          System.out.println(list.get(tokenNum).type + " DISCOVERED AT LINE " + list.get(tokenNum).lineNum + ", INDEX " +list.get(tokenNum).indexNum);
          tokenNum = tokenNum + 1;
        }
        
        //REPLACE CHECKING CODE ABOVE!!!!
          
      }
      indexNum = 0;
    }
  }
}