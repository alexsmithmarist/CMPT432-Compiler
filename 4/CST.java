import java.util.ArrayList;

class CSTNode{
  String name = null;
  String tokType = " ";
  String tru = "";
  
  int lineNum = 0;
  int indexNum = 0;
  int scope = 0;
  ArrayList<CSTNode> children = new ArrayList<CSTNode>();
  CSTNode parent = null;
  
  public CSTNode(){}

  //Ids, DIgits and Chars
  public CSTNode(String tName, String tType, int line, int index){
    name = tName;
    tokType = tType;
    lineNum = line;
    indexNum = index;
  }
    
  public CSTNode(String tName, String tType, int line, int index, int scopee){
    name = tName;
    tokType = tType;
    lineNum = line;
    indexNum = index;
    scope = scopee;
  }
    
  public CSTNode(String tName, String tType, int line, int index, int scopee, String truType){
    name = tName;
    tokType = tType;
    lineNum = line;
    indexNum = index;
    scope = scopee;
    tru = truType;
  }
    
  //Everything Else
  public CSTNode(String type, int line, int index){
    tokType = type;
    lineNum = line;
    indexNum = index;
  }
    
  public CSTNode(String type, int line, int index, int scopee){
    tokType = type;
    lineNum = line;
    indexNum = index;
    scope = scopee;
  }

}

class CST{
  CSTNode root = null;
  CSTNode current = null;
  
  //Default Constructor
  public CST(){}

  //Adding a Branch
  public void addBranch(String token){
    CSTNode node = new CSTNode(token, 0, 0);
    if(this.root == null){
      this.root = node;
    }
    else{
      node.parent = this.current;
      this.current.children.add(node);
    }
    this.current = node;
  }
    
  public void addBranch(String token, int scope){
    CSTNode node = new CSTNode(token, 0, 0, scope);
    if(this.root == null){
      this.root = node;
    }
    else{
      node.parent = this.current;
      this.current.children.add(node);
    }
    this.current = node;
  }
  
  //Adding a leaf (no children)
  public void addLeaf(String name, String type, int line, int index){
    CSTNode node = new CSTNode(name, type, line, index);
    if(this.root == null){
      this.root = node;
    }
    else{
      node.parent = this.current;
      this.current.children.add(node);
    }  
  }

  public void addLeaf(String name, String type, int line, int index, int scope){
    CSTNode node = new CSTNode(name, type, line, index, scope);
    if(this.root == null){
      this.root = node;
    }
    else{
      node.parent = this.current;
      this.current.children.add(node);
    }  
  }
    
  public void addLeaf(String name, String type, int line, int index, int scope, String truel){
    CSTNode node = new CSTNode(name, type, line, index, scope, truel);
    if(this.root == null){
      this.root = node;
    }
    else{
      node.parent = this.current;
      this.current.children.add(node);
    }  
  }
  
  //change the current node to the parent
  public void endChildren(){
    if(this.current.parent != null){
      this.current = this.current.parent;
    }
  }

  //Depth First in order traversal to print the tree
  public CSTNode printTree(CSTNode node, int depth){
    String traversalResult = "";
    
    for(int i = 0; i < depth; i++){
      traversalResult = traversalResult + "-";
    }
     
    //If the node has children, it is a branch
    if(node.children.size() != 0){
      traversalResult = traversalResult + "<" + node.tokType + ">";
      System.out.println(traversalResult);
     
          
      for (int j = 0; j < node.children.size(); j++){
        printTree(node.children.get(j), depth+1);
      }
    }
     
    //If the node has no children, it is a leaf.
    else{
      if(node.name == null){
        traversalResult = traversalResult + "[" + node.tokType + "] ";
        System.out.println(traversalResult);
      }
      else{
        traversalResult = traversalResult + "[" + node.name + "] ";
        System.out.println(traversalResult);
      }
    }
    
      
    return root;
  }

}