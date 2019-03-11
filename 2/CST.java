import java.util.ArrayList;

class CSTNode{
  String name = " ";
  ArrayList<CSTNode> children = new ArrayList<CSTNode>();
  CSTNode parent = null;
  
  public CSTNode(){}

  public CSTNode(String type){
    name = type;
  }

}

class CST{
  CSTNode root = null;
  CSTNode current = null;
  
  public CST(){}

  public void addBranch(String token){
    CSTNode node = new CSTNode(token);
    if(this.root == null){
      this.root = node;
    }
    else{
      node.parent = this.current;
      this.current.children.add(node);
    }
    this.current = node;
  }
  
  public void addLeaf(String token){
    CSTNode node = new CSTNode(token);
    if(this.root == null){
      this.root = node;
    }
    else{
      node.parent = this.current;
      this.current.children.add(node);
    }  
  }
    
  public void endChildren(){
    if(this.current.parent != null){
      this.current = this.current.parent;
    }
  }

  public void printTree(CSTNode node, int depth){
    String traversalResult = "";
    
    for(int i = 0; i < depth; i++){
      traversalResult = traversalResult + "-";
    }
      
    if(node.children.size() != 0){
      traversalResult = traversalResult + "<" + node.name + ">";
      //traversalResult = traversalResult + "\n";
      System.out.println(traversalResult);
      if(node.name.equals("char") || node.name.equals("space")){
        for (int j = 0; j < node.children.size(); j++){
          printTree(node.children.get(j), depth);
        }
      }  
          
      else{
        for (int j = 0; j < node.children.size(); j++){
          printTree(node.children.get(j), depth+1);
        }
      }
    }
      
    else{
      traversalResult = traversalResult + "[" + node.name + "] ";
      System.out.println(traversalResult);
    }
    
      
    root = null;
  }

}