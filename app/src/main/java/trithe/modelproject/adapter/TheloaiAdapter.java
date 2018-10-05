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
import trithe.modelproject.database.TheloaiDAO;
import trithe.modelproject.model.Sach;
import trithe.modelproject.model.Theloai;

public class TheloaiAdapter extends BaseAdapter implements Filterable {
    List<Theloai> arrTheLoai;
    List<Theloai> arrSortTheLoai;
    private Filter theLoaiFilter;
    public Activity context;
    public LayoutInflater inflater;
    TheloaiDAO theLoaiDAO;

    public TheloaiAdapter(Activity context, List<Theloai> arrayTheLoai) {
        super();
        this.context = context;
        this.arrTheLoai = arrayTheLoai;
        this.arrSortTheLoai=arrayTheLoai;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        theLoaiDAO = new TheloaiDAO(context);
    }


    @Override
    public int getCount() {
        return arrTheLoai.size();
    }

    @Override
    public Object getItem(int position) {
        return arrTheLoai.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public static class ViewHolder {
        TextView txtMaTheLoai,txtTenTheLoai,txtDesTheLoai,txtPrositon;
//        ImageView imgDelete;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_theloai, null);
            holder.txtMaTheLoai = (TextView) convertView.findViewById(R.id.tvMaTheLoai);
            holder.txtTenTheLoai = (TextView) convertView.findViewById(R.id.tvTenTheLoai);
            holder.txtDesTheLoai = (TextView) convertView.findViewById(R.id.tvDesTheLoai);
            holder.txtPrositon = (TextView) convertView.findViewById(R.id.tvPositionTheLoai);
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
//                            theLoaiDAO.deleteTheLoaiByID(arrTheLoai.get(position).getMaTheLoai());
//                            arrTheLoai.remove(position);
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
        Theloai _entry = (Theloai) arrTheLoai.get(position);
        holder.txtMaTheLoai.setText(_entry.getMaTheLoai());
        holder.txtTenTheLoai.setText(_entry.getTenTheLoai());
        holder.txtDesTheLoai.setText(_entry.getMoTa());
        holder.txtPrositon.setText(_entry.getViTri()+"");
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void changeDatasetTheloai(List<Theloai> items) {
        this.arrTheLoai = items;
        notifyDataSetChanged();
    }

    public void resetData() {
        arrTheLoai = arrSortTheLoai;
    }


    @Override
    public Filter getFilter() {
        if (theLoaiFilter == null)
            theLoaiFilter = new CustomFilter();
        return theLoaiFilter;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                results.values = arrSortTheLoai;
                results.count = arrSortTheLoai.size();
            } else {
                List<Theloai> lsTheloai = new ArrayList<Theloai>();
                for (Theloai p : arrTheLoai) {
                    if (p.getMaTheLoai().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        lsTheloai.add(p);
                }
                results.values = lsTheloai;
                results.count = lsTheloai.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                arrTheLoai = (List<Theloai>) results.values;
                notifyDataSetChanged();
            }
        }

    }
}
