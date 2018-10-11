package trithe.modelproject.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import trithe.modelproject.R;
import trithe.modelproject.activity.EditUserActivity;
import trithe.modelproject.activity.ListHoaDonActivity;
import trithe.modelproject.adapter.UserAdapter;
import trithe.modelproject.database.SachDAO;
import trithe.modelproject.database.UserDAO;
import trithe.modelproject.model.User;

public class ListUserFragment extends Fragment implements View.OnClickListener {
    //    private FloatingActionButton fabThem;
    CardView cardView;
    private ListView listView;
    private EditText edUser, edPass, edConfirmpass, edName, edPhone, editText;
    private UserDAO userDAO;
    UserAdapter adapter;
    private List<User> list;
    private CardView cardsave, cardcancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_list_user_fragment, container, false);
//        fabThem = view.findViewById(R.id.fabuser);
        listView = view.findViewById(R.id.lvuser);
        userDAO = new UserDAO(getContext());
        list = userDAO.getAllUser();
        adapter = new UserAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        cardView = view.findViewById(R.id.fabuser1);
        cardView.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo
            menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.menu_lv, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                AdapterView.AdapterContextMenuInfo menuinfo1 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int poistion1 = menuinfo1.position;
                Intent intent = new Intent(getContext(), EditUserActivity.class);
                Bundle b = new Bundle();
                b.putString("USERNAME", list.get(poistion1).getUserName());
                b.putString("PHONE", list.get(poistion1).getPhone());
                b.putString("NAME", list.get(poistion1).getName());
                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.search:
                list.clear();
                userDAO = new UserDAO(getContext());
                list = userDAO.getAllUser();
                adapter = new UserAdapter(getActivity(), list);
                adapter.changeDataset(userDAO.getAllUser());
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                final View view = inflater.inflate(R.layout.dialog_search, null);
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                builder.setTitle("Search");
                builder.setIcon(R.drawable.youtube_search);
                builder.setView(view);
                editText = view.findViewById(R.id.edSearch);
                //lv 3loai
                listView.setAdapter(adapter);
//                listView.setAdapter(adapter);
                //
                listView.setTextFilterEnabled(true);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int
                            count) {
                        System.out.println("Text [" + s + "] - Start [" + start + "] - Before [" + before + "] - Count [" + count + "]");
                        if (count < before) {
                            adapter.resetData();
//                            adapter.resetData();
                        }
                        adapter.getFilter().filter(s.toString());
//                        adapter.getFilter().filter(s.toString());
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
//
//                        dsUser.clear();
//                        dsUser = userDAO.getAllUser();
//                        adapter.changeDataset(userDAO.getAllUser());
//
                        list.clear();
                        list = userDAO.getAllUser();
                        adapter.changeDataset(userDAO.getAllUser());

                    }
                });
                builder.show();
                break;
            case R.id.delete:
                AdapterView.AdapterContextMenuInfo menuinfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int poistion = menuinfo.position;
                userDAO.deleteUserByID(list.get(poistion).getUserName());
                list.remove(poistion);
                list.clear();
                list = userDAO.getAllUser();
                adapter.changeDataset(userDAO.getAllUser());
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        list.clear();
        list = userDAO.getAllUser();
        adapter.changeDataset(userDAO.getAllUser());
    }

    public void card() {
        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.dialog, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add User");
        builder.setView(view);
        builder.setIcon(R.drawable.note_add);
        edName = view.findViewById(R.id.edNamedialog);
        edPass = view.findViewById(R.id.edPassdialog);
        edConfirmpass = view.findViewById(R.id.edConfirmPassdialog);
        edPhone = view.findViewById(R.id.edPhonedialog);
        edUser = view.findViewById(R.id.edUserdialog);
        cardsave = view.findViewById(R.id.cardsave);
        cardcancel = view.findViewById(R.id.cardcancel);
        final AlertDialog dialog = builder.show();
        cardsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDAO = new UserDAO(getContext());
                User user = new User(edUser.getText().toString(), edPass.getText().toString(), edName.getText().toString(), edPhone.getText().toString());
                try {
                    if (validateForm() > 0) {
                        if (userDAO.inserUser(user) > 0) {
                            Toast.makeText(getContext(), getString(R.string.emptyadd), Toast.LENGTH_SHORT).show();
                            list.clear();
                            list = userDAO.getAllUser();
                            adapter.changeDataset(userDAO.getAllUser());
                            dialog.dismiss();
                        } else {
                            edUser.setError(getString(R.string.emptyadderror));

                        }
                    }
                } catch (Exception ex) {
                    Log.e("Error", ex.toString());
                }
            }
        });
        cardcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edUser.setText("");
                edPass.setText("");
                edConfirmpass.setText("");
                edName.setText("");
                edPhone.setText("");

            }
        });

        //thay đổi vị trídialog
//        AlertDialog dialog = builder.create();
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
//
//        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
//        wmlp.x = 100;   //x position
//        wmlp.y = 100;   //y position
//
//        dialog.show();

//        Window window = dialog.getWindow();
//        WindowManager.LayoutParams wlp = window.getAttributes();
//
//        wlp.gravity = Gravity.BOTTOM;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        window.setAttributes(wlp);
//        dialog.show();


//
//
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        userDAO = new UserDAO(getContext());
//                        User user = new User(edUser.getText().toString(), edPass.getText().toString(), edName.getText().toString(), edPhone.getText().toString());
//                        try {
//                            if (validateForm() > 0) {
//                                if (userDAO.inserUser(user) > 0) {
//                                    Toast.makeText(getContext(), getString(R.string.emptyadd), Toast.LENGTH_SHORT).show();
//                                    list.clear();
//                                    list = userDAO.getAllUser();
//                                    adapter.changeDataset(userDAO.getAllUser());
//                                } else {
//                                    edUser.setError(getString(R.string.emptyadderror));
//
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
        if (edUser.getText().toString().length() == 0) {
            edUser.setError(getString(R.string.error_empty));
            check = -1;
        } else if (edUser.getText().toString().length() > 50) {
            edUser.setError(getString(R.string.leng));
            check = -1;
        } else if (edPass.getText().toString().length() == 0) {
            edPass.setError(getString(R.string.error_emptyps));
            check = -1;
        } else if ((edPass.getText().toString().length() < 6)) {
            edPass.setError(getString(R.string.error_length));
            check = -1;
        } else if (edPass.getText().toString().length() > 50) {
            edPass.setError(getString(R.string.leng));
            check = -1;
        } else if (edConfirmpass.getText().toString().equals("")) {
            edConfirmpass.setError(getString(R.string.empty_confirmpassword));
        } else if (!(edConfirmpass.getText().toString()).equals(edPass.getText().toString())) {
            edConfirmpass.setError(getString(R.string.error_likepw));
            check = -1;
        } else if (edName.getText().toString().length() == 0) {
            edName.setError(getString(R.string.error_emptyname));
            check = -1;
        } else if (edName.getText().toString().length() > 20) {
            edName.setError(getString(R.string.lengthname));
            check = -1;
        } else if (edPhone.getText().toString().length() == 0) {
            edPhone.setError(getString(R.string.error_emptyphone));
            check = -1;
        } else if ((edPhone.getText().toString()).length() < 10 || (edPhone.getText().toString()).length() > 11) {
            edPhone.setError(getString(R.string.error_emptyphonelength));
            check = -1;
        }
        return check;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabuser1:
                card();
                break;
        }
    }
}
