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
  int parent = -1;
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
      parent = currentScope;
    }
    
    else if(node.tokType.equals("Assignment")){
      opCode[curPos] = "A9";
      curPos = curPos +1;
        
      if(!node.children.get(0).tokType.equals("String")){
        
        if(node.children.get(1).tokType.equals("True") || node.children.get(1).tokType.equals("False") ){
            
          if(node.children.get(1).tokType.equals("True")){
            opCode[curPos] = "01";
            curPos = curPos+1;
          }
          else{
            opCode[curPos] = "00";
            curPos = curPos+1;
          }
        }
        
        else if(node.children.get(1).tokType.equals("Add")){
          //????
        }
        else if(node.children.get(1).tokType.equals("Digit")){
          String numName = "0" + node.children.get(1).name;
          opCode[curPos] = numName;
          curPos = curPos + 1;
        }
      
          
        opCode[curPos] = "8D";
        curPos = curPos + 1;
          
        int tempScope = -99;
        boolean found = false;
        String temp1 = "";
        String temp2 = "";
        for(int i = 0; i < staticTable.size(); i++){
            
          if(staticTable.get(i).id.equals(node.children.get(0).name)){
            tempScope = staticTable.get(i).scope;
            if(staticTable.get(i).scope == currentScope){
              found = true;
              temp1 = staticTable.get(i).temp;
              temp2 = staticTable.get(i).temp2;
            }
          }
        }
          
        if(!found && tempScope != -99){
        
          if(staticNum < 10){ 
            String tempName = "T" + staticNum;
            temp1 = tempName;
            temp2 = "XX";
            sVar tempSpot = new sVar(tempName, "XX", node.children.get(0).name, currentScope, staticNum);
            staticTable.add(tempSpot);
            
            staticNum = staticNum + 1;
          }

          else{
            String tempName = Integer.toString(staticNum);
            temp1 = "T0";
            temp2 = tempName;
            sVar tempSpot = new sVar("T0", tempName, node.children.get(0).name, currentScope, staticNum);
            staticTable.add(tempSpot);
            
            staticNum = staticNum + 1;
    
          }
        
          staticNum = staticNum+1;
            
          opCode[curPos] = temp1;
          curPos = curPos+1;
          opCode[curPos] = temp2;
          curPos = curPos+1;
        }
        
        else{
          
          opCode[curPos] = temp1;
          curPos = curPos+1;
          opCode[curPos] = temp2;
          curPos = curPos +1;
        }
      }
       
      //STRING ASSIGNMENT
      else{
        
      }
    }
      
    else if(node.tokType.equals("Print")){
      if(node.children.get(0).tokType.equals("Id")){
        opCode[curPos] = "AC";
        curPos = curPos+1;
      
        String temp1 = "";
        String temp2 = "";
        boolean found = false;
        boolean first = false;
        sVar hold = null;
        for(int i = 0; i < staticTable.size(); i++){
          
          if(staticTable.get(i).id.equals(node.children.get(0).name)){
            if(!first){
              hold = staticTable.get(i);
              first = true;
            }
            if(staticTable.get(i).scope == currentScope){
              found = true;
              temp1 = staticTable.get(i).temp;
              temp2 = staticTable.get(i).temp2;
            }
          }
        }
        
        if(!found){
          if(first){
            temp1 = hold.temp;
            temp2 = hold.temp2;
          }
        }
          
        opCode[curPos] = temp1;
        curPos = curPos+1;
        opCode[curPos] = temp2;
        curPos = curPos +1;
        
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
      }
        
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
    curPos = curPos+1;
    for(int i = 0; i < staticTable.size(); i++){
      staticTable.get(i).realP = this.toHex(curPos);
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
    
  public void printOp (){
    int nicePrint = 0;
    System.out.println();
    String line = "";
    for(int i = 0; i < opCode.length; i++){
      if(nicePrint == 8){
        System.out.println(line);
        nicePrint = 0;
        line = "";
      }
      line = line + opCode[i] + " ";
      nicePrint = nicePrint+1;
    }
  }
    
  public String toHex(int n){
    char hex[] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    int remainder = 0;
    String theHex = "";
    while(n>0){
      remainder = n%16;
      theHex = hex[remainder]+theHex;
      n = n/16;
    }
      
    if(theHex.length() == 1){
      theHex = "0" + theHex;
    }
    return theHex;
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