package trithe.modelproject.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import trithe.modelproject.R;
import trithe.modelproject.database.HoaDonDAO;
import trithe.modelproject.model.HoaDon;

public class HoaDonActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Toolbar toolbar;
    EditText edNgayMua, edMaHoaDon;
    HoaDonDAO hoaDonDAO;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private BottomNavigationView bottomNavigationView;
    public List<HoaDon> dsHoaDon = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don);
        toolbar = findViewById(R.id.toolbaraddhoadon);
        setSupportActionBar(toolbar);
        edNgayMua = (EditText) findViewById(R.id.edDateHoaDon);
        edMaHoaDon = (EditText) findViewById(R.id.edIDHoaDon);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void outhd(View view) {
        finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }

    private void setDate(final Calendar calendar) {
        edNgayMua.setText(sdf.format(calendar.getTime()));
    }

    public void huyAddHoaDon(View view) {
        finish();
    }

    public static class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
        }
    }

    public void datePicker(View view) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(), "date");
    }

    public void addHoadon(View view) {
        hoaDonDAO = new HoaDonDAO(HoaDonActivity.this);
        try {
            if (validation() > 0) {
                HoaDon hoaDon = new HoaDon(edMaHoaDon.getText().toString(), sdf.parse(edNgayMua.getText().toString()));
                if (hoaDonDAO.inserHoaDon(hoaDon) > 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.emptyadd), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HoaDonActivity.this, HoaDonChiTietActivity.class);
                    Bundle b = new Bundle();
                    b.putString("MAHOADON", edMaHoaDon.getText().toString());
                    intent.putExtras(b);
                    startActivity(intent);
                } else {
                    edMaHoaDon.setError(getString(R.string.error_idhd));
                }
            } else {
            }
        } catch (Exception ex) {
            Log.e("Error", ex.toString());
        }
    }

    public int validation() {
        if (edMaHoaDon.getText().toString().isEmpty()) {
            edMaHoaDon.setError(getString(R.string.emptyaddidhd));
            return -1;
        } else if (edNgayMua.getText().toString().isEmpty()) {
            edNgayMua.setError(getString(R.string.emptyadddatehd));
            return -1;
        }
        return 1;
    }

}
