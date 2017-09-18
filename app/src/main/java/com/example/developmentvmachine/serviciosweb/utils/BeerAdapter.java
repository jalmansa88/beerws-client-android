package com.example.developmentvmachine.serviciosweb.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.developmentvmachine.serviciosweb.R;
import com.example.developmentvmachine.serviciosweb.model.Cerveza;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Development VMachine on 28/07/2017.
 */

public class BeerAdapter extends BaseAdapter implements Filterable {

    private List<Cerveza> beerList;
    private List<Cerveza> filteredBeerList;
    private Context context;
    private LayoutInflater inflater;

    public BeerAdapter(Context context, List<Cerveza> beerList) {
        this.beerList = beerList;
        this.context = context;
        this.filteredBeerList = beerList;
    }

    @Override
    public int getCount() {
        return filteredBeerList.size();
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
                .load(filteredBeerList.get(position).getImagePath())
                .crossFade()
                .error(android.R.drawable.ic_menu_report_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(beerImage);

        beerName.setText(filteredBeerList.get(position).getName());
        beerDesc.setText(filteredBeerList.get(position).getDescription());
        beerCountry.setText(filteredBeerList.get(position).getCountry());
        beerFamily.setText(filteredBeerList.get(position).getFamily());
        beerType.setText(filteredBeerList.get(position).getType());
        beerAlc.setText(filteredBeerList.get(position).getAlc().toString() + "%");

        return beerView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                List<Cerveza> filteredList = new ArrayList<>();

                for(Cerveza beer : beerList){
                    if(beer.getName().toLowerCase().contains(charSequence.toString())){
                        filteredList.add(beer);
                    }
                }

                filterResults.values = filteredList;
                filterResults.count = filteredList.size();

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredBeerList = (List<Cerveza>) filterResults.values;
                if(filterResults.count == 0){
                    notifyDataSetInvalidated();
                }else {
                    notifyDataSetChanged();
                }
            }
        };

        return filter;
    }
}
