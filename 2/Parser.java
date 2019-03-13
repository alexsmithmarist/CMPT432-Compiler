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
    System.out.println("parse: Program");
    tree.addBranch("program");
    boolean retVal = false;
    parseBlock();
    retVal = match("EOP");
    System.out.println(" ");
      
    return retVal;
  }
    
  boolean parseBlock(){
    System.out.println("parse: Block");
    tree.addBranch("block");
      
    boolean retVal = false;
    retVal = match("L_Bracket");
    parseStateList();
    retVal = match("R_Bracket");
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseStateList(){
    System.out.println("parse: Statement List");
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
    System.out.println("parse: Statement");
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
    System.out.println("parse: Print Statement");
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
    System.out.println("parse: Assignment Statement");
    tree.addBranch("assignment statement");
      
    boolean retVal = false;
    parseId();
    match("assignment");
    parseExpr();
    
    tree.endChildren();
    return retVal;
  }
  
  boolean parseVar(){
    System.out.println("parse: VarDecl");
    tree.addBranch("varDecl");
      
    boolean retVal = false;
    parseType();
    parseId();
      
    tree.endChildren();
    return retVal;
  }
   
  boolean parseWhile(){
    System.out.println("parse: While Statement");
    tree.addBranch("while statement");
      
    boolean retVal = false;
    match("While");
    parseBooleanExpr();
    parseBlock();
      
    tree.endChildren();
    return retVal;
  }
  
  boolean parseIf(){
    System.out.println("parse: If Statement");
    tree.addBranch("if statement");
      
    boolean retVal = false;
    retVal = match("If");
    retVal = parseBooleanExpr();
    retVal = parseBlock();
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseExpr(){
    System.out.println("parse: Expression");
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
    System.out.println("parse: Int Expression");
    tree.addBranch("Int Expr");
      
    boolean retVal = false;
    System.out.println("parse: Digit");
    retVal = match("Digit");
    tree.endChildren();
    if(stream.get(i).type.equals("Plus")){
      System.out.println("parse: IntOp");
      retVal = match("Plus");
      retVal = parseExpr();
    }
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseStringExpr(){
    System.out.println("parse: String Expression");
    tree.addBranch("String Expr");
      
    boolean retVal = false;
    retVal = match("quote");
    retVal = parseCharList();
    retVal = match("quote");
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseBooleanExpr(){
    System.out.println("parse: Boolean Expression");
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
    System.out.println("parse: Id");
    tree.addBranch("Id");
      
    boolean retVal = false;
    retVal = match("Id");
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseCharList(){
    System.out.println("parse: Char List");
    tree.addBranch("char list");
      
    boolean retVal = false;
    if(stream.get(i).type.equals("char")){
      //tree.addBranch("char list");
        
      if(!stream.get(i).name.equals(" ")){
        System.out.println("parse: Char");
        tree.addBranch("char");
        //tree.addBranch("char list");
        
        retVal = match("char");
        tree.endChildren();
        retVal = parseCharList();
      }
      else if(stream.get(i).name.equals(" ")){
        System.out.println("parse: Space");
        tree.addBranch("space");
        //tree.addBranch("char list");

        retVal = match("char");
        tree.endChildren();
        retVal = parseCharList();
      }
    }
    else{
      /*epsilon*/
    }
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseType(){
    System.out.println("parse: Type");
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
    System.out.println("parse: Bool Op");
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
    System.out.println("parse: Bool");
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
    if(parseError == 0){
      if(stream.get(i).type.equals(expTok)){
        if(expTok.equals("Id")){
          tree.addLeaf(stream.get(i).name);
        }
        else if(expTok.equals("Digit")){
          tree.addBranch(expTok);
          tree.addLeaf(Integer.toString(stream.get(i).value));
        }
        else if(expTok.equals("char")){
          tree.addLeaf(stream.get(i).name);
        }
        else{
          tree.addLeaf(expTok);
        }
        retVal = true;
      }
      
      
      if(retVal == false){
        System.out.println("Parse Error: Found " + stream.get(i).type + " on line " + stream.get(i).lineNum + ", index " + stream.get(i).indexNum + " when expecting " + expTok);
        
        parseError++;
      }
      //i++;
    }
    
    if(i != stream.size()-1){
      i++;
    }
    
    return retVal;
  }
    
  void printCST(){
    tree.printTree(tree.root, 0);
  }
    
}