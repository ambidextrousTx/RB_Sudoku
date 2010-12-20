import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ambidextrous
 * Date: Dec 19, 2010
 * Time: 5:17:46 PM
 * To change this template use File | Settings | File Templates.
 */

public class RecursiveBacktrackSudoku {
    public static boolean sudoku(int [][] sudokuBoard, boolean [] stickies, int cell, boolean back) {

        if(cell == 81) // Successfully solved all blanks
            return true;

        boolean result = false;
        int row = cell / 9;
        int col = cell % 9;
        int block = getBlock(row, col);

        if(stickies[cell])
            if(back)
                result = sudoku(sudokuBoard, stickies, cell - 1, back); // Backtrack
            else
                result = sudoku(sudokuBoard, stickies, cell + 1, back);
        else {
            System.out.println("Currently processing (" + row + ", " + col + ")");
            int digit = sudokuBoard[row][col]+1; // During each call, we check if the next digit fits            
            System.out.println("With digit " + digit);
            if(digit > 9) {
                sudokuBoard[row][col] = 0;
                System.out.println("Exhausted all options, backtracking");                
                result = sudoku(sudokuBoard, stickies, cell - 1, true); // Exhausted all options, time to backtrack
            }

            else {
                boolean rowSafe = isRowSafe(sudokuBoard, row, digit);
                boolean colSafe = isColSafe(sudokuBoard, col, digit);
                boolean blockSafe = isBlockSafe(sudokuBoard, block, digit);
                sudokuBoard[row][col] = digit;

                if(rowSafe && colSafe && blockSafe) {
                    System.out.println("Assigned " + digit +" to (" + row + ", " + col + ")");
                    result = sudoku(sudokuBoard, stickies, cell + 1, false);
                }

                else
                    result = sudoku(sudokuBoard, stickies, cell, false);
            }
        }

        return false;
    }

    private static int getBlock(int row, int col) {
        // row          col         block
        // 0, 1, 2      0, 1, 2     1
        // 0, 1, 2      3, 4, 5     2
        // 0, 1, 2      6, 7, 8     3
        // 3, 4, 5      0, 1, 2     4
        // 3, 4, 5      3, 4, 5     5
        // 3, 4, 5      6, 7, 8     6
        // 6, 7, 8      0, 1, 2     7
        // 6, 7, 8      3, 4, 5     8
        // 6, 7, 8      6, 7, 8     9

        int block;
        if (row < 3)
            if(col < 3)
                block = 1;
            else if(col < 6)
                block = 2;
            else
                block = 3;
        else if (row < 6)
            if(col < 3)
                block = 4;
            else if(col < 6)
                block = 5;
            else
                block = 6;
        else
            if(col < 3)
                block = 7;
            else if(col < 6)
                block = 8;
            else
                block = 9;

        return block;
    }
  
    private static boolean isRowSafe(int [][] sudokuBoard, int row, int digit) {
        boolean safe = true;
        int [] r = sudokuBoard[row];
        for (int number : r) {
            if(number == digit)
                safe = false;
        }
        
        return safe;
    }

    private static boolean isColSafe(int [][] sudokuBoard, int col, int digit) {
        boolean safe = true;
        for(int i = 0; i < sudokuBoard.length; i++) {
            if(sudokuBoard[i][col] == digit)
                safe = false;
        }

        return safe;
    }

    private static boolean isBlockSafe(int [][] sudokuBoard, int block, int digit) {
        boolean safe = true;
        if(block == 1)
            for(int i = 0; i < 3; i++)
                for(int j = 0; j < 3; j++)
                    if(sudokuBoard[i][j] == digit)
                        safe = false;

        if(block == 2)
            for(int i = 0; i < 3; i++)
                for(int j = 3; j < 6; j++)
                    if(sudokuBoard[i][j] == digit)
                        safe = false;

        if(block == 3)
            for(int i = 0; i < 3; i++)
                for(int j = 6; j < 9; j++)
                    if(sudokuBoard[i][j] == digit)
                        safe = false;

        if(block == 4)
            for(int i = 3; i < 6; i++)
                for(int j = 0; j < 3; j++)
                    if(sudokuBoard[i][j] == digit)
                        safe = false;

        if(block == 5)
            for(int i = 3; i < 6; i++)
                for(int j = 3; j < 6; j++)
                    if(sudokuBoard[i][j] == digit)
                        safe = false;

        if(block == 6)
            for(int i = 3; i < 6; i++)
                for(int j = 6; j < 9; j++)
                    if(sudokuBoard[i][j] == digit)
                        safe = false;

        if(block == 7)
            for(int i = 6; i < 9; i++)
                for(int j = 0; j < 3; j++)
                    if(sudokuBoard[i][j] == digit)
                        safe = false;

        if(block == 8)
            for(int i = 6; i < 9; i++)
                for(int j = 3; j < 6; j++)
                    if(sudokuBoard[i][j] == digit)
                        safe = false;

        if(block == 9)
            for(int i = 6; i < 9; i++)
                for(int j = 6; j < 9; j++)
                    if(sudokuBoard[i][j] == digit)
                        safe = false;

        return safe;
    }

    public static String[] getInput() {
        String [] inputs = new String[9];
        File file = new File("C:\\Users\\Ambidextrous\\Documents\\Code\\DataStructures\\src\\sudoku.txt");

        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);

            for(int i = 0; i < 9; i++) {
                try {
                    inputs[i] = dis.readLine();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return inputs;
    }

    public static int [][] fillInput(String [] inLines) {
        int N = inLines.length;
        int [][] sudokuBoard = new int[N][N];
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < inLines[i].split(" ").length; j++) {
                sudokuBoard[i][j] = Integer.parseInt(inLines[i].split(" ")[j]);
            }
        }
        return sudokuBoard;
    }

    public static boolean [] getStickies(int [][] sudokuBoard) {
        int L = sudokuBoard.length;
        boolean [] stickies = new boolean[L*L];

        int counter = 0;
        for(int i = 0; i < L; i++)
            for(int j = 0; j < L; j++)
                if(sudokuBoard[i][j] != 0)
                    stickies[counter++] = true;
                else
                    stickies[counter++] = false;
                    
        return stickies;
    }

    public static void main(String[] args) {
        String [] inLines = getInput();
        int [][] sudokuBoard = fillInput(inLines);
        boolean [] stickies = getStickies(sudokuBoard); // Find which cells are fixed [had numbers at the start]

        boolean done = sudoku(sudokuBoard, stickies, 0, false); // Cell = 0, Digit = 1

        for(int [] row : sudokuBoard) {
            for(int entry : row)
                System.out.print(entry + " ");
            System.out.println();
        }

    }
}
