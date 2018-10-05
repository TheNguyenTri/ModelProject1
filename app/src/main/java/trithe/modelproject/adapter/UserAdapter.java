package trithe.modelproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import trithe.modelproject.R;
import trithe.modelproject.database.UserDAO;
import trithe.modelproject.model.Theloai;
import trithe.modelproject.model.User;

public class UserAdapter extends BaseAdapter implements Filterable {
    List<User> list;
    List<User> listSort;
    private Filter UserFilter;
    public Activity context;
    public LayoutInflater inflater;
    UserDAO userDAO;

    public UserAdapter(Activity context, List<User> list) {
        super();
        this.context = context;
        this.list = list;
        this.listSort = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        userDAO = new UserDAO(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public static class ViewHolder {
        ImageView img;
        TextView txtName;
        TextView txtPhone;
//        ImageView imgDelete;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_user, null);
            holder.txtName = (TextView) convertView.findViewById(R.id.tvName);
            holder.txtPhone = (TextView) convertView.findViewById(R.id.tvPhone);
//            holder.imgDelete = (ImageView) convertView.findViewById(R.id.ivDelete);
//            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setTitle("Message");
//                    builder.setMessage("Do you want delete this item ?");
//                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            userDAO.deleteUserByID(list.get(position).getUserName());
//                            list.remove(position);
//                            notifyDataSetChanged();
//                        }
//                    });
//                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    });
//                    builder.show();
//                }
//            });
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        User _entry = list.get(position);
        holder.txtName.setText(_entry.getName());
        holder.txtPhone.setText(_entry.getPhone());
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void changeDataset(List<User> items) {
        this.list = items;
        notifyDataSetChanged();
    }

    public void resetData() {
        list = listSort;
    }

    @Override
    public Filter getFilter() {
        if (UserFilter == null)
            UserFilter = new UserAdapter.CustomFilter();
        return UserFilter;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                results.values = listSort;
                results.count = listSort.size();
            } else {
                List<User> users = new ArrayList<User>();
                for (User p : list) {
                    if (p.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        users.add(p);
                }
                results.values = users;
                results.count = users.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                list = (List<User>) results.values;
                notifyDataSetChanged();
            }
        }

    }
}
