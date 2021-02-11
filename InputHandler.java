import java.util.Scanner;
/**
 *
 * @author Jacob Denton (jdento12@uncc.edu)
 * For ITSC-3153
 * @version 09/28/18
 */
public class InputHandler {
    
    public static Node[] getStartAndEndNodes(Node[][] grid) {
        Scanner input = new Scanner(System.in);
        Node[] nodes = new Node[2];
        int[] positionData;
        String startPos, endPos;
        boolean validPosition;
        
        /* get the start node from the user */
        do {
            System.out.println("Enter a starting Node position from 0-14: (row,column). Example: 10,5 ");
            startPos = input.next();
            positionData = formatAndExtractPositionData(startPos);
            
            validPosition = isValidPosition(positionData, grid);
            
        } while(!validPosition);
        
        /* Fetch the reference for the start node from the grid */
        nodes[0] = grid[positionData[0]][positionData[1]];
        
        /* Get the end/goal node from the user */
        do {
            System.out.println("Enter a ending Node position from 0-14: (row,column). Example: 10,5 ");
            endPos = input.next();
            positionData = formatAndExtractPositionData(endPos);
            
            validPosition = isValidPosition(positionData, grid);
            
        } while(!validPosition);
        
        /* Fetch the reference for the end/goal node from the grid */
        nodes[1] = grid[positionData[0]][positionData[1]];
        
        
        /* check to see if the user has short-term memory issues */
        if (nodes[0] == nodes[1]) {
            System.out.println("Silly user, the start node and end node are the same location!");
            System.out.println("Get ready for some 'heavy' computation... brace yourself");
            System.out.println();
        }
        
        
        return nodes;
    }
    
    private static int[] formatAndExtractPositionData(String pos) {
        int[] position = new int[2];
        String refinedData;
        int row, col;
        
        refinedData = pos.replaceAll("[^0-9,]", ""); // regex for the win!
        
        String[] parsed = refinedData.split("[,]");
        
        if (parsed.length == 2) {
            row = Integer.parseInt(parsed[0]);
            col = Integer.parseInt(parsed[1]);
        } else {
            row = -1;
            col = -1;
        }
        position[0] = row;
        position[1] = col;
        
        return position;
    }
    
    
    private static boolean isValidPosition(int[] point, Node[][] grid) {
        int row = point[0];
        int col = point[1];
        boolean isValid = false;
        
        // If location is not out of bounds        
        if (( row < grid.length && row  >= 0) && (col < grid.length && col >= 0)) {
            
            if (grid[row][col].getType() == 0) {
                isValid = true;
            } else {
                System.out.println("INVALID NODE: node is an unpathable barrier");
            }
        } else {
            System.out.println("INVALID POSITION: point (" + row + "," + col + ") does not exist");
        }
        
        return isValid;
    }
    
} // S.D.G.