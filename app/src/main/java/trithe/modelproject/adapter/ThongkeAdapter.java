package trithe.modelproject.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import trithe.modelproject.R;
import trithe.modelproject.model.Thongke;

public class ThongkeAdapter extends ArrayAdapter<Thongke> {
    private List<Thongke> list;
    private LayoutInflater inflater;

    public ThongkeAdapter(@NonNull Context context, int resource, @NonNull List<Thongke> objects) {
        super(context, resource, objects);
        list = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_thong_ke, parent, false);
            holder.tvTen = convertView.findViewById(R.id.edTen);
            holder.tvTien = convertView.findViewById(R.id.edTien);
//lưu holder vào converView
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //thiết lập giá trị item
        Thongke thongke = list.get(position);
        holder.tvTen.setText(thongke.getTen());
        holder.tvTien.setText(thongke.getSotien());
        //bắt sự kiện người dùng

        return convertView;
    }

    public static class ViewHolder {
        public ImageView imaAnh;
        public TextView tvTen, tvTien;

    }
}
