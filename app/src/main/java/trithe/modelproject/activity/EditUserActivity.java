package trithe.modelproject.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import trithe.modelproject.R;
import trithe.modelproject.database.UserDAO;

public class EditUserActivity extends AppCompatActivity {
    EditText editName, editPhone;
    UserDAO userDAO;
    String editusername, editname, editphone;
    Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        editName = findViewById(R.id.edFullName);
        editPhone = findViewById(R.id.edPhone);
        toolbar = findViewById(R.id.toolbarupdateuser);
        setSupportActionBar(toolbar);
        userDAO = new UserDAO(this);
        Intent in = getIntent();
        Bundle b = in.getExtras();
        editname = b.getString("NAME");
        editphone = b.getString("PHONE");
        editusername = b.getString("USERNAME");
        editName.setText(editname);
        editPhone.setText(editphone);
        bottomNavigationView = findViewById(R.id.btnnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                        break;
                    case R.id.hoadon:
                        startActivity(new Intent(getApplicationContext(), ListHoaDonActivity.class));
                        overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                        break;
                    case R.id.thongke:
                        startActivity(new Intent(getApplicationContext(), ThongkeActivity.class));
                        overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                        break;
                }
                return false;
            }
        });
    }

    public void out(View view) {
        finish();
    }

    public void updateuser(View view) {
        if (editName.getText().toString().equals("")) {
            editName.setError(getString(R.string.error_emptyname));
        } else if (editName.getText().toString().length() > 20) {
            editName.setError(getString(R.string.lengthname));
        }else  if(editPhone.getText().toString().equals("")) {
            editPhone.setError(getString(R.string.error_emptyphone));
        }else if(editPhone.getText().toString().length()<10 || editPhone.getText().toString().length()>11){
            editPhone.setError(getString(R.string.error_emptyphonelength));
        } else if (userDAO.updateUser(editusername, editName.getText().toString(), editPhone.getText().toString()) > 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.alertsuccessfully), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void huyuser(View view) {
       editName.setText("");
       editPhone.setText("");
    }
}
