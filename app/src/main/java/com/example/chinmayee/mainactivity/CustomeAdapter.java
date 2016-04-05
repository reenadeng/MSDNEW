package com.example.chinmayee.mainactivity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

;

/**
 * Created by Swapnil on 3/28/2016.
 */
public class CustomeAdapter extends BaseAdapter {

    Context context;
    List<Opportunity> opp;

    public CustomeAdapter(Context context, List<Opportunity> opp) {
        this.context = context;
        this.opp = opp;
    }

    @Override
    public int getCount() {
        return opp.size();
    }

    @Override
    public Object getItem(int position) {
        return opp.get(position);
    }

    @Override
    public long getItemId(int position) {
        return opp.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtDate = (TextView) convertView.findViewById(R.id.date);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtLoc = (TextView) convertView.findViewById(R.id.location);
        TextView txtPts = (TextView) convertView.findViewById(R.id.pts);

        Opportunity opp_pos = opp.get(position);
        // setting the image resource and title
        String imgName = opp_pos.getImg_loc();
        int id = parent.getContext().getResources().getIdentifier(imgName, "drawable", parent.getContext().getPackageName());
        imgIcon.setScaleType(ImageView.ScaleType.FIT_XY);
        imgIcon.setImageResource(id);
        txtDate.setText("When: "+opp_pos.getDate());
        txtLoc.setText("Where: " + opp_pos.getLocation());
        txtTitle.setText("What: "+opp_pos.getShortDesc());
        txtPts.setText(opp_pos.getTotScore()+"pts");

        return convertView;
    }
}
