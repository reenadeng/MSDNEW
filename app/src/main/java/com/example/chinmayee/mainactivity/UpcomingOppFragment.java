package com.example.chinmayee.mainactivity;

import android.content.Intent;
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

public class UpcomingOppFragment extends ListFragment {
    private List<Opportunity> toDisplay;
    private Firebase myFirebaseRef = new Firebase(new Drive().getFirebaseURL());
    private Bundle bundle;
    private final List<Integer> upcoming = new ArrayList<Integer>();
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.inprogress_opportunities, container, false);
        bundle = getArguments();
        myFirebaseRef.child("upcoming").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.child("nuid").getValue().toString().equals(bundle.getString("nuId"))) {
                        upcoming.add(Integer.valueOf(dataSnapshot1.child("oppId").getValue().toString()));
                    }
                }
                processIds();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        return rootView;
    }

    private void processIds (){
        if (upcoming.size() != 0) {
            myFirebaseRef.child("opportunity").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    toDisplay = new ArrayList<>();
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
                            if (upcoming.contains(id))
                                toDisplay.add(new Opportunity(id, name, img_loc, date, level, longDecs, shortDesc, dimScore, location, category));
                        }
                    }
                    CustomeAdapter adapter = new CustomeAdapter(getActivity(), toDisplay);
                    setListAdapter(adapter);
                }

                @Override
                public void onCancelled(FirebaseError error) {
                }
            });
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent i = new Intent(getActivity().getApplicationContext(), OppDetail.class);
        Bundle b = new Bundle();
        b.putInt("oppId", toDisplay.get(position).getId()); //Your id
        b.putString("userLevel", bundle.getString("userLevel"));
        b.putBoolean("isComplete", true);
        i.putExtras(b); //Put your id to your next Intent
        startActivity(i);
    }

}
