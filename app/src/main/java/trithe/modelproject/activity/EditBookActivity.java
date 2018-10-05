package trithe.modelproject.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import trithe.modelproject.R;
import trithe.modelproject.database.SachDAO;
import trithe.modelproject.database.TheloaiDAO;
import trithe.modelproject.model.Theloai;

public class EditBookActivity extends AppCompatActivity {
    SachDAO sachDAO;
    TheloaiDAO theLoaiDAO;
    Spinner spnTheLoai;
    EditText edTenSach, edGiaBia, edSoLuong, edProducer, edAuthor;
    String maTheLoai = "";
    String masach;
    List<Theloai> listTheLoai = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;
    private ImageView imageView;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);
        spnTheLoai = (Spinner) findViewById(R.id.speditbook);
        getTheLoai();
        imageView = findViewById(R.id.outthembook);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar = findViewById(R.id.toolbareditbook);
        setSupportActionBar(toolbar);
        edTenSach = (EditText) findViewById(R.id.edTenSach);
        edGiaBia = (EditText) findViewById(R.id.edGiaBia);
        edAuthor = findViewById(R.id.edAuthorSach);
        edProducer = findViewById(R.id.edProducerSach);
        edSoLuong = (EditText) findViewById(R.id.edSoLuong);
        sachDAO = new SachDAO(this);
        spnTheLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maTheLoai = listTheLoai.get(spnTheLoai.getSelectedItemPosition()).getMaTheLoai();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Intent in = getIntent();
        Bundle b = in.getExtras();
        masach = b.getString("MASACH");
        maTheLoai = b.getString("MATHELOAI");
        edTenSach.setText(b.getString("TENSACH"));
        edProducer.setText(b.getString("PRODUCER"));
        edAuthor.setText(b.getString("AUTHOR"));
        edGiaBia.setText(b.getString("GIABIA"));
        edSoLuong.setText(b.getString("SOLUONG"));
        spnTheLoai.setSelection(checkPositionTheLoai(maTheLoai));
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
        //
    }

    public void getTheLoai() {
        theLoaiDAO = new TheloaiDAO(getApplicationContext());
        listTheLoai = theLoaiDAO.getAllTheLoai();
        ArrayAdapter<Theloai> dataAdapter = new ArrayAdapter<Theloai>(this, android.R.layout.simple_spinner_item, listTheLoai);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTheLoai.setAdapter(dataAdapter);
    }

    //
//
    public int checkPositionTheLoai(String strTheLoai) {
        for (int i = 0; i < listTheLoai.size(); i++) {
            if (strTheLoai.equals(listTheLoai.get(i).getMaTheLoai())) {
                return i;
            }
        }
        return 0;
    }

    public void huybook(View view) {
        finish();
    }

    public void savebook(View view) {
        if (sachDAO.updateSach(masach, edTenSach.getText().toString(), maTheLoai, edAuthor.getText().toString(), edProducer.getText().toString(), edGiaBia.getText().toString(), edSoLuong.getText().toString()) > 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.alertsuccessfully), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
