import java.util.*;
public class Sudoku {
    public int[][] grid = new int[9][9];
    public void print() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) System.out.print(grid[i][j] + " ");
            System.out.println();
        }
        System.out.println();
    }
    public int getCell(int x, int y) {
        return grid[x][y];
    }
    public int[][] getGrid() {return grid;}
    public void setGrid(int[][] g) {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                grid[x][y] = g[x][y];
            }
        }
    }
    public void setCell(int x, int y, int n) {
        grid[x][y] = n;
    }
    public void clear() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j] = 0;             // sets every num to 0
            }
        }
    }
    public void generate() {
        clear();
        fill(0, 0);
        removeNums();
    }
    public boolean solve() {
        int count = 0;

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (grid[x][y] != 0) count++;
            }
        }

        if (count < 17) return false;     // sudoku has to have a minimum of 17 clues
        else if (count == 81) return true;         // sudoku is already solved

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (grid[x][y] == 0) {
                    for (int n = 1; n < 10; n++) {
                        if (valid(x, y, n)) {
                            grid[x][y] = n;
                            if (solve()) return true;       // recursion
                            else grid[x][y] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
    private boolean fill(int row, int col) {       // completely fills grid w random numbers
        Random random = new Random();

        if (row == 8 && col == 9) {
            return true;
        }

        if (col == 9) {
            row++;
            col = 0;
        }

        if (grid[row][col]!= 0) return fill(row, col + 1);

        for (int i = 0; i < 9; i++) {
            int num = random.nextInt(1,10);
            if (valid(row, col, num)) {
                grid[row][col] = num;
                if (fill(row, col + 1)) {
                    return true;
                }
                grid[row][col] = 0; // backtrack
            }
        }

        return false;
    }
    private void removeNums() {
        Random random = new Random();
        int temp;
        int count = 0;
        int i, j;

        for (int x = 0; x < 300; x++) {
            i = random.nextInt(0,9);
            j = random.nextInt(0,9);

            if (grid[i][j] == 0) {
                x++;
                continue;
            }

            temp = grid[i][j];
            grid[i][j] = 0;

            for (int k = 1; k < 10; k++) if (valid(i,j,k)) count++;

            if (count > 1) grid[i][j] = temp;       // backtrack if there's more than one solution

            count = 0;
        }
    }
    private boolean valid(int row, int col, int n) {
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
    public static void main(String[] args) {
        Sudoku s = new Sudoku();
        s.generate();
        s.print();
    }
}