package trithe.modelproject.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import trithe.modelproject.R;
import trithe.modelproject.activity.EditTheLoaiActivity;
import trithe.modelproject.activity.ListHoaDonActivity;
import trithe.modelproject.adapter.TheloaiAdapter;
import trithe.modelproject.database.TheloaiDAO;
import trithe.modelproject.model.Theloai;

public class ListTheLoaiFragment extends Fragment {
//    private FloatingActionButton floatingActionButton;
    private CardView cardView;
    public static List<Theloai> dsTheLoai = new ArrayList<>();
    ListView lvTheLoai;
    TheloaiAdapter adapter = null;
    private EditText edid, edname, edvitri, edmota, editText;
    TheloaiDAO theLoaiDAO;
    CardView cardViewsavetl, cardViewcaceltl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_list_the_loai_fragment, container, false);
//        floatingActionButton = view.findViewById(R.id.fabtheloai);
        cardView = view.findViewById(R.id.fabtheloai);
        lvTheLoai = view.findViewById(R.id.lvtheloai);
        theLoaiDAO = new TheloaiDAO(getContext());
        registerForContextMenu(lvTheLoai);
        dsTheLoai = theLoaiDAO.getAllTheLoai();
        adapter = new TheloaiAdapter(getActivity(), dsTheLoai);
        lvTheLoai.setAdapter(adapter);


//        lvTheLoai.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getContext(), EditTheLoaiActivity.class);
//                Bundle b = new Bundle();
//                b.putString("ID", dsTheLoai.get(position).getMaTheLoai());
//                b.putString("NAME", dsTheLoai.get(position).getTenTheLoai());
//                intent.putExtras(b);
//                startActivity(intent);
//                return false;
//            }
//        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                final View view = inflater.inflate(R.layout.dialog_theloai, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add Type Book");
                builder.setIcon(R.drawable.note_add);
                builder.setView(view);
                final AlertDialog dialog = builder.show();
                edid = view.findViewById(R.id.edidtheloai);
                edname = view.findViewById(R.id.edtentheloai);
                edvitri = view.findViewById(R.id.edvitritheloai);
                edmota = view.findViewById(R.id.edmotatheloai);
                cardViewsavetl = view.findViewById(R.id.cardsavetl);
                cardViewcaceltl = view.findViewById(R.id.cardcanceltl);
                cardViewcaceltl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edid.setText("");
                        edname.setText("");
                        edvitri.setText("");
                        edmota.setText("");
                    }
                });
                cardViewsavetl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        theLoaiDAO = new TheloaiDAO(getContext());
                        try {
                            if (validateForm() > 0) {
                                Theloai theloai = new Theloai(edid.getText().toString(), edname.getText().toString(), edmota.getText().toString(), Integer.parseInt(edvitri.getText().toString()));
                                if (theLoaiDAO.inserTheLoai(theloai) > 0) {
                                    Toast.makeText(getContext(), getString(R.string.alertsuccessfully), Toast.LENGTH_SHORT).show();
                                    dsTheLoai.clear();
                                    dsTheLoai = theLoaiDAO.getAllTheLoai();
                                    adapter.changeDatasetTheloai(theLoaiDAO.getAllTheLoai());
                                    dialog.dismiss();
                                } else {
                                    edid.setError(getString(R.string.alerterrortheloaiid));
                                }
                            }
                        } catch (Exception ex) {
                            Log.e("Error", ex.toString());
                        }
                    }
                });

//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        theLoaiDAO = new TheloaiDAO(getContext());
//                        try {
//                            if (validateForm() > 0) {
//                                Theloai theloai = new Theloai(edid.getText().toString(), edname.getText().toString(), edmota.getText().toString(), Integer.parseInt(edvitri.getText().toString()));
//                                if (theLoaiDAO.inserTheLoai(theloai) > 0) {
//                                    Toast.makeText(getContext(), getString(R.string.alertsuccessfully), Toast.LENGTH_SHORT).show();
//                                    dsTheLoai.clear();
//                                    dsTheLoai = theLoaiDAO.getAllTheLoai();
//                                    adapter.changeDatasetTheloai(theLoaiDAO.getAllTheLoai());
//                                } else {
//                                    Toast.makeText(getContext(), getString(R.string.alerterrortheloaiid), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        } catch (Exception ex) {
//                            Log.e("Error", ex.toString());
//                        }
//                    }
//                });
//                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
            }

            private int validateForm() {
                int check = 1;
                if (edid.getText().toString().equals("")) {
                    edid.setError(getString(R.string.emptyidtheloai));
                    check = -1;
                } else if (edid.getText().toString().length() > 5) {
                    edid.setError(getString(R.string.lengthtl));
                    check = -1;
                } else if (edname.getText().toString().equals("")) {
                    edname.setError(getString(R.string.emptynametheloai));
                    check = -1;
                } else if (edname.getText().toString().length() > 30) {
                    edname.setError(getString(R.string.lengthnametl));
                    check = -1;
                } else if (edvitri.getText().toString().equals("")) {
                    edvitri.setError(getString(R.string.emptypositiontheloai));
                    check = -1;
                } else if (edvitri.getText().toString().length() > 100) {
                    edvitri.setError(getString(R.string.posotion));
                    check = -1;
                } else if (edmota.getText().toString().equals("")) {
                    edmota.setError(getString(R.string.emptymotatheloai));
                    check = -1;
                } else if (edmota.getText().toString().length() > 15) {
                    edmota.setError(getString(R.string.des));
                    check = -1;
                }
                return check;
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        dsTheLoai.clear();
        dsTheLoai = theLoaiDAO.getAllTheLoai();
        adapter.changeDatasetTheloai(theLoaiDAO.getAllTheLoai());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.menu_lvtheloai, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edittl:
                AdapterView.AdapterContextMenuInfo menuinfo1 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int poistion1 = menuinfo1.position;
                Intent intent = new Intent(getContext(), EditTheLoaiActivity.class);
                Bundle b = new Bundle();
                b.putString("ID", dsTheLoai.get(poistion1).getMaTheLoai());
                b.putString("NAME", dsTheLoai.get(poistion1).getTenTheLoai());
                b.putString("VITRI", String.valueOf(Integer.parseInt(String.valueOf(dsTheLoai.get(poistion1).getViTri()))));
                b.putString("DES", dsTheLoai.get(poistion1).getMoTa());
                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.searchtl:
                dsTheLoai.clear();
                theLoaiDAO = new TheloaiDAO(getContext());
                dsTheLoai = theLoaiDAO.getAllTheLoai();
                adapter = new TheloaiAdapter(getActivity(), dsTheLoai);
                adapter.changeDatasetTheloai(theLoaiDAO.getAllTheLoai());

                LayoutInflater inflater = LayoutInflater.from(getContext());
                final View view = inflater.inflate(R.layout.dialog_search, null);
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setTitle("Search");
                builder.setIcon(R.drawable.youtube_search);
                builder.setView(view);
                editText = view.findViewById(R.id.edSearch);
                lvTheLoai.setAdapter(adapter);
                lvTheLoai.setTextFilterEnabled(true);

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


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }

                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dsTheLoai.clear();
                        dsTheLoai = theLoaiDAO.getAllTheLoai();
                        adapter.changeDatasetTheloai(theLoaiDAO.getAllTheLoai());
                    }
                });
                builder.show();
                break;
            case R.id.deletetl:
                AdapterView.AdapterContextMenuInfo menuinfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int poistion = menuinfo.position;
                theLoaiDAO.deleteTheLoaiByID(dsTheLoai.get(poistion).getMaTheLoai());
                dsTheLoai.remove(poistion);
                dsTheLoai.clear();
                dsTheLoai = theLoaiDAO.getAllTheLoai();
                adapter.changeDatasetTheloai(theLoaiDAO.getAllTheLoai());
                break;
        }
        return super.onContextItemSelected(item);
    }
}
