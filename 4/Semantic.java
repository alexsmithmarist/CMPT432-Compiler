import java.util.*;


public class Semantic{
  int typeError = 0;
  CST ast = new CST();
  CST tree = null;
  boolean stop = false;
  ArrayList<Scope> symTable = new ArrayList<Scope>();
  int currentScope = -1;
  boolean exist = false;
  boolean err = false;
  

    
  public Semantic(){}
      
  public CSTNode construct(CSTNode node){
  
    if(node.tokType.equals("L_Bracket")){
      // - Indication to start a new scope 
        
      /*System.out.println(currentScope);
      System.out.println("block");
      System.out.println();*/
      // - -1 indicates it is the starting scope
      if(currentScope == -1){
        symTable.add(new Scope());
      }
      else{
        symTable.add(new Scope(currentScope));
      }
        
      // - update pointer to current scope
      currentScope = symTable.size()-1;
      ast.addBranch("Block", currentScope); 
    }
      
    else if(node.tokType.equals("R_Bracket")){
      // - end of scope, return scope pointer to parent
      ast.endChildren();
      if(symTable.get(currentScope).parent != -99){
        currentScope = symTable.get(currentScope).parent;
      }
    }
      
    else if(node.tokType.equals("print statement")){
      ast.addBranch("Print", currentScope);
        
      CSTNode expr = null;
      expr = node.children.get(2);
        
      makeExpr(expr, 0);
      ast.endChildren();
    }
      
    else if(node.tokType.equals("varDecl")){
      // - VarDecl will always be Id and Type, so creating an expr is not needed here
      ast.addBranch("Var Decl", currentScope);
      
      ast.addLeaf(node.children.get(0).children.get(0).tokType, node.children.get(0).children.get(0).tokType, node.children.get(0).children.get(0).lineNum, node.children.get(0).children.get(0).indexNum, currentScope);
      ast.addLeaf(node.children.get(1).children.get(0).name, node.children.get(1).children.get(0).tokType, node.children.get(1).children.get(0).lineNum, node.children.get(1).children.get(0).indexNum, currentScope, node.children.get(0).children.get(0).tokType);
     
      // - since we are using an identifier, make sure it is not already declared
      err = symTable.get(currentScope).inTable(node.children.get(1).children.get(0).name, node.children.get(0).children.get(0).tokType, node.children.get(1).children.get(0).lineNum, node.children.get(1).children.get(0).indexNum, currentScope);
        
      if(err == true){
        typeError = typeError +1;
      }
      err = false;
      ast.endChildren();
        
    }
      
    else if(node.tokType.equals("assignment statement")){
      String type = "";
      ast.addBranch("Assignment", currentScope);
      // - left side of an assignment is always an identifier
      ast.addLeaf(node.children.get(0).children.get(0).name, node.children.get(0).children.get(0).tokType, node.children.get(0).children.get(0).lineNum, node.children.get(0).children.get(0).indexNum, currentScope);
        
      //EXPR CHILD
      type = makeExpr(node.children.get(2), 0);
        
      // - Check for type mismatches
      err = symTable.get(currentScope).typeCheck(node.children.get(0).children.get(0).name, type, node.children.get(0).children.get(0).lineNum, node.children.get(0).children.get(0).indexNum, currentScope, symTable, currentScope);
        
      if(err == true){
        typeError = typeError + 1;
      }
        
      err = false;
      ast.endChildren();
    }
      
    else if(node.tokType.equals("while statement")){
      ast.addBranch("While", currentScope);
      
      // - If there is only one child, no need to check complex boolean expressions.
      // - It has to be only true or false.
      if(node.children.get(1).children.size() == 1){
        ast.addLeaf(node.children.get(1).children.get(0).children.get(0).tokType, node.children.get(1).children.get(0).children.get(0).tokType, node.children.get(1).children.get(0).children.get(0).lineNum, node.children.get(1).children.get(0).children.get(0).indexNum, currentScope);
          
      }
        
      else{
        String type1 = "";
        String type2 = "";
        
        // - Complex boolean expressions, first determine equal or not equal
        if(node.children.get(1).children.get(2).children.get(0).tokType.equals("equal")){
          ast.addBranch("Equal", currentScope);
        }
        else{
          ast.addBranch("Not Equal", currentScope);
        }
          
        type1 = makeExpr(node.children.get(1).children.get(1), 1);
        type2 = makeExpr(node.children.get(1).children.get(3), 0);
        if(!(type1.equals(type2))){
          System.out.println("Semantic Error: Type Mismatch in while statement in scope " + currentScope);
          typeError = typeError + 1;
        }
        
      }
        
    }
      
    else if(node.tokType.equals("if statement")){
      ast.addBranch("If");
      // - Exactly the same as While
      if(node.children.get(1).children.size() == 1){
        ast.addLeaf(node.children.get(1).children.get(0).children.get(0).tokType, node.children.get(1).children.get(0).children.get(0).tokType, node.children.get(1).children.get(0).children.get(0).lineNum, node.children.get(1).children.get(0).children.get(0).indexNum, currentScope);
          
        
      }
        
      else{
        String type1 = "";
        String type2 = "";
          
        if(node.children.get(1).children.get(2).children.get(0).tokType.equals("equal")){
          ast.addBranch("Equal", currentScope);
        }
        else{
          ast.addBranch("Not Equal", currentScope);
        }
          
        type1 = makeExpr(node.children.get(1).children.get(1), 1);
        type2 = makeExpr(node.children.get(1).children.get(3), 0);
        System.out.println("Type1: "+type1);
        //System.out.println(type2);
        if(!(type1.equals(type2))){
          System.out.println("Semantic Error: Type Mismatch in if statement in scope " + currentScope);
          typeError = typeError +1;
        }
        
      }
        
      
    }
      
    // - Keep constructing the AST for the children of a L_Bracket in the CST
    if(node.children.size() != 0 && !stop){
      for (int j = 0; j < node.children.size(); j++){
        construct(node.children.get(j));
      }
    }
     
    return ast.root;
  }
    
  public String makeExpr(CSTNode expr, int temp){
    // - if the expression is an identifier
    if(expr.children.get(0).tokType.equals("Id")){
      ast.addLeaf(expr.children.get(0).children.get(0).name, expr.children.get(0).children.get(0).tokType, expr.children.get(0).children.get(0).lineNum, expr.children.get(0).children.get(0).indexNum, currentScope) ;
    
      // - make sure the identifier exists before using it
      exist = symTable.get(currentScope).existCheck(expr.children.get(0).children.get(0).name, expr.children.get(0).children.get(0).lineNum, expr.children.get(0).children.get(0).indexNum, currentScope, symTable, currentScope);
      if(temp == 0){
        ast.endChildren();
      }
      
      
      if(exist){
        return symTable.get(currentScope).getType(expr.children.get(0).children.get(0).name, symTable);
      }
      else{
        exist = false;
        typeError = typeError +1;
        return "Id";
      }
      
    }
      
    else if(expr.children.get(0).tokType.equals("Int Expr")){
      // - if the children size is 1, there is only a single digit
      if(expr.children.get(0).children.size() == 1){
        ast.addLeaf(expr.children.get(0).children.get(0).children.get(0).name, expr.children.get(0).children.get(0).children.get(0).tokType, expr.children.get(0).children.get(0).children.get(0).lineNum, expr.children.get(0).children.get(0).children.get(0).indexNum, currentScope);
          
        ast.endChildren();
      }
      // - if the children size is more than 1, there is addition of two (or more) digits
      else{
        String xtype = "";
        ast.addBranch("Add", currentScope);
        ast.addLeaf(expr.children.get(0).children.get(0).children.get(0).name, expr.children.get(0).children.get(0).children.get(0).tokType, expr.children.get(0).children.get(0).children.get(0).lineNum, expr.children.get(0).children.get(0).children.get(0).indexNum, currentScope);
        
        // - recursively call the makeExpr method to continuously add digits
        xtype = makeExpr(expr.children.get(0).children.get(2), 0);
          
        // - if identifiers are used, make sure they are ints
        if(!(xtype.equals("Int"))){
          System.out.println("Semantic Error: Expected Int when adding on line " +expr.children.get(0).children.get(0).children.get(0).lineNum +", got " +xtype);
          typeError = typeError + 1;
        }
        //ast.endChildren();
        
      }
        
      return "Int";
    }
      
    else if(expr.children.get(0).tokType.equals("Boolean Expr")){
      // - same as while statement / if statement in construct method above, except recursively
      if(expr.children.get(0).children.size() == 1){
        ast.addLeaf(expr.children.get(0).children.get(0).children.get(0).tokType, expr.children.get(0).children.get(0).children.get(0).tokType, expr.children.get(0).children.get(0).children.get(0).lineNum, expr.children.get(0).children.get(0).children.get(0).indexNum, currentScope);
          
        ast.endChildren();
      }
      else{
        String xtype1 = "";
        String xtype2 = "";
        if(expr.children.get(0).children.get(2).children.get(0).tokType.equals("equal")){
          ast.addBranch("Equal", currentScope);
        }
        else{
          ast.addBranch("Not Equal", currentScope);
        }
          
        xtype1 = makeExpr(expr.children.get(0).children.get(1),1);
        xtype2 = makeExpr(expr.children.get(0).children.get(3), 0);
        if(!(xtype1.equals(xtype2))){
          System.out.println("Semantic Error: Type Mismatch in boolean expression statement in scope " + currentScope);
          typeError = typeError +1;
        }
        ast.endChildren();
        
      }
      return "Boolean";
    }
      
    else if(expr.children.get(0).tokType.equals("String Expr")){
      String word = "";
      CSTNode start = expr.children.get(0).children.get(1);
        
      while(start.children.size() != 0){
        word = word + start.children.get(0).children.get(0).name;
        start = start.children.get(1);
      }
        
      ast.addLeaf(word, "String", expr.children.get(0).children.get(0).lineNum, expr.children.get(0).children.get(0).indexNum+1, currentScope) ;
        
      return "String";
    }
    return "Error";
  }
    
  public void printAST(CSTNode node, int depth){
    // - almost identical to print CST
    String traversalResult = "";
    
    for(int i = 0; i < depth; i++){
      traversalResult = traversalResult + "-";
    }
     
    //If the node has children, it is a branch
    if(node.name == null){
      if(node.children.size() != 0){
        traversalResult = traversalResult + "<" + node.tokType + ">";
      }
      else{
        traversalResult = traversalResult + "[" + node.tokType + "]";
      }
      System.out.println(traversalResult);
    }
    else{
      if(node.children.size() != 0){
        traversalResult = traversalResult + "<" + node.name + ">";
      }
      else{
        traversalResult = traversalResult + "[" + node.name + "]";
      }
      System.out.println(traversalResult);
    }
     
          
    if(node.children.size() != 0){
      for (int j = 0; j < node.children.size(); j++){
        printAST(node.children.get(j), depth+1);
      }
    }
  }
    
  public void printSym(){
    // - Print out the contents of each scope's symbol table
    for(int i = 0; i < symTable.size(); i++){
      for(int j = 0; j < 26; j++){
        if(symTable.get(i).table[j] != null){
          if(symTable.get(i).table[j].sType.equals("Int")){
            System.out.println("Id: " + symTable.get(i).table[j].sName + "     Type: " + symTable.get(i).table[j].sType + "      Scope: " + symTable.get(i).table[j].sScope);
          }
          else if(symTable.get(i).table[j].sType.equals("String")){
            System.out.println("Id: " + symTable.get(i).table[j].sName + "     Type: " + symTable.get(i).table[j].sType + "   Scope: " + symTable.get(i).table[j].sScope);
          }
          else if(symTable.get(i).table[j].sType.equals("Boolean")){
            System.out.println("Id: " + symTable.get(i).table[j].sName + "     Type: " + symTable.get(i).table[j].sType + "  Scope: " + symTable.get(i).table[j].sScope);
          }
        }
      }
    }
  }
    
  public void warnCheck(){
    // - make sure each identifier was used at some point in the program
    for(int i = 0; i < symTable.size(); i++){
      for(int j = 0; j < 26; j++){
        if(symTable.get(i).table[j] != null){
          if(symTable.get(i).table[j].use == false && symTable.get(i).table[j].init == false){
            System.out.println("Semantic Warning: Variable " + symTable.get(i).table[j].sName +" in scope " + symTable.get(i).table[j].sScope + " declared but not used or initialized.");
          }
          /*else if(symTable.get(i).table[j].use == true && symTable.get(i).table[j].init == false){
            System.out.println("Semantic Warning: Variable " + symTable.get(i).table[j].sName +" in scope " + symTable.get(i).table[j].sScope + " used but not initialized."); 
          }*/
          else if(symTable.get(i).table[j].use == false && symTable.get(i).table[j].init == true){
            System.out.println("Semantic Warning: Variable " + symTable.get(i).table[j].sName +" in scope " + symTable.get(i).table[j].sScope + " initialized but not used.");
          }
        }
      }
    }
  }
}

class sNode {
  // - used to store if an identifier has been used or initialized
  String sName = "";
  String sType = "";
  int sLine = 0;
  int sScope = 0;
  int sIndex = 0;
  boolean use = false;
  boolean init = false;
  ArrayList<Integer> initscope = new ArrayList<>();
    
  public sNode(){}

  public sNode(String name, String type, int line, int index, int scope){
    sName = name;
    sType = type;
    sLine = line;
    sIndex = index;
    sScope = scope;
  }
}

class Scope{
  // - contains the symbol table of each scope
  // - also does type checking, declaration checking, etc.
  int parent = -99;
    
  // - a array of 26 is used because identifiers are only single ids
  // - same concept as lexer to determine column in the transition table
  sNode[] table = new sNode [26];
    
  public Scope(){
    for(int i = 0; i != 26; i++){
      table[i] = null;
    }
  }

  public Scope(int p){
    parent = p;
      
    for(int i = 0; i != 26; i++){
      table[i] = null;
    }
      

  }
    
  public boolean inTable(String id, String type, int line, int index, int scope){
    boolean err = false;
    char[] alpha = new char[] 
    {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o',
     'p','q','r','s','t','u','v','w','x','y','z'};
      
    char next = id.charAt(0);
    int currentCol = 0;
      
    for(int j = 0; j < alpha.length; j++){
      if(next == alpha[j]){
        currentCol = j;
      }
    }
    sNode temp = new sNode(id, type, line, index, scope);
    
    if(table[currentCol] == null){
      table[currentCol] = temp;
      err = false;
    }
    else{
      System.out.println("Semantic Error: Redeclared variable " +id+ " in scope " + scope + " on line "+line+ ", index "+index);
      err = true;
    }
      
    return err;
  }
    
  public boolean typeCheck(String id, String type, int line, int index, int scope, ArrayList<Scope> tbl, int fScope){
    boolean err = false;
    char[] alpha = new char[] 
    {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o',
     'p','q','r','s','t','u','v','w','x','y','z'};
      
    char next = id.charAt(0);
    int currentCol = 0;
      
    for(int j = 0; j < alpha.length; j++){
      if(next == alpha[j]){
        currentCol = j;
      }
    }
    
    // - check to see if it declared in a parent scope
    if(table[currentCol] == null && parent != -99){
      err = tbl.get(parent).typeCheck(id, type, line, index, parent, tbl, fScope);
    }
    else if(table[currentCol] == null && parent == -99){
      System.out.println("Semantic Error: Variable " +id+ " used before declared on line " +line+ " index "+index);
      err = true;
    }
    else if(table[currentCol] != null){
      if(!(table[currentCol].sType.equals(type))){
        System.out.println("Semantic Error: Type mismatch variable " +id+" on line " +line+ " index "+index+" expecting "+type+", got "+table[currentCol].sType);
        err = true;
      }
      else{
        // - add where the identifier is currently being initialized
        table[currentCol].init = true;
        table[currentCol].initscope.add(fScope);
        err = false;
      }
    }
      
    return err;
  }
    
  public boolean existCheck(String id, int line, int index, int scope, ArrayList<Scope> tbl, int fScope){
    boolean ifexist = false;
    boolean initialized = false;
    char[] alpha = new char[] 
    {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o',
     'p','q','r','s','t','u','v','w','x','y','z'};
      
    char next = id.charAt(0);
    int currentCol = 0;
      
    for(int j = 0; j < alpha.length; j++){
      if(next == alpha[j]){
        currentCol = j;
      }
    }
      
    if(table[currentCol] == null && parent != -99){
      ifexist = tbl.get(parent).existCheck(id, line, index, parent, tbl, fScope);
    }
    else if(table[currentCol] == null && parent == -99){
      System.out.println("Semantic Error: Variable " +id+ " used before declared on line " +line+ " index "+index);
      ifexist = false;
    }
    else if(table[currentCol] != null){
      table[currentCol].use = true;
      // - check if the identifier is initialized when attempting to use it
      initialized = tbl.get(fScope).initCheck(id, line, index, fScope, tbl);
      
      ifexist = true;
    }

    return ifexist;
  }
    
  public String getType(String id, ArrayList<Scope> tbl){
    String ktype = "";
    char[] alpha = new char[] 
    {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o',
     'p','q','r','s','t','u','v','w','x','y','z'};
      
    char next = id.charAt(0);
    int currentCol = 0;
      
    for(int j = 0; j < alpha.length; j++){
      if(next == alpha[j]){
        currentCol = j;
      }
    }
      
    if(table[currentCol] !=null){
      ktype = table[currentCol].sType;
    }
    else if(table[currentCol] == null && parent != -99){
      ktype = tbl.get(parent).getType(id, tbl);
    }
    else{
      ktype = "Error";
    }
      
    return ktype;
  }
    
  public boolean initCheck(String id, int line, int index, int scope, ArrayList<Scope> tbl){
    /*System.out.println(id);
    System.out.println(scope);
    System.out.println(parent);*/
    boolean ifexist = false;
    char[] alpha = new char[] 
    {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o',
     'p','q','r','s','t','u','v','w','x','y','z'};
      
    char next = id.charAt(0);
    int currentCol = 0;
      
    for(int j = 0; j < alpha.length; j++){
      if(next == alpha[j]){
        currentCol = j;
      }
    }
     
    if(table[currentCol] != null){
      for(int i = 0; i < table[currentCol].initscope.size(); i++){
        if(table[currentCol].initscope.get(i) == scope){
        
          ifexist = true;
        }
      }
    }
   
    if(!ifexist){
      if(parent != -99){
        ifexist = tbl.get(parent).initCheck(id, line, index, parent, tbl);
      }
      else{
        System.out.println("Semantic Warning: Variable " + id +" in scope " + scope + " used on line "+ line+" index "+index+" but not initialized.");
        System.out.println();
      }
    }
    
    return ifexist;
  }
}