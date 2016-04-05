package com.example.chinmayee.mainactivity;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

;

/**
 * Created by Swapnil on 3/24/2016.
 */
public class ThisWeekOppFragment extends ListFragment {
        private List<Opportunity> toDisplay;
        private Firebase myFirebaseRef;
        private Bundle bundle;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View rootView = inflater.inflate(R.layout.this_week_opportunities, container, false);
                myFirebaseRef = new Firebase("https://flickering-inferno-293.firebaseio.com/");
                myFirebaseRef.child("opportunity").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                                toDisplay = new ArrayList<>();
                                bundle = getArguments();
                                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                        for (int i =0; i<1; i++) {
                                                int id = Integer.parseInt((String)messageSnapshot.child("oppId").getValue());
                                                String img_loc= (String) messageSnapshot.child("pic").getValue();
                                                int level = Integer.parseInt((String)messageSnapshot.child("level").getValue());
                                                String longDecs = (String) messageSnapshot.child("longDesc").getValue();
                                                String shortDesc = (String) messageSnapshot.child("shortDesc").getValue();
                                                String location = (String) messageSnapshot.child("location").getValue();
                                                Integer[] dimScore = new Integer[5];
                                                int sumScore=0;
                                                for (int j=1; j<6; j++) {
                                                        dimScore[j - 1] = Integer.parseInt((String) messageSnapshot.child("score").child("d" + j).getValue());
                                                        sumScore += dimScore[j - 1];
                                                }
                                                String date = (String) messageSnapshot.child("start date").getValue();
                                                String name = (String) messageSnapshot.child("name").getValue();
                                                String category = (String) messageSnapshot.child("category").getValue();
                                                String catFilter = bundle.getString("filter");
                                                if (catFilter == "" || catFilter.equals(category))
                                                        toDisplay.add(new Opportunity(id, name, img_loc, date, level, longDecs, shortDesc, dimScore, location));
                                        }
                                }
                                CustomeAdapter adapter = new CustomeAdapter(getActivity(), toDisplay);
                                setListAdapter(adapter);
                        }
                        @Override
                        public void onCancelled(FirebaseError error) {
                        }
                });
                return rootView;
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
                Toast.makeText(getActivity(),getListView().getItemAtPosition(position).toString(),Toast.LENGTH_SHORT ).show();
        }
}
