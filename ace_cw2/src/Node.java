public class Node{
    private String value;
    private Node leftChild;
    private Node rightChild;
    public boolean isNegated = false;

    Node(Node leftChild, String value, Node rightChild){
        this.setLeftChild(leftChild);
        this.value = value;
        this.setRightChild(rightChild);
    }

    public String getElement() throws IllegalStateException {
        return value;
    }

    public void setElement(String value) throws IllegalStateException {
        this.value = value;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public boolean isExternal(){
        return (leftChild==null && rightChild == null);
    }

    public void Negate(){
        if(isExternal()){return;}
        if (isNegated && getElement().equals("&")){
            setElement("|");
            isNegated = ! isNegated;
            getLeftChild().isNegated = ! getLeftChild().isNegated;
            getRightChild().isNegated = ! getRightChild().isNegated;
        }
        else if (isNegated && getElement().equals("|")){
            setElement("&");
            isNegated = ! isNegated;
            getLeftChild().isNegated = ! getLeftChild().isNegated;
            getRightChild().isNegated = ! getRightChild().isNegated;
        }
        getLeftChild().Negate();
        getRightChild().Negate();
    }

    public void Arrow(){ //n.value = ->
        if (!getElement().equals("->")){return;}
        setElement("|");
        getLeftChild().isNegated = !getLeftChild().isNegated;
        getLeftChild().Arrow();
        getRightChild().Arrow();
    }

    public void Distribute(){
        if (isExternal()){return;}
        Node new_left_node;
        Node new_right_node;
        if(getRightChild().getElement().equals("|") && getElement().equals("&") ){
            new_left_node = new Node(getLeftChild(), "&", getRightChild().getLeftChild());
            new_right_node = new Node(getLeftChild(), "&", getRightChild().getRightChild());
            this.setLeftChild(new_left_node);
            this.setElement("|");
            this.setRightChild(new_right_node);
        }
        else if(getLeftChild().getElement().equals("|")&& getElement().equals("&") ){
            new_left_node = new Node(getLeftChild().getLeftChild(), "&", getRightChild());
            new_right_node = new Node(getLeftChild().getRightChild(), "&", getRightChild());
            this.setLeftChild(new_left_node);
            this.setElement("|");
            this.setRightChild(new_right_node);
        }

        getLeftChild().Distribute();
        getRightChild().Distribute();
    }

    public void Collect(){

        if(isExternal()){return;}
        if(getElement().equals("->")){return;}
        String leftContent = getLeftChild().getElement();
        Node leftLeft = getLeftChild().getLeftChild();
        Node leftRight = getLeftChild().getRightChild();

        String rightContent = getRightChild().getElement();
        Node rightLeft = getRightChild().getLeftChild();
        Node rightRight = getRightChild().getRightChild();

        if (getLeftChild().MyEquals(getRightChild()) && (getLeftChild().isNegated == getRightChild().isNegated)){
            isNegated = (getLeftChild().isNegated && !isNegated) || (!getLeftChild().isNegated && isNegated);
            setLeftChild(leftLeft);
            setElement(leftContent);

            setRightChild(leftRight);
        }
        else if (getLeftChild().MyEquals(getRightChild()) && (getElement().equals("|")) ){

            setLeftChild(null);
            setElement("T");
            setRightChild(null);
        }
        else if (getLeftChild().MyEquals(getRightChild()) && (getElement().equals("&")) ){
            setLeftChild(null);
            setElement("F");
            setRightChild(null);
        }
        else if (getElement().equals("T") && isNegated){
            setLeftChild(null);
            setElement("F");
            setRightChild(null);
        }
        else if (getElement().equals("F") && isNegated){
            setLeftChild(null);
            setElement("T");
            setRightChild(null);
        }
        else if (getElement().equals("|") && (leftContent.equals("T")||rightContent.equals("T"))){
            setLeftChild(null);
            setElement("T");
            setRightChild(null);
        }
        else if (getElement().equals("&") && (leftContent.equals("F")||rightContent.equals("F"))){
            setLeftChild(null);
            setElement("F");
            setRightChild(null);
        }
        else if (getElement().equals("&") && (leftContent.equals("T")||rightContent.equals("T"))){
            if (leftContent.equals("T")){
                isNegated = getRightChild().isNegated;
                setLeftChild(rightLeft);
                setElement(rightContent);
                setRightChild(rightRight);
            }
            else{
                isNegated = getLeftChild().isNegated;
                setLeftChild(leftLeft);
                setElement(leftContent);
                setRightChild(leftRight);
            }
        }
        else if (getElement().equals("|") && (leftContent.equals("F")||rightContent.equals("F"))){
            if (leftContent.equals("F")){
                isNegated = getRightChild().isNegated;
                setLeftChild(rightLeft);
                setElement(rightContent);
                setRightChild(rightRight);
            }
            else{
                isNegated = getLeftChild().isNegated;
                setLeftChild(leftLeft);
                setElement(leftContent);
                setRightChild(leftRight);
            }
        }
        else if (((getElement().equals("&"))||getElement().equals("|"))&& (getElement().equals(leftContent))){
            if (leftRight.MyEquals(getRightChild()) && !(getLeftChild().isNegated)){
                // a & a -> a, a | a -> a
                if(leftRight.isNegated == getRightChild().isNegated){
                    setLeftChild(leftLeft);
                }
                // a & ~a -> F
                else if (getElement().equals("&")){
                    setElement("F");
                }
                // a | ~a -> T
                else{
                    setElement("T");
                }
            }
        }
        else if (((getElement().equals("&"))||getElement().equals("|"))&& (getElement().equals(rightContent))){
            if (rightLeft.MyEquals(getLeftChild()) && !(getRightChild().isNegated)){
                // a & a -> a, a | a -> a
                if(rightLeft.isNegated == getLeftChild().isNegated){
                    setRightChild(rightRight);
                }
                // a & ~a -> F
                else if (getElement().equals("&")){
                    setElement("F");
                }
                // a | ~a -> T
                else{
                    setElement("T");
                }
            }
        }

        if(isExternal()){return;}
        else{
            getLeftChild().Collect();
            getRightChild().Collect();
        }
    }

    public void toDNF(){
        Collect();
        Arrow();
        Negate();
        Distribute();
        Collect();
    }

// check if two node are equal (do not check if is negated)
    public boolean MyEquals(Node n){
        if (!(n.getElement().equals(getElement()))){
            return false;
        }
        if (n.isExternal() && isExternal()){
            return true;
        }
        if ((getLeftChild().MyEquals(n.getLeftChild())) && (getRightChild().MyEquals(n.getRightChild()))){
            return true;
        }
        return (getLeftChild().MyEquals(n.getRightChild())) && (getRightChild().MyEquals(n.getLeftChild()));
    }

}
