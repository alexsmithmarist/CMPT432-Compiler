import java.util.*;

public class codeGen{
  String[] opCode = new String[255];
  ArrayList<sVar> staticTable = new ArrayList<sVar>();
  ArrayList<jVar> jumpTable = new ArrayList<jVar>();
  ArrayList<blocklist> blockTable = new ArrayList<blocklist>();
  ArrayList<stringPlace> wordList = new ArrayList<stringPlace>();
  int curPos = 0;
  int staticNum = 0;
  int jumpNum = 0;
  int currentScope = -1;
  int parent = -1;
  int scopeTotal = -1;
  int blockpos = -99;
  boolean ignoreNext = false;
  int stringPos = 254;
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
        sVar tempSpot;
        if(node.children.get(0).tokType.equals("String")){
          tempSpot = new sVar(tempName, "XX", node.children.get(1).name, node.children.get(1).scope, staticNum, true);
        }
        else{
          tempSpot = new sVar(tempName, "XX", node.children.get(1).name, node.children.get(1).scope, staticNum, false);
        }
        staticTable.add(tempSpot);
        staticNum = staticNum + 1;
          
        opCode[curPos] = tempName;
        curPos = curPos +1;
          
        opCode[curPos] = "XX";
        curPos = curPos+1;
      }
      else{
        String tempName = Integer.toString(staticNum);
        sVar tempSpot;
        if(node.children.get(0).tokType.equals("String")){
          tempSpot = new sVar("T0", tempName, node.children.get(1).name, node.children.get(1).scope, staticNum, true);
        }
        else{
          tempSpot = new sVar("T0", tempName, node.children.get(1).name, node.children.get(1).scope, staticNum, false);
        }
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
        
      //System.out.println(node.children.get(1).tokType);
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
          this.addInt(node.children.get(1), true, "", "");
          ignoreNext = true;
        }
        else if(node.children.get(1).tokType.equals("Digit")){
          String numName = "0" + node.children.get(1).name;
          opCode[curPos] = numName;
          curPos = curPos + 1;
        }
        
        else if(node.children.get(1).tokType.equals("String")){
          String word = node.children.get(1).name;
          boolean skip = false;
            
          for(int i =0; i < wordList.size(); i++){
            if(wordList.get(i).word.equals(word)){
              opCode[curPos] = wordList.get(i).place;
              curPos = curPos+1;
              skip = true;
            }
          }
         
          if(!skip){
            stringPos = stringPos - word.length();
            int tempo = stringPos;
          
            for(int i = 0; i < word.length(); i++){
              char temp = word.charAt(i);
              opCode[stringPos] = this.toHex((int) temp);
              stringPos = stringPos + 1;
            }
            
            opCode[stringPos] = "00";
            opCode[curPos] = this.toHex(tempo);
            
            stringPlace storage = new stringPlace(word);
            storage.place = this.toHex(tempo);
            wordList.add(storage);
          
            curPos = curPos+1;
            stringPos = tempo;
            
          }
            
          skip = false;
          
        }
      
          
        opCode[curPos] = "8D";
        curPos = curPos + 1;
          
        int tempScope = -99;
        boolean found = false;
        String temp1 = "";
        String temp2 = "";
        for(int i = 0; i < staticTable.size(); i++){
            
          if(staticTable.get(i).id != null){
          if(staticTable.get(i).id.equals(node.children.get(0).name)){
            tempScope = staticTable.get(i).scope;
            if(staticTable.get(i).scope == currentScope){
              found = true;
              temp1 = staticTable.get(i).temp;
              temp2 = staticTable.get(i).temp2;
            }
          }
          }
        }
          
        if(!found && tempScope != -99){
        
          if(staticNum < 10){ 
            String tempName = "T" + staticNum;
            temp1 = tempName;
            temp2 = "XX";
            sVar tempSpot;
            if(node.children.get(1).tokType.equals("String")){
              tempSpot = new sVar(tempName, "XX", node.children.get(0).name, currentScope, staticNum, true);
            }
            else{
              tempSpot = new sVar(tempName, "XX", node.children.get(0).name, currentScope, staticNum, false);
            }
            staticTable.add(tempSpot);
            
            staticNum = staticNum + 1;
          }

          else{
            String tempName = Integer.toString(staticNum);
            temp1 = "T0";
            temp2 = tempName;
            sVar tempSpot;
            if(node.children.get(1).tokType.equals("String")){
              tempSpot = new sVar("T0", tempName, node.children.get(0).name, currentScope, staticNum, true);
            }
            else{
              tempSpot = new sVar("T0", tempName, node.children.get(0).name, currentScope, staticNum, false);
            }
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
          
          if(staticTable.get(i).id != null){
          if(staticTable.get(i).id.equals(node.children.get(0).name)){
            if(!first){
              hold = staticTable.get(i);
              first = true;
            }
            if(staticTable.get(i).scope == currentScope){
              found = true;
              temp1 = staticTable.get(i).temp;
              temp2 = staticTable.get(i).temp2;
              hold = staticTable.get(i);
            }
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
        System.out.println(node.children.get(0).tru);
        if(hold.string == false){
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
      
    else if(node.tokType.equals("If")){
      if(node.children.get(0).tokType.equals("Equal")){
          
        if(node.children.get(0).children.get(0).tokType.equals("Id") && node.children.get(0).children.get(1).tokType.equals("Id")){
          opCode[curPos] = "AE";
          curPos = curPos + 1;
            
          if(node.children.get(0).children.get(0).tokType.equals("Id")){
            String temp1 = "";
            String temp2 = "";
            boolean found = false;
            boolean first = false;
            sVar hold = null;
            for(int i = 0; i < staticTable.size(); i++){
          
              if(staticTable.get(i).id.equals(node.children.get(0).children.get(0).name)){
                if(!first){
                  hold = staticTable.get(i);
                  first = true;
                }
                if(staticTable.get(i).scope == currentScope){
                  found = true;
                  temp1 = staticTable.get(i).temp;
                  temp2 = staticTable.get(i).temp2;
                  hold = staticTable.get(i);
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
          }
            
          opCode[curPos] = "EC";
          curPos = curPos + 1;
            
          if(node.children.get(0).children.get(1).tokType.equals("Id")){
            String temp1 = "";
            String temp2 = "";
            boolean found = false;
            boolean first = false;
            sVar hold = null;
            for(int i = 0; i < staticTable.size(); i++){
          
              if(staticTable.get(i).id.equals(node.children.get(0).children.get(1).name)){
                if(!first){
                  hold = staticTable.get(i);
                  first = true;
                }
                if(staticTable.get(i).scope == currentScope){
                  found = true;
                  temp1 = staticTable.get(i).temp;
                  temp2 = staticTable.get(i).temp2;
                  hold = staticTable.get(i);
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
          }
            
          opCode[curPos] = "D0";
          curPos = curPos+1;
          
          String tempName = "J" + jumpNum;
          jVar tempSpot;
          tempSpot = new jVar(tempName);
          jumpTable.add(tempSpot);
          jumpNum = jumpNum + 1;
          
          opCode[curPos] = tempName;
          int lengthTrack = curPos;
          curPos = curPos +1;
          
          this.generate(node.children.get(1));
          ignoreNext = true;
            
          tempSpot.setLength(curPos - lengthTrack);
        }
          
        else if(node.children.get(0).children.get(0).tokType.equals("Id") || node.children.get(0).children.get(1).tokType.equals("Id")){
          if(node.children.get(0).children.get(0).tokType.equals("Id")){
            if(!node.children.get(0).children.get(1).tokType.equals("String")){
              if(node.children.get(0).children.get(1).tokType.equals("Digit")){
                opCode[curPos] = "A2";
                curPos = curPos+1;
                
                String numName = "0" + node.children.get(0).children.get(1).name;
                opCode[curPos] = numName;
                curPos = curPos + 1;
              }
              
              else if(node.children.get(0).children.get(1).tokType.equals("True")){
                opCode[curPos] = "A2";
                curPos = curPos+1;
                
                opCode[curPos] = "01";
                curPos=curPos+1;
              }
              
              else if(node.children.get(0).children.get(1).tokType.equals("False")){
                opCode[curPos] = "A2";
                curPos = curPos+1;
                
                opCode[curPos] = "00";
                curPos=curPos+1;
              }
                
              opCode[curPos] = "EC";
              curPos = curPos+1;
              
              String temp1 = "";
              String temp2 = "";
              boolean found = false;
              boolean first = false;
              sVar hold = null;
              for(int i = 0; i < staticTable.size(); i++){
          
                if(staticTable.get(i).id != null){  
                if(staticTable.get(i).id.equals(node.children.get(0).children.get(0).name)){
                  if(!first){
                    hold = staticTable.get(i);
                    first = true;
                  }
                  if(staticTable.get(i).scope == currentScope){
                    found = true;
                    temp1 = staticTable.get(i).temp;
                    temp2 = staticTable.get(i).temp2;
                    hold = staticTable.get(i);
                  }
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
                
              opCode[curPos] = "D0";
              curPos = curPos+1;
          
              String tempName = "J" + jumpNum;
             jVar tempSpot;
             tempSpot = new jVar(tempName);
             jumpTable.add(tempSpot);
             jumpNum = jumpNum + 1;
          
             opCode[curPos] = tempName;
             int lengthTrack = curPos;
             curPos = curPos +1;
          
             this.generate(node.children.get(1));
             ignoreNext = true;
            
             tempSpot.setLength(curPos - lengthTrack);
            }
            else{
              opCode[curPos] = "A2";
              curPos=curPos+1;
                
              boolean sfound = false;
                
              for(int i =0; i < wordList.size(); i++){
                if(wordList.get(i).word.equals(node.children.get(0).children.get(1).name)){
                  opCode[curPos] = wordList.get(i).place;
                  curPos = curPos+1;
                  sfound = true;
                }
              }
                
              if(!sfound){
                opCode[curPos] = "FF";
                curPos = curPos+1;
              }
                
              opCode[curPos] = "EC";
              curPos = curPos+1;
              
              String temp1 = "";
              String temp2 = "";
              boolean found = false;
              boolean first = false;
              sVar hold = null;
              for(int i = 0; i < staticTable.size(); i++){
          
                if(staticTable.get(i).id.equals(node.children.get(0).children.get(0).name)){
                  if(!first){
                    hold = staticTable.get(i);
                    first = true;
                  }
                  if(staticTable.get(i).scope == currentScope){
                    found = true;
                    temp1 = staticTable.get(i).temp;
                    temp2 = staticTable.get(i).temp2;
                    hold = staticTable.get(i);
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
                
              opCode[curPos] = "D0";
              curPos = curPos+1;
          
              String tempName = "J" + jumpNum;
             jVar tempSpot;
             tempSpot = new jVar(tempName);
             jumpTable.add(tempSpot);
             jumpNum = jumpNum + 1;
          
             opCode[curPos] = tempName;
             int lengthTrack = curPos;
             curPos = curPos +1;
          
             this.generate(node.children.get(0).children.get(2));
             ignoreNext = true;
            
             tempSpot.setLength(curPos - lengthTrack);
                
            }
              
          }
            
          else{
            if(!node.children.get(0).children.get(0).tokType.equals("String")){
              if(node.children.get(0).children.get(0).tokType.equals("Digit")){
                opCode[curPos] = "A2";
                curPos = curPos+1;
                
                String numName = "0" + node.children.get(0).children.get(0).name;
                opCode[curPos] = numName;
                curPos = curPos + 1;
              }
              
              else if(node.children.get(0).children.get(0).tokType.equals("True")){
                opCode[curPos] = "A2";
                curPos = curPos+1;
                
                opCode[curPos] = "01";
                curPos=curPos+1;
              }
              
              else if(node.children.get(0).children.get(0).tokType.equals("False")){
                opCode[curPos] = "A2";
                curPos = curPos+1;
                
                opCode[curPos] = "00";
                curPos=curPos+1;
              }
                
              opCode[curPos] = "EC";
              curPos = curPos+1;
              
              String temp1 = "";
              String temp2 = "";
              boolean found = false;
              boolean first = false;
              sVar hold = null;
              for(int i = 0; i < staticTable.size(); i++){
          
                if(staticTable.get(i).id.equals(node.children.get(0).children.get(1).name)){
                  if(!first){
                    hold = staticTable.get(i);
                    first = true;
                  }
                  if(staticTable.get(i).scope == currentScope){
                    found = true;
                    temp1 = staticTable.get(i).temp;
                    temp2 = staticTable.get(i).temp2;
                    hold = staticTable.get(i);
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
                
              opCode[curPos] = "D0";
              curPos = curPos+1;
          
              String tempName = "J" + jumpNum;
             jVar tempSpot;
             tempSpot = new jVar(tempName);
             jumpTable.add(tempSpot);
             jumpNum = jumpNum + 1;
          
             opCode[curPos] = tempName;
             int lengthTrack = curPos;
             curPos = curPos +1;
          
             this.generate(node.children.get(1));
             ignoreNext = true;
            
             tempSpot.setLength(curPos - lengthTrack);
            }
            else{
              opCode[curPos] = "A2";
              curPos=curPos+1;
                
              boolean sfound = false;
                
              for(int i =0; i < wordList.size(); i++){
                if(wordList.get(i).word.equals(node.children.get(0).children.get(0).name)){
                  opCode[curPos] = wordList.get(i).place;
                  curPos = curPos+1;
                  sfound = true;
                }
              }
                
              if(!sfound){
                opCode[curPos] = "FF";
                curPos = curPos+1;
              }
                
              opCode[curPos] = "EC";
              curPos = curPos+1;
              
              String temp1 = "";
              String temp2 = "";
              boolean found = false;
              boolean first = false;
              sVar hold = null;
              for(int i = 0; i < staticTable.size(); i++){
          
                if(staticTable.get(i).id.equals(node.children.get(0).children.get(1).name)){
                  if(!first){
                    hold = staticTable.get(i);
                    first = true;
                  }
                  if(staticTable.get(i).scope == currentScope){
                    found = true;
                    temp1 = staticTable.get(i).temp;
                    temp2 = staticTable.get(i).temp2;
                    hold = staticTable.get(i);
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
                
              opCode[curPos] = "D0";
              curPos = curPos+1;
          
              String tempName = "J" + jumpNum;
             jVar tempSpot;
             tempSpot = new jVar(tempName);
             jumpTable.add(tempSpot);
             jumpNum = jumpNum + 1;
          
             opCode[curPos] = tempName;
             int lengthTrack = curPos;
             curPos = curPos +1;
          
             this.generate(node.children.get(1));
             ignoreNext = true;
            
             tempSpot.setLength(curPos - lengthTrack);
                
            } 
          }
        }
          
        else{
            
          String holdTemp = "";
          String holdTemp2 = "";
          opCode[curPos] = "A9";
          curPos = curPos+1;
            
          if(node.children.get(0).children.get(0).tokType.equals("Digit")){
            String numName = "0" + node.children.get(0).children.get(0).name;
            opCode[curPos] = numName;
            curPos = curPos + 1;
          }
          else if(node.children.get(0).children.get(0).tokType.equals("True")){
            opCode[curPos] = "01";
            curPos = curPos+1;
          }
          else if(node.children.get(0).children.get(0).tokType.equals("False")){
            opCode[curPos] = "00";
            curPos = curPos+1;
          }
          else if(node.children.get(0).children.get(0).tokType.equals("Add")){
            this.addInt(node.children.get(0).children.get(0), true, "", "");
            //ignoreNext = true;
          }
          else if(node.children.get(0).children.get(0).tokType.equals("String")){
            String word = node.children.get(0).children.get(0).name;
            boolean skip = false;
            
            for(int i =0; i < wordList.size(); i++){
              if(wordList.get(i).word.equals(word)){
                opCode[curPos] = wordList.get(i).place;
                curPos = curPos+1;
                skip = true;
              }
            }
         
            if(!skip){
              stringPos = stringPos - word.length();
              int tempo = stringPos;
          
              for(int i = 0; i < word.length(); i++){
                char temp = word.charAt(i);
                opCode[stringPos] = this.toHex((int) temp);
                stringPos = stringPos + 1;
              }
            
              opCode[stringPos] = "00";
              opCode[curPos] = this.toHex(tempo);
            
              stringPlace storage = new stringPlace(word);
              storage.place = this.toHex(tempo);
              wordList.add(storage);
          
              curPos = curPos+1;
              stringPos = tempo;
              
            }
            
            skip = false;
          }
            
          opCode[curPos] = "8D";
          curPos = curPos+1;
            
          if(staticNum < 10){ 
            String tempName = "T" + staticNum;
            sVar tempSpot;
            if(node.children.get(0).children.get(0).tokType.equals("String")){
              tempSpot = new sVar(tempName, "XX", node.children.get(0).children.get(0).name, node.children.get(0).children.get(0).scope, staticNum, true);
            }
            else{
              tempSpot = new sVar(tempName, "XX", node.children.get(0).children.get(0).name, node.children.get(0).children.get(0).scope, staticNum, false);
            }
            staticTable.add(tempSpot);
            staticNum = staticNum + 1;
          
            opCode[curPos] = tempName;
            holdTemp = tempName;
            curPos = curPos +1;
          
            opCode[curPos] = "XX";
            holdTemp2 = "XX";
            curPos = curPos+1;
          }
          else{
            String tempName = Integer.toString(staticNum);
            sVar tempSpot;
            if(node.children.get(0).children.get(0).tokType.equals("String")){
             tempSpot = new sVar("T0", tempName, node.children.get(0).children.get(0).name, node.children.get(0).children.get(0).scope, staticNum, true);
            }
            else{
              tempSpot = new sVar("T0", tempName, node.children.get(0).children.get(0).name, node.children.get(0).children.get(0).scope, staticNum, false);
            }
            staticTable.add(tempSpot);
            staticNum = staticNum + 1;
          
            opCode[curPos] = "T0";
            holdTemp = "T0";
            curPos = curPos +1;
          
            opCode[curPos] = tempName;
            holdTemp2 = tempName;
            curPos = curPos+1;
          }
        
          staticNum = staticNum+1;
            
          if(!node.children.get(0).children.get(1).tokType.equals("String")){
              if(node.children.get(0).children.get(1).tokType.equals("Digit")){
                opCode[curPos] = "A2";
                curPos = curPos+1;
                
                String numName = "0" + node.children.get(0).children.get(1).name;
                opCode[curPos] = numName;
                curPos = curPos + 1;
              }
              
              else if(node.children.get(0).children.get(1).tokType.equals("True")){
                opCode[curPos] = "A2";
                curPos = curPos+1;
                
                opCode[curPos] = "01";
                curPos=curPos+1;
              }
              
              else if(node.children.get(0).children.get(1).tokType.equals("False")){
                opCode[curPos] = "A2";
                curPos = curPos+1;
                
                opCode[curPos] = "00";
                curPos=curPos+1;
              }
                
              opCode[curPos] = "EC";
              curPos = curPos+1;
              
          
              opCode[curPos] = holdTemp;
              curPos = curPos+1;
              opCode[curPos] = holdTemp2;
              curPos = curPos +1;
                
              opCode[curPos] = "D0";
              curPos = curPos+1;
          
              String tempName = "J" + jumpNum;
             jVar tempSpot;
             tempSpot = new jVar(tempName);
             jumpTable.add(tempSpot);
             jumpNum = jumpNum + 1;
          
             opCode[curPos] = tempName;
             int lengthTrack = curPos;
             curPos = curPos +1;
          
             this.generate(node.children.get(1));
             ignoreNext = true;
            
             tempSpot.setLength(curPos - lengthTrack);
            }
            else{
              opCode[curPos] = "A2";
              curPos=curPos+1;
                
              boolean sfound = false;
                
              for(int i =0; i < wordList.size(); i++){
                if(wordList.get(i).word.equals(node.children.get(0).children.get(1).name)){
                  opCode[curPos] = wordList.get(i).place;
                  curPos = curPos+1;
                  sfound = true;
                }
              }
                
              if(!sfound){
                opCode[curPos] = "FF";
                curPos = curPos+1;
              }
                
              opCode[curPos] = "EC";
              curPos = curPos+1;
          
              opCode[curPos] = holdTemp;
              curPos = curPos+1;
              opCode[curPos] = holdTemp2;
              curPos = curPos +1;
                
              opCode[curPos] = "D0";
              curPos = curPos+1;
          
              String tempName = "J" + jumpNum;
             jVar tempSpot;
             tempSpot = new jVar(tempName);
             jumpTable.add(tempSpot);
             jumpNum = jumpNum + 1;
          
             opCode[curPos] = tempName;
             int lengthTrack = curPos;
             curPos = curPos +1;
          
             this.generate(node.children.get(0).children.get(2));
             ignoreNext = true;
            
             tempSpot.setLength(curPos - lengthTrack);
                
            }
        }
          
          
      }
        
      else if(node.children.get(0).tokType.equals("True")){
        String holdTemp = "";
        String holdTemp2 = "";
        
        opCode[curPos] = "A9";
        curPos = curPos+1;
          
        opCode[curPos] = "01";
        curPos = curPos+1;
          
        opCode[curPos] = "8D";
        curPos = curPos+1;
          
        if(staticNum < 10){ 
          String tempName = "T" + staticNum;
          sVar tempSpot;
          if(node.children.get(0).tokType.equals("String")){
            tempSpot = new sVar(tempName, "XX", node.children.get(0).name, node.children.get(0).scope, staticNum, true);
          }
          else{
            tempSpot = new sVar(tempName, "XX", node.children.get(0).name, node.children.get(0).scope, staticNum, false);
          }
          staticTable.add(tempSpot);
          staticNum = staticNum + 1;
          
          opCode[curPos] = tempName;
          holdTemp = tempName;
          curPos = curPos +1;
          
          opCode[curPos] = "XX";
          holdTemp2 = "XX";
          curPos = curPos+1;
        }
        else{
          String tempName = Integer.toString(staticNum);
          sVar tempSpot;
          if(node.children.get(0).tokType.equals("String")){
            tempSpot = new sVar("T0", tempName, node.children.get(0).name, node.children.get(0).scope, staticNum, true);
          }
          else{
            tempSpot = new sVar("T0", tempName, node.children.get(0).name, node.children.get(0).scope, staticNum, false);
          }
          staticTable.add(tempSpot);
          staticNum = staticNum + 1;
          
          opCode[curPos] = "T0";
          holdTemp = "T0";
          curPos = curPos +1;
          
          opCode[curPos] = tempName;
          holdTemp2 = tempName;
          curPos = curPos+1;
        }
        
        staticNum = staticNum+1;
          
        opCode[curPos] = "A2";
        curPos = curPos+1;
          
        opCode[curPos] = "01";
        curPos = curPos+1;
          
        opCode[curPos] = "EC";
        curPos = curPos+1;
          
        opCode[curPos] = holdTemp;
        curPos = curPos+1;
        opCode[curPos] = holdTemp2;
        curPos = curPos+1;
          
        opCode[curPos] = "D0";
        curPos = curPos+1;
          
        String tempName = "J" + jumpNum;
        jVar tempSpot;
        tempSpot = new jVar(tempName);
        jumpTable.add(tempSpot);
        jumpNum = jumpNum + 1;
          
        opCode[curPos] = tempName;
        int lengthTrack = curPos;
        curPos = curPos +1;
          
        this.generate(node.children.get(1));
        ignoreNext = true;
            
        tempSpot.setLength(curPos - lengthTrack);
                
      }
        
      else if(node.children.get(0).tokType.equals("False")){
        String holdTemp = "";
        String holdTemp2 = "";
        
        opCode[curPos] = "A9";
        curPos = curPos+1;
          
        opCode[curPos] = "00";
        curPos = curPos+1;
          
        opCode[curPos] = "8D";
        curPos = curPos+1;
          
        if(staticNum < 10){ 
          String tempName = "T" + staticNum;
          sVar tempSpot;
          if(node.children.get(0).tokType.equals("String")){
            tempSpot = new sVar(tempName, "XX", node.children.get(0).name, node.children.get(0).scope, staticNum, true);
          }
          else{
            tempSpot = new sVar(tempName, "XX", node.children.get(0).name, node.children.get(0).scope, staticNum, false);
          }
          staticTable.add(tempSpot);
          staticNum = staticNum + 1;
          
          opCode[curPos] = tempName;
          holdTemp = tempName;
          curPos = curPos +1;
          
          opCode[curPos] = "XX";
          holdTemp2 = "XX";
          curPos = curPos+1;
        }
        else{
          String tempName = Integer.toString(staticNum);
          sVar tempSpot;
          if(node.children.get(0).tokType.equals("String")){
            tempSpot = new sVar("T0", tempName, node.children.get(0).name, node.children.get(0).scope, staticNum, true);
          }
          else{
            tempSpot = new sVar("T0", tempName, node.children.get(0).name, node.children.get(0).scope, staticNum, false);
          }
          staticTable.add(tempSpot);
          staticNum = staticNum + 1;
          
          opCode[curPos] = "T0";
          holdTemp = "T0";
          curPos = curPos +1;
          
          opCode[curPos] = tempName;
          holdTemp2 = tempName;
          curPos = curPos+1;
        }
        
        staticNum = staticNum+1;
          
        opCode[curPos] = "A2";
        curPos = curPos+1;
          
        opCode[curPos] = "01";
        curPos = curPos+1;
          
        opCode[curPos] = "EC";
        curPos = curPos+1;
          
        opCode[curPos] = holdTemp;
        curPos = curPos+1;
        opCode[curPos] = holdTemp2;
        curPos = curPos+1;
          
        opCode[curPos] = "D0";
        curPos = curPos+1;
          
        String tempName = "J" + jumpNum;
        jVar tempSpot;
        tempSpot = new jVar(tempName);
        jumpTable.add(tempSpot);
        jumpNum = jumpNum + 1;
          
        opCode[curPos] = tempName;
        int lengthTrack = curPos;
        curPos = curPos +1;
          
        this.generate(node.children.get(1));
        ignoreNext = true;
            
        tempSpot.setLength(curPos - lengthTrack);
                
      }
    }
    
      
      
      
      
      
      
    if(node.children.size() != 0 && !ignoreNext){
      for (int j = 0; j < node.children.size(); j++){
        generate(node.children.get(j));
      }
      ignoreNext = false;
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
    opCode[curPos] = "00";
    curPos = curPos+1;
    opCode[curPos] = "00";
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
      
    for(int i = 0; i < jumpTable.size(); i++){
      jumpTable.get(i).replace = this.toHex(jumpTable.get(i).length);
    }
      
    for(int i = 0; i < jumpTable.size(); i++){
      for(int j = 0; j < opCode.length; j++){
        if(jumpTable.get(i).temp.equals(opCode[j])){
          opCode[j] = jumpTable.get(i).replace;
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
      if(i == 254){
        System.out.println(line);
      }
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
    
  public void addInt (CSTNode node, boolean first, String temp1, String temp2){
      
    String holdTemp = "";
    String holdTemp2 = "";
      
    if(!node.children.get(1).tokType.equals("Id")){
      if(first){
        String numName = "0" + node.children.get(0).name;
        opCode[curPos] = numName;
        curPos = curPos + 1;
      
    
        opCode[curPos] = "8D";
        curPos = curPos + 1;
        
        if(staticNum < 10){ 
          String tempName = "T" + staticNum;
          sVar tempSpot;
        
          tempSpot = new sVar(tempName, "XX", node.children.get(1).name, node.children.get(1).scope, staticNum, false);
          staticTable.add(tempSpot);
          staticNum = staticNum + 1;
          
          opCode[curPos] = tempName;
          holdTemp = tempName;
          curPos = curPos +1;
          
          opCode[curPos] = "XX";
          holdTemp2 = "XX";
          curPos = curPos+1;
        }
        else{
          String tempName = Integer.toString(staticNum);
          sVar tempSpot;
          tempSpot = new sVar("T0", tempName, node.children.get(1).name, node.children.get(1).scope, staticNum, false);
        
          staticTable.add(tempSpot);
          staticNum = staticNum + 1;
          
          opCode[curPos] = "T0";
          holdTemp = "T0";
          curPos = curPos +1;
          
          opCode[curPos] = tempName;
          holdTemp2 = tempName;
          curPos = curPos+1;
        }
        
      }
      
      if(node.children.get(1).tokType.equals("Add")){
        opCode[curPos] = "A9";
        curPos = curPos+1;
        
        String numName = "0" + node.children.get(1).children.get(0).name;
        opCode[curPos] = numName;
        curPos = curPos + 1;
      
        opCode[curPos] = "6D";
        curPos = curPos+1;
        
        opCode[curPos] = holdTemp;
        curPos = curPos+1;
        opCode[curPos] = holdTemp2;
        curPos = curPos+1;
          
        opCode[curPos] = "8D";
        curPos = curPos+1;
          
        opCode[curPos] = holdTemp;
        curPos = curPos+1;
        opCode[curPos] = holdTemp2;
        curPos = curPos+1;
          
        this.addInt(node.children.get(1), false, holdTemp, holdTemp2);
      }
      else{
        opCode[curPos] = "A9";
        curPos = curPos+1;
        
        String numName = "0" + node.children.get(1).name;
        opCode[curPos] = numName;
        curPos = curPos + 1;
      
        opCode[curPos] = "6D";
        curPos = curPos+1;
        
        if(!first){
          opCode[curPos] = temp1;
          curPos = curPos+1;
          opCode[curPos] = temp2;
          curPos = curPos+1;
        }
        else{
          opCode[curPos] = holdTemp;
          curPos = curPos+1;
          opCode[curPos] = holdTemp2;
          curPos = curPos+1;
        }
      }
    }
      
    else{
      if(!first){
        opCode[curPos] = "A9";
        curPos = curPos +1;
      }
      String numName = "0" + node.children.get(0).name;
      opCode[curPos] = numName;
      curPos = curPos + 1;
        
      opCode[curPos] = "6D";
      curPos = curPos+1;
        
      String temp12 = "";
      String temp22 = "";
      boolean found = false;
      boolean first2 = false;
      sVar hold = null;
      for(int i = 0; i < staticTable.size(); i++){
          
        if(staticTable.get(i).id != null){
        if(staticTable.get(i).id.equals(node.children.get(1).name)){
          if(!first){
            hold = staticTable.get(i);
            first2 = true;
          }
          if(staticTable.get(i).scope == currentScope){
            found = true;
            temp12 = staticTable.get(i).temp;
            temp22 = staticTable.get(i).temp2;
            hold = staticTable.get(i);
          }
        }
        }
      }
        
      if(!found){
        if(first2){
          temp12 = hold.temp;
          temp22 = hold.temp2;
        }
      }
         
      if(!first){
        opCode[curPos] = temp1;
        curPos = curPos+1;
        opCode[curPos] = temp2;
        curPos = curPos +1;
      }
      else{
        opCode[curPos] = temp12;
        curPos = curPos+1;
        opCode[curPos] = temp22;
        curPos = curPos +1;
      }
        
        
    }
  }
    
  
  
}

class sVar{
  String temp = "";
  String temp2 = "";
  String realP = "";
  boolean string = false;
  int scope = 0;
  String id = "";
  int offset = 0;
    
  public sVar(){};

  public sVar(String name, String name2, String ident, int scopee, int offs, boolean stin){
    temp = name;
    temp2 = name2;
    scope = scopee;
    id = ident;
    offset = offs;
    string = stin;
  }
}

class jVar{
  String temp = "";
  int length = 0;
  String replace = "";
    
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

class stringPlace{
  String word = "";
  String place = "";
    
  public stringPlace(){};
    
  public stringPlace(String name){
    word = name;
  }
}