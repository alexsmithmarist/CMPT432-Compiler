import java.util.*;


public class Semantic{
  int typeError = 0;
  CST ast = new CST();
  CST tree = null;
  boolean stop = false;
  ArrayList<Scope> symTable = new ArrayList<Scope>();
  int currentScope = -1;
  boolean exist = false;
  

    
  public Semantic(){}
      
  public CSTNode construct(CSTNode node){
  
    if(node.tokType.equals("L_Bracket")){
      ast.addBranch("Block"); 
      if(currentScope == -1){
        symTable.add(new Scope());
      }
      else{
        symTable.add(new Scope(currentScope));
      }
      currentScope = currentScope + 1;
    }
      
    else if(node.tokType.equals("R_Bracket")){
      ast.endChildren();
      if(symTable.get(currentScope).parent != -99){
        currentScope = symTable.get(currentScope).parent;
      }
    }
      
    else if(node.tokType.equals("print statement")){
      ast.addBranch("Print");
        
      CSTNode expr = null;
      expr = node.children.get(2);
        
      makeExpr(expr, 0);
      ast.endChildren();
    }
      
    else if(node.tokType.equals("varDecl")){
      ast.addBranch("Var Decl");
      ast.addLeaf(node.children.get(0).children.get(0).tokType, node.children.get(0).children.get(0).tokType, node.children.get(0).children.get(0).lineNum, node.children.get(0).children.get(0).indexNum);
      ast.addLeaf(node.children.get(1).children.get(0).name, node.children.get(1).children.get(0).tokType, node.children.get(1).children.get(0).lineNum, node.children.get(1).children.get(0).indexNum);
     
      symTable.get(currentScope).inTable(node.children.get(1).children.get(0).name, node.children.get(0).children.get(0).tokType, node.children.get(1).children.get(0).lineNum, node.children.get(1).children.get(0).indexNum, currentScope);
        
      ast.endChildren();
        
    }
      
    else if(node.tokType.equals("assignment statement")){
      String type = "";
      ast.addBranch("Assignment");
      ast.addLeaf(node.children.get(0).children.get(0).name, node.children.get(0).children.get(0).tokType, node.children.get(0).children.get(0).lineNum, node.children.get(0).children.get(0).indexNum);
        
      //EXPR CHILD
      type = makeExpr(node.children.get(2), 0);
        
      symTable.get(currentScope).typeCheck(node.children.get(0).children.get(0).name, type, node.children.get(0).children.get(0).lineNum, node.children.get(0).children.get(0).indexNum, currentScope, symTable);
        
      ast.endChildren();
    }
      
    else if(node.tokType.equals("while statement")){
      ast.addBranch("While");
      
      if(node.children.get(1).children.size() == 1){
        ast.addLeaf(node.children.get(1).children.get(0).children.get(0).tokType, node.children.get(1).children.get(0).children.get(0).tokType, node.children.get(1).children.get(0).children.get(0).lineNum, node.children.get(1).children.get(0).children.get(0).indexNum);
          
        //ast.endChildren();
      }
        
      else{
        String type1 = "";
        String type2 = "";
        
        if(node.children.get(1).children.get(2).children.get(0).tokType.equals("equal")){
          ast.addBranch("Equal");
        }
        else{
          ast.addBranch("Not Equal");
        }
          
        type1 = makeExpr(node.children.get(1).children.get(1), 1);
        type2 = makeExpr(node.children.get(1).children.get(3), 0);
        if(!(type1.equals(type2))){
          System.out.println("Semantic Error: Type Mismatch in while statement");
        }
        //ast.endChildren();
      }
        
      //BLOCK CHILD
      //construct(node.children.get(2).children.get(0));
        
      //ast.endChildren();
    }
      
    else if(node.tokType.equals("if statement")){
      ast.addBranch("If");
      
      if(node.children.get(1).children.size() == 1){
        ast.addLeaf(node.children.get(1).children.get(0).children.get(0).tokType, node.children.get(1).children.get(0).children.get(0).tokType, node.children.get(1).children.get(0).children.get(0).lineNum, node.children.get(1).children.get(0).children.get(0).indexNum);
          
        //ast.endChildren();
      }
        
      else{
        String type1 = "";
        String type2 = "";
          
        if(node.children.get(1).children.get(2).children.get(0).tokType.equals("equal")){
          ast.addBranch("Equal");
        }
        else{
          ast.addBranch("Not Equal");
        }
          
        type1 = makeExpr(node.children.get(1).children.get(1), 1);
        type2 = makeExpr(node.children.get(1).children.get(3), 0);
        if(!(type1.equals(type2))){
          System.out.println("Semantic Error: Type Mismatch in if statement");
        }
        //ast.endChildren();
      }
        
      //BLOCK CHILD
      //construct(node.children.get(2).children.get(0));
        
      //ast.endChildren();
      
    }
      
    if(node.children.size() != 0 && !stop){
      for (int j = 0; j < node.children.size(); j++){
        construct(node.children.get(j));
      }
    }
     
    return ast.root;
  }
    
  public String makeExpr(CSTNode expr, int temp){
    if(expr.children.get(0).tokType.equals("Id")){
      ast.addLeaf(expr.children.get(0).children.get(0).name, expr.children.get(0).children.get(0).tokType, expr.children.get(0).children.get(0).lineNum, expr.children.get(0).children.get(0).indexNum) ;
    
      exist = symTable.get(currentScope).existCheck(expr.children.get(0).children.get(0).name, expr.children.get(0).children.get(0).lineNum, expr.children.get(0).children.get(0).indexNum, currentScope, symTable);
      if(temp == 0){
        ast.endChildren();
      }
      
      if(exist){
        return symTable.get(currentScope).getType(expr.children.get(0).children.get(0).name, symTable);
      }
      else{
        exist = false;
        return "Id";
      }
      
    }
      
    else if(expr.children.get(0).tokType.equals("Int Expr")){
      if(expr.children.get(0).children.size() == 1){
        ast.addLeaf(expr.children.get(0).children.get(0).children.get(0).name, expr.children.get(0).children.get(0).children.get(0).tokType, expr.children.get(0).children.get(0).children.get(0).lineNum, expr.children.get(0).children.get(0).children.get(0).indexNum);
          
        ast.endChildren();
      }
      else{
        String xtype = "";
        ast.addBranch("Add");
        ast.addLeaf(expr.children.get(0).children.get(0).children.get(0).name, expr.children.get(0).children.get(0).children.get(0).tokType, expr.children.get(0).children.get(0).children.get(0).lineNum, expr.children.get(0).children.get(0).children.get(0).indexNum);
        
        xtype = makeExpr(expr.children.get(0).children.get(2), 0);
          
        if(!(xtype.equals("Int"))){
          System.out.println("Semantic Error: Expected Int when adding on line " +expr.children.get(0).children.get(0).children.get(0).lineNum +", got " +xtype);
        }
        //ast.endChildren();
        
      }
        
      return "Int";
    }
      
    else if(expr.children.get(0).tokType.equals("Boolean Expr")){
      if(expr.children.get(0).children.size() == 1){
        ast.addLeaf(expr.children.get(0).children.get(0).children.get(0).tokType, expr.children.get(0).children.get(0).children.get(0).tokType, expr.children.get(0).children.get(0).children.get(0).lineNum, expr.children.get(0).children.get(0).children.get(0).indexNum);
          
        ast.endChildren();
      }
      else{
        String xtype1 = "";
        String xtype2 = "";
        if(expr.children.get(0).children.get(2).children.get(0).tokType.equals("equal")){
          ast.addBranch("Equal");
        }
        else{
          ast.addBranch("Not Equal");
        }
          
        xtype1 = makeExpr(expr.children.get(0).children.get(1),1);
        xtype2 = makeExpr(expr.children.get(0).children.get(3), 0);
        if(!(xtype1.equals(xtype2))){
          System.out.println("Semantic Error: Type Mismatch in boolean expression statement");
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
        
      ast.addLeaf(word, "String", expr.children.get(0).children.get(0).lineNum, expr.children.get(0).children.get(0).indexNum+1) ;
        
      return "String";
    }
    return "Error";
  }
    
  public void printAST(CSTNode node, int depth){
    String traversalResult = "";
    
    for(int i = 0; i < depth; i++){
      traversalResult = traversalResult + "-";
    }
     
    //If the node has children, it is a branch
    if(node.name == null){
      traversalResult = traversalResult + "<" + node.tokType + ">";
      System.out.println(traversalResult);
    }
    else{
      traversalResult = traversalResult + "<" + node.name + ">";
      System.out.println(traversalResult);
    }
     
          
    if(node.children.size() != 0){
      for (int j = 0; j < node.children.size(); j++){
        printAST(node.children.get(j), depth+1);
      }
    }
  }
}

class sNode {
  String sName = "";
  String sType = "";
  int sLine = 0;
  int sScope = 0;
  int sIndex = 0;
  boolean use = false;
  boolean init = false;
    
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
  int parent = -99;
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
    
  public void inTable(String id, String type, int line, int index, int scope){
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
    }
    else{
      System.out.println("Semantic Error: Redeclared variable " +id+ " in scope " + scope + " on line "+line+ ", index "+index);
    }
  }
    
  public void typeCheck(String id, String type, int line, int index, int scope, ArrayList<Scope> tbl){
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
      tbl.get(parent).typeCheck(id, type, line, index, parent, tbl);
    }
    else if(table[currentCol] == null && parent == -99){
      System.out.println("Semantic Error: Variable " +id+ " used before declared on line " +line+ " index "+index);
    }
    else if(table[currentCol] != null){
      if(!(table[currentCol].sType.equals(type))){
        System.out.println("Semantic Error: Type mismatch variable " +id+" on line " +line+ " index "+index+" expecting "+type+", got "+table[currentCol].sType);
      }
      else{
        table[currentCol].init = true;
      }
    }
  }
    
  public boolean existCheck(String id, int line, int index, int scope, ArrayList<Scope> tbl){
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
      
    if(table[currentCol] == null && parent != -99){
      tbl.get(parent).existCheck(id, line, index, parent, tbl);
    }
    else if(table[currentCol] == null && parent == -99){
      System.out.println("Semantic Error: Variable " +id+ " used before declared on line " +line+ " index "+index);
      ifexist = false;
    }
    else if(table[currentCol] != null){
      table[currentCol].use = true;
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
      tbl.get(parent).getType(id, tbl);
    }
    else{
      ktype = "Error";
    }
      
    return ktype;
  }
}