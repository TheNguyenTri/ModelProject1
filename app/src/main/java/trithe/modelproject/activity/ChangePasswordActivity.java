package trithe.modelproject.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import trithe.modelproject.R;
import trithe.modelproject.database.UserDAO;
import trithe.modelproject.model.User;

public class ChangePasswordActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    EditText edPass, edRePass;
    UserDAO nguoiDungDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        toolbar = findViewById(R.id.toolbarchange);
        setSupportActionBar(toolbar);
        edPass = (EditText) findViewById(R.id.edPassword);
        edRePass = (EditText) findViewById(R.id.edConfirmpassword);
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

    public void huychange(View view) {
        finish();
    }

    public void outchange(View view) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public int validateForm() {
        int check = 1;

        if (edPass.getText().toString().length() == 0) {
            edPass.setError(getString(R.string.error_emptyps));
            check = -1;
        } else {
            String pass = edPass.getText().toString();
            String rePass = edRePass.getText().toString();
            if (!pass.equals(rePass)) {
                edRePass.setError(getString(R.string.error_likepw));
                check = -1;
            }
        }
        return check;
    }

    public void changePassword(View view) {
        SharedPreferences pref = getSharedPreferences("USERFILE", MODE_PRIVATE);
        String strUserName = pref.getString("username", "");
        nguoiDungDAO = new UserDAO(ChangePasswordActivity.this);
        User user = new User(strUserName, edPass.getText().toString(), "", "");
        try {
            if (validateForm() > 0) {
                if (nguoiDungDAO.changePasswordNguoiDung(user) > 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.alertsuccessfully),
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.alerterror),
                            Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception ex) {
            Log.e("Error", ex.toString());
        }
    }
}
