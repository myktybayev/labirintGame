package kz.informatics.labirintoiyn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import kz.informatics.labirintoiyn.database.StoreDatabase;

import static kz.informatics.labirintoiyn.database.StoreDatabase.COLUMN_LEVEL;
import static kz.informatics.labirintoiyn.database.StoreDatabase.COLUMN_NAME;
import static kz.informatics.labirintoiyn.database.StoreDatabase.TABLE_STUDENTS;

public class MainActivity extends AppCompatActivity{
    static TextView tvLevel, tv_timer;
    static ImageView img_live1, img_live2, img_live3;

    static MediaPlayer correct, incorrect;
    static Dialog dialogEndGame;

    StoreDatabase storeDatabase;
    SQLiteDatabase sqLiteDatabase;
    static long sec = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvLevel = findViewById(R.id.tvLevel);
        tv_timer = findViewById(R.id.tv_timer);
        img_live1 = findViewById(R.id.img_live1);
        img_live2 = findViewById(R.id.img_live2);
        img_live3 = findViewById(R.id.img_live3);

        storeDatabase = new StoreDatabase(this);
        sqLiteDatabase = storeDatabase.getWritableDatabase();

        Intent intent = getIntent();
        String userName = intent.getStringExtra("name");

        dialogEndGame = new Dialog(this);
        dialogEndGame.setContentView(R.layout.dialog_end_game);
        Button btnOk = dialogEndGame.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEndGame.dismiss();
                ContentValues userValue = new ContentValues();
                userValue.put(COLUMN_NAME, userName);
                userValue.put(COLUMN_LEVEL, userLevel);

                sqLiteDatabase.insert(TABLE_STUDENTS, null, userValue);

                Intent intent1 = new Intent(MainActivity.this, ListActivity.class);
                intent1.putExtra("name", userName);

                startActivity(intent1);
            }
        });
        lifeCount = 3;
        correct = MediaPlayer.create(getApplicationContext(), R.raw.correct_audio);
        incorrect = MediaPlayer.create(getApplicationContext(), R.raw.incorrect);
    }

    static CountDownTimer countDownTimer;

    public static void startExtraTimer() {
        countDownTimer.cancel();
        startTimer((int) (sec + 10));
    }

    public static void startTimer(int second) {

        countDownTimer = new CountDownTimer(second * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                sec = millisUntilFinished / 1000;
                String secStr = "00:";

                if (sec <= 9) {
                    secStr = secStr + "0" + sec;
                } else {
                    secStr = secStr + sec;
                }

                tv_timer.setText(secStr);
            }

            public void onFinish() {
                dialogEndGame.show();
                tv_timer.setText("done!");
            }

        };
        countDownTimer.start();
    }

    static int userLevel = 0, lifeCount = 3;
    public static void correct(int level) {
        userLevel = level;
        tvLevel.setText("Level: "+level);
        correct.start();
    }

    public static void incorrect(){
        if(lifeCount == 0){
            dialogEndGame.show();
        }

        lifeCount--;

        switch (lifeCount){
            case 2:
                img_live1.setVisibility(View.INVISIBLE);
                break;
            case 1:
                img_live2.setVisibility(View.INVISIBLE);
                break;
            case 0:
                img_live3.setVisibility(View.INVISIBLE);
                break;
        }
        incorrect.start();
    }

}