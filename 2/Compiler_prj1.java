//Scanner to read input
import java.util.Scanner;
import java.util.ArrayList;

/* TO DO:
   ?
*/

public class Compiler_prj1 {
  
  public static void main(String[] args) {
    
    // The list of valid symobls in the grammar. Ordered to be in line with
    // - the columns of the transition table.
    char[] grammar = new char[] 
    {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o',
     'p','q','r','s','t','u','v','w','x','y','z','0','1','2','3',
     '4','5','6','7','8','9','{','}','(',')','=','"','!','+'};
    
    //49 = error. Valid states are indicated by comments and below when
    // - checked 
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
    
    //Variables used to detect input
    Scanner input = new Scanner(System.in);
    String line;
   
    //Variables used for position tracking
    int lineNum = 0;
    int indexNum = 0;
    int tokenNum = 0;
    int startTokenNum = 0;
    int errorNum = 0;
    int programNum = 0;
    
    //Variable used for various condition checks
    boolean eop = false;
    boolean validToken = false;
    boolean isString = false;
    boolean isComment = false;
    boolean isLetter = false;
    
    //Variables for token detection and token creation
    int state = 0;
    int currentCol = 1000;
    int tokLine = 0;
    int tokIndex = 0;
    String tokType = " ";
    String tokName = "placehold";
    ArrayList<Token> list = new ArrayList<Token>();
    char next = ' ';
    char commentNext = ' ';
    
    //The Lexer will continue as long as input is detected within a program
    // - Max of one program per line, any more will be ignored.
    while(input.hasNext()){
      
      //Update line specific information and get the next line of input
      line = input.nextLine();
      lineNum = lineNum + 1;
      eop = false;
     
      //Code used for each character of input
      for(int i = 0; i != line.length(); i++){

        next = line.charAt(i);
        if(eop == false){
          indexNum = indexNum + 1;
          
          //If the character is contained in a comment, it will be completely ignored
          // - unless the character will end the comment.
          if(isComment){
            if(next == '*'){
              if(line.charAt(i+1) == '/'){
                isComment = false;
                indexNum++;
                i = i+1;
              }
            }    
          }
          
          //If the character is not in a comment.
          else{  
            
            //Lowercase letters in a string are char tokens, except for in comments
            if(isString && next != '"'){
              
              //Only lowercase letters are char tokens in strings
              for(int p = 0; p < 26; p++){
                if(next == grammar[p]){
                  isLetter = true;
                }
              }
                
              if(isLetter || next == ' '){
                list.add(new Token("char", lineNum, indexNum, String.valueOf(next)));
                System.out.println("Lex: " + list.get(tokenNum).type + " with name "+String.valueOf(next)+" detected at line " + list.get(tokenNum).lineNum + ", index " +list.get(tokenNum).indexNum);
                tokenNum = tokenNum + 1;
                state = 0;
              }
    
              else if(line.length()-1 > i && next == '/'){
                if(line.charAt(i+1) == '*'){
                  isComment = true;
                  indexNum++;
                  i = i+1;
                  state = 0;
                }
              }
                
              else{
                System.out.println("Lex Error: Invalid symbol " +next+ " detected at line " +lineNum+ " index " +indexNum);
                errorNum++;
              }
             
              isLetter = false;
            }
          
            //Not a string or comment    
            else{
              
              //List of valid symbols that acts as separators to create tokens
              if(next == '$' || next == ' ' || next == '=' || next == '"' || next == '+' || next == '!'|| next == '\t' || next == '/' || next == '*'){
                
                //If there is a valid token detected
                if(validToken){
                  
                  //If the token is an Id, it has to be constructed differently since
                  // - the symbol must be recorded for future use
                  if(tokName.equals("placehold")){
                    list.add(new Token(tokType, tokLine, tokIndex));
                    System.out.println("Lex: " +list.get(tokenNum).type + " detected at line " + list.get(tokenNum).lineNum + ", index " +list.get(tokenNum).indexNum);
                  }
                  else{
                    list.add(new Token(tokType, tokLine, tokIndex, tokName));
                    System.out.println("Lex: " +list.get(tokenNum).type + " with name "+tokName+" detected at line " + list.get(tokenNum).lineNum + ", index " +list.get(tokenNum).indexNum);
                  }
              
                  //Update the tracking information
                  tokenNum = tokenNum + 1;
                  state = 0;
                  indexNum = tokIndex;
                  i = tokIndex-1;
              
                  //Reset the token information for the next valid token
                  tokIndex = 0;
                  tokLine = 0;
                  tokType = " ";
                  tokName = "placehold";
                  validToken = false;
                }
            
                //Specific rules for = (to detect assignment vs equals)
                else if(next == '='){
                //This length check is used to avoid looking at characters that don't exist
                if(line.length()-1 > i){
                  if(line.charAt(i+1) == '='){
                    list.add(new Token("equal", lineNum, indexNum+1));
                    System.out.println("Lex: " +list.get(tokenNum).type + " detected at line " + list.get(tokenNum).lineNum + ", index " +list.get(tokenNum).indexNum);
                
                    tokenNum = tokenNum + 1;
                    state = 0;
                    indexNum = indexNum+1;
                    i = i+1;
            
                    tokIndex = 0;
                    tokLine = 0;
                    tokType = " ";
                    tokName = "placehold";
                    validToken = false;
                  }
                  
                  else{
                    list.add(new Token("assignment", lineNum, indexNum));
                    System.out.println("Lex: " +list.get(tokenNum).type + " detected at line " + list.get(tokenNum).lineNum + ", index " +list.get(tokenNum).indexNum);
                
                    tokenNum = tokenNum + 1;
                    state = 0;
                
                    tokIndex = 0;
                    tokLine = 0;
                    tokType = " ";
                    tokName = "placehold";
                    validToken = false;
                  }
                }
                
                else{
                  list.add(new Token("assignment", lineNum, indexNum));
                  System.out.println("Lex: " +list.get(tokenNum).type + " detected at line " + list.get(tokenNum).lineNum + ", index " +list.get(tokenNum).indexNum);
                
                  tokenNum = tokenNum + 1;
                  state = 0;
                
                  tokIndex = 0;
                  tokLine = 0;
                  tokType = " ";
                  tokName = "placehold";
                  validToken = false;
                }
                
              }
              
                //Specific rules for quotation marks (Starting and ending strings)
                else if(next == '"'){
                list.add(new Token("quote", lineNum, indexNum));
                System.out.println("Lex: " +list.get(tokenNum).type + " detected at line " + list.get(tokenNum).lineNum + ", index " +list.get(tokenNum).indexNum);
                
                tokenNum = tokenNum+1;
                state = 0;
                
                tokIndex = 0;
                tokLine = 0;
                tokType = " ";
                tokName = "placehold";
            
                isString = !isString; 
              }
            
                //Specific rules for / (detecting comments)
                else if(next == '/'){
                  if(line.length()-1 > i){
                    if(line.charAt(i+1) == '*'){ 
                      isComment = true;
                      indexNum++;
                      i = i+1;
                      state = 0;
                    }
                    else{
                      System.out.println("Lex Error: / detected at line " +lineNum +" index "+indexNum +" without *");
                      errorNum++;
                    }
                  }
                  else{
                    System.out.println("Lex Error: / detected at line " +lineNum +" index "+indexNum +" without *");
                    errorNum++;
                  }
                }
            
                //Specific rules for +
                else if(next == '+'){
                  list.add(new Token("Plus", lineNum, indexNum));
                  System.out.println("Lex: " +list.get(tokenNum).type + " detected at line " + list.get(tokenNum).lineNum + ", index " +list.get(tokenNum).indexNum);
                
                  tokenNum = tokenNum+1;
                  state = 0;
                
                  tokIndex = 0;
                  tokLine = 0;
                  tokType = " ";
                  tokName = "placehold";
                }
              
                //Specific rules for ! (detecting inequality)
                else if(next == '!'){
                  //This check is to make sure the program does not try to find characters
                  // - that don't exist on the current line
                  if(line.length()-1 > i){
                    if(line.charAt(i+1) == '='){
                      list.add(new Token("not_equal", lineNum, indexNum));
                      System.out.println("Lex: " +list.get(tokenNum).type + " detected at line " + list.get(tokenNum).lineNum + ", index " +list.get(tokenNum).indexNum);
                
                      tokenNum = tokenNum + 1;
                      state = 0;
                      indexNum = indexNum+1;
                      i = i+1;
                
                      tokIndex = 0;
                      tokLine = 0;
                      tokType = " ";
                      tokName = "placehold";
                      validToken = false;
                    } 
              
                    else{
                      System.out.println("Lex Error: Expected = after ! on line "+ lineNum+ ", index " +indexNum);
                      errorNum++;
                    }
                  }
            
                  else{
                    System.out.println("Lex Error: Expected = after ! on line "+ lineNum+ ", index " +indexNum);
                    errorNum++;
                  }
                }
              
                //* should not be encountered here since it is only valid in a comment
                else if(next == '*'){
                  if(line.charAt(i+1) == '/'){
                    System.out.println("Lex Error: End comment symbol detected before start comment symbol");
                    errorNum++;
                    i = i+1;
                  }
                  else{
                    System.out.println("Lex Error: * detected at line " +lineNum+", index "+indexNum+" when not ending a comment");
                    errorNum++;
                  }
                }
        
                //$ ends the program, so text on the same line after it will not be detected
                else if(next == '$'){
                  list.add(new Token("EOP", lineNum, indexNum));
                  tokenNum = tokenNum+1;
                  System.out.println("Lex: $ detected at line " + lineNum + ", index " +indexNum);
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
        
              //Not separators, will not create tokens when detected
              else{
                //Check if the character is in the table of valid symbols
                for(int j = 0; j < grammar.length; j++){
                  if(next == grammar[j]){
                    currentCol = j;
                  }
                }
        
                //If the next character was not found in the table, it will be this value
                if(currentCol == 1000){
                  //Tokens will be made at errors since they separate valid text
                  if(validToken){
                    if(tokName.equals("placehold")){
                      list.add(new Token(tokType, tokLine, tokIndex));
                      System.out.println("Lex: " +list.get(tokenNum).type + " detected at line " + list.get(tokenNum).lineNum + ", index " +list.get(tokenNum).indexNum);
                    }
                    
                    else{
                      list.add(new Token(tokType, tokLine, tokIndex, tokName));
                      System.out.println("Lex: " +list.get(tokenNum).type + " with name "+tokName+" detected at line " + list.get(tokenNum).lineNum + ", index " +list.get(tokenNum).indexNum);
                    }
                  
                    tokenNum = tokenNum + 1;
                    state = 0;
                    indexNum = tokIndex;
                    i = tokIndex-1;
              
                    tokIndex = 0;
                    tokLine = 0;
                    tokType = " ";
                    tokName = "placehold";
                    validToken = false;
                  }
                
                  //After there are no more tokens to make, return the error.
                  else{
                    System.out.println("Lex Error: Invalid symbol " +next+" detected at line " +lineNum+ ", index "+indexNum);
                    errorNum++;
                  }
                }
            
                //The symbol is valid in our grammar
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
                    tokName = "i";
                  }
                  else if (state == 18){ //if
                    validToken = true;
                    tokLine = lineNum;
                    tokIndex = indexNum;
                    tokType = "If";
                    tokName = "placehold";
                  }
                  else if (state == 1){ // {
                    validToken = true;
                    tokLine = lineNum;
                    tokIndex = indexNum;
                    tokType = "L_Bracket";
                    tokName = "placehold";
                  }
                  else if (state == 2){ // }
                    validToken = true;
                    tokLine = lineNum;
                    tokIndex = indexNum;
                    tokType = "R_Bracket";
                    tokName = "placehold";
                  }
                  else if (state == 3){ // id for p
                    validToken = true;
                    tokLine = lineNum;
                    tokIndex = indexNum;
                    tokType = "Id";
                    tokName = "p";
                  }
                  else if (state == 7){ //print
                    validToken = true;
                    tokLine = lineNum;
                    tokIndex = indexNum;
                    tokType = "Print";
                    tokName = "placehold";
                  }
                  else if (state == 8){ // (
                    validToken = true;
                    tokLine = lineNum;
                    tokIndex = indexNum;
                    tokType = "L_Paren";
                    tokName = "placehold";
                  }
                  else if (state == 9){ // )
                    validToken = true;
                    tokLine = lineNum;
                    tokIndex = indexNum;
                    tokType = "R_Paren";
                    tokName = "placehold";
                  }
                  else if (state == 12){ // id for w
                    validToken = true;
                    tokLine = lineNum;
                    tokIndex = indexNum;
                    tokType = "Id";
                    tokName = "w";
                  }
                  else if (state == 16){ // while
                    validToken = true;
                    tokLine = lineNum;
                    tokIndex = indexNum;
                    tokType = "While";
                    tokName = "placehold";
                  }
                  else if (state == 22){ // id for s
                    validToken = true;
                    tokLine = lineNum;
                    tokIndex = indexNum;
                    tokType = "Id";
                    tokName = "s";
                  }
                  else if (state == 27){ // string
                    validToken = true;
                    tokLine = lineNum;
                    tokIndex = indexNum;
                    tokType = "String";
                    tokName = "placehold";
                  }
                  else if (state == 28){ // id for b
                    validToken = true;
                    tokLine = lineNum;
                    tokIndex = indexNum;
                    tokType = "Id";
                    tokName = "b";
                  }
                  else if (state == 34){ // boolean
                    validToken = true;
                    tokLine = lineNum;
                    tokIndex = indexNum;
                    tokType = "Boolean";
                    tokName = "placehold";
                  }
                  else if (state == 35){ // id for f
                    validToken = true;
                    tokLine = lineNum;
                    tokIndex = indexNum;
                    tokType = "Id";
                    tokName = "f";
                  }
                  else if (state == 39){ // false
                    validToken = true;
                    tokLine = lineNum;
                    tokIndex = indexNum;
                    tokType = "False";
                    tokName = "placehold";
                  }
                  else if (state == 40){ // id for t
                    validToken = true;
                    tokLine = lineNum;
                    tokIndex = indexNum;
                    tokType = "Id";
                    tokName = "t";
                  }
                  else if (state == 43){ // true
                    validToken = true;
                    tokLine = lineNum;
                    tokIndex = indexNum;
                    tokType = "True";
                    tokName = "placehold";
                  }
                  else if (state == 44){ // id for not keyword starters
                    validToken = true;
                    tokLine = lineNum;
                    tokIndex = indexNum;
                    tokType = "Id";
                    tokName = String.valueOf(next);
                  }
                
                  //Since digits are only single numbers in our grammar, we can create
                  // - this token as soon as we detect it
                  else if (state == 45){ // digit
                    list.add(new Token("Digit", lineNum, indexNum, Character.getNumericValue(next)));
                    System.out.println("Lex: " +list.get(tokenNum).type + " with value "+Character.getNumericValue(next)+" detected at line " + list.get(tokenNum).lineNum + ", index " +list.get(tokenNum).indexNum);
                
                    tokenNum = tokenNum+1;
                    state = 0;
                  
                    tokIndex = 0;
                    tokLine = 0;
                    tokType = " ";
                    tokName = "placehold";
                    validToken = false;
                  }
                } 

                //Reset the column once states are detected  
                currentCol = 1000;
              }
            }
          }
            
          //Create tokens at the end of the line, since it is considered whitespace
          if(i == line.length() -1){
            if(validToken){
              if(tokName.equals("placehold")){
                list.add(new Token(tokType, tokLine, tokIndex));
                System.out.println("Lex: " +list.get(tokenNum).type + " detected at line " + list.get(tokenNum).lineNum + ", index " +list.get(tokenNum).indexNum);
              }
              else{
                list.add(new Token(tokType, tokLine, tokIndex, tokName));
                System.out.println("Lex: " +list.get(tokenNum).type + " with name "+tokName+" detected at line " + list.get(tokenNum).lineNum + ", index " +list.get(tokenNum).indexNum);
              }
              
              tokenNum = tokenNum + 1;
              state = 0;
              indexNum = tokIndex;
              i = tokIndex-1;
              
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
      //Makes sure there are no unfinished strings
      if(isString){
        System.out.println("Lex Error: Ending quote not detected by end of line.");
        errorNum++;
      }
    
      //At the end of each line, reset the index number
      indexNum = 0;
      isString = false;
        
      if(eop){
        if(isString){
          System.out.println("Lex Error: Ending quote not detected by end of line.");
          errorNum++;
        }  
        programNum++;
        System.out.println(errorNum + " Lexer errors detected in program " +programNum);
        System.out.println();
          
          
        //PARSER IMPLEMENTATION
        
        if(errorNum == 0){
          Parser parse = new Parser(list, startTokenNum);
          parse.parseProgram();
          if(parse.parseError!=0){
            System.out.println(parse.parseError +" Parser Errors detected in program " + programNum);
          }
            
          if(parse.parseError == 0){
            parse.printCST();
          }
          else{
            System.out.println("Skipping CST due to Parse Error.");
          }
        }
        else{
          System.out.println("Skipping Parse due to Lex Error.");
        }
          
        errorNum = 0;
        startTokenNum = tokenNum;
      }
        
    }
    
    if(isComment){
      System.out.println("Warning: No comment termination by end of file.");
    }
    //Gives warning if there is no program ending symbol
    if(next != '$'){
      System.out.println("Warning: No program termination symbol for last program.");
    
    }
  }
}