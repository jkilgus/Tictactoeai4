        package com.example.tictactoe;
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

// AdapterView.OnItemSelectedListener is for the Spinner to select themes
public class GameActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button exit;

    // Sounds for end game conditions
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

    // For setting the game board background image
    private FrameLayout frame;
    private Spinner spinner;
    private static final String[] paths = {"Orange and Purple", "VTC Green and Gold", "Green and Blue",
            "Red and Blue", "Robinhood Brown and Green", "Jungle", "High-Tech"};

    private int theme;

    TextView output;

    Player x;
    Player o;
    TicTacToeState state;
    private char[][] board = new char[3][3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // For setting game board background
        frame = findViewById(R.id.PVC_MODE);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(GameActivity.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        aiMode = getIntent().getExtras().getString("com.example.tictactoe.MESSAGE");
        System.out.println(aiMode);
        if (aiMode.equals("pvceasy") || aiMode.equals("pvcmed") || aiMode.equals("pvchard")) {
            ai = true;
        } else {
            ai = false;
        }

        turnNumber = 0;
        winner = false;

        tieSound = MediaPlayer.create(this, R.raw.tie);
        loseSound = MediaPlayer.create(this, R.raw.lose1);
        winSound = MediaPlayer.create(this, R.raw.win);

        exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        numbers[0] = findViewById(R.id.one);
        numbers[1] = findViewById(R.id.two);
        numbers[2] = findViewById(R.id.three);
        numbers[3] = findViewById(R.id.four);
        numbers[4] = findViewById(R.id.five);
        numbers[5] = findViewById(R.id.six);
        numbers[6] = findViewById(R.id.seven);
        numbers[7] = findViewById(R.id.eight);
        numbers[8] = findViewById(R.id.nine);

        unsetNumbers = new LinkedList<>(Arrays.asList(numbers));
        output = findViewById(R.id.game_state);
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

    }

    // Set selected game board background from spinner choice
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
                break;
            case 6:
                frame.setBackgroundResource(R.drawable.tictactemplate_hightech);
        }
    }

    // When nothing is selected on the background spinner
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    public void buttonClick(View view) {

        // Make a way to try again/play again

        turnNumber++;

        // Player one's turn
        if ((turnNumber % 2) == 1) {

            if (theme == 6) {
                view.setBackgroundResource(R.drawable.cat);
            } else {
                view.setBackgroundResource(R.drawable.x);
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

            view.setEnabled(false);
            checkWin();
            if(aiMode.equalsIgnoreCase("pvceasy") && winner==false) {
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
        // Player two's turn
        else if (((turnNumber % 2) == 0) && aiMode.equalsIgnoreCase("pvp")) {
            if (theme == 6) {
                view.setBackgroundResource(R.drawable.dog);
            } else {
                view.setBackgroundResource(R.drawable.o);
            }


            output.setText("X's turn to play!");
            for (int i = 0; i < 9; i++) {
                if (view == numbers[i]) {
                    gamePlays[i] = 2;
                    //unsetNumbers.remove(view);
                }
            }
            view.setEnabled(false);
            checkWin();
        }
    }


    private void checkWin() {
        checkWinPlayerOne();

        checkWinPlayerTwo();

        checkTie();
    }

    private void checkWinPlayerOne () {
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

    private void checkWinPlayerTwo() {
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

    private void checkTie() {
        TextView output = findViewById(R.id.game_state);
        if(gamePlays[0] != 0 && gamePlays[1] != 0 && gamePlays[2] != 0 && gamePlays[3] != 0 && gamePlays[4] != 0
                && gamePlays[5] != 0 && gamePlays[6] != 0 && gamePlays[7] != 0 && gamePlays[8] != 0 && !winner) {
            winner =  true;
            tieSound.start();
            output.setText("Tie game!");
        }
    }

    private void disableTiles() {
        for(int i = 0; i < 9; i++) {
            numbers[i].setEnabled(false);
        }
    }

    public void resetGame(View view) {
        turnNumber = 0;
        unsetNumbers = new LinkedList<>(Arrays.asList(numbers));
        winner = false;

        for(int a = 0; a < 9; a++) {
            gamePlays[a] =0;
            numbers[a].setEnabled(true);
            numbers[a].setBackgroundResource(R.drawable.tile);
        }
    }

    /*
By default player two is the AI in AI mode.
Easy mode: Choose random open game space.
 */
    private void aiEasyPlay() {
        System.out.println("Easy\n");
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
        turnNumber++;
        System.out.println("Hard\n");
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

        System.out.println(state);
        output.setText("O's turn to play!");
        checkWin();
    }


}
