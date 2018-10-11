package trithe.modelproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import trithe.modelproject.R;
import trithe.modelproject.database.SachDAO;
import trithe.modelproject.model.Sach;

public class BookAdapter extends BaseAdapter implements Filterable {
    List<Sach> arrSach;
    List<Sach> arrSortSach;
    private Filter sachFilter;
    public Activity context;
    public LayoutInflater inflater;
    SachDAO sachDAO;

    public BookAdapter(Activity context, List<Sach> arraySach) {
        super();
        this.context = context;
        this.arrSach = arraySach;
        this.arrSortSach = arraySach;
        this.inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sachDAO = new SachDAO(context);
    }

    @Override
    public int getCount() {
        return arrSach.size();
    }

    @Override
    public Object getItem(int position) {
        return arrSach.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolder {
        TextView txtBookID, txtTheloaiBook, txtNameBook, txtPrice, txtSoLuong;
//        ImageView imgDelete;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_book, null);
            holder.txtSoLuong = (TextView) convertView.findViewById(R.id.tvSoLuong);
            holder.txtBookID = convertView.findViewById(R.id.tvBookID);
            holder.txtNameBook = convertView.findViewById(R.id.tvBookName);
            holder.txtTheloaiBook = convertView.findViewById(R.id.tvTheloaiBook);
            holder.txtPrice = convertView.findViewById(R.id.tvPrice);

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
//                            sachDAO.deleteSachByID(arrSach.get(position).getMaSach());
//                            arrSach.remove(position);
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
        Sach _entry = (Sach) arrSach.get(position);
        holder.txtBookID.setText( _entry.getMaSach());
        holder.txtNameBook.setText(_entry.getTenSach());
        holder.txtTheloaiBook.setText(_entry.getMaTheLoai());
        holder.txtPrice.setText( _entry.getGiaBia()+" VND");
        holder.txtSoLuong.setText(_entry.getSoLuong() + "");
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void changeDatasetBook(List<Sach> items) {
        this.arrSach = items;
        notifyDataSetChanged();
    }

    public void resetData() {
        arrSach = arrSortSach;
    }

    public Filter getFilter() {
        if (sachFilter == null) {
            sachFilter = new CustomFilter();
        }
        return sachFilter;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                results.values = arrSortSach;
                results.count = arrSortSach.size();
            } else {
                List<Sach> lsSach = new ArrayList<Sach>();
                for (Sach p : arrSach) {
                    if (p.getMaSach().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        lsSach.add(p);
                }
                results.values = lsSach;
                results.count = lsSach.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                arrSach = (List<Sach>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}
