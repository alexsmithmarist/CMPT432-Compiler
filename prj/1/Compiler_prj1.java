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
    boolean eop = false;
    int lineNum = 0;
    int indexNum = 0;
    int indexTrack = 0;
    int tokenNum = 0;
    int currentCol = 1000;
    boolean validToken = false;
    boolean isString = false;
    
    String tokType = " ";
    int tokLine = 0;
    int tokIndex = 0;
    //String tokInput = " ";
    String tokName = "placehold";
    ArrayList<Token> list = new ArrayList<Token>();
    
    
    while(input.hasNext()){
      line = input.nextLine();
      lineNum = lineNum + 1;
      eop = false;
      
      for(int i = 0; i != line.length(); i++){
        if(eop == false){
          indexNum = indexNum + 1;
          char next = line.charAt(i);
          
          if(isString && next != '"'){
            list.add(new Token("char", lineNum, indexNum, String.valueOf(next)));
            System.out.println(list.get(tokenNum).type + " WITH NAME "+String.valueOf(next)+" DISCOVERED AT LINE " + list.get(tokenNum).lineNum + ", INDEX " +list.get(tokenNum).indexNum);
            tokenNum = tokenNum + 1;
            state = 0;
          }
          
            
          //INDENT THIS CODE PROPERLY !!!! !!!! CURRENTLY NOT INDENTED PROPERLY!!!!
             //INDENT THIS CODE PROPERLY !!!! !!!! CURRENTLY NOT INDENTED PROPERLY!!!!
             //INDENT THIS CODE PROPERLY !!!! !!!! CURRENTLY NOT INDENTED PROPERLY!!!!
             //INDENT THIS CODE PROPERLY !!!! !!!! CURRENTLY NOT INDENTED PROPERLY!!!!
             //INDENT THIS CODE PROPERLY !!!! !!!! CURRENTLY NOT INDENTED PROPERLY!!!!
             //INDENT THIS CODE PROPERLY !!!! !!!! CURRENTLY NOT INDENTED PROPERLY!!!!
            
            
          else{
          //Insert any symbol check here
          if(next == '$' || next == ' ' || next == '=' || next == '"' || next == '+' || next == '!'){
            if(validToken){
                if(tokName.equals("placehold")){
                  list.add(new Token(tokType, tokLine, tokIndex));
                  System.out.println(list.get(tokenNum).type + " DISCOVERED AT LINE " + list.get(tokenNum).lineNum + ", INDEX " +list.get(tokenNum).indexNum);
                }
                else{
                  list.add(new Token(tokType, tokLine, tokIndex, tokName));
                  System.out.println(list.get(tokenNum).type + " WITH NAME "+tokName+" DISCOVERED AT LINE " + list.get(tokenNum).lineNum + ", INDEX " +list.get(tokenNum).indexNum);
                }
              
                tokenNum = tokenNum + 1;
                state = 0;
                indexNum = tokIndex;
                i = tokIndex-1;
              
                //line = tokInput;
                tokIndex = 0;
                tokLine = 0;
                tokType = " ";
                tokName = "placehold";
                validToken = false;
            }
            
            else if(next == '='){
              if(line.charAt(i+1) == '='){
                list.add(new Token("equal", lineNum, indexNum+1));
                System.out.println(list.get(tokenNum).type + " DISCOVERED AT LINE " + list.get(tokenNum).lineNum + ", INDEX " +list.get(tokenNum).indexNum);
                
                tokenNum = tokenNum + 1;
                state = 0;
                indexNum = indexNum+1;
                i = i+1;
                
                //line = tokInput;
                tokIndex = 0;
                tokLine = 0;
                tokType = " ";
                tokName = "placehold";
                validToken = false;
              }
              
              else{
                list.add(new Token("assignment", lineNum, indexNum));
                System.out.println(list.get(tokenNum).type + " DISCOVERED AT LINE " + list.get(tokenNum).lineNum + ", INDEX " +list.get(tokenNum).indexNum);
                
                tokenNum = tokenNum + 1;
                state = 0;
                
                //line = tokInput;
                tokIndex = 0;
                tokLine = 0;
                tokType = " ";
                tokName = "placehold";
                validToken = false;
              }
            }
              
            else if(next == ' ' && isString == true){
              list.add(new Token("char", lineNum, indexNum, String.valueOf(next)));
              System.out.println(list.get(tokenNum).type + " WITH NAME "+String.valueOf(next)+" DISCOVERED AT LINE " + list.get(tokenNum).lineNum + ", INDEX " +list.get(tokenNum).indexNum);
              tokenNum = tokenNum + 1;
              state = 0;
            }
              
            else if(next == '"'){
              list.add(new Token("quote", lineNum, indexNum));
              System.out.println(list.get(tokenNum).type + " DISCOVERED AT LINE " + list.get(tokenNum).lineNum + ", INDEX " +list.get(tokenNum).indexNum);
                
              tokenNum = tokenNum+1;
              state = 0;
                
              tokIndex = 0;
              tokLine = 0;
              tokType = " ";
              tokName = "placehold";
            
              if(isString == false){
                isString = true;
              }
              else{
                isString = false;
              }
                
            }
            
            else if(next == '+'){
              list.add(new Token("Plus", lineNum, indexNum));
              System.out.println(list.get(tokenNum).type + " DISCOVERED AT LINE " + list.get(tokenNum).lineNum + ", INDEX " +list.get(tokenNum).indexNum);
                
              tokenNum = tokenNum+1;
              state = 0;
                
              tokIndex = 0;
              tokLine = 0;
              tokType = " ";
              tokName = "placehold";
            }
              
            else if(next == '!'){
              if(line.charAt(i+1) == '='){
                list.add(new Token("not_equal", lineNum, indexNum));
                System.out.println(list.get(tokenNum).type + " DISCOVERED AT LINE " + list.get(tokenNum).lineNum + ", INDEX " +list.get(tokenNum).indexNum);
                
                tokenNum = tokenNum + 1;
                state = 0;
                
                //line = tokInput;
                tokIndex = 0;
                tokLine = 0;
                i = i+1;
                tokType = " ";
                tokName = "placehold";
                validToken = false;
              }
              
              else{
                System.out.println("Error: Expected = after ! on line "+ lineNum+ ", index " +indexNum);
              }
            }
        
            else if(next == '$'){
              System.out.println("$ DISCOVERED AT LINE " + lineNum + ", INDEX " +indexNum);
              eop = true;
              state = 0;
              
              tokIndex = 0;
              tokLine = 0;
              tokType = " ";
              tokName = "placehold";
              validToken = false;
              isString = false;
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
              
               //Start of list of accepting states
                
               if(state == 20){ //int
                validToken = true;
                tokLine = lineNum;
                tokIndex = indexNum;
                tokType = "Int";
                tokName = "placehold";
                }
              else if (state == 17){ //id for i
                validToken = true;
                tokLine = lineNum;
                tokIndex = indexNum;
                tokType = "Id";
                //tokInput = line;
                tokName = "i";
              }
              else if (state == 18){ //if
                validToken = true;
                tokLine = lineNum;
                tokIndex = indexNum;
                tokType = "If";
                //tokInput = line;
                tokName = "placehold";
              }
              else if (state == 1){ // {
                validToken = true;
                tokLine = lineNum;
                tokIndex = indexNum;
                tokType = "L_Bracket";
                //tokInput = line;
                tokName = "placehold";
              }
              else if (state == 2){ // }
                validToken = true;
                tokLine = lineNum;
                tokIndex = indexNum;
                tokType = "R_Bracket";
                //tokInput = line;
                tokName = "placehold";
              }
              else if (state == 3){ // id for p
                validToken = true;
                tokLine = lineNum;
                tokIndex = indexNum;
                tokType = "Id";
                //tokInput = line;
                tokName = "p";
              }
              else if (state == 7){ //print
                validToken = true;
                tokLine = lineNum;
                tokIndex = indexNum;
                tokType = "print";
                //tokInput = line;
                tokName = "placehold";
              }
              else if (state == 8){ // (
                validToken = true;
                tokLine = lineNum;
                tokIndex = indexNum;
                tokType = "L_Paren";
                //tokInput = line;
                tokName = "placehold";
              }
              else if (state == 9){ // )
                validToken = true;
                tokLine = lineNum;
                tokIndex = indexNum;
                tokType = "R_Paren";
                //tokInput = line;
                tokName = "placehold";
              }
              else if (state == 12){ // id for w
                validToken = true;
                tokLine = lineNum;
                tokIndex = indexNum;
                tokType = "Id";
                //tokInput = line;
                tokName = "w";
              }
              else if (state == 16){ // while
                validToken = true;
                tokLine = lineNum;
                tokIndex = indexNum;
                tokType = "While";
                //tokInput = line;
                tokName = "placehold";
              }
              else if (state == 22){ // id for s
                validToken = true;
                tokLine = lineNum;
                tokIndex = indexNum;
                tokType = "Id";
                //tokInput = line;
                tokName = "s";
              }
              else if (state == 27){ // string
                validToken = true;
                tokLine = lineNum;
                tokIndex = indexNum;
                tokType = "String";
                //tokInput = line;
                tokName = "placehold";
              }
              else if (state == 28){ // id for b
                validToken = true;
                tokLine = lineNum;
                tokIndex = indexNum;
                tokType = "Id";
                //tokInput = line;
                tokName = "b";
              }
              else if (state == 34){ // boolean
                validToken = true;
                tokLine = lineNum;
                tokIndex = indexNum;
                tokType = "Boolean";
                //tokInput = line;
                tokName = "placehold";
              }
              else if (state == 35){ // id for f
                validToken = true;
                tokLine = lineNum;
                tokIndex = indexNum;
                tokType = "Id";
                //tokInput = line;
                tokName = "f";
              }
              else if (state == 39){ // false
                validToken = true;
                tokLine = lineNum;
                tokIndex = indexNum;
                tokType = "False";
                //tokInput = line;
                tokName = "placehold";
              }
              else if (state == 40){ // id for t
                validToken = true;
                tokLine = lineNum;
                tokIndex = indexNum;
                tokType = "Id";
                //tokInput = line;
                tokName = "t";
              }
              else if (state == 43){ // true
                validToken = true;
                tokLine = lineNum;
                tokIndex = indexNum;
                tokType = "True";
                //tokInput = line;
                tokName = "placehold";
              }
              else if (state == 44){ // id for not keyword starters
                validToken = true;
                tokLine = lineNum;
                tokIndex = indexNum;
                tokType = "Id";
                //tokInput = line;
                tokName = String.valueOf(next);
              }
              else if (state == 45){ // digit
                list.add(new Token("Digit", lineNum, indexNum, Character.getNumericValue(next)));
                System.out.println(list.get(tokenNum).type + " WITH VALUE "+Character.getNumericValue(next)+" DISCOVERED AT LINE " + list.get(tokenNum).lineNum + ", INDEX " +list.get(tokenNum).indexNum);
                
                tokenNum = tokenNum+1;
                state = 0;
                  
                tokIndex = 0;
                tokLine = 0;
                tokType = " ";
                tokName = "placehold";
                validToken = false;
              }
            } 
            
                currentCol = 1000;
          }
          }
        
          if(i == line.length() -1){
            if(validToken){
              if(tokName.equals("placehold")){
                  list.add(new Token(tokType, tokLine, tokIndex));
                  System.out.println(list.get(tokenNum).type + " DISCOVERED AT LINE " + list.get(tokenNum).lineNum + ", INDEX " +list.get(tokenNum).indexNum);
              }
              else{
                list.add(new Token(tokType, tokLine, tokIndex, tokName));
                System.out.println(list.get(tokenNum).type + " WITH NAME "+tokName+" DISCOVERED AT LINE " + list.get(tokenNum).lineNum + ", INDEX " +list.get(tokenNum).indexNum);
              }
              
              tokenNum = tokenNum + 1;
              state = 0;
              indexNum = tokIndex;
              i = tokIndex-1;
              
              //line = tokInput;
              tokIndex = 0;
              tokLine = 0;
              tokType = " ";
              tokName = "placehold";
              validToken = false;
          }
          state = 0;
        }
        }
      }
        indexNum = 0;
    }
      
  }
}