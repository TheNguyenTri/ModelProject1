package trithe.modelproject.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
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
import trithe.modelproject.fragment.HomePager;
import trithe.modelproject.model.HoaDon;
import trithe.modelproject.model.Sach;
import trithe.modelproject.model.Theloai;
import trithe.modelproject.model.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private HomePager chiPagerAdapter;
    private BottomNavigationView bottomNavigationView;

//    public List<User> dsUser = new ArrayList<>();
//    private UserDAO userDAO;
//    UserAdapter adapter = null;
//    private EditText editText;
//    private ListView listView, listViewtl, listViewbook;
//
//    public List<Theloai> dsTheloai = new ArrayList<>();
//    private TheloaiDAO theloaiDAO;
//    TheloaiAdapter adaptertheloai = null;
//
//    public List<Sach> dsSach = new ArrayList<>();
//    private SachDAO sachDAO;
//    BookAdapter adaptersach = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewpaper);
        tabLayout = findViewById(R.id.tablelayout);
        chiPagerAdapter = new HomePager(getSupportFragmentManager());
        viewPager.setAdapter(chiPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        bottomNavigationView = findViewById(R.id.btnnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
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
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.searchtop10:
//
//
////                //book
//                dsSach.clear();
//                sachDAO = new SachDAO(this);
//                dsSach = sachDAO.getAllSach();
//                adaptersach = new BookAdapter(this, dsSach);
//                adaptersach.changeDatasetBook(sachDAO.getAllSach());
//                listViewbook = findViewById(R.id.lvBook);
//////                the loai
//                dsTheloai.clear();
//                theloaiDAO = new TheloaiDAO(this);
//                dsTheloai = theloaiDAO.getAllTheLoai();
//                adaptertheloai = new TheloaiAdapter(this, dsTheloai);
//                adaptertheloai.changeDatasetTheloai(theloaiDAO.getAllTheLoai());
//                listViewtl = findViewById(R.id.lvtheloai);
//
//                //                user
////                dsUser.clear();
////                dsSach.clear();
////                userDAO = new UserDAO(this);
////                dsUser = userDAO.getAllUser();
////                adapter = new UserAdapter(this, dsUser);
////                adapter.changeDataset(userDAO.getAllUser());
////                listView = findViewById(R.id.lvuser);
//
//
//                LayoutInflater inflater = LayoutInflater.from(this);
//                final View view = inflater.inflate(R.layout.dialog_search, null);
//                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
//                builder.setTitle("Search");
//                builder.setIcon(R.drawable.ic_youtube_searched_for_black_24dp);
//                builder.setView(view);
//                editText = view.findViewById(R.id.edSearch);
//                //lv 3loai
//                listViewbook.setAdapter(adaptersach);
////                listView.setAdapter(adapter);
//                listViewtl.setAdapter(adaptertheloai);
//                //
//                listViewbook.setTextFilterEnabled(true);
//                listViewtl.setTextFilterEnabled(true);
//
//                editText.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int
//                            count) {
//                        System.out.println("Text [" + s + "] - Start [" + start + "] - Before [" + before + "] - Count [" + count + "]");
//                        if (count < before) {
//                            adaptersach.resetData();
////                            adapter.resetData();
//                            adaptertheloai.resetData();
//                        }
//                        adaptersach.getFilter().filter(s.toString());
////                        adapter.getFilter().filter(s.toString());
//                        adaptertheloai.getFilter().filter(s.toString());
//                    }
//
//
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                    }
//                });
//
//
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//
//                });
//                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////
////                        dsUser.clear();
////                        dsUser = userDAO.getAllUser();
////                        adapter.changeDataset(userDAO.getAllUser());
////
//                        dsTheloai.clear();
//                        dsTheloai = theloaiDAO.getAllTheLoai();
//                        adaptertheloai.changeDatasetTheloai(theloaiDAO.getAllTheLoai());
//
//                        dsSach.clear();
//                        dsSach = sachDAO.getAllSach();
//                        adaptersach.changeDatasetBook(sachDAO.getAllSach());
//                    }
//                });
//                builder.show();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_top10) {
            startActivity(new Intent(getApplicationContext(), Top10SachActivity.class));
        } else if (id == R.id.nav_hoadon) {
            startActivity(new Intent(getApplicationContext(), ListHoaDonActivity.class));
        } else if (id == R.id.nav_thongke) {
            startActivity(new Intent(getApplicationContext(), ThongkeActivity.class));
        } else if (id == R.id.nav_change) {
            startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
        } else if (id == R.id.nav_intro) {
            startActivity(new Intent(getApplicationContext(), IntroduceActivity.class));
        } else if (id == R.id.nav_out) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sign out");
            builder.setIcon(R.drawable.logout);
            builder.setMessage("Do you want sign out ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    SharedPreferences pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);
//                    SharedPreferences.Editor edit = pref.edit();
//                    //xoa tinh trang luu tru truoc do
//                    edit.clear();
//                    edit.commit();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);


                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
