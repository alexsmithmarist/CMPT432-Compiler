//Scanner to read input
import java.util.Scanner;
import java.util.ArrayList;

public class Compiler_prj1 {
  
  public static void main(String[] args) {
    
    char[] grammar = new char[] 
    {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o',
     'p','q','r','s','t','u','v','w','x','y','z','0','1','2','3',
     '4','5','6','7','8','9','{','}','(',')','=','"','!','+'};
    
    //49 = error
      
    int[][] transition = new int[][]{
    {44,28,44,44,44,35,44,44,17,44,44,44,44,44,44,3,44,44,22,40,44,44,12,44,44,44,45,45,45,45,45,45,45,45,45,45,1,2,8,9,10,21,47,46},      //start
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49}, //L_Brace
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49}, //R_Brace
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,4,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //id for p
    {49,49,49,49,49,49,49,49,5,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},
    {49,49,49,49,49,49,49,49,49,49,49,49,49,6,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,7,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //print
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //L_Paren
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //R_Paren
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,11,49,49,49},  //Assign
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,11,49,49,49},  //Equal
    {49,49,49,49,49,49,49,13,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //id for w
    {49,49,49,49,49,49,49,49,14,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},
    {49,49,49,49,49,49,49,49,49,49,49,15,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},
    {49,49,49,49,16,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //while
    {49,49,49,49,49,18,49,49,49,49,49,49,49,19,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //id for i
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //if
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,20,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //int
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //quote
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,23,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //id for s
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,24,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},
    {49,49,49,49,49,49,49,49,25,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},
    {49,49,49,49,49,49,49,49,49,49,49,49,49,26,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},
    {49,49,49,49,49,49,27,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //string
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,29,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //id for b
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,30,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},
    {49,49,49,49,49,49,49,49,49,49,49,31,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},
    {49,49,49,49,32,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},
    {33,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},
    {49,49,49,49,49,49,49,49,49,49,49,49,49,34,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //boolean
    {36,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //id for f
    {49,49,49,49,49,49,49,49,49,49,49,37,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,38,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},
    {49,49,49,49,39,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //false
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,41,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //id for t
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,42,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},
    {49,49,49,49,43,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //true
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //id for not b,f,i,p,s,t,w
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //digit
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //+ or plus
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,48,49,49,49},
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //not equal
    {49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49},  //invalid
    };
    
      
      
    Scanner input = new Scanner(System.in);
    String line;
      
    int state = 0;
    int lineNum = 0;
    int indexNum = 0;
    int indexTrack = 0;
    int tokenNum = 0;
    int currentCol = 1000;
    boolean validToken = false;
    
    String tokType = " ";
    int tokLine = 0;
    int tokIndex = 0;
    String tokInput = " ";
    String tokName = "placehold";
    ArrayList<Token> list = new ArrayList<Token>();
    
    
    while(input.hasNext()){
      line = input.nextLine();
      lineNum = lineNum + 1;
      
      for(int i = 0; i != line.length(); i++){
        indexNum = indexNum + 1;
        char next = line.charAt(i);
          
        //Insert any symbol check here, using $ just as a test
        if(next == '$'){
          if(validToken){
              list.add(new Token(tokType, tokLine, tokIndex));
              System.out.println(list.get(tokenNum).type + " DISCOVERED AT LINE " + list.get(tokenNum).lineNum + ", INDEX " +list.get(tokenNum).indexNum);
              
              tokenNum = tokenNum + 1;
              state = 0;
              indexNum = tokIndex;
              i = tokIndex-1;
              
              line = tokInput;
              tokIndex = 0;
              tokLine = 0;
              tokType = " ";
              validToken = false;
          }
        
          else{
            System.out.println("$ DISCOVERED AT LINE " + lineNum + ", INDEX " +indexNum);
            state = 0;
          }
        }
        
        else{
          for(int j = 0; j < grammar.length; j++){
            if(next == grammar[j]){
              currentCol = j;
            }
          }
        
          if(currentCol == 1000){
            System.out.println("Error: Invalid symbol " +next+" detected at line " +lineNum+ " index "+indexNum);
          }
            
          else{
            state = transition[state][currentCol];
            //Insert full accept check here, using only int just as a test
            if(state == 20){
              validToken = true;
              tokLine = lineNum;
              tokIndex = indexNum;
              tokType = "Int";
              tokInput = line;
            }
          }
            
          currentCol = 1000;
        }
        
        if(i == line.length() -1){
          if(validToken){
            list.add(new Token(tokType, tokLine, tokIndex));
            System.out.println(list.get(tokenNum).type + " DISCOVERED AT LINE " + list.get(tokenNum).lineNum + ", INDEX " +list.get(tokenNum).indexNum);
              
            tokenNum = tokenNum + 1;
            state = 0;
            indexNum = tokIndex;
            i = tokIndex-1;
              
            line = tokInput;
            tokIndex = 0;
            tokLine = 0;
            tokType = " ";
            validToken = false;
        }
        state = 0;
      }
      }
      indexNum = 0;
    }
      
  }
}