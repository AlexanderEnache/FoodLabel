package com.example.foodlabel6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FoodListAdapter extends ArrayAdapter<String> {
    private int layout;
    DBHelper DB;

    public FoodListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        layout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String[] itemAndId = getItem(position).split(";");
        System.out.println(itemAndId[0] + " ID " + itemAndId[1]);
//        System.out.println(getItem(position));

        if(convertView == null){
            LayoutInflater li = LayoutInflater.from(getContext());
            convertView = li.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.id = (TextView)convertView.findViewById(R.id.id);
            viewHolder.title = (TextView)convertView.findViewById(R.id.name);
            viewHolder.button = (Button)convertView.findViewById(R.id.delete);
            viewHolder.isDeleted = false;

//            viewHolder.title.setText(getItem(position));

            viewHolder.title.setText(itemAndId[0]);
            viewHolder.id.setText(itemAndId[1]);

            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DB = new DBHelper(getContext());
                    DB.deleteItem(itemAndId[1]);
                    Toast.makeText(getContext(), viewHolder.title.getText(), Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            });
            convertView.setTag(viewHolder);
        }else{
            ViewHolder mainViewHolder = (ViewHolder) convertView.getTag();
            mainViewHolder.title.setText(getItem(position));

            mainViewHolder.title.setText(itemAndId[0]);
            mainViewHolder.id.setText(itemAndId[1]);
        }
        return convertView;
    }
    public class ViewHolder{
//        int id;
        TextView title;
        TextView id;
        boolean isDeleted;
        Button button;
    }
}
