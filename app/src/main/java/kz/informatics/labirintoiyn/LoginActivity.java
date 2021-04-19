package kz.informatics.labirintoiyn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import kz.informatics.labirintoiyn.database.StoreDatabase;

import static kz.informatics.labirintoiyn.database.StoreDatabase.COLUMN_NAME;
import static kz.informatics.labirintoiyn.database.StoreDatabase.TABLE_STUDENTS;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText userName;
    Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = findViewById(R.id.userName);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(userName.getText())) {
                    userName.setError("Толық толтырыңыз");
                    return;
                }

//                ContentValues userValue = new ContentValues();
//                userValue.put(COLUMN_NAME, userName.getText().toString());
//
//                sqLiteDatabase.insert(TABLE_STUDENTS, null, userValue);

                Toast.makeText(LoginActivity.this, "Ойынға қош келдің!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, ListActivity.class);
                intent.putExtra("name", userName.getText().toString());

                startActivity(intent);

            }
        });

    }
}