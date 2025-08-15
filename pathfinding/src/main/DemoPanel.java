package main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DemoPanel extends JPanel {
    // Screen settings
    final int maxCol = 15;
    final int maxRow = 10;
    final int nodeSize = 70;
    final int screenWidth = maxCol * nodeSize;
    final int screenHeight = maxRow * nodeSize;

    // NODE
    Node[][] nodes = new Node[maxRow][maxCol];
    Node startNode,goalNode,currentNode;
    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> checkedList = new ArrayList<>();

    boolean goalReached = false;
    int step = 0;

    public DemoPanel() {
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.BLACK);
        this.setLayout(new GridLayout(maxRow,maxCol));
        this.addKeyListener(new KeyHandler(this));
        this.setFocusable(true);

        // Place nodes on the panel
        for(int i = 0; i<maxRow; i++) {
            for(int j=0; j<maxCol; j++) {
                nodes[i][j] = new Node(i,j);
                this.add(nodes[i][j]);
            }
        }

        // Set start and goal nodes
        setStartNode(6,3);
        setGoalNode(3,11);

        // Set solid nodes
        setSolidNode(2,10);
        setSolidNode(3,10);
        setSolidNode(4,10);
        setSolidNode(5,10);
        setSolidNode(6,10);
        setSolidNode(7,10);
        setSolidNode(2,6);
        setSolidNode(2,6);
        setSolidNode(2,7);
        setSolidNode(2,8);
        setSolidNode(2,9);
        setSolidNode(7,11);
        setSolidNode(7,12);
        setSolidNode(1,6);

        // Set cost on nodes
        setCostOnNodes();
    }

    private void setStartNode(int row,int col) {
        nodes[row][col].setAsStart();
        startNode = nodes[row][col];
        currentNode = startNode;
    }

    private void setGoalNode(int row,int col) {
        nodes[row][col].setAsGoal();
        goalNode = nodes[row][col];
    }

    private void setSolidNode(int row,int col) {
        nodes[row][col].setAsSolid();

    }

    private void setCostOnNodes()  {
        for(int i = 0; i<maxRow; i++) {
            for(int j=0; j<maxCol; j++) {
                getCost(nodes[i][j]);
            }
        }
    }

    private void getCost(Node node) {
        // Get G Cost, the cost from the start node to this node
        int xDistance = Math.abs(node.row - startNode.row);
        int yDistance = Math.abs(node.col - startNode.col);
        node.gCost = xDistance + yDistance;

        // Get H Cost, the cost from this node to the goal node
        xDistance = Math.abs(node.row - goalNode.row);
        yDistance = Math.abs(node.col - goalNode.col);
        node.hCost = xDistance + yDistance;

        // Get F Cost, the total cost of this node
        node.fCost = node.gCost + node.hCost;

        if(node!=startNode && node!=goalNode) {
            node.setText("<html>F:" + node.fCost + "<br>G:" + node.gCost + "<html>");
        }

    }

    public void search() {
        if(!goalReached) {
            int row = currentNode.row;
            int col = currentNode.col;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            // Open the surrounding nodes
            if(row > 0) openNode(nodes[row-1][col]); // Up
            if(row < maxRow-1) openNode(nodes[row+1][col]); // Down
            if(col > 0) openNode(nodes[row][col-1]); // Left
            if(col < maxCol-1) openNode(nodes[row][col+1]); // Right

            // Find the best node.
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for(int i = 0; i < openList.size(); i++) {
                if(openList.get(i).fCost < bestNodefCost) {
                    bestNodefCost = openList.get(i).fCost;
                    bestNodeIndex = i;
                }

                else if(openList.get(i).fCost == bestNodefCost) {
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }
            // After finding the best node, set it as the current node.
            currentNode = openList.get(bestNodeIndex);
            if(currentNode == goalNode) {
                goalReached = true;
            }
        }
    }
    public void autoSearch() {
        while(!goalReached && step < 500) {
            int row = currentNode.row;
            int col = currentNode.col;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            // Open the surrounding nodes
            if(row > 0) openNode(nodes[row-1][col]); // Up
            if(row < maxRow-1) openNode(nodes[row+1][col]); // Down
            if(col > 0) openNode(nodes[row][col-1]); // Left
            if(col < maxCol-1) openNode(nodes[row][col+1]); // Right

            // Find the best node.
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for(int i = 0; i < openList.size(); i++) {
                if(openList.get(i).fCost < bestNodefCost) {
                    bestNodefCost = openList.get(i).fCost;
                    bestNodeIndex = i;
                }

                else if(openList.get(i).fCost == bestNodefCost) {
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }
            // After finding the best node, set it as the current node.
            currentNode = openList.get(bestNodeIndex);
            if(currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }

            step++;
        }
    }

    private void openNode(Node node) {
        if(!node.open && !node.solid && !node.checked) {
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
        }
    }

    private void trackThePath() {
        Node current = goalNode;
        while(current!=startNode) {
            current = current.parent;
            if(current!=startNode) {
                current.setAsPath();
            }
        }
    }
}
