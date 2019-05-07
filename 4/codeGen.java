import java.util.*;

public class codeGen{
  String[] opCode = new String[255];
  ArrayList<sVar> staticTable = new ArrayList<sVar>();
  ArrayList<jVar> jumpTable = new ArrayList<jVar>();
  ArrayList<blocklist> blockTable = new ArrayList<blocklist>();
  int curPos = 0;
  int staticNum = 0;
  int jumpNum = 0;
  int currentScope = -1;
  int scopeTotal = -1;
  int blockpos = -99;
  boolean stop = false;
    
  public codeGen(){
    for(int i = 0; i < 255; i++){
      opCode[i] = "00";
    }
  };
    
  public void generate (CSTNode node){
      
    currentScope = node.scope;
      
    if(node.tokType.equals("Var Decl")){
      opCode[curPos] = "A9";
      curPos = curPos + 1;
      
      opCode[curPos] = "00";
      curPos = curPos + 1;
        
      opCode[curPos] = "8D";
      curPos = curPos + 1;
        
      if(staticNum < 10){ 
        String tempName = "T" + staticNum;
        sVar tempSpot = new sVar(tempName, "XX", node.children.get(1).name, node.children.get(1).scope, staticNum);
        staticTable.add(tempSpot);
        staticNum = staticNum + 1;
          
        opCode[curPos] = tempName;
        curPos = curPos +1;
          
        opCode[curPos] = "XX";
        curPos = curPos+1;
      }
      else{
        String tempName = Integer.toString(staticNum);
        sVar tempSpot = new sVar("T0", tempName, node.children.get(1).name, node.children.get(1).scope, staticNum);
        staticTable.add(tempSpot);
        staticNum = staticNum + 1;
          
        opCode[curPos] = "T0";
        curPos = curPos +1;
          
        opCode[curPos] = tempName;
        curPos = curPos+1;
      }
        
      staticNum = staticNum+1;
      
    }
      
    else if (node.tokType.equals("Block")){
      currentScope = scopeTotal + 1;
      /*
      if(blockpos != -99){
        blocklist bbb = new blocklist(node.children.size(), -99, currentScope);
        blockTable.add(bbb);
        blockpos = 0;
      }
      else{
        blocklist bbb = new blocklist(node.children.size(), blockpos, currentScope);
        blockTable.add(bbb);
        blockpos = blockTable.size()-1;
      }
      */
    }
    
    else if(node.tokType.equals("Assignment")){
      opCode[curPos] = "A9";
      curPos = curPos +1;
        
      if(!node.children.get(0).tokType.equals("String")){
          
        if(node.children.get(0).tokType.equals("Boolean")){
            
          if(node.children.get(1).tokType.equals("True")){
            opCode[curPos] = "01";
            curPos = curPos+1;
          }
          else{
            opCode[curPos] = "00";
            curPos = curPos+1;
          }
        }
        
        else{
          if(node.children.get(1).tokType.equals("Add")){
            //????
          }
          else{
            String numName = "0" + node.children.get(1).name;
            opCode[curPos] = numName;
            curPos = curPos + 1;
          }
        }
          
        opCode[curPos] = "8D";
        curPos = curPos + 1;
          
        String temp1 = "";
        String temp2 = "";
        for(int i = 0; i < staticTable.size()-1; i++){
          if(staticTable.get(i).id.equals(node.children.get(1).name) && staticTable.get(i).scope == currentScope){
             temp1 = staticTable.get(i).temp;
             temp2 = staticTable.get(i).temp2;
          }
        }
          
        opCode[curPos] = temp1;
        curPos = curPos+1;
        opCode[curPos] = temp2;
        curPos = curPos +1;
      }
       
      //STRING ASSIGNMENT
      else{
        
      }
    }
      
    else if(node.tokType.equals("Print")){
      opCode[curPos] = "A2";
      curPos = curPos+1;
      
      if(!node.children.get(0).tokType.equals("String")){
        opCode[curPos] = "01";
        curPos = curPos+1;
      }
      else{
        opCode[curPos] = "02";
        curPos = curPos+1;
      }
        
      opCode[curPos] = "AC";
      curPos = curPos+1;
      
      String temp1 = "";
      String temp2 = "";
      for(int i = 0; i < staticTable.size()-1; i++){
        if(staticTable.get(i).id.equals(node.children.get(1).name) && staticTable.get(i).scope == currentScope){
          temp1 = staticTable.get(i).temp;
          temp2 = staticTable.get(i).temp2;
        }
      }
          
      opCode[curPos] = temp1;
      curPos = curPos+1;
      opCode[curPos] = temp2;
      curPos = curPos +1;
        
      opCode[curPos] = "FF";
      curPos = curPos + 1;
    }
      
      
      
      
      
      
    if(node.children.size() != 0 && !stop){
      for (int j = 0; j < node.children.size(); j++){
        generate(node.children.get(j));
      }
    }
      
    /*
    if(node.parent.tokType.equals("Block")){
      blockTable.get(blockpos).curNum = blockTable.get(blockpos).curNum +1;
      if(blockTable.get(blockpos).curNum == blockTable.get(blockpos).childNum){
        currentScope = blockTable.get(blockpos).scope;
        if(blockTable.get(blockpos).prevBlock == -99){
          blockpos = blockTable.get(blockpos).prevBlock;
        }
      }
    }
    */
  }
    
  public void backPatch(){
    for(int i = 0; i < staticTable.size(); i++){
      staticTable.get(i).realP = Integer.toHexString(curPos);
      curPos = curPos+1;
    }
      
    for(int i = 0; i < staticTable.size(); i++){
      for(int j = 0; j < opCode.length; j++){
        if(staticTable.get(i).temp.equals(opCode[j])){
          if(staticTable.get(i).temp2.equals(opCode[j+1])){
            opCode[j] = staticTable.get(i).realP;
            opCode[j+1] = "00";
          }
        }
      }
    }
  }
    
  
  
}

class sVar{
  String temp = "";
  String temp2 = "";
  String realP = "";
  int scope = 0;
  String id = "";
  int offset = 0;
    
  public sVar(){};

  public sVar(String name, String name2, String ident, int scopee, int offs){
    temp = name;
    temp2 = name2;
    scope = scopee;
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

class blocklist{
  int childNum = 0;
  int curNum = 0;
  int scope = 0;
  int prevBlock = -99;
    
  public blocklist(){};
    
  public blocklist(int cnum, int bnum, int scopee){
    childNum = cnum;
    prevBlock = bnum;
    scope = scopee;
  }
}