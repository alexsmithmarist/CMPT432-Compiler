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
         //COME BACK HERE WHEN WE IMPLEMENT ADDING
        }
      }
      else if(expr.children.get(0).tokType.equals("Boolean Expr")){
        if(expr.children.get(0).children.size() == 1){
          ast.addLeaf(expr.children.get(0).children.get(0).children.get(0).tokType, expr.children.get(0).children.get(0).children.get(0).tokType, expr.children.get(0).children.get(0).children.get(0).lineNum, expr.children.get(0).children.get(0).children.get(0).indexNum);
          ast.endChildren();
        }
        else{
          //COMPLEX BOOLEANS
        }
      }
      else if(expr.children.get(0).tokType.equals("String Expr")){
        //STRING COMBINATION
      }
      
        
        
      //ast.endChildren();
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