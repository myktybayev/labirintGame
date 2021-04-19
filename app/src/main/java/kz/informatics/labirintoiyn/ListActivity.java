package kz.informatics.labirintoiyn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kz.informatics.labirintoiyn.database.StoreDatabase;
import kz.informatics.labirintoiyn.models.User;

import static kz.informatics.labirintoiyn.database.StoreDatabase.COLUMN_LEVEL;
import static kz.informatics.labirintoiyn.database.StoreDatabase.COLUMN_NAME;
import static kz.informatics.labirintoiyn.database.StoreDatabase.TABLE_STUDENTS;

public class ListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager linearLayoutManager;

    UserListAdapter userListAdapter;
    private ArrayList<User> groupList;
    Button startGame;
    StoreDatabase storeDatabase;
    SQLiteDatabase sqLiteDatabase;
    TextView emptyTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        linearLayoutManager = new LinearLayoutManager(this);
        emptyTv = findViewById(R.id.emptyTv);
        recyclerView = findViewById(R.id.recyclerView);
        startGame = findViewById(R.id.startGame);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        groupList = new ArrayList<>();

        storeDatabase = new StoreDatabase(this);
        sqLiteDatabase = storeDatabase.getWritableDatabase();

        Cursor gameCursor = sqLiteDatabase.query(TABLE_STUDENTS, null, null, null, null, null, COLUMN_LEVEL + " DESC ");
        int rating = 0;

        if (gameCursor != null & gameCursor.getCount() > 0) {
            while (gameCursor.moveToNext()) {
                rating++;
                String uName = gameCursor.getString(gameCursor.getColumnIndex(COLUMN_NAME));
                int uLevel = gameCursor.getInt(gameCursor.getColumnIndex(COLUMN_LEVEL));

                User u = new User(uName, "Level: " + uLevel, " " +rating);
                groupList.add(u);
            }
        }

        if(groupList.size() == 0){
            emptyTv.setVisibility(View.VISIBLE);
        }else{
            emptyTv.setVisibility(View.INVISIBLE);
        }

        userListAdapter = new UserListAdapter(this, groupList);
        recyclerView.setAdapter(userListAdapter);
        Intent intent = getIntent();
        String userName = intent.getStringExtra("name");

        startGame.setOnClickListener(view -> {
            Intent intent1 = new Intent(ListActivity.this, MainActivity.class);
            intent1.putExtra("name", userName);

            startActivity(intent1);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.exit:

                Intent intent1 = new Intent(ListActivity.this, LoginActivity.class);
                startActivity(intent1);
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}