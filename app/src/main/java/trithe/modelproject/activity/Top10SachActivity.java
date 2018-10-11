package trithe.modelproject.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import trithe.modelproject.R;
import trithe.modelproject.adapter.BookAdapter;
import trithe.modelproject.adapter.BookAdapterTop10;
import trithe.modelproject.database.SachDAO;
import trithe.modelproject.database.UserDAO;
import trithe.modelproject.model.Sach;
import trithe.modelproject.model.User;

public class Top10SachActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView lvBook;
    private BottomNavigationView bottomNavigationView;
    private EditText editText;
    BookAdapterTop10 adapter = null;
    SachDAO sachDAO;
    public static List<Sach> dsSach = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top10_sach);
        toolbar = findViewById(R.id.toolbartop10);
        lvBook = findViewById(R.id.lvtop10);
        sachDAO = new SachDAO(Top10SachActivity.this);
        adapter = new BookAdapterTop10(Top10SachActivity.this, dsSach);

        dsSach.clear();
        dsSach = sachDAO.getSachTop10show();
        adapter.changeDatasetBookTop10(sachDAO.getSachTop10show());

        lvBook.setAdapter(adapter);

        setSupportActionBar(toolbar);

        lvBook = (ListView) findViewById(R.id.lvtop10);
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

    public void outtop10(View view) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top10, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_top10:
                dsSach.clear();
                dsSach = sachDAO.getSachTop10show();
                adapter.changeDatasetBookTop10(sachDAO.getSachTop10show());
                if (dsSach.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.data), Toast.LENGTH_SHORT).show();
                } else {
                    LayoutInflater inflater = LayoutInflater.from(Top10SachActivity.this);
                    final View view = inflater.inflate(R.layout.dialog_search, null);
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Top10SachActivity.this);
                    builder.setTitle("Search");
                    builder.setIcon(R.drawable.youtube_search);
                    builder.setView(view);
                    editText = view.findViewById(R.id.edSearch);
                    lvBook.setTextFilterEnabled(true);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (editText.getText().toString().equals("")) {
                            } else if (Integer.parseInt(editText.getText().toString()) > 13 ||
                                    Integer.parseInt(editText.getText().toString()) < 0) {
                                Toast.makeText(getApplicationContext(), getString(R.string.errormonth), Toast.LENGTH_SHORT).show();
                            } else {
                                sachDAO = new SachDAO(Top10SachActivity.this);
                                dsSach = sachDAO.getSachTop10(editText.getText().toString());
                                adapter = new BookAdapterTop10(Top10SachActivity.this, dsSach);
                                lvBook.setAdapter(adapter);
                            }
                        }

                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dsSach.clear();
                            dsSach = sachDAO.getAllSach();
                            adapter.changeDatasetBookTop10(sachDAO.getSachTop10show());
                        }
                    });
                    builder.show();
                }
        }

        return super.onOptionsItemSelected(item);
    }
}
