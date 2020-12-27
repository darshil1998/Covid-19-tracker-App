package com.covid19.covid_19tracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.covid19.covid_19tracker.R;
import com.covid19.covid_19tracker.activities.AffectedCountriesActivity;
import com.covid19.covid_19tracker.models.CountryModel;

import java.util.ArrayList;
import java.util.List;

public class CountryCaseAdapter extends ArrayAdapter<CountryModel> {
    private Context context;
    private List<CountryModel> countryModelList;
    private List<CountryModel> countryModelListFiltered;

    public CountryCaseAdapter(Context context, List<CountryModel> countryModelList) {
        super(context, R.layout.row_iteam_country_list, countryModelList);
        this.context = context;
        this.countryModelList = countryModelList;
        this.countryModelListFiltered = countryModelList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_iteam_country_list, null, true);
        ImageView imgFlag = view.findViewById(R.id.imgFlag);
        TextView txtCountryName = view.findViewById(R.id.txtCountryName);

        txtCountryName.setText(countryModelListFiltered.get(position).getCountry());
        Glide.with(context).load(countryModelListFiltered.get(position).getFlag()).into(imgFlag);
        return view;
    }

    @Override
    public int getCount() {
        return countryModelListFiltered.size();
    }

    @Nullable
    @Override
    public CountryModel getItem(int position) {
        return countryModelListFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    filterResults.count = countryModelList.size();
                    filterResults.values = countryModelList;
                } else {
                    List<CountryModel> resultModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for (CountryModel itemsModel : countryModelList) {
                        if (itemsModel.getCountry().toLowerCase().contains(searchStr)) {
                            resultModel.add(itemsModel);
                        }
                        filterResults.count = resultModel.size();
                        filterResults.values = resultModel;
                    }

                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                countryModelListFiltered = (List<CountryModel>) results.values;
                AffectedCountriesActivity.countryModelList=(List<CountryModel>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}
