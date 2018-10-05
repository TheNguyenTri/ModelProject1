package trithe.modelproject.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import trithe.modelproject.R;
import trithe.modelproject.adapter.CartAdapter;
import trithe.modelproject.database.HoaDonChiTietDAO;
import trithe.modelproject.database.HoaDonDAO;
import trithe.modelproject.database.SachDAO;
import trithe.modelproject.database.TheloaiDAO;
import trithe.modelproject.model.HoaDon;
import trithe.modelproject.model.HoaDonChiTiet;
import trithe.modelproject.model.Sach;
import trithe.modelproject.model.Theloai;

public class HoaDonChiTietActivity extends AppCompatActivity {
    EditText edMaSach, edMaHoaDon, edSoLuong;
    TextView tvThanhTien;
    HoaDonChiTietDAO hoaDonChiTietDAO;
    SachDAO sachDAO;
    HoaDonDAO hoaDonDAO;
    public List<HoaDonChiTiet> dsHDCT = new ArrayList<>();
    private List<Sach> dsSach = new ArrayList<>();
    private List<HoaDon> dshoadon = new ArrayList<>();
    ListView lvCart;
    CartAdapter adapter = null;
    double thanhTien = 0;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private Spinner spinner, spinnerhd;
    String masach, mahd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = findViewById(R.id.toolbardetailhoadon);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_hoa_don_chi_tiet);
//        edMaSach = (EditText) findViewById(R.id.idbookdetailhd);
        spinner = findViewById(R.id.idbookdetailhd);
        spinnerhd = findViewById(R.id.idhddetailhd);
        sachDAO = new SachDAO(this);
        hoaDonDAO = new HoaDonDAO(this);
//        edMaHoaDon = (EditText) findViewById(R.id.iddetailhd);
        edSoLuong = (EditText) findViewById(R.id.soluongdeltailhd);
        lvCart = (ListView) findViewById(R.id.lvCart);
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

        getMahd();
//        if (b != null) {
//            edMaHoaDon.setText(b.getString("MAHOADON"));
//        }

        spinnerhd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mahd = dshoadon.get(spinnerhd.getSelectedItemPosition()).getMaHoaDon();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tvThanhTien = (TextView) findViewById(R.id.tvThanhTien);
        adapter = new CartAdapter(this, dsHDCT);
        lvCart.setAdapter(adapter);
        Intent in = getIntent();
        Bundle b = in.getExtras();
        mahd = b.getString("MAHOADON");
        spinnerhd.setSelection(checkPositionHD(mahd));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                masach = dsSach.get(spinner.getSelectedItemPosition()).getMaSach();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getMaSach();
    }

    public void outdetailhd(View view) {
        startActivity(new Intent(getApplicationContext(), ListHoaDonActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void ADDHoaDonCHITIET(View view) {
        try {
            if (valuedation() > 0) {
                hoaDonChiTietDAO = new HoaDonChiTietDAO(HoaDonChiTietActivity.this);
                sachDAO = new SachDAO(HoaDonChiTietActivity.this);
                Sach sach = sachDAO.getSachByID(masach);
                if (sach != null) {
                    int pos = checkMaSach(dsHDCT, masach);
//                    HoaDon hoaDon = new HoaDon(edMaHoaDon.getText().toString(), new Date());
                    HoaDon hoaDon = new HoaDon(mahd, new Date());
                    HoaDonChiTiet hoaDonChiTiet = new
                            HoaDonChiTiet(1, hoaDon, sach, Integer.parseInt(edSoLuong.getText().toString()));
                    if (pos >= 0) {
                        int soluong = dsHDCT.get(pos).getSoLuongMua();
                        hoaDonChiTiet.setSoLuongMua(soluong +
                                Integer.parseInt(edSoLuong.getText().toString()));
                        dsHDCT.set(pos, hoaDonChiTiet);
                    } else {
                        dsHDCT.add(hoaDonChiTiet);
                    }
                    adapter.changeDatasetHDCT(dsHDCT);

                }
//                else {
//                    edMaSach.setError(getString(R.string.alerterroridbooknotexists));
//                }
            }
        } catch (Exception ex) {
            Log.e("Error", ex.toString());
        }
    }


    public void thanhToanHoaDon(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pay");
        builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        builder.setMessage("Do you want to pay ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                hoaDonChiTietDAO = new HoaDonChiTietDAO(HoaDonChiTietActivity.this);
                //tinh tien
                thanhTien = 0;
                try {
                    for (HoaDonChiTiet hd : dsHDCT) {
                        hoaDonChiTietDAO.inserHoaDonChiTiet(hd);
                        thanhTien = thanhTien + hd.getSoLuongMua() *
                                hd.getSach().getGiaBia();
                    }
                    tvThanhTien.setText("Money : " + thanhTien + "$");
                } catch (Exception ex) {
                    Log.e("Error", ex.toString());
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public int checkMaSach(List<HoaDonChiTiet> lsHD, String maSach) {
        int pos = -1;
        for (int i = 0; i < lsHD.size(); i++) {
            HoaDonChiTiet hd = lsHD.get(i);
            if (hd.getSach().getMaSach().equalsIgnoreCase(maSach)) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    public int valuedation() {
        if (edSoLuong.getText().toString().equals("")) {
            edSoLuong.setError(getString(R.string.emptyquantitybook));
            return -1;
        }
        return 1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void getMaSach() {
        sachDAO = new SachDAO(this);
        dsSach = sachDAO.getAllSach();
        ArrayAdapter<Sach> dataAdapter = new ArrayAdapter<Sach>(this, android.R.layout.simple_spinner_item, dsSach);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    public void getMahd() {
        hoaDonDAO = new HoaDonDAO(this);
        try {
            dshoadon = hoaDonDAO.getAllHoaDon();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayAdapter<HoaDon> dataAdapter = new ArrayAdapter<HoaDon>(this, android.R.layout.simple_spinner_item, dshoadon);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerhd.setAdapter(dataAdapter);
    }

    public int checkPositionHD(String strTheLoai) {
        for (int i = 0; i < dshoadon.size(); i++) {
            if (strTheLoai.equals(dshoadon.get(i).getMaHoaDon())) {
                return i;
            }
        }
        return 0;
    }

}
