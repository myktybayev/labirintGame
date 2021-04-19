package kz.informatics.labirintoiyn;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

import static kz.informatics.labirintoiyn.MainActivity.startExtraTimer;

public class GameView extends View implements View.OnClickListener {


    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private Cell[][] cells;
    private Cell player, exit, prize, extraTime;

    private int COLS = 3, ROWS = 3;
    private final float WALL_THICK = 4;
    private float cellSize, hMargin, vMargin;
    private Paint wallPaint, playerPaint, exitPaint, prizePaint, extraTimePaint;
    private Random random;
    static int level = 1, prizeLevel = 3, extraTimeLevel = 5;
    Bitmap b, key, finishImg, extraTimeImg;
    Rect src, srcPrize, srcFinish, srcExtraTime;
    boolean extraTimeEnable = false, extraTimeTaken = false, exitTaken = false;

    Dialog questionTest;
    int colors[] = {R.color.light_purple_500
            , R.color.light_green, R.color.teal_200
            , R.color.teal_700, R.color.grey
            , R.color.blue};
    int indexColor = 0;
    Button bt1, bt2, bt3;
    int tests[] = {R.array.q1, R.array.q2, R.array.q3, R.array.q4, R.array.q5, R.array.q6, R.array.q7, R.array.q8};
    String questions[];
    ArrayList<String> variantsStore = new ArrayList<String>();
    ArrayList<Question> testStore = new ArrayList<Question>();

    String correctAns;
    String question;
    Context context;
    TextView textViewQuestion;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        wallPaint = new Paint();
        wallPaint.setColor(Color.BLACK);
        wallPaint.setStrokeWidth(WALL_THICK);

        questionTest = new Dialog(context);
        questionTest.setContentView(R.layout.dialog_test);
        textViewQuestion = questionTest.findViewById(R.id.textViewQuestion);
        bt1 = questionTest.findViewById(R.id.bt1);
        bt2 = questionTest.findViewById(R.id.bt2);
        bt3 = questionTest.findViewById(R.id.bt3);

        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);

        for (int i = 0; i < tests.length; i++) {
            questions = getResources().getStringArray(tests[i]);
            Question q = new Question(questions[0], questions[1], questions[2], questions[3]);
            testStore.add(q);
        }

        Collections.shuffle(testStore);

        changeQuestion();

        playerPaint = new Paint();
        COLS = 5;
        ROWS = 5;

        level = 1;
        prizeLevel = 3;
        extraTimeLevel = 5;

        exitPaint = new Paint();
        exitPaint.setColor(Color.RED);

        extraTimePaint = new Paint();
        prizePaint = new Paint();
        prizePaint.setColor(Color.YELLOW);

        random = new Random();
        b = BitmapFactory.decodeResource(getResources(), R.drawable.robot4);
        key = BitmapFactory.decodeResource(getResources(), R.drawable.key);
        finishImg = BitmapFactory.decodeResource(getResources(), R.drawable.key);
        extraTimeImg = BitmapFactory.decodeResource(getResources(), R.drawable.key);

        src = new Rect();
        src.top = 0;
        src.bottom = b.getHeight();
        src.left = 0;
        src.right = b.getWidth();

        srcPrize = new Rect();
        srcPrize.top = 0;
        srcPrize.bottom = key.getHeight();
        srcPrize.left = 0;
        srcPrize.right = key.getWidth();


        srcFinish = new Rect();
        srcFinish.top = 0;
        srcFinish.bottom = finishImg.getHeight();
        srcFinish.left = 0;
        srcFinish.right = finishImg.getWidth();

        srcExtraTime = new Rect();
        srcExtraTime.top = 0;
        srcExtraTime.bottom = extraTimeImg.getHeight();
        srcExtraTime.left = 0;
        srcExtraTime.right = extraTimeImg.getWidth();

        createLabirint();
    }

    int count = 0;

    public void changeQuestion() {

        if (count >= tests.length) {
//            Toast.makeText(context, "Сұрақтар бітті!", Toast.LENGTH_SHORT).show();
            count = 0;

        } else {

//            Collections.shuffle(testStore);
            Question current = testStore.get(count);
            question = current.getQ();
            correctAns = current.getV1();

            variantsStore.clear();
            variantsStore.add(current.getV1());
            variantsStore.add(current.getV2());
            variantsStore.add(current.getV3());

            textViewQuestion.setText(question);

            Collections.shuffle(variantsStore);

            bt1.setText(variantsStore.get(0));
            bt2.setText(variantsStore.get(1));
            bt3.setText(variantsStore.get(2));

            bt1.setBackgroundDrawable(getResources().getDrawable(R.drawable.border));
            bt2.setBackgroundDrawable(getResources().getDrawable(R.drawable.border));
            bt3.setBackgroundDrawable(getResources().getDrawable(R.drawable.border));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt1:
                checkCorrect(bt1);
                break;
            case R.id.bt2:
                checkCorrect(bt2);
                break;
            case R.id.bt3:
                checkCorrect(bt3);
                break;
        }
    }

    public void checkCorrect(Button bt) {
        if (bt.getText().toString().equals(correctAns)) {
            Log.i("checkCorrect", "keyCollected: " + keyCollected);

            count++;
            changeQuestion();
            MainActivity.correct(level);

            if (keyCollected == 1) {
                nextLevel();

            } else if (keyCollected == 2) {
                keyCollected--;

            } else if (keyCollected == 3) {
                keyCollected--;

            }

            /*
            if(prizeTaken || extraTimeTaken){
                count++;
                changeQuestion();
            }

            if(level >= prizeLevel){
                if(prizeTaken){
                    nextLevel();
                    prizeTaken = false;

                }else{
                    Log.i("levelPrize", "nooo prizeTaken: ");
                }

            }else {
                nextLevel();
            }

             */

            questionTest.dismiss();
        } else {
            MainActivity.incorrect();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(getResources().getColor(colors[indexColor]));

        int width = getWidth();
        int height = getHeight();

        if (width / height < COLS / ROWS) {
            cellSize = width / (COLS + 1);
        } else {
            cellSize = height / (ROWS + 1);
        }

        hMargin = (width - COLS * cellSize) / 2;
        vMargin = (height - ROWS * cellSize) / 2;
        canvas.translate(hMargin, vMargin);

        for (int x = 0; x < COLS; x++) {
            for (int y = 0; y < ROWS; y++) {

                if (cells[x][y].topWall) {
                    canvas.drawLine(
                            x * cellSize,
                            y * cellSize,
                            (x + 1) * cellSize,
                            y * cellSize,
                            wallPaint);
                }

                if (cells[x][y].leftWall) {
                    canvas.drawLine(
                            x * cellSize,
                            y * cellSize,
                            x * cellSize,
                            (y + 1) * cellSize,
                            wallPaint);
                }

                if (cells[x][y].bottomWall) {
                    canvas.drawLine(
                            x * cellSize,
                            (y + 1) * cellSize,
                            (x + 1) * cellSize,
                            (y + 1) * cellSize,
                            wallPaint);
                }

                if (cells[x][y].rightWall) {
                    canvas.drawLine(
                            (x + 1) * cellSize,
                            y * cellSize,
                            (x + 1) * cellSize,
                            (y + 1) * cellSize,
                            wallPaint);
                }
            }
        }

        float margin = cellSize / 10;

        if (!exitTaken) {
            Rect dstFinish = new Rect();
            dstFinish.top = (int) (exit.row * cellSize + margin);
            dstFinish.bottom = (int) ((exit.row + 1) * cellSize - margin);
            dstFinish.left = (int) (exit.col * cellSize + margin);
            dstFinish.right = (int) ((exit.col + 1) * cellSize - margin);

            canvas.drawBitmap(finishImg, srcFinish, dstFinish, exitPaint);
        }

        if (level >= extraTimeLevel && !extraTimeTaken) {
            extraTimeEnable = true;
            Rect dstExtraTime = new Rect();
            dstExtraTime.top = (int) (extraTime.row * cellSize + margin);
            dstExtraTime.bottom = (int) ((extraTime.row + 1) * cellSize - margin);
            dstExtraTime.left = (int) (extraTime.col * cellSize + margin);
            dstExtraTime.right = (int) ((extraTime.col + 1) * cellSize - margin);

            canvas.drawBitmap(extraTimeImg, srcExtraTime, dstExtraTime, extraTimePaint);

        }

        if (level >= prizeLevel && !prizeTaken) {

            Rect dstPrize = new Rect();
            dstPrize.top = (int) (prize.row * cellSize + margin);
            dstPrize.bottom = (int) ((prize.row + 1) * cellSize - margin);
            dstPrize.left = (int) (prize.col * cellSize + margin);
            dstPrize.right = (int) ((prize.col + 1) * cellSize - margin);

            canvas.drawBitmap(key, srcPrize, dstPrize, prizePaint);
        }

        Rect dst = new Rect();
        dst.top = (int) (player.row * cellSize + margin);
        dst.bottom = (int) ((player.row + 1) * cellSize - margin);
        dst.left = (int) (player.col * cellSize + margin);
        dst.right = (int) ((player.col + 1) * cellSize - margin);

        canvas.drawBitmap(b, src, dst, playerPaint);

//        canvas.drawBitmap(b,
//                player.col * cellSize + margin,
//                player.row * cellSize + margin, playerPaint);

//        canvas.drawRect(
//                player.col * cellSize + margin,
//                player.row * cellSize + margin,
//                (player.col+1) * cellSize - margin,
//                (player.row+1) * cellSize - margin,
//                playerPaint);


    }

    boolean prizeTaken = false;
    int keyCollected = 1;

    private void checkExit() {

        if (player == prize && level >= prizeLevel) {
            questionTest.show();
            prizeTaken = true;
        }

        if (player == extraTime && extraTimeEnable) {
            extraTimeTaken = true;
            extraTimeEnable = false;
            questionTest.show();
        }

        if (player == exit) {
            exitTaken = true;
            questionTest.show();
        }

    }

    public void nextLevel() {
        extraTimeTaken = false;
        prizeTaken = false;
        exitTaken = false;
        indexColor = random.nextInt(colors.length);
        level++;
        COLS++;
        ROWS++;
        createLabirint();
        invalidate();

        if (level >= extraTimeLevel) {
            keyCollected = 3;
        }else if (level >= prizeLevel) {
            keyCollected = 2;
        }
    }

    public void createLabirint() {
        Stack<Cell> stack = new Stack<>();
        Cell current, next;

        cells = new Cell[COLS][ROWS];

        for (int x = 0; x < COLS; x++) {
            for (int y = 0; y < ROWS; y++) {
                cells[x][y] = new Cell(x, y);

            }
        }
        player = cells[0][0];
        exit = cells[COLS - 1][ROWS - 1];
        prize = cells[0][ROWS - 1];
        int index1 = random.nextInt(COLS - 1);
        extraTime = cells[index1][index1];

        current = cells[0][0];
        current.visited = true;

        do {
            next = getNeighbour(current);
            if (next != null) {
                removeWall(current, next);
                stack.push(current);
                current = next;
                current.visited = true;
            } else {
                current = stack.pop();
            }
        } while (!stack.empty());
    }

    private void removeWall(Cell current, Cell next) {
        if (current.col == next.col && current.row == next.row + 1) {
            current.topWall = false;
            next.bottomWall = false;
        }

        if (current.col == next.col && current.row == next.row - 1) {
            current.bottomWall = false;
            next.topWall = false;
        }

        if (current.col == next.col + 1 && current.row == next.row) {
            current.leftWall = false;
            next.rightWall = false;
        }

        if (current.col == next.col - 1 && current.row == next.row) {
            current.rightWall = false;
            next.leftWall = false;
        }
    }

    private Cell getNeighbour(Cell cell) {
        ArrayList<Cell> neightbours = new ArrayList<>();

        //left
        if (cell.col > 0) {
            if (!cells[cell.col - 1][cell.row].visited) {
                neightbours.add(cells[cell.col - 1][cell.row]);
            }
        }

        //right
        if (cell.col < COLS - 1) {
            if (!cells[cell.col + 1][cell.row].visited) {
                neightbours.add(cells[cell.col + 1][cell.row]);
            }
        }

        //top
        if (cell.row > 0) {
            if (!cells[cell.col][cell.row - 1].visited) {
                neightbours.add(cells[cell.col][cell.row - 1]);
            }
        }

        //bottom
        if (cell.row < ROWS - 1) {
            if (!cells[cell.col][cell.row + 1].visited) {
                neightbours.add(cells[cell.col][cell.row + 1]);
            }
        }
        if (neightbours.size() > 0) {
            int index = random.nextInt(neightbours.size());
            return neightbours.get(index);
        }

        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            return true;

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float x = event.getX();
            float y = event.getY();

            float playerCenterX = hMargin + (player.col + 0.5f) * cellSize;
            float playerCenterY = vMargin + (player.row + 0.5f) * cellSize;

            float dx = x - playerCenterX;
            float dy = y - playerCenterY;

            float absDx = Math.abs(dx);
            float absDy = Math.abs(dy);

            if (absDx > cellSize || absDy > cellSize) {
                if (absDx > absDy) {
                    //move in x direction
                    if (dx > 0) {
                        //move to the right
                        movePlayer(Direction.RIGHT);
                    } else {
                        //move to the left
                        movePlayer(Direction.LEFT);
                    }

                } else {
                    //move in y direction

                    if (dy > 0) {
                        //move down
                        movePlayer(Direction.DOWN);
                    } else {
                        //move up
                        movePlayer(Direction.UP);
                    }
                }

            }

            return true;
        }

        return super.onTouchEvent(event);
    }

    private void movePlayer(Direction direction) {
        switch (direction) {
            case UP:
                if (!player.topWall)
                    player = cells[player.col][player.row - 1];

                break;
            case DOWN:
                if (!player.bottomWall)
                    player = cells[player.col][player.row + 1];
                break;
            case LEFT:
                if (!player.leftWall)
                    player = cells[player.col - 1][player.row];
                break;
            case RIGHT:
                if (!player.rightWall)
                    player = cells[player.col + 1][player.row];
                break;
        }

        checkExit();
        invalidate();
    }

    private class Cell {
        boolean topWall = true,
                leftWall = true,
                bottomWall = true,
                rightWall = true,
                visited = false;

        int col, row;

        public Cell(int col, int row) {
            this.col = col;
            this.row = row;
        }
    }

}
