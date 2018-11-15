package com.gmail.thales_silva_nascimento.alarmmed.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gmail.thales_silva_nascimento.alarmmed.activity.BemVindo;
import com.gmail.thales_silva_nascimento.alarmmed.R;

public class SlideInicialAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    private Context context;
    private int [] layouts;

    public SlideInicialAdapter(Context context, int [] layouts){
        this.context = context;
        this.layouts = layouts;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(layouts[position], container, false);
        container.addView(view);

        if(position == 1){
            Button btn = (Button) view.findViewById(R.id.btnTesteLayout);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("Apertou", "Funcionou");
                    BemVindo activity = (BemVindo) context;
                    activity.LiberaPermissoes();
                }
            });
        }

        return view;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
