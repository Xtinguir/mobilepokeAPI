package id.ac.umn.projectuas00000013728;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private ArrayList<User> userArrayList;
    String DB_NAME = "Credential.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText edtUsername = findViewById(R.id.edtUsername);
        final EditText edtPassword = findViewById(R.id.edtPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean success=false;
                for(int i = 0;i<userArrayList.size();i++){
                    if(edtUsername.getText().toString().equals(userArrayList.get(i).getUser_name().toString()) && edtPassword.getText().toString().equals(userArrayList.get(i).getUser_password().toString())){
                        success = true;
                        break;
                    }
                }
                if(!success){
                    Toast
                            .makeText(MainActivity.this,"Pasangan username-password tidak cocok" + edtUsername.getText() + " " + edtPassword.getText(), Toast.LENGTH_LONG)
                            .show();
                }
                else{
                    Intent intent = new Intent(MainActivity.this, ListPokemonActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }
    void getData(){
        userArrayList = new ArrayList<User>();
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(getApplicationContext(),DB_NAME,null,1);
        databaseAdapter.openDatabase();
        Cursor cursor = databaseAdapter.getAllUsers();
        if(cursor!=null && (cursor.getCount()>0)){
            userArrayList.clear();
            cursor.moveToFirst();
            do{
                String user_name = cursor.getString(0);
                String user_password = cursor.getString(1);
                userArrayList.add(new User(user_name,user_password));
            }while(cursor.moveToNext());
        }
        databaseAdapter.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dialog,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.menu_dialog_aboutme:
                intent = new Intent(MainActivity.this,AboutMeActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
