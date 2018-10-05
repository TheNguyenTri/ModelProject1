package trithe.modelproject.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import trithe.modelproject.R;
import trithe.modelproject.activity.EditBookActivity;
import trithe.modelproject.activity.ListHoaDonActivity;
import trithe.modelproject.activity.ThongkeActivity;
import trithe.modelproject.adapter.BookAdapter;
import trithe.modelproject.database.SachDAO;
import trithe.modelproject.database.TheloaiDAO;
import trithe.modelproject.model.Sach;
import trithe.modelproject.model.Theloai;

public class ListBookFragment extends Fragment {
    public static List<Sach> dsSach = new ArrayList<>();
    ListView lvBook;
    BookAdapter adapter = null;
    SachDAO sachDAO;
    TheloaiDAO theLoaiDAO;
    Spinner spnTheLoai;
    List<Theloai> listTheLoai = new ArrayList<>();
    private FloatingActionButton floatingActionButton,fabsearch;
    String maTheLoai = "";
    EditText edMaSach, edTenSach, edNXB, edTacGia, edGiaBia, edSoLuong, editText;
    CardView cardViewsaveBook, cardViewcancelBook;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_list_book_fragment, container, false);
        lvBook = (ListView) view.findViewById(R.id.lvBook);
        floatingActionButton = view.findViewById(R.id.fabAddSach);
        fabsearch=view.findViewById(R.id.fabsearchbook);
        fabsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dsSach.isEmpty()) {
                    Toast.makeText(getContext(), "Data in System is Empty", Toast.LENGTH_SHORT).show();
                } else {
                    dsSach.clear();
                    sachDAO = new SachDAO(getContext());
                    dsSach = sachDAO.getAllSach();
                    adapter = new BookAdapter(getActivity(), dsSach);
                    adapter.changeDatasetBook(sachDAO.getAllSach());

                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    final View view = inflater.inflate(R.layout.dialog_search, null);
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                    builder.setTitle("Search");
                    builder.setIcon(R.drawable.ic_youtube_searched_for_black_24dp);
                    builder.setView(view);
                    editText = view.findViewById(R.id.edSearch);
                    lvBook.setAdapter(adapter);
                    lvBook.setTextFilterEnabled(true);

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
                            dsSach.clear();
                            dsSach = sachDAO.getAllSach();
                            adapter.changeDatasetBook(sachDAO.getAllSach());
                        }
                    });
                    builder.show();
                }
            }
        });
        sachDAO = new SachDAO(getContext());
        theLoaiDAO = new TheloaiDAO(getContext());
        registerForContextMenu(lvBook);
        dsSach = sachDAO.getAllSach();
        adapter = new BookAdapter(getActivity(), dsSach);
        lvBook.setAdapter(adapter);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                final View view = inflater.inflate(R.layout.dialog_book, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add Book");
                builder.setView(view);
                builder.setIcon(R.drawable.ic_note_add_black_24dp);
                spnTheLoai = (Spinner) view.findViewById(R.id.spnTheLoai);
//                imageView = view.findViewById(R.id.logsang);
                edMaSach = (EditText) view.findViewById(R.id.edMaSach);
                edTenSach = (EditText) view.findViewById(R.id.edTenSach);
                edNXB = (EditText) view.findViewById(R.id.edNXB);
                edTacGia = (EditText) view.findViewById(R.id.edTacGia);
                edGiaBia = (EditText) view.findViewById(R.id.edGiaBia);
                final AlertDialog dialog = builder.show();
                edSoLuong = (EditText) view.findViewById(R.id.edSoLuong);
                cardViewcancelBook = view.findViewById(R.id.cardcancelbook);
                cardViewcancelBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                cardViewsaveBook = view.findViewById(R.id.cardsavebook);
                cardViewsaveBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sachDAO = new SachDAO(getActivity());

                        try {
                            if (validateForm() > 0) {
                                Sach sach = new Sach(edMaSach.getText().toString(), maTheLoai, edTenSach.getText().toString(),
                                        edTacGia.getText().toString(), edNXB.getText().toString(),
                                        Double.parseDouble(edGiaBia.getText().toString()), Integer.parseInt(edSoLuong.getText().toString()));
                                if (sachDAO.inserSach(sach) > 0) {
                                    Toast.makeText(getActivity(), getString(R.string.alertsuccessfully), Toast.LENGTH_SHORT).show();
                                    dsSach.clear();
                                    dsSach = sachDAO.getAllSach();
                                    adapter.changeDatasetBook(sachDAO.getAllSach());
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getActivity(), getString(R.string.alerterroridbook), Toast.LENGTH_SHORT).show();
                                    edMaSach.setError(getString(R.string.alerterroridbook));
                                }
                            }
                        } catch (Exception ex) {
                            Log.e("Error", ex.toString());
                        }
                    }
                });


                spnTheLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        maTheLoai = listTheLoai.get(spnTheLoai.getSelectedItemPosition()).getMaTheLoai();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                getTheLoai();
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        sachDAO = new SachDAO(getActivity());
//
//                        try {
//                            if (validateForm() > 0) {
//                                Sach sach = new Sach(edMaSach.getText().toString(), maTheLoai, edTenSach.getText().toString(),
//                                        edTacGia.getText().toString(), edNXB.getText().toString(),
//                                        Double.parseDouble(edGiaBia.getText().toString()), Integer.parseInt(edSoLuong.getText().toString()));
//                                if (sachDAO.inserSach(sach) > 0) {
//                                    Toast.makeText(getActivity(), getString(R.string.alertsuccessfully), Toast.LENGTH_SHORT).show();
//                                    dsSach.clear();
//                                    dsSach = SachDAO.getAllSach();
//                                    adapter.changeDatasetBook(SachDAO.getAllSach());
//                                } else {
//                                    Toast.makeText(getActivity(), getString(R.string.alerterroridbook), Toast.LENGTH_SHORT).show();
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
                if (edMaSach.getText().toString().equals("")) {
                    edMaSach.setError(getString(R.string.emptyidbook));
                    check = -1;
                } else if (edTenSach.getText().toString().equals("")) {
                    edTenSach.setError(getString(R.string.emptynamebook));
                    check = -1;
                } else if (edTacGia.getText().toString().equals("")) {
                    edTacGia.setError(getString(R.string.emptyauthorbook));
                    check = -1;
                } else if (edNXB.getText().toString().equals("")) {
                    edNXB.setError(getString(R.string.emptyproducerbook));
                    check = -1;
                } else if (edGiaBia.getText().toString().equals("")) {
                    edGiaBia.setError(getString(R.string.emptypricebook));
                    check = -1;
                } else if (edSoLuong.getText().toString().equals("")) {
                    edSoLuong.setError(getString(R.string.emptyquantitybook));
                    check = -1;
                }
                return check;
            }
            public void getTheLoai() {
                theLoaiDAO = new TheloaiDAO(getContext());
                listTheLoai = theLoaiDAO.getAllTheLoai();
                ArrayAdapter<Theloai> dataAdapter = new ArrayAdapter<Theloai>(getContext(), android.R.layout.simple_spinner_item, listTheLoai);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnTheLoai.setAdapter(dataAdapter);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        dsSach.clear();
        dsSach = sachDAO.getAllSach();
        adapter.changeDatasetBook(sachDAO.getAllSach());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.menu_lvbook, menu);
        menu.setHeaderTitle("Choose your select");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editbook:
                AdapterView.AdapterContextMenuInfo menuinfo1 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int poistion1 = menuinfo1.position;
                Intent intent = new Intent(getContext(), EditBookActivity.class);
                Bundle b = new Bundle();
                b.putString("MASACH", dsSach.get(poistion1).getMaSach());
                b.putString("MATHELOAI", dsSach.get(poistion1).getMaTheLoai());
                b.putString("TENSACH", dsSach.get(poistion1).getTenSach());
                b.putString("PRODUCER", dsSach.get(poistion1).getNXB());
                b.putString("AUTHOR", dsSach.get(poistion1).getTacGia());
                b.putString("GIABIA", String.valueOf(Double.parseDouble(String.valueOf(dsSach.get(poistion1).getGiaBia()))));
                b.putString("SOLUONG", String.valueOf(Integer.parseInt(String.valueOf(dsSach.get(poistion1).getSoLuong()))));
                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.deletebook:
                AdapterView.AdapterContextMenuInfo menuinfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int poistion = menuinfo.position;
                sachDAO.deleteSachByID(dsSach.get(poistion).getMaSach());
                dsSach.remove(poistion);
                dsSach.clear();
                dsSach = sachDAO.getAllSach();
                adapter.changeDatasetBook(sachDAO.getAllSach());
                break;
        }
        return super.onContextItemSelected(item);
    }
}
