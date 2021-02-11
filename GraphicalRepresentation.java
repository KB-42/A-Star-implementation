import java.util.ArrayList;

/**
 *
 * @author Jacob Denton (jdento12@uncc.edu)
 * @version 09/28/18
 * For ITSC-3153
 * ############## BEST RESULTS IF USED WITH A MONOSPACE FONT #################
 */
public class GraphicalRepresentation {
    
    Node[][] nodeGrid;
    char[][] graphicalGrid;
    Node startNode;
    Node endNode;
    boolean fancyGraphics;
    
    char unpathableSymbol;
    char pathableSymbol;
    
    public GraphicalRepresentation(Node[][] nodeGrid, boolean fancyGraphics) {
        this.nodeGrid = nodeGrid;
        this.fancyGraphics = fancyGraphics;
        determineSymbols();
        graphicalGrid = generateRepresentation();
        startNode = null;
        endNode = null;
    }
    
    
    public void setStartNode(Node n) {
        startNode = n;
    }
    
    
    public void setEndNode(Node n) {
        endNode = n;
    }
    
    
    public void printGraphicalGrid() {
        graphicalGrid = generateRepresentation();
        
        System.out.println("Best looking results with a monospace font.\nLegend: pathable '"+ pathableSymbol + "', unpathable '"+ unpathableSymbol +"', start Node '§', goal node '¶', pathed ' '");
        
        for (int row = 0; row < graphicalGrid.length; row++) {
            
            if (row < 10) {
                System.out.print(row + ":  ");
            } else {
                System.out.print(row + ": ");
            }
            for (int col = 0; col < graphicalGrid[row].length; col++) {
                System.out.print(graphicalGrid[row][col] + " ");
            }
            System.out.println();
        }   
    }
    
    
    public void runPathAnimation(ArrayList<Node> path) {
        Node prevNode = null;
        graphicalGrid = generateRepresentation();
        
        for (Node n : path) {
            
            if (prevNode != null) {
                graphicalGrid[prevNode.getRow()][prevNode.getCol()] = ' ';
            }
            prevNode = n;
            graphicalGrid[n.getRow()][n.getCol()] = '§';
            printGraph();
            System.out.println();
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Thread sleep was interrupted. How dare you wake it!!!!");
            }        
        }
    }
    
    
    private char[][] generateRepresentation() {
        char[][] gr = new char[nodeGrid.length][nodeGrid.length];
        
        for (int row = 0; row < nodeGrid.length; row++) {
            for (int col = 0; col < nodeGrid[row].length; col++) {
                
                if (nodeGrid[row][col].getType() == 1) {
                    gr[row][col] = unpathableSymbol; // unpathable
                } else if (nodeGrid[row][col] == startNode) {
                    gr[row][col] = '§'; // start point
                }  else if (nodeGrid[row][col] == endNode) {
                    gr[row][col] = '¶'; // will prolly change. End point 
                } else { // is a pathable location
                    gr[row][col] = pathableSymbol; // pathable   
                }
            }
        }
        return gr;
    }
    
    
    private void printGraph() {
        for (int row = 0; row < graphicalGrid.length; row++) {
            
            // Keeps the grid printing out evenly
            if (row < 10) {
                System.out.print(row + ":  ");
            } else {
                System.out.print(row + ": ");
            }
            
            /* print out every node in each row */
            for (int col = 0; col < graphicalGrid[row].length; col++) {
                System.out.print(graphicalGrid[row][col] + " ");
            }
            System.out.println();
        }
    }
    
    private void determineSymbols() {
        if (fancyGraphics) {
            unpathableSymbol = '█';
            pathableSymbol = '▒';
        } else {
            unpathableSymbol = 'X';
            pathableSymbol = '0';
        }
    }
    
} // S.D.G.