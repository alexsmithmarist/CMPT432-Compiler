import java.util.*;

public class codeGen{
  String[] opCode = new String[255];
  ArrayList<sVar> staticTable = new ArrayList<sVar>();
  ArrayList<jVar> jumpTable = new ArrayList<jVar>();
  int curPos = 0;
  int staticNum = 0;
  int jumpNum = 0;
  boolean stop = false;
    
  public codeGen(){
    for(int i = 0; i < 255; i++){
      opCode[i] = "00";
    }
  };
    
  public void generate (CSTNode node){
    if(node.tokType.equals("Var Decl")){
      opCode[curPos] = "A9";
      curPos = curPos + 1;
      
      opCode[curPos] = "00";
      curPos = curPos + 1;
        
      String tempName = "T" + staticNum;
      sVar tempSpot = new sVar(tempName, node.children.get(1).name, staticNum);
        
      opCode[curPos] = tempName;
      curPos = curPos +1;
        
      opCode[curPos] = "XX";
      curPos = curPos+1;
      staticNum = staticNum+1;
      
    }
      
      
      
      
      
      
    if(node.children.size() != 0 && !stop){
      for (int j = 0; j < node.children.size(); j++){
        generate(node.children.get(j));
      }
    }
  }
    
  
  
}

class sVar{
  String temp = "";
  String id = "";
  int offset = 0;
    
  public sVar(){};

  public sVar(String name, String ident, int offs){
    temp = name;
    id = ident;
    offset = offs;
  }
}

class jVar{
  String temp = "";
  int length = 0;
    
  public jVar(){};

  public jVar(String name){
    temp = name;
  }
    
  public void setLength(int l){
    length = l;
  }
}