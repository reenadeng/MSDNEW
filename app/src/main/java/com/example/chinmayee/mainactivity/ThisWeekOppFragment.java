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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * Chinmayee Nitin Vaidya, Bhumitra Nagar, Swapnil Mahajan, Xinyan Deng
 * This is for the HOME page. This fragment is used in HOME page,
 * THIS WEEK tab
 *
 */
public class ThisWeekOppFragment extends ListFragment  {
        private List<Opportunity> toDisplay;
        private Firebase myFirebaseRef;
        private Bundle bundle;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View rootView = inflater.inflate(R.layout.this_week_opportunities, container, false);
                myFirebaseRef = new Firebase(new Drive().getFirebaseURL());
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
                                                try {
                                                        if ((catFilter == "" || shortDesc.toUpperCase().contains(catFilter.toUpperCase()))&& calDate(date))
                                                                toDisplay.add(new Opportunity(id, name, img_loc, date, level, longDecs, shortDesc, dimScore, location, category));
                                                } catch (ParseException e) {
                                                        e.printStackTrace();
                                                }
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

        private boolean calDate(String d3) throws ParseException {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy, h:mm a");
                Calendar c = Calendar.getInstance();
                Calendar c3 = Calendar.getInstance();
                Date date3 = sdf.parse(d3);
                c3.setTime(date3);
                c.setTime(new Date()); // Now use today date.
                c.add(Calendar.DATE, 7); // Adding 7 days
                int diffInDays =
                        (int) (c.getTimeInMillis() - c3.getTimeInMillis()) / (1000 * 60 * 60 *
                                24);
                if (diffInDays <= 7 && diffInDays > 0)
                        return true;
                else
                        return false;
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
                Intent i = new Intent(getActivity().getApplicationContext(), OppDetail.class);
                Bundle b = new Bundle();
                b.putInt("oppId", toDisplay.get(position).getId()); //Your id
                b.putString("userLevel", bundle.getString("userLevel"));
                i.putExtras(b); //Put your id to your next Intent
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
        }
}
