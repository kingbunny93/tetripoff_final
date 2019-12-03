package tetripoff_pack;

import tetripoff_pack.Shape.Tetrominoe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;

/**
 * The Board class contains the board object which is what controls the whole game. It contains many variables
 * and methods that control the appearance of the board, and nested classes that are used to play the game based
 * on a timer and drop speed. It contains the actionlisteners for the keyboard inputs that control each of the many
 * methods and functions in this class.
 * @author Angelito Sabino
 * This class also contains methods such as JFrame and/or saving data to a text file, which is what we recently learned
 * in my CS401 class.
 */

public class Board extends JPanel{

    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 24;
    static GraphicsConfiguration gc;

    private String diff = "";               // to set difficulty
    private int finalScore = 0;             // Score after calculating LinesCleared * Difficulty
    private Timer timer;
    private boolean isFallDone = false;
    private boolean isPaused = false;
    private int linesCleared = 0;
    private int curX = 0;
    private int curY = 0;

    private JLabel statusbar;
    private Shape currentPiece;
    private Tetrominoe[] board;

    private int dropRate = 0;      // Base speed (will change based on difficulty level)

    /**
     * The constructor takes the Tetripoff Object parent passed by the driver class and the difficulty level to assign
     * the dropRate to a certain int. This is used to calculate the final score and to adjust the speed at which the
     * board repaints or how fast the blocks move.
     * @param parent
     * @param df
     */
    public Board(Tetripoff parent, int df) {
        this.dropRate = df;
        initBoard(parent);
    }

    /**
     * This method initializes the board and is called by the Board Constructor. Basically does what is mentioned in
     * the constructor, but adds a JPanel status bar at the bottom which lets the player know the current game status
     * or how many lines they have cleared.
     * @param parent
     */
    private void initBoard(Tetripoff parent) {
        setFocusable(true);
        statusbar = parent.getStatusBar();
        addKeyListener(new TAdapter());
    }

    /** Adjusts the square width size based on the Frame Size and the width of the frame which is based on the regular
     * tetris size of 10x24 and returns that as an integer
     * @return
     * */
    private int squareWidth() {
        return (int) getSize().getWidth() / BOARD_WIDTH;
    }

    /**Adjusts the square height size based on the Frame Size and the height of the frame which is based on the regular
     * tetris size of 10x24 and returns that as an integer
     * @return
     */
    private int squareHeight() {
        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }

    /**
     *returns the shape at the coordinates passed into them by the shape and board.
     * @param x
     * @param y
     * @return
     */
    private Tetrominoe shapeAt(int x, int y) {
        return board[(y * BOARD_WIDTH) + x];
    }

    /**
     * starts the game by initializing a new piece and board which calls the clearBoard and newPiece methods that
     * continually execute based on the timer speed that's affected by the dropRate.
     */
    void start() {
        currentPiece = new Shape();
        board = new Tetrominoe[BOARD_WIDTH * BOARD_HEIGHT];
        clearBoard();
        newPiece();
        timer = new Timer(dropRate, new GameCycle());
        timer.start();

    }
    /** Method temporarily pauses the games current status. Can be unpaused */
    private void pause() {
        isPaused = !isPaused;
        if (isPaused) {
            statusbar.setText("paused");
        } else {
            statusbar.setText(String.valueOf(linesCleared));
        }
        repaint();
    }

    /** Method will pause the game, and open up the Hi Scores window
     * currently unfinished but function does stand to pause game */
    private void checkScores() {
        pause();
        System.out.println("It doesn't do anything at the moment");
    }

    /** Method called will remove the current piece, save it's information on the side, create a new piece,
     * and after that piiece is placed then it will bring back this exact piece.
     * currently unfinished but function does stand to pause game until further code is added. pause will be removed
     * and will instead repaint using a new currentpiece.
     */
    private void holdPiece() {
        pause();
        System.out.println("It doesn't do anything at the moment");
    }

    /** Method called using the difficulty chosen by the User, the speed/period Interval will change and affect
     * the score as well. returns the final score which is to be saved.
     * @return
     * */
    private int calculateScore() {
        if (dropRate == 150) {
            finalScore = 300 * linesCleared; }
        else if (dropRate == 300) {
            finalScore = 200 * linesCleared; }
        else if (dropRate == 600) {
            finalScore = 100 * linesCleared; }
        else {                                  // something went wrong
            return 1000000;
        }
        return finalScore;
    }

    /**
     * Method used to repaint the board graphics to represent an active game.
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    /**
     * The graphical part which paints the pieces onto the board to represent their position in the array and their
     * placement on the board.
     * @param g
     */
    private void doDrawing(Graphics g) {
        var size = getSize();
        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                Tetrominoe shape = shapeAt(j, BOARD_HEIGHT - i - 1);
                if (shape != Tetrominoe.NoShape) {
                    drawSquare(g, j * squareWidth(),
                            boardTop + i * squareHeight(), shape);
                }
            }
        }
        if (currentPiece.getShape() != Tetrominoe.NoShape) {
            for (int i = 0; i < 4; i++) {
                int x = curX + currentPiece.x(i);
                int y = curY - currentPiece.y(i);
                drawSquare(g, x * squareWidth(),
                        boardTop + (BOARD_HEIGHT - y - 1) * squareHeight(),
                        currentPiece.getShape());
            }
        }
    }

    /**
     * method that is invoked by the down key on the keyboard that drops the block down to the button most Y coordinate.
     */
    private void dropDown() {
        int newY = curY;
        while (newY > 0) {
            if (!tryMove(currentPiece, curX, newY - 1))
            {
                break;
            }
            newY--;
        }
        pieceDropped();
    }

    /**
     * method that is called to bring the block down by one line
     */
    private void oneLineDown() {
        if (!tryMove(currentPiece, curX, curY - 1)) {
            pieceDropped();
        }
    }

    /**
     * method used to clear the board when the game is initialized or played again.
     */
    private void clearBoard() {
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {
            board[i] = Tetrominoe.NoShape;
        }
    }

    /**
     * method called to determine whether the piece has touched the bottom, and calls removefulllines to check if lines
     * can be cleared. it also creates a new piece.
     */
    private void pieceDropped() {
        for (int i = 0; i < 4; i++) {
            int x = curX + currentPiece.x(i);
            int y = curY - currentPiece.y(i);
            board[(y * BOARD_WIDTH) + x] = currentPiece.getShape();
        }
        removeFullLines();
        if (!isFallDone) {
            newPiece();
        }
    }

    /**
     * method called to createa a new piece, whether thats at the beginning or after the current piece has touched
     * the bottom
     */
    private void newPiece() {
        currentPiece.setRandomShape();
        curX = BOARD_WIDTH / 2 + 1;
        curY = BOARD_HEIGHT - 1 + currentPiece.minY();
        if (!tryMove(currentPiece, curX, curY)) {
            currentPiece.setShape(Tetrominoe.NoShape);
            timer.stop();
            var msg = String.format("Game over. Lines Removed: %d", linesCleared + " Final Score: " + calculateScore());
            statusbar.setText(msg);
            saveMenu();
        }
    }

    /**
     * takes the new piece to be painted and determines whether it conflicts with another piece or the bottom.
     * if it can still move then it will repaint the new piece with it's new coordinates and.
     * @param newPiece
     * @param newX
     * @param newY
     * @return
     */
    private boolean tryMove(Shape newPiece, int newX, int newY) {
        for (int i = 0; i < 4; i++) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
                return false;
            }
            if (shapeAt(x, y) != Tetrominoe.NoShape) {
                return false;
            }
        }
        currentPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }

    /**
     * method called after a piece has fell to the bottom to see if any full lines have been completed. if a line is
     * full, then it adds to the numFullLines which is updated at the status bar as well as the finalScore.
     */
    private void removeFullLines() {
        int numFullLines = 0;
        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean lineIsFull = true;
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (shapeAt(j, i) == Tetrominoe.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }
            if (lineIsFull) {
                numFullLines++;
                for (int k = i; k < BOARD_HEIGHT - 1; k++) {
                    for (int j = 0; j < BOARD_WIDTH; j++) {
                        board[(k * BOARD_WIDTH) + j] = shapeAt(j, k + 1);
                    }
                }
            }
        }
        if (numFullLines > 0) {
            linesCleared += numFullLines;
            statusbar.setText("Lines Cleared: " + String.valueOf(linesCleared)
                    + "   Current Score: " + (calculateScore()));
            isFallDone = true;
            currentPiece.setShape(Tetrominoe.NoShape);
        }
    }

    /**
     * This method actually draws out the blocks by coloring each block. The colors are assigned using the enumeration
     * of the pieces. I reflected the colors to match the original tetris block colors. it accepts graphics g as a
     * parameter as well as the x and y coordinates of the piece and it's shape. it then fills those blocks of the shape
     * accordingly.
     * @param g
     * @param x
     * @param y
     * @param shape
     */
    private void drawSquare(Graphics g, int x, int y, Tetrominoe shape) {
        Color colors[] = {new Color(0, 0, 0),   // NOBlock
                new Color(20, 255, 20),   // RSBlock
                new Color(255, 10, 10),   // SBlock
                new Color(100, 200, 200),   // LNBlock
                new Color(170, 50, 255),    // TBlock
                new Color(200, 180, 0),     // SQBlock
                new Color(220, 150, 0),   // LBlock
                new Color(20, 10, 250)      // RLBlock
        };
        var color = colors[shape.ordinal()];
        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);
        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);
        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + 1);
    }


    /**
     * nested class used as an action listener to be called based on an event which updates the board and repaints
     * it. this is constantly called again in order to constantly update the board based on the timer/droprate
     */
    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    /**
     * method used by the GameCycle class whcih updates and repaints the board as i previously mentioned.
     */
    private void doGameCycle() {
        update();
        repaint();
    }

    /**
     * method caused to update the board. if the board is currently paused though, it will not do anything.
     * It will create a new piece if the currentpiece is at the bottom, otherwise it will move the current piece's x
     * coordinates down by one line.
     */
    private void update() {
        if (isPaused) {
            return;
        }
        if (isFallDone) {
            isFallDone = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }

    /**
     * method that creates a JFrame window that asks the user if they would like to save the game. This method is called
     * if they press the "s" key on the keyboard or as soon as the game ends. It contains a save button which leads
     * the user to a screen to enter their name which saves their name and score to the HiScores.txt file or an exit
     * button that disposes the frame.
     */
    private void saveMenu(){

        JFrame saveframe = new JFrame(gc);
        saveframe.setTitle("Pause");
        saveframe.setSize(400, 250);
        saveframe.setVisible(true);
        saveframe.setResizable(false);

        JButton bSave = new JButton ("Save");
        bSave.setBounds(60, 100, 140, 40);

        JButton bSNo = new JButton ("No");
        bSave.setBounds(200, 100, 140, 40);

        saveframe.add(bSave);
        saveframe.add(bSNo);

        saveframe.setLayout(null);

        pause();

        /**
         * the save button pulls up the second nested save menu or the saveScore() method that opens the JFrame
         * which again allows the user to input their name and save it to the HiScores.txt file.
         */
        bSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0){
                EventQueue.invokeLater(() -> {
                    try {
                        saveScore();
                        timer.stop();
                        var msg = String.format("Game over. Lines Removed: %d", linesCleared);
                        statusbar.setText(msg);
                    } catch (IOException e) {
                        saveframe.dispose();
                    }
                });
            }
        });

        /**
         * If the user selects this button, then it will not save their score. Instead I have not coded it yet, but
         * It will bring up a different window that asks if they would like to play again? With further implementation
         * I can make it so that this option is available at any time if the user wishes to restart.
         */
        bSNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0){
                EventQueue.invokeLater(() -> {
                    saveframe.dispose();
                    isPaused = !isPaused;
                });
            }
        });
    }

    /**
     * the save score method pulls up the Jframe window that prompts the user for their name to be saved alongside their
     * score into the HiScores.txt. They can also choose to cancel. Again this is implemented from an earlier
     * Homework Assignment in my CS401 class that taught me how to do this.
     * @throws IOException
     */
    private void saveScore() throws IOException {
        File hs = new File("HiScores.txt");
        JFrame hsframe = new JFrame("Save Score");
        JButton bSaveToFile= new JButton("Save");
        bSaveToFile.setBounds(60,100,140, 40);
        JButton bCancelToFile = new JButton ("Cancel");
        bCancelToFile.setBounds(200,100,140, 40);

        JLabel entName = new JLabel();
        entName.setText("Enter your Name:");
        entName.setBounds(30, 10, 100, 100);

        JLabel label = new JLabel();
        label.setBounds(10, 110, 200, 100);

        JTextField tfName = new JTextField();
        tfName.setBounds(130, 50, 130, 30);

        hsframe.add(entName);
        hsframe.add(tfName);
        hsframe.add(label);


        hsframe.add(bSaveToFile);
        hsframe.setSize(400,250);
        hsframe.setLayout(null);
        hsframe.setVisible(true);
        hsframe.add(bCancelToFile);


        /**
         * simply disposes the frame if they don't want to save after all.
         */
        bCancelToFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hsframe.dispose();
            }
        });

        /**
         * the method that actually saves the scores and names to the HiScores.txt file.
         */
        bSaveToFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = tfName.getText();
                int score = calculateScore();
                try {
                    FileWriter fw = new FileWriter(hs, true);
                    fw.append(name + "\n" + score + "\n");
                    fw.close();
                    label.setText("Score Saved.");
                }
                catch (IOException el){
                    hsframe.dispose();
                    label.setText("Error.");
                }
            }
        });
    }
    /**
     * This class acts as a keyEvent Listener that waits for key presses from the keyboard which certain keys
     * are routed to their respective functions. There are many functions I had planned but i believe I hit the 500
     * Lines of code a while ago.
     */
    class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (currentPiece.getShape() == Tetrominoe.NoShape) {
                return;
            }
            int keycode = e.getKeyCode();
            // Java 12 switch expressions
            if (keycode == KeyEvent.VK_P)
                pause();
            if (keycode == KeyEvent.VK_LEFT)
                tryMove(currentPiece, curX - 1, curY);
            if (keycode == KeyEvent.VK_RIGHT)
                tryMove(currentPiece, curX + 1, curY);
            if (keycode == KeyEvent.VK_UP)
                tryMove(currentPiece.rotateRight(), curX, curY);
            if (keycode == KeyEvent.VK_DOWN)
                tryMove(currentPiece.rotateLeft(), curX, curY);;
            if (keycode == KeyEvent.VK_SPACE)
                dropDown();
            if (keycode == KeyEvent.VK_D)
                oneLineDown();
            if (keycode == KeyEvent.VK_S)
                saveMenu();
            if (keycode == KeyEvent.VK_C)
                holdPiece();
            if(keycode == KeyEvent.VK_TAB)
                checkScores();
        }
    }


}
