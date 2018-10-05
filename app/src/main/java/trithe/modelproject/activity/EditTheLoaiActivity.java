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
import trithe.modelproject.database.TheloaiDAO;

public class EditTheLoaiActivity extends AppCompatActivity {
    EditText edtenTheLoai, edVitri, edDes;
    TheloaiDAO theLoaiDAO;
    String id, name, vitri, des;
    Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_the_loai);
        edVitri = findViewById(R.id.edVtTheloai);
        edDes = findViewById(R.id.edDesTheloai);
        toolbar = findViewById(R.id.toolbarupdatetheloai);
        setSupportActionBar(toolbar);
        edtenTheLoai = findViewById(R.id.edtenTheloai);
        theLoaiDAO = new TheloaiDAO(this);
        Intent in = getIntent();
        Bundle b = in.getExtras();
        id = b.getString("ID");
        name = b.getString("NAME");
        vitri = b.getString("VITRI");
        des = b.getString("DES");
//        edMaTheloai.setText(id);
        edVitri.setText(vitri);
        edtenTheLoai.setText(name);
        edDes.setText(des);
        bottomNavigationView = findViewById(R.id.btnnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        break;
                    case R.id.hoadon:
                        startActivity(new Intent(getApplicationContext(), ListHoaDonActivity.class));
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        break;
                    case R.id.thongke:
                        startActivity(new Intent(getApplicationContext(), ThongkeActivity.class));
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        break;
                }
                return false;
            }
        });
    }

    public void outtheloai(View view) {
        finish();
    }

    //    public void updatetheloai(View view) {
//        if (theLoaiDAO.updateTheLoai(edMaTheloai.getText().toString(), edtenTheLoai.getText().toString()) > 0) {
//            Toast.makeText(getApplicationContext(), "Lưu thành công", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//    }
    public void updatetheloai(View view) {
        if (theLoaiDAO.updateTheLoai(id, edtenTheLoai.getText().toString(), edVitri.getText().toString(), edDes.getText().toString()) > 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.alertsuccessfully), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void huytheloai(View view) {
        finish();
    }
}
