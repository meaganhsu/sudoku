import java.util.*;

public class Sudoku2 {

    public static void main(String[] args) {
        int[][] grid = new int[9][9];
        generate(grid);
        print(grid);
        solve(grid);
        System.out.println("\n\n");
        print(grid);
    }

    private static void generate(int[][] grid) {
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j] = 0;
            }
        }

        fill(grid, 0, 0);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (random.nextInt(2) == 0) {
                    grid[i][j] = 0;
                }
            }
        }
    }

    private static boolean fill(int[][] grid, int row, int col) {   // good gen
        Random random = new Random();

        if (row == 8 && col == 9) {
            return true;
        }

        if (col == 9) {
            row++;
            col = 0;
        }

        if (grid[row][col]!= 0) return fill(grid, row, col + 1);

        for (int i = 0; i < 10; i++) {
            int num = random.nextInt(1,10);
            if (valid(grid, row, col, num)) {
                grid[row][col] = num;
                if (fill(grid, row, col + 1)) {
                    return true;
                }
                grid[row][col] = 0; // backtrack
            }
        }

        return false;
    }

    private static boolean valid(int[][] grid, int row, int col, int n) {
        for (int i = 0; i < 9; i++) {       // column check
            if (grid[i][col] == n) return false;
        }

        for (int i = 0; i < 9; i++) {         // row check
            if (grid[row][i] == n) return false;
        }

        // 3x3 box check
        row = (row / 3) * 3;
        col = (col / 3) * 3;

        for (int i = row; i < row+3; i++) {
            for (int j = col; j < col+3; j++) {
                if (grid[i][j] == n) return false;
            }
        }
        return true;
    }

    private static void print(int[][] grid) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(grid[i][j] + " ");
                if ((j + 1) % 3 == 0 && j < 8) {
                    System.out.print("| ");
                }
            }
            System.out.println();
            if ((i + 1) % 3 == 0 && i < 8) {
                System.out.println("------+-------+------");
            }
        }
    }
    private static boolean solve(int[][] grid) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (grid[r][c] == 0) {
                    for (int n = 1; n < 10; n++) {
                        if (valid(grid, r, c, n)) {
                            grid[r][c] = n;
                            if (solve(grid)) return true;
                            else grid[r][c] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
}