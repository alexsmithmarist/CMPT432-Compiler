import java.util.ArrayList;


public class Parser{
  
  //Initialize needed variables
  int i = 0;
  int parseError = 0;
  ArrayList<Token> stream = new ArrayList<Token>();
  CST tree = new CST();
  
  //Default Constructor
  public Parser(){}
  
  //Constructor used to pass info from lexed program to parser
  public Parser(ArrayList<Token> list, int start){
    stream = list;
    i = start;
  }

  //Parsing of each 'sentence' is in its own method
  boolean parseProgram(){
    if(parseError == 0){
      System.out.println("parse: Program");
    }
    tree.addBranch("program");
    boolean retVal = false;
    parseBlock();
    retVal = match("EOP");
    System.out.println(" ");
      
    return retVal;
  }
    
  boolean parseBlock(){
    if(parseError == 0){
      System.out.println("parse: Block");
    }
    tree.addBranch("block");
      
    boolean retVal = false;
    retVal = match("L_Bracket");
    parseStateList();
    retVal = match("R_Bracket");
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseStateList(){
    if(parseError == 0){
      System.out.println("parse: Statement List");
    }
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
    if(parseError == 0){
      System.out.println("parse: Statement");
    }
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
    if(parseError == 0){
      System.out.println("parse: Print Statement");
    }
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
    if(parseError == 0){
      System.out.println("parse: Assignment Statement");
    }
    tree.addBranch("assignment statement");
      
    boolean retVal = false;
    parseId();
    match("assignment");
    parseExpr();
    
    tree.endChildren();
    return retVal;
  }
  
  boolean parseVar(){
    if(parseError == 0){
      System.out.println("parse: VarDecl");
    }
    tree.addBranch("varDecl");
      
    boolean retVal = false;
    parseType();
    parseId();
      
    tree.endChildren();
    return retVal;
  }
   
  boolean parseWhile(){
    if(parseError == 0){
      System.out.println("parse: While Statement");
    }
    tree.addBranch("while statement");
      
    boolean retVal = false;
    match("While");
    parseBooleanExpr();
    parseBlock();
      
    tree.endChildren();
    return retVal;
  }
  
  boolean parseIf(){
    if(parseError == 0){
      System.out.println("parse: If Statement");
    }
    tree.addBranch("if statement");
      
    boolean retVal = false;
    retVal = match("If");
    retVal = parseBooleanExpr();
    retVal = parseBlock();
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseExpr(){
    if(parseError == 0){
      System.out.println("parse: Expression");
    }
    tree.addBranch("Expr");
      
    boolean retVal = false;
    if(stream.get(i).type.equals("Digit")){
      retVal = parseIntExpr();
    }
    else if(stream.get(i).type.equals("quote")){
      retVal = parseStringExpr();
    }
    else if(stream.get(i).type.equals("L_Paren") || stream.get(i).type.equals("True") || stream.get(i).type.equals("False") ){
      retVal = parseBooleanExpr();
    }
    else{
      retVal = parseId();
    }
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseIntExpr(){
    if(parseError == 0){
      System.out.println("parse: Int Expression");
    }
    tree.addBranch("Int Expr");
      
    boolean retVal = false;
    if(parseError == 0){
      System.out.println("parse: Digit");
    }
    retVal = match("Digit");
    tree.endChildren();
    if(stream.get(i).type.equals("Plus")){
      if(parseError == 0){
        System.out.println("parse: IntOp");
      }
      retVal = match("Plus");
      retVal = parseExpr();
    }
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseStringExpr(){
    if(parseError == 0){
      System.out.println("parse: String Expression");
    }
    tree.addBranch("String Expr");
      
    boolean retVal = false;
    retVal = match("quote");
    retVal = parseCharList();
    retVal = match("quote");
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseBooleanExpr(){
    if(parseError == 0){
      System.out.println("parse: Boolean Expression");
    }
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
    if(parseError == 0){
      System.out.println("parse: Id");
    }
    tree.addBranch("Id");
      
    boolean retVal = false;
    retVal = match("Id");
      
    tree.endChildren();
    return retVal;
  }
    
  boolean parseCharList(){
    if(parseError == 0){
      System.out.println("parse: Char List");
    }
    tree.addBranch("char list");
      
    boolean retVal = false;
    if(stream.get(i).type.equals("char")){
      
        
      if(!stream.get(i).name.equals(" ")){
        if(parseError == 0){
          System.out.println("parse: Char");
        }
        tree.addBranch("char");
        
        
        retVal = match("char");
        tree.endChildren();
        retVal = parseCharList();
      }
      else if(stream.get(i).name.equals(" ")){
        if(parseError == 0){
          System.out.println("parse: Space");
        }
        tree.addBranch("space");
        

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
    if(parseError == 0){
      System.out.println("parse: Type");
    }
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
    if(parseError == 0){
      System.out.println("parse: Bool Op");
    }
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
    if(parseError == 0){
      System.out.println("parse: Bool");
    }
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
   
  //Matches expected token with the current token to add
  //- nodes to the CST.
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
      
    }
    
    //This if statement prevents inputs smaller than 3
    //- (The min size of a Block) from crashing the program.
    if(i != stream.size()-1){
      i++;
    }
    
    return retVal;
  }
  
  //Depth First In order traversal to print CST.
  void printCST(){
    tree.printTree(tree.root, 0);
  }
    
}