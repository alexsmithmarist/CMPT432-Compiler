import java.util.ArrayList;


public class Parser{
  
  int i = 0;
  int parseError = 0;
  ArrayList<Token> stream = new ArrayList<Token>();
    
  public Parser(){}
    
  public Parser(ArrayList<Token> list, int start){
    stream = list;
    i = start;
  }
    
  boolean parseProgram(){
    boolean retVal = false;
    parseBlock();
    retVal = match("EOP");
      
    return retVal;
  }
    
  boolean parseBlock(){
    boolean retVal = false;
    retVal = match("L_Bracket");
    parseStateList();
    retVal = match("R_Bracket");
    return retVal;
  }
    
  boolean parseStateList(){
    boolean retVal = false;
    if(stream.get(i).type.equals("Print") || stream.get(i).type.equals("Id") || stream.get(i).type.equals("Int") || stream.get(i).type.equals("String") || stream.get(i).type.equals("Boolean") || stream.get(i).type.equals("While") || stream.get(i).type.equals("If") || stream.get(i).type.equals("L_Bracket")){
      
      retVal = parseState();
      retVal = parseStateList();
    }
      
    else{
      /*epsilon production */
    }
      
    return retVal;
  }
    
  boolean parseState(){
    boolean retVal = false;
    if(stream.get(i).type.equals("Print")){
      retVal = parsePrint();
    }
    else if(stream.get(i).type.equals("Id")){
      retVal = parseAssign();
    }
    else if(stream.get(i).type.equals("Int") || stream.get(i).type.equals("String") || stream.get(i).type.equals("Boolean")){
      retVal = parseVar();
    }
    else if(stream.get(i).type.equals("While")){
      retVal = parseWhile();
    }
    else if(stream.get(i).type.equals("If")){
      retVal = parseIf();
    }
    else{
      retVal = parseBlock();
    }
    
    return retVal;
  }
    
  boolean parsePrint(){
    boolean retVal = false;
    retVal = match("Print");
    retVal = match("L_Paren");
    retVal = parseExpr();
    retVal = match("R_Paren");
    return retVal;
  }
    
  boolean parseAssign(){
    boolean retVal = false;
    parseId();
    match("assignment");
    parseExpr();
    return retVal;
  }
  
  boolean parseVar(){
    boolean retVal = false;
    parseType();
    parseId();
    return retVal;
  }
   
  boolean parseWhile(){
    boolean retVal = false;
    match("While");
    parseBooleanExpr();
    parseBlock();
    return retVal;
  }
  
  boolean parseIf(){
    boolean retVal = false;
    retVal = match("If");
    retVal = parseBooleanExpr();
    retVal = parseBlock();
    return retVal;
  }
    
  boolean parseExpr(){
    boolean retVal = false;
    if(stream.get(i).type.equals("Digit")){
      retVal = parseIntExpr();
    }
    else if(stream.get(i).type.equals("quote")){
      retVal = parseStringExpr();
    }
    else if(stream.get(i).type.equals("L_Paren")){
      retVal = parseBooleanExpr();
    }
    else{
      retVal = parseId();
    }
    return retVal;
  }
    
  boolean parseIntExpr(){
    boolean retVal = false;
    retVal = match("Digit");
    if(stream.get(i).type.equals("Plus")){
      retVal = match("Plus");
      retVal = parseExpr();
    }
    return retVal;
  }
    
  boolean parseStringExpr(){
    boolean retVal = false;
    retVal = match("quote");
    retVal = parseCharList();
    retVal = match("quote");
    return retVal;
  }
    
  boolean parseBooleanExpr(){
    boolean retVal = false;
    if(stream.get(i).type.equals("L_Paren")){
      retVal = match("L_Paren");
      retVal = parseExpr();
      retVal = parseBoolop();
      retVal = parseExpr();
      retVal = match("R_Paren");
    }
    else{
      retVal = parseBool();
    }
    return retVal;
  }
    
  boolean parseId(){
    boolean retVal = false;
    retVal = match("Id");
    return retVal;
  }
    
  boolean parseCharList(){
    boolean retVal = false;
    if(stream.get(i).type.equals("char")){
        
      if(!stream.get(i).name.equals(" ")){
        retVal = match("char");
        retVal = parseCharList();
      }
      else{
        retVal = match("char");
        /* INDICATE SPACE IN CST HERE */
        retVal = parseCharList();
      }
    
    }
    else{
     /*epsilon production*/
    }
    return retVal;
  }
    
  boolean parseType(){
    boolean retVal = false;
    if(stream.get(i).type.equals("Int")){
      retVal = match("Int");
    }
    else if(stream.get(i).type.equals("String")){
      retVal = match("String");
    }
    else{
      retVal = match("Boolean");
    }
    return retVal;
  }
    
  boolean parseBoolop(){
    boolean retVal = false;
    if(stream.get(i).type.equals("equal")){
      retVal = match("equal");
    }
    else{
     retVal = match("not_equal");
    }
    return retVal;
  }
    
  boolean parseBool(){
    boolean retVal = false;
    if(stream.get(i).type.equals("False")){
      retVal = match("False");
    }
    else{
      retVal = match("True");
    }
    return retVal;
  }
    
  boolean match(String expTok){
    boolean retVal = false;
    if(stream.get(i).type.equals(expTok)){
      retVal = true;
      System.out.println(expTok);
      /* CST STUFF GOES HERE */
    }
      
    if(retVal == false){
      System.out.println("Parse Error: Found " + stream.get(i).type + " on line " + stream.get(i).lineNum + ", index " + stream.get(i).indexNum + " when expecting " + expTok);
        
      parseError++;
    }
      
    i++;
    return retVal;
  }
    
}