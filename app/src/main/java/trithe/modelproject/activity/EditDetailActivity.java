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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import trithe.modelproject.R;
import trithe.modelproject.adapter.CartAdapter;
import trithe.modelproject.database.HoaDonChiTietDAO;
import trithe.modelproject.database.SachDAO;
import trithe.modelproject.database.TheloaiDAO;
import trithe.modelproject.model.HoaDonChiTiet;
import trithe.modelproject.model.Sach;
import trithe.modelproject.model.Theloai;

public class EditDetailActivity extends AppCompatActivity {
    private Toolbar toolbarupdatetdetail;
    public List<HoaDonChiTiet> dsHDCT = new ArrayList<>();
    public List<Sach> dsSach = new ArrayList<>();
    //    private EditText edMasachdetail;
    SachDAO sachDAO;
    private EditText edquantitydetail;
    private BottomNavigationView btnnav;
    String idhdct, maidbook;
    HoaDonChiTietDAO hoaDonChiTietDAO;
    private Spinner spdetail;
    CartAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detail);
        toolbarupdatetdetail = (Toolbar) findViewById(R.id.toolbarupdatetdetail);
        setSupportActionBar(toolbarupdatetdetail);
//        edMasachdetail = (EditText) findViewById(R.id.edMasachdetail);
        spdetail = (Spinner) findViewById(R.id.spdetail);
        getMaSachdetail();
        spdetail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maidbook = dsSach.get(spdetail.getSelectedItemPosition()).getMaSach();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        edquantitydetail = (EditText) findViewById(R.id.edquantitydetail);
        btnnav = (BottomNavigationView) findViewById(R.id.btnnav);
        btnnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
        hoaDonChiTietDAO = new HoaDonChiTietDAO(this);
        Intent in = getIntent();
        Bundle b = in.getExtras();
        idhdct = b.getString("MAHDCT");
        maidbook = b.getString("IDBOOK");
        edquantitydetail.setText(b.getString("QUANTITY"));
        spdetail.setSelection(checkPositionHDCT(maidbook));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void huydetail(View view) {
        finish();
    }


    public void outdetail(View view) {
        finish();
    }

    public void updatetdetailhda(View view) {
        if (hoaDonChiTietDAO.updateHoaDonChiTiet(idhdct, maidbook, edquantitydetail.getText().toString()) > 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.alertsuccessfully), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void getMaSachdetail() {
        sachDAO = new SachDAO(getApplicationContext());
        dsSach = sachDAO.getAllSach();
        ArrayAdapter<Sach> dataAdapter = new ArrayAdapter<Sach>(this, android.R.layout.simple_spinner_item, dsSach);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spdetail.setAdapter(dataAdapter);
    }

    public int checkPositionHDCT(String strTheLoai) {
        for (int i = 0; i < dsSach.size(); i++) {
            if (strTheLoai.equals(dsSach.get(i).getMaSach())) {
                return i;
            }
        }
        return 0;
    }
}
