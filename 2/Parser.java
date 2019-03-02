import java.util.ArrayList;


public class Parser{
  
  int i = 0;
  int parseError = 0;
  ArrayList<Token> stream = new ArrayList<Token>();
  CST tree = new CST();
    
  public Parser(){}
    
  public Parser(ArrayList<Token> list, int start){
    stream = list;
    i = start;
  }
    
  boolean parseProgram(){
    tree.addBranch("program");
    boolean retVal = false;
    parseBlock();
    retVal = match("EOP");
      
    return retVal;
  }
    
  boolean parseBlock(){
    tree.addBranch("block");
      
    boolean retVal = false;
    retVal = match("L_Bracket");
    parseStateList();
    retVal = match("R_Bracket");
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseStateList(){
    tree.addBranch("statement list");
      
    boolean retVal = false;
    if(stream.get(i).type.equals("Print") || stream.get(i).type.equals("Id") || stream.get(i).type.equals("Int") || stream.get(i).type.equals("String") || stream.get(i).type.equals("Boolean") || stream.get(i).type.equals("While") || stream.get(i).type.equals("If") || stream.get(i).type.equals("L_Bracket")){
      
      retVal = parseState();
      retVal = parseStateList();
    }
      
    else{
      /*epsilon production */
    }
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseState(){
    tree.addBranch("statement");
      
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
    
    tree.endChildren();
    return retVal;
  }
    
  boolean parsePrint(){
    tree.addBranch("print statement");
      
    boolean retVal = false;
    retVal = match("Print");
    retVal = match("L_Paren");
    retVal = parseExpr();
    retVal = match("R_Paren");
    
    tree.endChildren();
    return retVal;
  }
    
  boolean parseAssign(){
    tree.addBranch("assignment statement");
      
    boolean retVal = false;
    parseId();
    match("assignment");
    parseExpr();
    
    tree.endChildren();
    return retVal;
  }
  
  boolean parseVar(){
    tree.addBranch("varDecl");
      
    boolean retVal = false;
    parseType();
    parseId();
      
    tree.endChildren();
    return retVal;
  }
   
  boolean parseWhile(){
    tree.addBranch("while statement");
      
    boolean retVal = false;
    match("While");
    parseBooleanExpr();
    parseBlock();
      
    tree.endChildren();
    return retVal;
  }
  
  boolean parseIf(){
    tree.addBranch("if statement");
      
    boolean retVal = false;
    retVal = match("If");
    retVal = parseBooleanExpr();
    retVal = parseBlock();
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseExpr(){
    tree.addBranch("Expr");
      
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
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseIntExpr(){
    tree.addBranch("Int Expr");
      
    boolean retVal = false;
    retVal = match("Digit");
    if(stream.get(i).type.equals("Plus")){
      retVal = match("Plus");
      retVal = parseExpr();
    }
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseStringExpr(){
    tree.addBranch("String Expr");
      
    boolean retVal = false;
    retVal = match("quote");
    retVal = parseCharList();
    retVal = match("quote");
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseBooleanExpr(){
    tree.addBranch("Boolean Expr");
      
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
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseId(){
    tree.addBranch("Id");
      
    boolean retVal = false;
    retVal = match("Id");
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseCharList(){
    tree.addBranch("char list");
      
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
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseType(){
    tree.addBranch("type");
    
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
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseBoolop(){
    tree.addBranch("bool op");
      
    boolean retVal = false;
    if(stream.get(i).type.equals("equal")){
      retVal = match("equal");
    }
    else{
     retVal = match("not_equal");
    }
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseBool(){
    tree.addBranch("bool");
      
    boolean retVal = false;
    if(stream.get(i).type.equals("False")){
      retVal = match("False");
    }
    else{
      retVal = match("True");
    }
      
    tree.endChildren();
    return retVal;
  }
    
  boolean match(String expTok){
    boolean retVal = false;
    if(stream.get(i).type.equals(expTok)){
      tree.addLeaf(expTok);
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
    
  void printCST(){
    tree.printTree(tree.root, 0);
  }
    
}