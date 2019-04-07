public class Semantic{
  int typeError = 0;
  CST ast = new CST();
  CST tree = null;
  boolean stop = false;

    
  public Semantic(){}
      
  public CSTNode construct(CSTNode node){
  
    if(node.tokType.equals("L_Bracket")){
      ast.addBranch("Block"); 
    }
      
    else if(node.tokType.equals("R_Bracket")){
      ast.endChildren();
    }
      
    else if(node.tokType.equals("Print")){
      ast.addBranch("Print");
        
      CSTNode expr = null;
      expr = node.parent.children.get(2);
        
      /* USE THIS FOR EXPR FUNCTION TO BE MADE LATER
      if(expr.children.get(0).tokType.equals("Id")){
        ast.addLeaf(expr.children.get(0).children.get(0).name, expr.children.get(0).children.get(0).tokType, expr.children.get(0).children.get(0).lineNum, expr.children.get(0).children.get(0).indexNum) ;
        ast.endChildren();
      }
      else if(expr.children.get(0).tokType.equals("Int Expr")){
        if(expr.children.get(0).children.size() == 1){
          ast.addLeaf(expr.children.get(0).children.get(0).children.get(0).name, expr.children.get(0).children.get(0).children.get(0).tokType, expr.children.get(0).children.get(0).children.get(0).lineNum, expr.children.get(0).children.get(0).children.get(0).indexNum);
          ast.endChildren();
        }
        else{
         //EXPR FUNCTION GOES HERE
        }
      }
      else if(expr.children.get(0).tokType.equals("Boolean Expr")){
        if(expr.children.get(0).children.size() == 1){
          ast.addLeaf(expr.children.get(0).children.get(0).children.get(0).tokType, expr.children.get(0).children.get(0).children.get(0).tokType, expr.children.get(0).children.get(0).children.get(0).lineNum, expr.children.get(0).children.get(0).children.get(0).indexNum);
          ast.endChildren();
        }
        else{
          //BOOLEAN EXPR FUNCTION GOES HERE
        }
      }
      else if(expr.children.get(0).tokType.equals("String Expr")){
        //STRING COMBINATION
      }
      
      */ 
    
    }
      
    else if(node.tokType.equals("varDecl")){
      ast.addBranch("Var Decl");
      ast.addLeaf(node.children.get(0).children.get(0).tokType, node.children.get(0).children.get(0).tokType, node.children.get(0).children.get(0).lineNum, node.children.get(0).children.get(0).indexNum);
      ast.addLeaf(node.children.get(1).children.get(0).name, node.children.get(1).children.get(0).tokType, node.children.get(1).children.get(0).lineNum, node.children.get(1).children.get(0).indexNum);
     
      ast.endChildren();
        
    }
      
    else if(node.tokType.equals("assignment statement")){
      ast.addBranch("Assignment");
      ast.addLeaf(node.children.get(0).children.get(0).tokType, node.children.get(0).children.get(0).tokType, node.children.get(0).children.get(0).lineNum, node.children.get(0).children.get(0).indexNum);
        
      //EXPR CHILD
      //makeExpr(node.parent.children.get(2));
        
      ast.endChildren();
    }
      
    else if(node.tokType.equals("while statement")){
      ast.addBranch("While");
      
      //BOOLEXPR CHILD
      //makeBoolExpr(node.parent.children.get(1))
        
      //BLOCK CHILD
      construct(node.children.get(2).children.get(0));
        
      ast.endChildren();
    }
      
    else if(node.tokType.equals("if statement")){
      ast.addBranch("If");
      
      //BOOLEXPR CHILD
      //makeBoolExpr(node.parent.children.get(1))
        
      //BLOCK CHILD
      construct(node.children.get(2).children.get(0));
        
      ast.endChildren();
    }
      
    if(node.children.size() != 0 && !stop){
      for (int j = 0; j < node.children.size(); j++){
        construct(node.children.get(j));
      }
    }
     
    
    return ast.root;
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