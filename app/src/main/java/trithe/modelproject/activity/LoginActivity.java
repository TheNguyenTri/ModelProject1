package trithe.modelproject.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import trithe.modelproject.R;
import trithe.modelproject.database.UserDAO;
import trithe.modelproject.model.User;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUser, edtPass;
    private CheckBox checkBox;
    private UserDAO userDAO;
    public static List<User> userlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtUser = findViewById(R.id.edUserLogin);
        edtPass = findViewById(R.id.edPassLogin);
        checkBox = findViewById(R.id.chkrememberme);
        userDAO = new UserDAO(this);
        restore();
    }

    public void login(View view) {
        save();
    }

    private void save() {
        String user = edtUser.getText().toString();
        User userdb = userDAO.getUser(user);
        String pass = edtPass.getText().toString();

        boolean chk = checkBox.isChecked();
        if (user.equals("")) {
            edtUser.setError(getString(R.string.error_empty));
        } else if (pass.equals("")) {
            edtPass.setError(getString(R.string.error_emptyps));
        } else if (pass.length() < 6) {
            edtPass.setError(getString(R.string.error_length));
        } else {
            String originalPassword = userdb.getPassword();
            String original = userdb.getUserName();
//            if (userDAO.checkLogin(user, pass) > 0) {
//                remember(user, pass, chk);
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            if (user.equals("admin") && pass.equals("admin1")) {
                remember(user, pass, chk);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
            } else if (original.equals(user) && originalPassword.equals(pass)) {
                remember(user, pass, chk);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
            } else if (!(user.equals("admin") && pass.equals("admin1")) || !(original.equals(user) && originalPassword.equals(pass))) {
                Toast.makeText(getApplicationContext(), getString(R.string.wrong_user_pass), Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void remember(String u, String p, boolean check) {
        SharedPreferences sharedPreferences = getSharedPreferences("USERFILE", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!check) {
            editor.clear();
        } else {
            editor.putString("username", u);
            editor.putString("password", p);
            editor.putBoolean("cbo", check);
        }
        editor.commit();
    }

    private void restore() {
        SharedPreferences pref = getSharedPreferences("USERFILE", MODE_PRIVATE);
        boolean check = pref.getBoolean("cbo", false);
        if (check) {
            String user = pref.getString("username", "");
            String pass = pref.getString("password", "");
            edtUser.setText(user);
            edtPass.setText(pass);
        }
        checkBox.setChecked(check);
    }

}
