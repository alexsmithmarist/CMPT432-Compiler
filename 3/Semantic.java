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
     
      ast.endChildren();
        
    }
      
    else if(node.tokType.equals("assignment statement")){
      ast.addBranch("Assignment");
      ast.addLeaf(node.children.get(0).children.get(0).name, node.children.get(0).children.get(0).tokType, node.children.get(0).children.get(0).lineNum, node.children.get(0).children.get(0).indexNum);
        
      //EXPR CHILD
      makeExpr(node.children.get(2), 0);
        
      ast.endChildren();
    }
      
    else if(node.tokType.equals("while statement")){
      ast.addBranch("While");
      
      if(node.children.get(1).children.size() == 1){
        ast.addLeaf(node.children.get(1).children.get(0).children.get(0).tokType, node.children.get(1).children.get(0).children.get(0).tokType, node.children.get(1).children.get(0).children.get(0).lineNum, node.children.get(1).children.get(0).children.get(0).indexNum);
          
        //ast.endChildren();
      }
        
      else{
          
        if(node.children.get(1).children.get(2).children.get(0).tokType.equals("equal")){
          ast.addBranch("Equal");
        }
        else{
          ast.addBranch("Not Equal");
        }
          
        makeExpr(node.children.get(1).children.get(1), 1);
        makeExpr(node.children.get(1).children.get(3), 0);
        //ast.endChildren();
      }
        
      //BLOCK CHILD
      construct(node.children.get(2).children.get(0));
        
      ast.endChildren();
    }
      
    else if(node.tokType.equals("if statement")){
      ast.addBranch("If");
      
      if(node.children.get(1).children.size() == 1){
        ast.addLeaf(node.children.get(1).children.get(0).children.get(0).tokType, node.children.get(1).children.get(0).children.get(0).tokType, node.children.get(1).children.get(0).children.get(0).lineNum, node.children.get(1).children.get(0).children.get(0).indexNum);
          
        //ast.endChildren();
      }
        
      else{
          
        if(node.children.get(1).children.get(2).children.get(0).tokType.equals("equal")){
          ast.addBranch("Equal");
        }
        else{
          ast.addBranch("Not Equal");
        }
          
        makeExpr(node.children.get(1).children.get(1), 1);
        makeExpr(node.children.get(1).children.get(3), 0);
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
    
  public void makeExpr(CSTNode expr, int temp){
    if(expr.children.get(0).tokType.equals("Id")){
      ast.addLeaf(expr.children.get(0).children.get(0).name, expr.children.get(0).children.get(0).tokType, expr.children.get(0).children.get(0).lineNum, expr.children.get(0).children.get(0).indexNum) ;
       
      if(temp == 0){
        ast.endChildren();
      }
    }
      
    else if(expr.children.get(0).tokType.equals("Int Expr")){
      if(expr.children.get(0).children.size() == 1){
        ast.addLeaf(expr.children.get(0).children.get(0).children.get(0).name, expr.children.get(0).children.get(0).children.get(0).tokType, expr.children.get(0).children.get(0).children.get(0).lineNum, expr.children.get(0).children.get(0).children.get(0).indexNum);
          
        ast.endChildren();
      }
      else{
        ast.addBranch("Add");
        ast.addLeaf(expr.children.get(0).children.get(0).children.get(0).name, expr.children.get(0).children.get(0).children.get(0).tokType, expr.children.get(0).children.get(0).children.get(0).lineNum, expr.children.get(0).children.get(0).children.get(0).indexNum);
        
        makeExpr(expr.children.get(0).children.get(2), 0);
          
        //ast.endChildren();
        
      }
    }
      
    else if(expr.children.get(0).tokType.equals("Boolean Expr")){
      if(expr.children.get(0).children.size() == 1){
        ast.addLeaf(expr.children.get(0).children.get(0).children.get(0).tokType, expr.children.get(0).children.get(0).children.get(0).tokType, expr.children.get(0).children.get(0).children.get(0).lineNum, expr.children.get(0).children.get(0).children.get(0).indexNum);
          
        ast.endChildren();
      }
      else{
        if(expr.children.get(0).children.get(2).children.get(0).tokType.equals("equal")){
          ast.addBranch("Equal");
        }
        else{
          ast.addBranch("Not Equal");
        }
          
        makeExpr(expr.children.get(0).children.get(1),1);
        makeExpr(expr.children.get(0).children.get(3), 0);
        ast.endChildren();
        
      }
      
    }
      
    else if(expr.children.get(0).tokType.equals("String Expr")){
      String word = "";
      CSTNode start = expr.children.get(0).children.get(1);
        
      while(start.children.size() != 0){
        word = word + start.children.get(0).children.get(0).name;
        start = start.children.get(1);
      }
        
      ast.addLeaf(word, "String", expr.children.get(0).children.get(0).lineNum, expr.children.get(0).children.get(0).indexNum+1) ;
    }
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