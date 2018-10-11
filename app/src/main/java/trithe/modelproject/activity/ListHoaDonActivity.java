package trithe.modelproject.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import trithe.modelproject.R;
import trithe.modelproject.adapter.BookAdapter;
import trithe.modelproject.adapter.HoaDonAdapter;
import trithe.modelproject.adapter.TheloaiAdapter;
import trithe.modelproject.adapter.UserAdapter;
import trithe.modelproject.database.HoaDonDAO;
import trithe.modelproject.database.SachDAO;
import trithe.modelproject.database.TheloaiDAO;
import trithe.modelproject.database.UserDAO;
import trithe.modelproject.model.HoaDon;

public class ListHoaDonActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Toolbar toolbar;
//    FloatingActionButton floatingActionButton;
    private BottomNavigationView bottomNavigationView;
    CardView cardView;

    public List<HoaDon> dsHoaDon = new ArrayList<>();
    ListView lvHoaDon;
    HoaDonAdapter adapter = null;
    HoaDonDAO hoaDonDAO;
    private EditText editText, editTextDate;
    ImageView btna;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    String idhd;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hoa_don);
        lvHoaDon = findViewById(R.id.lvhoadon);
        hoaDonDAO = new HoaDonDAO(this);
        try {
            dsHoaDon = hoaDonDAO.getAllHoaDon();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        adapter = new HoaDonAdapter(this, dsHoaDon);
        lvHoaDon.setAdapter(adapter);
        registerForContextMenu(lvHoaDon);
        toolbar = findViewById(R.id.toolbarhoadon);
        setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.btnnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
        cardView = findViewById(R.id.fabhaodon);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HoaDonActivity.class));
            }
        });
    }

    public void outhoadon(View view) {
        startActivity(new Intent(ListHoaDonActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dsHoaDon.clear();
        try {
            dsHoaDon = hoaDonDAO.getAllHoaDon();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            adapter.changeDataset(HoaDonDAO.getAllHoaDon());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_lvhd, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edithd:
                AdapterView.AdapterContextMenuInfo menuinfo1 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int poistion1 = menuinfo1.position;
                Intent intent = new Intent(ListHoaDonActivity.this, ListHoaDonChiTietByIDActivity.class);
                Bundle b = new Bundle();
                b.putString("MAHOADON", dsHoaDon.get(poistion1).getMaHoaDon());
                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.searchd:
                search();
                break;
            case R.id.deletehd:
                AdapterView.AdapterContextMenuInfo menuinfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int poistion = menuinfo.position;
                hoaDonDAO.deleteHoaDonByID(dsHoaDon.get(poistion).getMaHoaDon());
                dsHoaDon.remove(poistion);
                try {
                    dsHoaDon.clear();
                    dsHoaDon = HoaDonDAO.getAllHoaDon();
                    adapter.changeDataset(HoaDonDAO.getAllHoaDon());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.edithdate:
                AdapterView.AdapterContextMenuInfo menuinfo2 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int poistion2 = menuinfo2.position;
                idhd = dsHoaDon.get(poistion2).getMaHoaDon();
                date = dsHoaDon.get(poistion2).getNgayMua();
                LayoutInflater inflater = LayoutInflater.from(ListHoaDonActivity.this);
                final View view = inflater.inflate(R.layout.dialogedithd, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(ListHoaDonActivity.this);
                builder.setTitle("Edit Date");
                builder.setView(view);
                builder.setIcon(R.drawable.timer);
                btna = view.findViewById(R.id.btna);
                //lỗi gay gắt nhât'
//                editTextDate = view.findViewById(R.id.edDateHoaDonedit);
//                editTextDate.setText(sdf.format(date));
                editTextDate = view.findViewById(R.id.edDateHoaDonedit);
                editTextDate.setText(sdf.format(date));
                //
                btna.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerFragment fragment = new DatePickerFragment();
                        fragment.show(getFragmentManager(), "date");
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if (hoaDonDAO.updateHoaDon(idhd, sdf.parse(editTextDate.getText().toString())) > 0) {
                                Toast.makeText(ListHoaDonActivity.this, getString(R.string.alertsuccessfully), Toast.LENGTH_SHORT).show();
                                dsHoaDon.clear();
                                dsHoaDon = hoaDonDAO.getAllHoaDon();
                                adapter.changeDataset(hoaDonDAO.getAllHoaDon());
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                break;
        }

        return super.onContextItemSelected(item);
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }

    private void setDate(final Calendar calendar) {
        editTextDate.setText(sdf.format(calendar.getTime()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }
    public void search(){
        dsHoaDon.clear();
        hoaDonDAO = new HoaDonDAO(ListHoaDonActivity.this);
        try {
            dsHoaDon = hoaDonDAO.getAllHoaDon();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        adapter = new HoaDonAdapter(ListHoaDonActivity.this, dsHoaDon);
        try {
            adapter.changeDataset(hoaDonDAO.getAllHoaDon());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LayoutInflater inflaters = LayoutInflater.from(ListHoaDonActivity.this);
        final View views = inflaters.inflate(R.layout.dialog_search, null);
        AlertDialog.Builder builders = new AlertDialog.Builder(ListHoaDonActivity.this);
        builders.setTitle("Search");
        builders.setView(views);
        builders.setIcon(R.drawable.youtube_search);
        editText = views.findViewById(R.id.edSearch);
        lvHoaDon.setAdapter(adapter);
        lvHoaDon.setTextFilterEnabled(true);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int
                    count) {
                System.out.println("Text [" + s + "] - Start [" + start + "] - Before [" + before + "] - Count [" + count + "]");
                if (count < before) {
                    adapter.resetData();
                }
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        builders.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }

        });
        builders.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dsHoaDon.clear();
                try {
                    dsHoaDon = hoaDonDAO.getAllHoaDon();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    adapter.changeDataset(hoaDonDAO.getAllHoaDon());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        builders.show();
    }
}
