package com.example.developmentvmachine.serviciosweb.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.developmentvmachine.serviciosweb.R;
import com.example.developmentvmachine.serviciosweb.model.Cerveza;

import java.util.List;

/**
 * Created by Development VMachine on 28/07/2017.
 */

public class BeerAdapter extends BaseAdapter {

    private List<Cerveza> beerList;
    private Context context;
    private LayoutInflater inflater;

    public BeerAdapter(Context context, List<Cerveza> beerList) {
        this.beerList = beerList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return beerList.size();
    }

    @Override
    public Object getItem(int position) {
        return beerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View beerView = inflater.inflate(R.layout.beer_item, parent, false);

        ImageView beerImage = (ImageView) beerView.findViewById(R.id.lvImage);
        TextView beerName = (TextView) beerView.findViewById(R.id.lvName);
        TextView beerDesc = (TextView) beerView.findViewById(R.id.lvDescription);
        TextView beerCountry = (TextView) beerView.findViewById(R.id.lvCountry);
        TextView beerFamily = (TextView) beerView.findViewById(R.id.lvFamily);
        TextView beerType = (TextView) beerView.findViewById(R.id.lvType);
        TextView beerAlc = (TextView) beerView.findViewById(R.id.lvAlc);

        Glide.with(context)
                .load(beerList.get(position).getImagePath())
                .crossFade()
                .error(android.R.drawable.ic_menu_report_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(beerImage);

        beerName.setText(beerList.get(position).getName());
        beerDesc.setText(beerList.get(position).getDescription());
        beerCountry.setText(beerList.get(position).getCountry());
        beerFamily.setText(beerList.get(position).getFamily());
        beerType.setText(beerList.get(position).getType());
        beerAlc.setText(beerList.get(position).getAlc().toString() + "%");

        return beerView;
    }
}
