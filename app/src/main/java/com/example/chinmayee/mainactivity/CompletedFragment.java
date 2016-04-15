package com.example.chinmayee.mainactivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompletedFragment extends Fragment {

    View rootView;
    ExpandableListView lv;
    private List<Opportunity> toDisplay;
    private Map<Integer, Integer[]> children;
    private Firebase myFirebaseRef = new Firebase(new Drive().getFirebaseURL());
    private Bundle bundle;
    private final List<Integer> completed = new ArrayList<Integer>();
    public CompletedFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.completed_opportunities, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bundle = getArguments();
        myFirebaseRef.child("completed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.child("nuid").getValue().toString().equals(bundle.getString("nuId"))) {
                        completed.add(Integer.valueOf(dataSnapshot1.child("oppId").getValue().toString()));
                    }
                }
                processIds(view);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    private void processIds(View view){
        lv = (ExpandableListView) view.findViewById(R.id.expListView);
        children = new HashMap<Integer, Integer[]>();
        if (completed.size()!=0) {
            myFirebaseRef.child("opportunity").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    toDisplay = new ArrayList<>();
                    int pos = 0;
                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        for (int i = 0; i < 1; i++) {
                            int id = Integer.parseInt((String) messageSnapshot.child("oppId").getValue());
                            String img_loc = (String) messageSnapshot.child("pic").getValue();
                            int level = Integer.parseInt((String) messageSnapshot.child("level").getValue());
                            String longDecs = (String) messageSnapshot.child("longDesc").getValue();
                            String shortDesc = (String) messageSnapshot.child("shortDesc").getValue();
                            String location = (String) messageSnapshot.child("location").getValue();
                            Integer[] dimScore = new Integer[5];
                            int sumScore = 0;
                            for (int j = 1; j < 6; j++) {
                                dimScore[j - 1] = Integer.parseInt((String) messageSnapshot.child("score").child("d" + j).getValue());
                                sumScore += dimScore[j - 1];
                            }
                            String date = (String) messageSnapshot.child("start date").getValue();
                            String name = (String) messageSnapshot.child("name").getValue();
                            String category = (String) messageSnapshot.child("category").getValue();
                            if (completed.contains(id)) {
                                toDisplay.add(pos, new Opportunity(id, name, img_loc, date, level, longDecs, shortDesc, dimScore, location, category));
                                children.put(pos, dimScore);
                                pos++;
                            }
                        }
                    }
                    lv.setAdapter(new ExpandableListAdapter(toDisplay, children));
                    lv.setGroupIndicator(null);
                }

                @Override
                public void onCancelled(FirebaseError error) {
                }
            });
        }

    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private final LayoutInflater inf;
        private List<Opportunity> toDisplay;
        private Map<Integer, Integer[]> children;

        public ExpandableListAdapter(List<Opportunity> toDisplay, Map<Integer, Integer[]> children) {
            this.toDisplay = toDisplay;
            this.children = children;
            inf = LayoutInflater.from(getActivity());
        }

        @Override
        public int getGroupCount() {
            return toDisplay.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return children.get(groupPosition).length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return toDisplay.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return children.get(groupPosition)[childPosition];
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            ViewImgHolder holder;
            if (convertView == null) {
                convertView = inf.inflate(R.layout.list_item_comp, parent, false);
                holder = new ViewImgHolder();

                holder.text = (TextView) convertView.findViewById(R.id.lblListItem);
                convertView.setTag(holder);
            } else {
                holder = (ViewImgHolder) convertView.getTag();
            }

            String dimentionName = "";
            System.out.println("Child position is : " + childPosition);
            if (childPosition ==0 ) dimentionName = "Intellectual Agility: ";
            if (childPosition ==1 ) dimentionName = "Global Mindset: ";
            if (childPosition ==2 ) dimentionName = "Social Consciousness and Interpersonal Commitment: ";
            if (childPosition ==3 ) dimentionName = "Professional and Personal Effectiveness: ";
            if (childPosition ==4 ) dimentionName = "Well-being: ";

            holder.text.setText(dimentionName +getChild(groupPosition, childPosition).toString());

            return convertView;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = inf.inflate(R.layout.list_group, parent, false);

                holder = new ViewHolder();
                holder.date = (TextView) convertView.findViewById(R.id.date);
                holder.location = (TextView) convertView.findViewById(R.id.location);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.img = (ImageView) convertView.findViewById(R.id.icon);
                holder.pts = (TextView) convertView.findViewById(R.id.pts);
                holder.cat = (TextView) convertView.findViewById(R.id.category);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Opportunity opp = (Opportunity)getGroup(groupPosition);
            holder.title.setText("What: "+opp.getShortDesc().toString());
            holder.date.setText("Date: "+opp.getDate().toString());
            holder.location.setText("Location: "+opp.getLocation().toString());
            holder.pts.setText(opp.getTotScore()+"pts");
            holder.cat.setText(opp.getCatagory().toUpperCase());
            int id = parent.getContext().getResources().getIdentifier(opp.getImg_loc(), "drawable", parent.getContext().getPackageName());
            holder.img.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.img.setImageResource(id);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        private class ViewHolder {
            TextView title;
            TextView date;
            TextView location;
            TextView pts;
            ImageView img;
            TextView cat;

        }

        private class ViewImgHolder {
            ImageView img;
            TextView text;

        }
    }
}
