package tetripoff_pack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.awt.GraphicsConfiguration;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** The Tetripoff class is the driver that initializes the UI to select various menu options.
 *  It also allow the user to choose the difficulty of the game which is passed to the Board.
 * @author Angelito Sabino
 * with heavy influence from Jan Bodnar who created a tutorial on zetcode:
 * http://zetcode.com/tutorials/javagamestutorial/tetris/ which taught me step by step
 * how to recreate this game. Out of all the videos and tutorials, I found his/her's to be
 * the simplest and easiest to understand.
 * This class also incoporates JFrame which is currently part of what we are learning in my CS401 class.
 */

public class Tetripoff extends JFrame {

    private JLabel statusbar;
    static GraphicsConfiguration gc;

    int diffLev = 0;

    /**
     * The Tetripoff constructor takes the difficulty level from the user and becomes a variable
     * to be used when the board initializes the speed and calculates the score.
     * @param df
     */
    public Tetripoff(int df) {
        setDiffLev(df);
        initUI();
    }

    /**
     * setter method used for constructor to set the difficulty level that is to be passed to the Board.
     * @param d
     */
    private void setDiffLev(int d){
        this.diffLev = d;
    }
    private int getDiffLev(){
        return this.diffLev;
    }

    /**
     * This method initializes the User Interface using JFrame and contains buttons that pull up nested menus
     * that contain functions and methods such as "Play" which allows the user to select the difficulty on
     * the nested Menu.
     */
    private void initUI() {
        statusbar = new JLabel(" 0");
        add(statusbar, BorderLayout.SOUTH);
        var board = new Board(this, diffLev);
        add(board);
        board.start();
        setTitle("Tetripoff");
        setSize(400, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * simple label used for updating the user at the bottom of the game status.
     * i.e. "paused" or how many lines are currently cleared.
     * @return
     */
    JLabel getStatusBar() {
        return statusbar;
    }

    /**
     * Main method used to start up the program and initializes the User Interface using JFrame and contains buttons
     * that pull up nested menus that contain functions and methods such as "Play" which allows the user to select the
     * difficulty on the nested Menu.
     * @param args
     */
    public static void main(String[] args) {
        int ez = 600;
        int norm = 300;
        int hard = 150;

        JButton bPlay = new JButton("Play");
        JButton bHiScores = new JButton("Hi-Scores");
        JButton bHowToPlay = new JButton("How to Play");
        JButton bExit = new JButton("Exit");
        JLabel tetripoff = new JLabel(("TET-RI-POFF"));


        JFrame frame = new JFrame(gc);
        frame.setTitle("Tet-ri-poff");
        frame.setSize(600, 800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);

        frame.add(tetripoff);
        frame.add(bPlay);
        frame.add(bHiScores);
        frame.add(bHowToPlay);
        frame.add(bExit);

        tetripoff.setBounds(263, 20, 200, 40);
        bPlay.setBounds(200, 100, 200, 80);
        bHiScores.setBounds(200, 250, 200, 80);
        bHowToPlay.setBounds(200, 400, 200, 80);
        bExit.setBounds(200, 550, 200, 80);


        bPlay.addActionListener(new ActionListener() {

            /** A Difficulty Menu "DiffMenu" Pops up when you press this Play button */
            @Override
            public void actionPerformed(ActionEvent arg0) {

                JFrame DiffMenu = new JFrame(gc);
                DiffMenu.setTitle("Difficulty");
                DiffMenu.setSize(600, 800);
                DiffMenu.setVisible(true);
                DiffMenu.setResizable(false);
                DiffMenu.setLayout(null);

                JButton bEasy = new JButton("EASY");
                JButton bNormal = new JButton("NORMAL");
                JButton bHard = new JButton("HARD");

                DiffMenu.add(bEasy);
                DiffMenu.add(bNormal);
                DiffMenu.add(bHard);

                bEasy.setBounds(200, 150, 200, 80);
                bNormal.setBounds(200, 325, 200, 80);
                bHard.setBounds(200, 500, 200, 80);

                // was trying to figure out how to do this in an easier way.
                bEasy.addActionListener(new ActionListener() {
                    /** The difficulty level passed into the Tetripoff Class is assigned to the ez variable*/
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        EventQueue.invokeLater(() -> {
                            var game = new Tetripoff(ez);      // EASY MODE
                            game.setVisible(true);
                        });
                    }
                });
                /** The difficulty level passed into the Tetripoff Class is assigned to the norm variable*/
                bNormal.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        EventQueue.invokeLater(() -> {
                            var game = new Tetripoff(norm);    // NORMAL MODE
                            game.setVisible(true);
                        });
                    }
                });
                /** The difficulty level int passed into the Tetripoff Class is assigned to the hard variable*/
                bHard.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        EventQueue.invokeLater(() -> {
                            var game = new Tetripoff(hard);    // HARD MODE
                            game.setVisible(true);
                        });
                    }
                });
            }
        });


        bHowToPlay.addActionListener(new ActionListener() {
            /** When the "How to Play button is selected, this function calls the "HowToPlay.txt" file and reads
             * the text to instruct the player of the controls. If only i finished this in time.
             */
            @Override
            public void actionPerformed(ActionEvent arg0) {
                EventQueue.invokeLater(() -> {

                    JFrame htpframe = new JFrame(gc);
                    htpframe.setTitle("How To Play");
                    htpframe.setSize(600, 800);
                    htpframe.setVisible(true);
                    htpframe.setResizable(false);
                    htpframe.setLayout(null);

                    JButton bhtpOK = new JButton ("Okie-Dokie");
                    bhtpOK.setBounds(250, 650, 100, 50);

                    htpframe.add(bhtpOK);
                    //create function to read text from HowToPlay file and display on frame.
                    bhtpOK.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            EventQueue.invokeLater(() -> {
                                htpframe.dispose();
                            });
                        }
                    });

                });
            }
        });

        bHiScores.addActionListener(new ActionListener() {
            /**
             * when the "Hi Scores" Button is selected, this brings up a window that
             * allows the user to read from the "HiScores.txt" file that has the names and scores of the
             * players who saved their score after playing a single game.
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                    Scanner sc = new Scanner("HiScores.txt");
                    System.out.println("Name : Number\n" +  "--------------" );
                    while(sc.hasNextLine()){
                        System.out.println(sc.nextLine() + " : " + sc.nextLine());
                    }
                    System.out.println("--------------");
                   */
                try (BufferedReader br = new BufferedReader(new FileReader(new File("HiScores.txt")))) {
                    String text = null;
                    while ((text = br.readLine()) != null)
                    {
                            System.out.println(br.readLine() + " : " + br.readLine());
                        }
                        System.out.println("--------------");
                    } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        /**
         * as simple as it gets, this closes the main frame which closes the whole program.
         */
        bExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                EventQueue.invokeLater(() -> {
                    frame.dispose();
                });
            }
        });
    }
}

/*

bhtpOK.addActionListener(new ActionListener() {
         @Override
          public void actionPerformed(ActionEvent arg0) {
           EventQueue.invokeLater(() -> {

           });
         }
});*/