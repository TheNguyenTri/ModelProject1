package trithe.modelproject.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import trithe.modelproject.R;
import trithe.modelproject.adapter.CartAdapter;
import trithe.modelproject.database.HoaDonChiTietDAO;
import trithe.modelproject.database.SachDAO;
import trithe.modelproject.model.HoaDonChiTiet;
import trithe.modelproject.model.Sach;

public class ListHoaDonChiTietByIDActivity extends AppCompatActivity {
    public List<HoaDonChiTiet> dsHDCT = new ArrayList<>();
    ListView lvCart;
    CartAdapter adapter = null;
    HoaDonChiTietDAO hoaDonChiTietDAO;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    String idhdct, maidbook, soluong;
    private Spinner spdetail;
    public List<Sach> dsSach = new ArrayList<>();
    private EditText edquantitydetail;
    SachDAO sachDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hoa_don_chi_tiet_by_id);
        lvCart = (ListView) findViewById(R.id.lvHoaDonChiTiet);
        toolbar = findViewById(R.id.toolbarhoadondetail2);
        setSupportActionBar(toolbar);
        lvCart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getApplicationContext(), EditDetailActivity.class);
//                Bundle b = new Bundle();
//                b.putString("MAHDCT", String.valueOf(dsHDCT.get(position).getMaHDCT()));
//                b.putString("IDBOOK", String.valueOf(dsHDCT.get(position).getSach()));
//                b.putString("QUANTITY", String.valueOf(dsHDCT.get(position).getSoLuongMua()));
//                intent.putExtras(b);
//                startActivity(intent);
//                finish();
                Intent in = getIntent();
                final Bundle b = in.getExtras();
                if (b != null) {
                    dsHDCT = hoaDonChiTietDAO.getAllHoaDonChiTietByID(b.getString("MAHOADON"));
                }
                hoaDonChiTietDAO = new HoaDonChiTietDAO(ListHoaDonChiTietByIDActivity.this);
                idhdct = String.valueOf(dsHDCT.get(position).getMaHDCT());
                maidbook = String.valueOf(dsHDCT.get(position).getSach());
                soluong = String.valueOf(dsHDCT.get(position).getSoLuongMua());
                LayoutInflater inflater = LayoutInflater.from(ListHoaDonChiTietByIDActivity.this);
                final View view1 = inflater.inflate(R.layout.dialog_detail, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(ListHoaDonChiTietByIDActivity.this);
                builder.setTitle("Edit Detail");
                builder.setView(view1);
                builder.setIcon(R.drawable.assgiment);
                spdetail = (Spinner) view1.findViewById(R.id.spdetail);
                edquantitydetail = view1.findViewById(R.id.edquantitydetail);
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
                edquantitydetail.setText(soluong);
                spdetail.setSelection(checkPositionHDCT(maidbook));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (hoaDonChiTietDAO.updateHoaDonChiTiet(idhdct, maidbook, edquantitydetail.getText().toString()) > 0) {
                            Toast.makeText(getApplicationContext(), getString(R.string.alertsuccessfully), Toast.LENGTH_SHORT).show();
                            dsHDCT.clear();
                            dsHDCT = hoaDonChiTietDAO.getAllHoaDonChiTietByID(b.getString("MAHOADON"));
                            adapter.changeDatasetHDCT(dsHDCT);
                            lvCart.setAdapter(adapter);
                        }
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                return false;
            }
        });
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
        hoaDonChiTietDAO = new HoaDonChiTietDAO(ListHoaDonChiTietByIDActivity.this);
        Intent in = getIntent();
        Bundle b = in.getExtras();
        if (b != null) {
            dsHDCT = hoaDonChiTietDAO.getAllHoaDonChiTietByID(b.getString("MAHOADON"));
        }
        adapter = new CartAdapter(this, dsHDCT);
        lvCart.setAdapter(adapter);

    }

    public void outhoadonDetail(View view) {
        finish();
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
