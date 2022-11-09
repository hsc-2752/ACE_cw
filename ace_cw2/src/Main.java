import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String input = null;
        Scanner scan = new Scanner(System.in);
        if (scan.hasNextLine()) {
            input = scan.nextLine();
        }
        scan.close();
        System.out.println(convert(input));
    }

    public static String convert(String input){
        //repeat until remain unchanged
        String s_last = input;
        String s_current = treeToString(DNF(toTree(input)));
        while (!s_last.equals(s_current)){
            s_last = s_current;
            s_current = treeToString(DNF(toTree(s_last)));
        }

        return(s_current);
    }

    public static Node DNF(Node n){//convert to DNF
         if (n.isExternal()){return n;}
         n.toDNF();

         if (n.isExternal()){return n;}
         n.setLeftChild(DNF(n.getLeftChild()));

         if (n.isExternal()){return n;}
         n.setRightChild(DNF(n.getRightChild()));

         return n;
    }

    public static Node toTree(String string){
//      count number of paratheis to form a tree
        int leftPara = 0;
        int rightPara = 0;
        Node n;
        int terminate_i =0;

        if (string.charAt(0) == '~'){
            n = toTree(string.substring(2));
            n.isNegated = !(n.isNegated);
            return n;
        }

        for(int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '[') {
                leftPara = leftPara + 1;
            } else if (string.charAt(i) == ']') {
                rightPara = rightPara + 1;
            }
            if (leftPara == rightPara && leftPara > 0) {
                terminate_i = i;
                break;
            }
        }

        // if get a single expression
        if(leftPara == 0){n = new Node(null,string,null); return n;}

        //para == 1 means do not need to remove outer parathesis anymore
        if (leftPara > 1){
            if (terminate_i == string.length()-1){
                n = toTree(string.substring(2,string.length()-2));
            }
            else {
                if (string.charAt(terminate_i+2) == '-'){
                    n = new Node(toTree(string.substring(0,terminate_i+1)),string.substring(terminate_i+2,terminate_i+4),toTree(string.substring(terminate_i+5)));
                    //n = new Node(toTree(string.substring(1,terminate_i)),string.substring(terminate_i+2,terminate_i+4),toTree(string.substring(terminate_i+5,string.length()-1)));
                }
                else {
                    n = new Node(toTree(string.substring(0,terminate_i+1)),string.substring(terminate_i+2,terminate_i+3),toTree(string.substring(terminate_i+4)));
                    //n = new Node(toTree(string.substring(0,terminate_i+1)),string.substring(terminate_i+2,terminate_i+3),toTree(string.substring(terminate_i+4)));
                }
            }
        }
        else{
            if (terminate_i == string.length()-1){
                    n = new Node(null, string.substring(2, terminate_i - 1), null);
            }
            else {
                    if (string.charAt(terminate_i+2) == '-'){
                        n = new Node(toTree(string.substring(0, terminate_i + 1)),string.substring(terminate_i+2,terminate_i+4),toTree(string.substring(terminate_i+5)));
                    }
                    else{
                        n = new Node(toTree(string.substring(0, terminate_i +1)),string.substring(terminate_i+2,terminate_i+3),toTree(string.substring(terminate_i+4)));
                    }
            }
        }
        return n;
    }

    public static String treeToString(Node n){
        //tree traversal to form a string
        String s;
        if (n.isExternal()){
            if (n.isNegated){
                if (n.getElement().equals("T")){
                    s = "F";
                }
                else if(n.getElement().equals("F")){
                    s = "T";
                }
                else{
                    s = "~ [ " + n.getElement()+" ]";
                }
            }
            else {
                s = n.getElement();
            }
        }
        else {
            s = "[ " + treeToString(n.getLeftChild())+" ] " +n.getElement()+" [ " + treeToString(n.getRightChild())+" ]";
        }
        return s;
    }
}
