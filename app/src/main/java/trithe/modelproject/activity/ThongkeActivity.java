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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import trithe.modelproject.R;
import trithe.modelproject.adapter.ThongkeAdapter;
import trithe.modelproject.database.HoaDonChiTietDAO;
import trithe.modelproject.model.Thongke;

public class ThongkeActivity extends AppCompatActivity {
    Toolbar toolbar;
    private ListView lvSinhVien;
    private List<Thongke> sinhVienList;
    private ThongkeAdapter adapter;
    private BottomNavigationView bottomNavigationView;
    HoaDonChiTietDAO hoaDonChiTietDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongke);
        toolbar = findViewById(R.id.toolbarthongke);
        setSupportActionBar(toolbar);
        sinhVienList = new ArrayList<>();
        adapter = new ThongkeAdapter(this, R.layout.item_thong_ke, sinhVienList);
        lvSinhVien = findViewById(R.id.lvthongke);
        lvSinhVien.setAdapter(adapter);
        hoaDonChiTietDAO = new HoaDonChiTietDAO(this);
        sinhVienList.add(new Thongke("Today", hoaDonChiTietDAO.getDoanhThuTheoNgay() + " VND"));
        sinhVienList.add(new Thongke("Month", hoaDonChiTietDAO.getDoanhThuTheoThang() + " VND"));
        sinhVienList.add(new Thongke("Year", hoaDonChiTietDAO.getDoanhThuTheoNam() + " VND"));
        adapter.notifyDataSetChanged();
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
                }
                return false;
            }
        });
    }

    public void outthongke(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }
}
