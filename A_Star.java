import java.util.Arrays;
import java.util.ArrayList;
import java.util.PriorityQueue; 
import java.util.HashMap;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
/**
 *
 * @author Jacob Denton (jddenton96@gmail.com), (jdento12@uncc.edu)
 * For ITSC-3153
 * @version 09/28/18
 */
public class A_Star {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PriorityQueue<Node> openList = new PriorityQueue<>(); // behaves like a min heap
        HashMap<String, Node> closedList = new HashMap<>();
        ArrayList<Node> path = new ArrayList<>();
        Node currentNode, goalNode, startNode;
        currentNode = null;
        boolean goalFound = false;
        GraphicalRepresentation gr;
        
        
        /* Generate the grid */
        Node[][] grid = generateGrid(); 
        
        /* Generate the graphical representation of the grid */
        gr = new GraphicalRepresentation(grid, enableFancyGraphics());
        gr.printGraphicalGrid();
        
        /* Get the results from the user for the start and end nodes */
        Node[] startAndEndNodes = InputHandler.getStartAndEndNodes(grid);
        startNode = startAndEndNodes[0];
        goalNode = startAndEndNodes[1];
        
        /* Set g, h, and f values for starting node*/
        startNode.setG(0);
        int h = 10 * Math.abs((startNode.getRow() - goalNode.getRow()) 
                + (startNode.getCol() - goalNode.getCol()));
        startNode.setH(h);
        startNode.setF();
        openList.add(startNode);
        
        /* Set the start and end nodes for graphical representation */
        gr.setStartNode(startNode);
        gr.setEndNode(goalNode);
        gr.printGraphicalGrid();
        System.out.println("Generating path...");
        
        
        while(!( goalFound || openList.isEmpty())) {
            
            /* Remove top of min heap */
            currentNode = openList.remove();
            
            /* Add the current node to closed list */
            closedList.put(currentNode.toString(), currentNode);
            
            //System.out.println("CurrentNode: " + currentNode);
            if (currentNode == goalNode) {
                
                goalFound = true;
                
            } else {
                
                /* Get the current node's neighbors */
                ArrayList<Node> neighbors = generateNeighbors(currentNode, grid, closedList);
                
                //System.out.println("Final neigbors for node " + currentNode.toString() + " " + neighbors.toString());
                
                /* Calculate g, h, and f values for neighbors */
                for (Node n : neighbors) {
                    
                    if (openList.contains(n)) {  // special case
                       
                        // compare G values
                        int gThruCurrent = 
                                (n.getRow() != currentNode.getRow() && 
                                n.getCol() != currentNode.getCol()) ? 
                                currentNode.getG() + 14 : currentNode.getG() + 10;
                        
                        
                        if (gThruCurrent < n.getG()) {    
                            n.setG(gThruCurrent);
                            n.setF();
                            n.setParent(currentNode);
                            // H was already set due to it being in open list
                            openList.add(n);
                        }
                        
                    } else {
                        // generate g, h, and f values.
                        h = 10 * Math.abs((n.getRow() - goalNode.getRow()) 
                                + (n.getCol() - goalNode.getCol()));
                        
                        int g = (n.getRow() != currentNode.getRow() && 
                                n.getCol() != currentNode.getCol()) ? 
                                currentNode.getG() + 14 : currentNode.getG() + 10;
                        
                        n.setG(g);
                        n.setH(h);
                        n.setF();
                        
                        // set parent as current node
                        n.setParent(currentNode);
                        
                        // add to open list
                        openList.add(n);
                    }
                }
            }
        }
        
        
        /* Generate Path */
        if (goalFound) {
            path = generatePath(goalNode, path);
            Collections.reverse(path);
        } else {
            System.out.println("Path was no found");
        }
        
        System.out.println("Path is: " + path.toString());
        System.out.println("Running path animation...");
        
        gr.runPathAnimation(path);
        
        System.out.println("Path was: " + path.toString());
        
        
        if (startNode == goalNode) {
            System.out.println("What a waste of CPU Cycles..");
        }
    }
    
    
    
    // Generates path using a recursive process
    private static ArrayList<Node> generatePath(Node n, ArrayList<Node> path) {
        if (n == null) {
            return path;
        } else {
            path.add(n);
            return generatePath(n.getParent(), path);
        }
    }
    
    
    
    private static void printGrid(Node[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            System.out.println(Arrays.toString(grid[i]));
        }
    }
    
    
    
    private static ArrayList<Node> generateNeighbors(Node current, 
            Node[][] grid, HashMap<String, Node> closedList) {
        
        ArrayList<Node> neighbors = new ArrayList<>();
        int minRow, maxRow, minCol, maxCol, cRow, cCol;
        cRow = current.getRow();
        cCol = current.getCol();
        minRow = cRow - 1;
        maxRow = cRow + 1;
        minCol = cCol - 1;
        maxCol = cCol + 1;
        int [] rows = {cRow, minRow, maxRow};
        int [] cols = {cCol, minCol, maxCol};
        
        /** Valid Neighbors:
          * 1. Must be within bounds of enviroment
          * 2. node must be pathable
          * 3. node must not be in closed list
          */
        
        // 1.
        for (int r = 0; r < rows.length; r++) {
            
            for (int c = 0; c < cols.length; c++) {
                
                /* add to neighbors list if the node is in bounds */
                if (( rows[r] < grid.length && rows[r] >= 0) && 
                        (cols[c] < grid.length && cols[c] >= 0)) {
                    
                    neighbors.add(grid[rows[r]][cols[c]]);
                }   
            }
        }
        
        // 2.
        /* remove non traversable nodes */
        neighbors.removeIf((Node n) -> n.getType() == 1);
        
        // 3.
        /* remove nodes that are in closed list */
        neighbors.removeIf((Node n) -> closedList.containsKey(n.toString()));
        
        return neighbors;
    } 
    
    
    
    private static Node[][] generateGrid() {
        Node[][] grid = new Node[15][15];
        Random rng = new Random();
        int nodeType;
        
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                
                // 10% chance of each node being a unpathable barrier node
                nodeType = (rng.nextInt(100) + 1 >= 90) ? 1 : 0;
                
                grid[row][col] = new Node(row, col, nodeType);
            }
        }
        
        return grid;
    }
    
    
    
    private static boolean enableFancyGraphics() {
        char select = ' ';
        Scanner read = new Scanner(System.in);
        
        do {
            System.out.println("Enable fancy characters? If '▒' is not the same height as '█'. Then it's recommended that you do NOT use fancy character.");
            System.out.print("y/n: ");
            select = read.next().charAt(0);
            System.out.println();
            
            if (!(select == 'y' || select == 'n')) {
                System.out.println("selection '" + select + "' is not a option");
            }
        } while (!(select == 'y' || select == 'n'));
        
        return select == 'y';
    }
    
} // S.D.G.