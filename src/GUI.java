import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import acm.graphics.*;
import acm.program.*;
public class GUI extends GraphicsProgram {
    JButton solve, play, clear, check;
    GRect[][] grid;
    GLabel[][] labels;
    JTextField[][] textFields;
    Sudoku sudoku;
    Sudoku programSudoku;
    GLabel message, message2;
    public void actionPerformed(ActionEvent e) {
        message.setVisible(false);
        message2.setVisible(false);

        if (e.getSource() == solve) solveB();
        else if (e.getSource() == play) {
            solve.setEnabled(false);
            playB();
        }
        else if (e.getSource() == clear) {
            clearB();
            check.setVisible(false);
            solve.setEnabled(true);
        } else if (e.getSource() == check) checkB();
    }
    public void run() {
        // initialising variables
        labels = new GLabel[9][9];
        textFields = new JTextField[9][9];
        grid = new GRect[9][9];
        sudoku = new Sudoku();
        message = new GLabel("");
        message2 = new GLabel("");
        createButtons();

        setSize(970,800);
        setTitle("sudoku!");

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                grid[x][y] = new GRect(80, 80);
                labels[x][y] = new GLabel( "");
                textFields[x][y] = new JTextField(3);
                setGridColour(x,y);

                textFields[x][y].setFont(new Font("Serif", Font.PLAIN, 30));
                textFields[x][y].setOpaque(false);
                textFields[x][y].setDocument(new JTextFieldLimit());
                textFields[x][y].setHorizontalAlignment(SwingConstants.CENTER);
                textFields[x][y].setForeground(Color.black);
                textFields[x][y].setBorder(javax.swing.BorderFactory.createEmptyBorder());
                textFields[x][y].setBounds(new Rectangle(10+(80*y),10+(80*x),79,79));

                add(textFields[x][y], 27+(80*y),27+(80*x));
                add(grid[x][y],25+(80*x), 25+(80*y));

                labels[x][y].setVisible(false);
                textFields[x][y].setVisible(true);
            }
        }
    }
    private void createButtons() {
        clear = new JButton("Clear");
        clear.setFont(new Font("Serif", Font.PLAIN, 18));
        clear.setSize(175,80);
        add(clear,770,90);
        clear.addActionListener(this);

        solve = new JButton("Solve");
        solve.setFont(new Font("Serif", Font.PLAIN, 18));
        solve.setSize(175,80);
        add(solve,770,200);
        solve.addActionListener(this);

        play = new JButton("Play");
        play.setFont(new Font("Serif", Font.PLAIN, 18));
        play.setSize(175,80);
        add(play,770,310);
        play.addActionListener(this);

        check = new JButton("Check answer");
        check.setFont(new Font("Serif", Font.PLAIN, 15));
        check.setSize(125,40);
        add(check,795,395);
        check.addActionListener(this);
        check.setVisible(false);
    }
    private void setGridColour(int x, int y) {
        int row = (x / 3) * 3;
        int col = (y / 3) * 3;

        if ((row == 0 && col == 0) || (row == 0 && col == 6) || (row == 3 && col == 3) || (row == 6 && col == 0) || (row == 6 && col == 6)) {
            grid[x][y].setFilled(true);
            grid[x][y].setFillColor(new Color(250, 248, 230));
        }
    }
    private void playB() {
        check.setVisible(true);

        clearB();

        sudoku.clear();
        sudoku.generate();
        sudoku.print();          // puzzle for the user to solve

        programSudoku = new Sudoku();
        int[][] temp = sudoku.getGrid();
        programSudoku.setGrid(temp);

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (sudoku.getCell(x,y) == 0) {                 // user input
                    textFields[x][y].setForeground(new Color(7, 31, 156));
                    textFields[x][y].setFont(new Font("Serif", Font.PLAIN, 30));
                    textFields[x][y].setOpaque(false);
                    textFields[x][y].setDocument(new JTextFieldLimit());
                    textFields[x][y].setHorizontalAlignment(SwingConstants.CENTER);
                    textFields[x][y].setBorder(javax.swing.BorderFactory.createEmptyBorder());
                    textFields[x][y].setBounds(new Rectangle(10+(80*y),10+(80*x),79,79));

                    add(textFields[x][y], 27+(80*y),27+(80*x));
                    textFields[x][y].setEditable(true);
                    textFields[x][y].setVisible(true);
                } else {
                    labels[x][y].setFont(new Font("Serif", Font.PLAIN, 30));
                    labels[x][y].setLabel(sudoku.getCell(x,y) + "");
                    add(labels[x][y],59+(80*y),75+(80*x));
                    textFields[x][y].setVisible(false);
                    labels[x][y].setVisible(true);
                }
            }
        }
    }
    private void checkB() {
        int num;
        boolean bad = false;

        // turning user inputted sudoku into a 2D array
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (sudoku.getCell(x,y) == 0) {
                    if (textFields[x][y].getText().isEmpty()) {
                        playMessage(3);
                        return;
                    }
                    num = Integer.parseInt(textFields[x][y].getText());
                    sudoku.setCell(x, y, num);
                }
            }
        }

        programSudoku.solve();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (programSudoku.getCell(i,j) != sudoku.getCell(i,j)) bad = true;
            }
        }

        if (bad) playMessage(2);
        else playMessage(1);
        check.setVisible(false);
    }
    private void solveB() {
        int num;

        // turning user inputted sudoku into a 2D array
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (!textFields[x][y].getText().isEmpty()) num = Integer.parseInt(textFields[x][y].getText());
                else num = 0;

                sudoku.setCell(x,y,num);
            }
        }

        sudoku.print();

        if (sudoku.solve()) {
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    if (textFields[x][y].getText().isEmpty()) {
                        labels[x][y].setColor(new Color(7, 31, 156));
                        labels[x][y].setLabel(sudoku.getCell(x,y) + "");
                        labels[x][y].setFont(new Font("Serif", Font.PLAIN, 30));
                    } else {
                        labels[x][y].setColor(Color.BLACK);
                        labels[x][y].setLabel(sudoku.getCell(x,y) + "");
                        labels[x][y].setFont(new Font("Serif", Font.PLAIN, 30));
                    }
                    add(labels[x][y],59+(80*y),75+(80*x));
                    labels[x][y].setVisible(true);
                    textFields[x][y].setVisible(false);
                }
            }
            message = new GLabel("solved.");
            message.setColor(new Color(41, 140, 42));
        } else {
            message = new GLabel("invalid sudoku.");
            message.setColor(new Color(201, 59, 48));
        }
        message.setFont(new Font("Serif", Font.PLAIN, 18));
        add(message, 800, 630);
        message.setVisible(true);
    }
    private void playMessage(int gd) {
        if (gd == 3) {        // unfinished
            message = new GLabel("please complete the");
            message2 = new GLabel("puzzle before checking.");
        } else if (gd == 2) {       // invalid
            message = new GLabel("so close...");
            message2 = new GLabel("try again!");
        } else if (gd == 1) {      // solved
            message = new GLabel("great job!");
            message2 = new GLabel("thanks for playing.");
        }

        message.setFont(new Font("Serif", Font.PLAIN, 18));
        message.setColor(new Color(201, 59, 48));
        message2.setFont(new Font("Serif", Font.PLAIN, 18));
        message2.setColor(new Color(201, 59, 48));
        add(message, 780, 600);
        add(message2, 780, 630);
        message.setVisible(true);
        message2.setVisible(true);
    }
    private void clearB() {
        // hides labels
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (sudoku.getCell(x,y) != 0) {
                    labels[x][y].setLabel("");
                    labels[x][y].setVisible(false);
                    textFields[x][y].setVisible(true);
                }
            }
        }

        // hides text
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                textFields[x][y].setText("");
                textFields[x][y].setVisible(false);
            }
        }

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                textFields[x][y].setForeground(Color.black);
                textFields[x][y].setFont(new Font("Serif", Font.PLAIN, 30));
                textFields[x][y].setOpaque(false);
                textFields[x][y].setDocument(new JTextFieldLimit());
                textFields[x][y].setHorizontalAlignment(SwingConstants.CENTER);
                textFields[x][y].setBorder(javax.swing.BorderFactory.createEmptyBorder());
                textFields[x][y].setBounds(new Rectangle(10+(80*y),10+(80*x),79,79));

                add(textFields[x][y], 27+(80*y),27+(80*x));

                textFields[x][y].setEditable(true);
                textFields[x][y].setVisible(true);
            }
        }
    }
    private static class JTextFieldLimit extends PlainDocument {
        // credits to Réal Gagnon (rgagnon.com) & @fiveelements (mutesoft.com)
        private boolean numeric(String x) {
            try {
                int n = Integer.parseInt(x);
                return n != 0;       // only 1-9
            } catch(NumberFormatException exception) {
                return false;
            }
        }
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (numeric(str) && (getLength() + str.length()) < 2) {    // if int and 1 char
                super.insertString(offset, str, attr);
            }
        }
    }
}