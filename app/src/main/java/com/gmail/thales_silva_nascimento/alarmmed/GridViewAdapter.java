package com.gmail.thales_silva_nascimento.alarmmed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.thales_silva_nascimento.alarmmed.model.ItemAlarme;

import java.util.ArrayList;

/**
 * Created by Thales on 21/01/2018.
 */

public class GridViewAdapter extends BaseAdapter {

    private ArrayList<ItemGridView> items;
    private Context context;

    public GridViewAdapter(Context context, ArrayList<ItemGridView> items){
        this.items = items;
        this.context = context;
    }
    @Override
    public int getCount() {
        return items.size() < 0 ? 0 : items.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = LayoutInflater.from(context).inflate(R.layout.grid_view_item, null);
        TextView tv = (TextView) v.findViewById(R.id.tvGridView);
        tv.setText(items.get(i).getTexto());

        ImageView imageView = (ImageView) v.findViewById(R.id.imgGridView);
        imageView.setImageResource(items.get(i).getImgResource());

        return v;
    }
}
