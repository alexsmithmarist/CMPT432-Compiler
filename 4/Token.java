public class Token{
  //Token attributes
  int lineNum;
  int indexNum;
  String name;
  int value;
  String type;

  //Default Constructor
  public Token(){}
  
  //Used for tokens that do not require names or values such as Parens, Brackets, while, etc.
  public Token(String type, int lineNum, int indexNum){
    this.type = type;
    this.lineNum = lineNum;
    this.indexNum = indexNum;
  }

  //Used for tokens that require values such as numbers
  public Token(String type, int lineNum, int indexNum, int value){
    this.type = type;
    this.lineNum = lineNum;
    this.indexNum = indexNum;
    this.value = value;
  }

  //Used for tokens that require names such as identifiers
  public Token(String type, int lineNum, int indexNum, String name){
    this.type = type;
    this.lineNum = lineNum;
    this.indexNum = indexNum;
    this.name = name;
  }

}