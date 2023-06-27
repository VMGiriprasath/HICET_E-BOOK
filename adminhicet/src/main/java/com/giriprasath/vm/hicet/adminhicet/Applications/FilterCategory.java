package com.giriprasath.vm.hicet.adminhicet.Applications;

import android.widget.Filter;

import com.giriprasath.vm.hicet.adminhicet.Adapter.AdapterCategory;
import com.giriprasath.vm.hicet.adminhicet.Model.ModelCategory;

import java.util.ArrayList;

public class FilterCategory extends Filter {
    ArrayList<ModelCategory> filterlist;
    AdapterCategory adapterCategory;

    public FilterCategory(ArrayList<ModelCategory> filterlist, AdapterCategory adapterCategory) {
        this.filterlist = filterlist;
        this.adapterCategory = adapterCategory;
    }
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        if (constraint!=null && constraint.length()>0){
            constraint=constraint.toString().toUpperCase();
            ArrayList<ModelCategory>filteredModels=new ArrayList<>();
            for (int i=0;i<filterlist.size();i++)
            {
                if (filterlist.get(i).getCategory().toUpperCase().contains(constraint))
                {
                    filteredModels.add(filterlist.get(i));
                }
            }
            results.count=filteredModels.size();
            results.values=filteredModels;
        }else
        {
            results.count=filterlist.size();
            results.values=filterlist;
        }

            return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapterCategory.categoryArrayList=(ArrayList<ModelCategory>)results.values;
        adapterCategory.notifyDataSetChanged();
    }
}
