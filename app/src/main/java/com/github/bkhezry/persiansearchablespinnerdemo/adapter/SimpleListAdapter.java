package com.github.bkhezry.persiansearchablespinnerdemo.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.bkhezry.persiansearchablespinnerdemo.R;
import com.github.bkhezry.persiansearchablespinnerdemo.util.AppUtils;
import com.github.bkhezry.searchablespinner.interfaces.ISpinnerSelectedView;

import java.util.ArrayList;

public class SimpleListAdapter extends BaseAdapter implements Filterable, ISpinnerSelectedView {

  private Typeface mTypeface;
  private Context mContext;
  private ArrayList<String> mBackupStrings;
  private ArrayList<String> mStrings;
  private StringFilter mStringFilter = new StringFilter();

  public SimpleListAdapter(Context context, ArrayList<String> strings, String fontName) {
    mContext = context;
    mStrings = strings;
    mBackupStrings = strings;
    if (fontName != null) {
      mTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);
    }
  }

  @Override
  public int getCount() {
    return mStrings == null ? 0 : mStrings.size();
  }

  @Override
  public Object getItem(int position) {
    if (mStrings != null) {
      return mStrings.get(position);
    } else {
      return null;
    }
  }

  @Override
  public long getItemId(int position) {
    if (mStrings == null) {
      return mStrings.get(position).hashCode();
    } else {
      return -1;
    }
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = View.inflate(mContext, R.layout.view_list_item, null);
    ImageView letters = view.findViewById(R.id.letter_image_view);
    TextView displayName = view.findViewById(R.id.display_name_text_view);
    letters.setImageDrawable(AppUtils.getTextDrawable(mContext, mStrings.get(position), mTypeface));
    displayName.setText(mStrings.get(position));
    return view;
  }

  @Override
  public View getSelectedView(int position) {
    View view = View.inflate(mContext, R.layout.view_list_item, null);
    ImageView letters = view.findViewById(R.id.letter_image_view);
    TextView displayName = view.findViewById(R.id.display_name_text_view);
    letters.setImageDrawable(AppUtils.getTextDrawable(mContext, mStrings.get(position), mTypeface));
    displayName.setText(mStrings.get(position));
    return view;
  }

  @Override
  public Filter getFilter() {
    return mStringFilter;
  }

  public class StringFilter extends Filter {

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
      final FilterResults filterResults = new FilterResults();
      if (TextUtils.isEmpty(constraint)) {
        filterResults.count = mBackupStrings.size();
        filterResults.values = mBackupStrings;
        return filterResults;
      }
      final ArrayList<String> filterStrings = new ArrayList<>();
      for (String text : mBackupStrings) {
        if (text.toLowerCase().contains(constraint)) {
          filterStrings.add(text);
        }
      }
      filterResults.count = filterStrings.size();
      filterResults.values = filterStrings;
      return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
      mStrings = (ArrayList) results.values;
      notifyDataSetChanged();
    }
  }
}