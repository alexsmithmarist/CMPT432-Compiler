import java.util.*;

public class codeGen{
  String[] opCode = new String[255];
  ArrayList<sVar> staticTable = new ArrayList<sVar>();
  ArrayList<jVar> jumpTable = new ArrayList<jVar>();
  int curPos = 0;
    
  public codeGen(){};
  
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