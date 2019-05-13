        package com.example.tictactoe;
        import android.graphics.Color;
        import android.media.MediaPlayer;
        import android.os.Handler;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.FrameLayout;
        import android.widget.Spinner;
        import android.widget.TextView;

        import com.example.tictactoe.AIGamePlayingSolution.src.minimax.Action;
        import com.example.tictactoe.AIGamePlayingSolution.src.minimax.AlphaBetaPlayer;
        import com.example.tictactoe.AIGamePlayingSolution.src.minimax.HumanPlayer;
        import com.example.tictactoe.AIGamePlayingSolution.src.minimax.Player;
        import com.example.tictactoe.AIGamePlayingSolution.src.tictactoe.TicTacToeState;

        import java.util.Arrays;
        import java.util.Collections;
        import java.util.LinkedList;
        import java.util.List;

/** AdapterView.OnItemSelectedListener is for the Spinner to select themes **/
public class GameActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button exit;

    /** Sounds for end game conditions **/
    MediaPlayer tieSound;
    MediaPlayer winSound;
    MediaPlayer loseSound;

    /**
     * gamePlay is an array with 9 elements, one for each place in tictactoe
     * 0 is no one has played there
     * 1 is player one has played there
     * 2 is player two have played there
     */
    private int[] gamePlays = {0,0,0,0,0,0,0,0,0};

    /** ArrayList of the Image Buttons */
    private View[] numbers = new View[9];


    /** Number of turns that have gone by */
    private int turnNumber;

    /**If the game has a winner then true, if not, then false */
    private boolean winner;

    //---
    /**If the game is in AI mode the set true in code*/
    private String aiMode;
    private boolean ai;

    /**Updates to have all possible moves if in AI mode*/
    private List<View> unsetNumbers;

    /**For setting the game board background image **/
    private FrameLayout frame;
    private Spinner spinner;
    private static final String[] paths = {"Orange and Purple", "VTC Green and Gold", "Green and Blue",
            "Red and Blue", "Robinhood Brown and Green", "Jungle", "High-Tech"};

    /** Tracks which theme is selected **/
    private int theme;

    /** Output text view to show who won or if it was a tie, or who's turn it is **/
    TextView output;

    /** Initialize players and game board **/
    Player x;
    Player o;
    TicTacToeState state;
    private char[][] board = new char[3][3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        /** For setting game board background with spinner **/
        frame = findViewById(R.id.PVC_MODE);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(GameActivity.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        /** Recieve difficulty setting from previous screen to choose AI mode **/
        aiMode = getIntent().getExtras().getString("com.example.tictactoe.MESSAGE");
        System.out.println(aiMode);
        if (aiMode.equals("pvceasy") || aiMode.equals("pvcmed") || aiMode.equals("pvchard")) {
            ai = true;
        } else {
            ai = false;
        }

        /** Initialize game state with turn number zero, and no winner **/
        turnNumber = 0;
        winner = false;

        /** Sound effects for various end game states **/
        tieSound = MediaPlayer.create(this, R.raw.tie);
        loseSound = MediaPlayer.create(this, R.raw.lose1);
        winSound = MediaPlayer.create(this, R.raw.win);

        /** Exit button **/
        exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /** The game tiles **/
        numbers[0] = findViewById(R.id.one);
        numbers[1] = findViewById(R.id.two);
        numbers[2] = findViewById(R.id.three);
        numbers[3] = findViewById(R.id.four);
        numbers[4] = findViewById(R.id.five);
        numbers[5] = findViewById(R.id.six);
        numbers[6] = findViewById(R.id.seven);
        numbers[7] = findViewById(R.id.eight);
        numbers[8] = findViewById(R.id.nine);

        /** Tiles yet to be played on, for consideration by the AI **/
        unsetNumbers = new LinkedList<>(Arrays.asList(numbers));

        /** Text output to display end game condition: Win, Lose, Tie **/
        output = findViewById(R.id.game_state);

        /** Create new X and O players, create new game board state **/
        if (aiMode.equalsIgnoreCase("pvcmed") || aiMode.equalsIgnoreCase("pvchard")) {
            x = new AlphaBetaPlayer("X");
            o = new HumanPlayer("O");
            state = new TicTacToeState(x, o, board);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    board[i][j] = '-';
                }
            }
        }
        if (theme == 6 || theme == 7) {
            output.setTextColor(Color.WHITE);
        } else {
            output.setTextColor(Color.BLACK);
        }
        output.setText("X starts first!");
    }

    /** This switch operates the Spinner dropdown choice menu
     * Each theme has it's own background and some have special
     * image tiles and ideally their own sounds too
     * The theme number is being kept track of so that playable icons
     * can be chosen during the player functions below using if statements**/
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch (position) {
            case 0:
                frame.setBackgroundResource(R.drawable.tictactemplate_orange_purp);
                theme = 1;
                break;
            case 1:
                frame.setBackgroundResource(R.drawable.tictactemplate_greengoldvtc);
                theme = 2;
                break;
            case 2:
                frame.setBackgroundResource(R.drawable.tictactemplate_greenblue);
                theme = 3;
                break;
            case 3:
                frame.setBackgroundResource(R.drawable.tictactemplate_redblue);
                theme = 4;
                break;
            case 4:
                frame.setBackgroundResource(R.drawable.tictactemplate_robinhood);
                theme = 5;
                break;
            case 5:
                frame.setBackgroundResource(R.drawable.tictactemplate_jungle);
                theme = 6;
                output.setTextColor(Color.WHITE);
                break;
            case 6:
                frame.setBackgroundResource(R.drawable.tictactemplate_hightech);
                theme = 7;
                output.setTextColor(Color.WHITE);
                break;
        }
    }

    /** When nothing is selected on the background spinner nothing happens
     * but java would still like to have this method here**/
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    public void buttonClick(View view) {

        turnNumber++;

        /** Player 1's turn **/
        if ((turnNumber % 2) == 1) {

            /** Set playable icons based on theme **/
            if (theme == 6) {
                view.setBackgroundResource(R.drawable.cat);
            } else {
                view.setBackgroundResource(R.drawable.x);
            }

            /** After move is made, set text to show it's the next player's turn
             * and update the state of the game board
             */

            if (theme == 6 || theme == 7) {
                output.setTextColor(Color.WHITE);
            } else {
                output.setTextColor(Color.BLACK);
            }
            output.setText("O's turn to play!");


            unsetNumbers.remove(view);
            int i;
            for (i = 0; i < 9; i++) {
                if (view == numbers[i]) {
                    gamePlays[i] = 1;
                    unsetNumbers.remove(view);
                    break;
                }
            }

            /** If playing versus a Computer player in medium or hard, this section gets activated
             * which sends the game state to the Easy and Hard AI methods way down below**/
            if (aiMode.equalsIgnoreCase("pvcmed") || aiMode.equalsIgnoreCase("pvchard")) {

                int row;
                int col;
                row = i/3;
                col = i%3;
                board[row][col] = 'O';


                state = new TicTacToeState(x, o, board);
                Action playerMove = new Action(state, 1);
                playerMove.perform();
                checkWin();
                }

            /** After Player 1 the human player has gone, and the medium and hard AI have
             * gotten a chance to run, the view is disabled and we check for win states
             */
            view.setEnabled(false);
            checkWin();

            /** These sections modulate the various AI difficulties.
             * If easy mode, easy AI is simply called. If Hard AI, hard AI is called.
             * For Medium AI, alternating calls are made between Easy and Hard AI.
             */
            if(aiMode.equalsIgnoreCase("pvceasy") && winner==false) {
                if (theme == 6 || theme == 7) {
                    output.setTextColor(Color.WHITE);
                } else {
                    output.setTextColor(Color.BLACK);
                }
                output.setText("O's turn to play!");

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        checkWin();
                        aiEasyPlay();
                    }
                };
                Handler hand = new Handler();
                hand.postDelayed(r, 1000);
            }

            if(aiMode.equalsIgnoreCase("pvcmed") && winner==false) {
                double rand = Math.random();
                Runnable r;
                if (rand < .5) {
                    r = new Runnable() {
                        @Override
                        public void run() {
                            checkWin();
                            aiEasyPlay();
                        }
                    };
                } else {
                    r = new Runnable() {
                        @Override
                        public void run() {
                            checkWin();
                            aiHardPlay();
                        }
                    };
                }
                Handler hand = new Handler();
                hand.postDelayed(r, 1000);
            }

            if(aiMode.equalsIgnoreCase("pvchard") && winner==false) {

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        checkWin();
                        aiHardPlay();
                    }
                };
                Handler hand = new Handler();
                hand.postDelayed(r, 1000);
            }
        }

        /** For player vs player mode, this is player 2's turn **/
        else if (((turnNumber % 2) == 0) && aiMode.equalsIgnoreCase("pvp")) {

            /** Custom playable icons based on theme **/
            if (theme == 6) {
                view.setBackgroundResource(R.drawable.dog);
            } else {
                view.setBackgroundResource(R.drawable.o);
            }
            output.setText("X's turn to play!");
            if (theme == 6 || theme == 7) {
                output.setTextColor(Color.WHITE);
            } else {
                output.setTextColor(Color.BLACK);
            }
                output.setText("X's turn to play!");

            for (int i = 0; i < 9; i++) {
                if (view == numbers[i]) {
                    gamePlays[i] = 2;
                }
            }
            view.setEnabled(false);
            checkWin();
        }
    }

    /** Checking to see if the game is over **/
    private void checkWin() {
        checkWinPlayerOne();

        checkWinPlayerTwo();

        checkTie();
    }

    /** Tests every combination of 3 in a row for win states **/
    private void checkWinPlayerOne () {
        if (theme == 6 || theme == 7) {
            output.setTextColor(Color.WHITE);
        } else {
            output.setTextColor(Color.BLACK);
        }

        TextView output = findViewById(R.id.game_state);
        if (gamePlays[0] == 1 && gamePlays[1] == 1 && gamePlays[2] == 1) {
            output.setText("The winner is X!");
            winner = true;
            winSound.start();
            disableTiles();
        }
        if(gamePlays[0] == 1 && gamePlays[3] == 1 && gamePlays[6] == 1) {
            output.setText("The winner is X!");
            winner = true;
            winSound.start();
            disableTiles();
        }
        if(gamePlays[2] == 1 && gamePlays[5] == 1 && gamePlays[8] == 1) {
            output.setText("The winner is X!");
            winner = true;
            winSound.start();
            disableTiles();
        }
        if(gamePlays[6] == 1 && gamePlays[7] == 1 && gamePlays[8] == 1) {
            output.setText("The winner is X!");
            winner = true;
            winSound.start();
            disableTiles();
        }
        if(gamePlays[3] == 1 && gamePlays[4] == 1 && gamePlays[5] == 1) {
            output.setText("The winner is X!");
            winner = true;
            winSound.start();
            disableTiles();
        }
        if(gamePlays[1] == 1 && gamePlays[4] == 1 && gamePlays[7] == 1) {
            output.setText("The winner is X!");
            winner = true;
            winSound.start();
            disableTiles();
        }
        if(gamePlays[0] == 1 && gamePlays[4] == 1 && gamePlays[8] == 1) {
            output.setText("The winner is X!");
            winner = true;
            winSound.start();
            disableTiles();
        }
        if(gamePlays[6] == 1 && gamePlays[4] == 1 && gamePlays[2] == 1) {
            output.setText("The winner is X!");
            winner = true;
            winSound.start();
            disableTiles();
        }
    }

    /** Again, testing every combination of 3 in a row but this time for player 2 **/
    private void checkWinPlayerTwo() {
        if (theme == 6 || theme == 7) {
            output.setTextColor(Color.WHITE);
        } else {
            output.setTextColor(Color.BLACK);
        }

        TextView output = findViewById(R.id.game_state);
        if (gamePlays[0] == 2 &&  gamePlays[1] == 2 && gamePlays[2] == 2) {
            output.setText("The winner is 0!");
            winner = true;
            if(ai) {
                loseSound.start();
            } else {
                winSound.start();
            }
            disableTiles();
        }
        if(gamePlays[0] == 2 && gamePlays[3] == 2 && gamePlays[6] == 2) {
            output.setText("The winner is O!");
            winner = true;
            if(ai) {
                loseSound.start();
            } else {
                winSound.start();
            }
            disableTiles();
        }
        if(gamePlays[2] == 2 && gamePlays[5] == 2 && gamePlays[8] == 2) {
            output.setText("The winner is O!");
            winner = true;
            if(ai) {
                loseSound.start();
            } else {
                winSound.start();
            }
            disableTiles();
        }
        if(gamePlays[6] == 2 && gamePlays[7] == 2 && gamePlays[8] == 2) {
            output.setText("The winner is O!");
            winner = true;
            if(ai) {
                loseSound.start();
            } else {
                winSound.start();
            }
            disableTiles();
        }
        if(gamePlays[3] == 2 && gamePlays[4] == 2 && gamePlays[5] == 2) {
            output.setText("The winner is O!");
            winner = true;
            if(ai) {
                loseSound.start();
            } else {
                winSound.start();
            }
            disableTiles();
        }
        if(gamePlays[1] == 2 && gamePlays[4] == 2 && gamePlays[7] == 2) {
            output.setText("The winner is O!");
            winner = true;
            if(ai) {
                loseSound.start();
            } else {
                winSound.start();
            }
            disableTiles();
        }
        if(gamePlays[0] == 2 && gamePlays[4] == 2 && gamePlays[8] == 2) {
            output.setText("The winner is O!");
            winner = true;
            if(ai) {
                loseSound.start();
            } else {
                winSound.start();
            }
            disableTiles();
        }
        if(gamePlays[6] == 2 && gamePlays[4] == 2 && gamePlays[2] == 2) {
            output.setText("The winner is O!");
            winner = true;
            if(ai) {
                loseSound.start();
            } else {
                winSound.start();
            }
            disableTiles();
        }
    }

    /** If a tie occurs, winner must still be set to true. It doesn't mean there is actually
     * a winner, but when it is set to true that means the end state is triggered. Without this
     * the game keeps running.
     */
    private void checkTie() {
        if (theme == 6 || theme == 7) {
            output.setTextColor(Color.WHITE);
        } else {
            output.setTextColor(Color.BLACK);
        }

        TextView output = findViewById(R.id.game_state);
        if(gamePlays[0] != 0 && gamePlays[1] != 0 && gamePlays[2] != 0 && gamePlays[3] != 0 && gamePlays[4] != 0
                && gamePlays[5] != 0 && gamePlays[6] != 0 && gamePlays[7] != 0 && gamePlays[8] != 0 && !winner) {
            winner =  true;
            tieSound.start();
            output.setText("Tie game!");
        }
    }

    /** Disable tiles so you can't make multiple moves quickly before the computer goes **/
    private void disableTiles() {
        for(int i = 0; i < 9; i++) {
            numbers[i].setEnabled(false);
        }
    }

    /** Restarts the game so you don't have to go back a menu to start again **/
    public void resetGame(View view) {
        turnNumber = 0;
        unsetNumbers = new LinkedList<>(Arrays.asList(numbers));
        winner = false;

        for(int a = 0; a < 9; a++) {
            gamePlays[a] = 0;
            numbers[a].setEnabled(true);
            numbers[a].setBackgroundResource(R.drawable.tile);
        }

        /** The game tiles **/
        numbers[0] = findViewById(R.id.one);
        numbers[1] = findViewById(R.id.two);
        numbers[2] = findViewById(R.id.three);
        numbers[3] = findViewById(R.id.four);
        numbers[4] = findViewById(R.id.five);
        numbers[5] = findViewById(R.id.six);
        numbers[6] = findViewById(R.id.seven);
        numbers[7] = findViewById(R.id.eight);
        numbers[8] = findViewById(R.id.nine);

        /** Tiles yet to be played on, for consideration by the AI **/
        unsetNumbers = new LinkedList<>(Arrays.asList(numbers));

        /** Text output to display end game condition: Win, Lose, Tie **/
        output = findViewById(R.id.game_state);

        /** Create new X and O players, create new game board state **/
        if (aiMode.equalsIgnoreCase("pvcmed") || aiMode.equalsIgnoreCase("pvchard")) {
            x = new AlphaBetaPlayer("X");
            o = new HumanPlayer("O");
            state = new TicTacToeState(x, o, board);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    board[i][j] = '-';
                }
            }
        }
        if (theme == 6 || theme == 7) {
            output.setTextColor(Color.WHITE);
        } else {
            output.setTextColor(Color.BLACK);
        }
        output.setText("X starts first!");

    }

    /**
    By default player two is the AI in AI mode.
    Easy mode: Choose random open game space.

     These are the actual Easy and Hard AI moves
     that easy, medium, and hard above call combinations of

     The above Easy, Medium, and Hard AI methods feed the board
     and other state info into these methods which then
     execute the play and then over write the new board state,
     and then pass the turn back to Player 1.
    **/
    private void aiEasyPlay() {
        if (theme == 6 || theme == 7) {
            output.setTextColor(Color.WHITE);
        } else {
            output.setTextColor(Color.BLACK);
        }

        turnNumber++;
        Collections.shuffle(unsetNumbers);

        // Sets playable icons from default x, o, or per theme
        if (theme == 6) {
            unsetNumbers.get(0).setBackgroundResource(R.drawable.dog);
        } else {
            unsetNumbers.get(0).setBackgroundResource(R.drawable.o);
        }

        output.setText("X's turn to play!");
        unsetNumbers.get(0).setEnabled(false);
        for (int i = 0; i < 9; i++) {
            if (unsetNumbers.get(0) == numbers[i]) {
                gamePlays[i] = 2;
                int row = i / 3;
                int col = i % 3;
                board[row][col] = 'X';
            }
        }
        unsetNumbers.remove(0);
        checkWin();
    }

    private void aiHardPlay() {
        if (theme == 6 || theme == 7) {
            output.setTextColor(Color.WHITE);
        } else {
            output.setTextColor(Color.BLACK);
        }

        turnNumber++;
        state.setBoard(board);
        Action move = x.chooseMove(state);
        state = (TicTacToeState) move.perform();

        // i row j col -- using board from IA


        board = state.getBoard();


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 'X' && unsetNumbers.contains(numbers[(i*3)+j])) {
                    if (theme == 6) {
                        numbers[(i*3)+j].setBackgroundResource(R.drawable.dog);
                    } else {
                        numbers[(i*3)+j].setBackgroundResource(R.drawable.o);
                    }
                    output.setText("X's turn to play!");
                    gamePlays[(i*3)+j] = 2;
                    unsetNumbers.remove(numbers[(i*3)+j]);
                    numbers[(i*3)+j].setEnabled(false);
                }
            }
        }
        output.setText("X's turn to play!");
        checkWin();
    }
}
