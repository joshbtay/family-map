package com.example.familymap.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.familymap.core.DataCache;
import com.example.familymap.core.FamilyMember;

import android.util.Pair;
import android.view.MenuInflater;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familymap.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import model.Event;
import model.Person;

public class PersonActivity extends AppCompatActivity {
    private FamilyMember person = null;
    DataCache dataCache = DataCache.getInstance();
    private String personID = null;
    private Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ExpandableListView personView = findViewById(R.id.personView);

        Bundle args = getIntent().getExtras();
        personID = args.getString("PersonID");
        if(personID == null){
            finish();
        }
        person = dataCache.getPerson(personID);
        TextView firstName = findViewById(R.id.firstName);
        firstName.setText(person.getFirstName());
        TextView lastName = findViewById(R.id.lastName);
        lastName.setText(person.getLastName());
        TextView gender = findViewById(R.id.gender);
        if(person.getGender().toLowerCase().equals("m")){
            gender.setText("Male");
        }else{
            gender.setText("Female");
        }
        List<Event> events = dataCache.getEvents(personID);
        List<Pair<FamilyMember,String>> people = new ArrayList<>();
        if(person.getFatherID() != null && !person.getFatherID().equals("")){
            people.add(new Pair(dataCache.getPerson(person.getFatherID()), "Father"));
        }
        if(person.getMotherID()!= null && !person.getMotherID().equals("")){
            people.add(new Pair(dataCache.getPerson(person.getMotherID()), "Mother"));
        }
        if(person.getSpouseID() != null && !person.getSpouseID().equals("")){
            people.add(new Pair(dataCache.getPerson(person.getSpouseID()), "Spouse"));
        }
        if(person.getChildren().size()> 0){
            for(String person : person.getChildren()){
                people.add(new Pair(dataCache.getPerson(person), "Child"));
            }
        }
        personView.setAdapter(new ExpandableListAdapter(people, events));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int PERSON_GROUP_POSITION = 1;
        private static final int EVENT_GROUP_POSITION = 0;
        private final List<Pair<FamilyMember,String>> people;
        private final List<Event> events;

        public ExpandableListAdapter(List<Pair<FamilyMember, String>> people, List<Event> events) {
            this.people = people;
            this.events = events;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch(groupPosition) {
                case PERSON_GROUP_POSITION:
                    return people.size();
                case EVENT_GROUP_POSITION:
                    return events.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch(groupPosition){
                case PERSON_GROUP_POSITION:
                    return getString(R.string.personListName);
                case EVENT_GROUP_POSITION:
                    return getString(R.string.eventListName);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch(groupPosition) {
                case EVENT_GROUP_POSITION:
                    return events.get(childPosition);
                case PERSON_GROUP_POSITION:
                    return people.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
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
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    titleView.setText(R.string.eventListName);
                    break;
                case PERSON_GROUP_POSITION:
                    titleView.setText(R.string.personListName);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;
            switch(groupPosition){
                case EVENT_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.event_item, parent, false);
                    initializeEventItem(itemView, childPosition);
                    break;
                case PERSON_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.person_item, parent, false);
                    initializePersonItem(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializePersonItem(View itemView, int childPosition) {
            TextView personName = itemView.findViewById(R.id.personName);
            FamilyMember person = people.get(childPosition).first;
            String relation = people.get(childPosition).second;
            personName.setText(person.getFirstName() + " " + person.getLastName());
            TextView relationship = itemView.findViewById(R.id.relationship);
            relationship.setText(relation);
            ImageView genderIcon = itemView.findViewById(R.id.genderIcon);
            if(person.getGender().toLowerCase().equals("m"))
                genderIcon.setImageDrawable(new IconDrawable(activity, FontAwesomeIcons.fa_male).colorRes(R.color.male_icon).sizeDp(64));
            else
                genderIcon.setImageDrawable(new IconDrawable(activity, FontAwesomeIcons.fa_female).colorRes(R.color.female_icon).sizeDp(64));
            itemView.setOnClickListener(v -> goToPerson(person.getPersonID()));
        }

        private void initializeEventItem(View itemView, int childPosition) {
            Event event = events.get(childPosition);
            TextView eventName = itemView.findViewById(R.id.eventName);
            eventName.setText(person.getFirstName() + " " + person.getLastName());
            TextView eventInfo = itemView.findViewById(R.id.eventInfo);
            eventInfo.setText(event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")");
            ImageView mapIcon = itemView.findViewById(R.id.mapIcon);
            mapIcon.setImageDrawable(new IconDrawable(activity, FontAwesomeIcons.fa_map_marker).colorRes(R.color.teal_200).sizeDp(64));
            itemView.setOnClickListener(v -> goToEvent(event.getEventID()));
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        private boolean goToPerson(String personID){
            Intent intent = new Intent(activity, PersonActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("PersonID", personID);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }

        private boolean goToEvent(String eventID){
            Intent intent = new Intent(activity, EventActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("EventID", eventID);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }
    }


}