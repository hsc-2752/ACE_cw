 <!-- Shuchang Huang 20214805--> 

Author: Shuchang Huang 20214805

Structure of this project:
This project contains two java files: Node.java and Main.java
In Node.java, the following methods are included: 
1. A constructor method that is used to construct a node(tree)
2. Get and Set methods for the value of the node(tree)
3. Get and Set methods for the left and right child of the node(tree)
4. 'isExternal' method to check if a node(tree) has child or not
5. Negate, Arrow, Distribute, Collect methods used when converting a node(tree) to to a new node(tree) whose string is in DNF form
6. A 'toDNF' method (a collection of the four methods mentioned in 5)
7. A helper method 'MyEquals' to check the equality of two nodes
In Main.java, the following methods are included: 
1. "toTree" converts a string to tree(node) structure
2. "treeToString" coverts a tree(node) back to string form
3. "DNF" method converts a tree(node) to a new tree(node) whose string is in DNF form 
4. "convert" method is the collection of the above three methods

How to run this project:
Run the Main.java file to start, you can enter a logical expression in LD form in the terminal, after entering, press the key "ENTER" and you will be able to see the equivalent DNF form of that logical expression. (Please note that this java program only support logical expression in the format as stated in the coursework specification)

