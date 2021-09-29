package com.example.familymap.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.familymap.R;
import com.example.familymap.core.DataCache;
import com.example.familymap.core.FamilyMember;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import model.Event;

public class SearchActivity extends AppCompatActivity {
    private String searchParam;
    private DataCache dataCache;
    private List<FamilyMember> people;
    private List<Event> events;
    private RecyclerView recyclerView;
    private SearchView search;
    private static final int PERSON_VIEW = 1;
    private static final int EVENT_VIEW = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search = findViewById(R.id.query);
        search.setIconifiedByDefault(false);
        dataCache = DataCache.getInstance();
        searchParam = "";
        setView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchParam = newText;
                setView();
                return true;
            }
        });
    }

    private void setView(){
        recyclerView = findViewById(R.id.RecyclerView);
        people = dataCache.searchPeople(searchParam);
        events = dataCache.searchEvents(searchParam);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        SearchAdapter adapter = new SearchAdapter();
        recyclerView.setAdapter(adapter);
    }


    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

        public int getItemViewType(int position) {
            return position < events.size() ? EVENT_VIEW : PERSON_VIEW;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if(viewType == EVENT_VIEW) {
                view = getLayoutInflater().inflate(R.layout.event_item, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.person_item, parent, false);
            }
            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if(position < events.size()) {
                holder.bind(events.get(position));
            } else {
                holder.bind(people.get(position - events.size()));
            }
        }

        @Override
        public int getItemCount() {
            return events.size() + people.size();
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView name;
        private final TextView info;
        private final ImageView icon;
        private final int viewType;
        private FamilyMember person;
        private Event event;
        public SearchViewHolder(@NonNull View view, int viewType) {
            super(view);
            this.viewType = viewType;
            itemView.setOnClickListener(this);

            if(viewType == EVENT_VIEW) {
                name = itemView.findViewById(R.id.eventName);
                info = itemView.findViewById(R.id.eventInfo);
                icon = itemView.findViewById(R.id.mapIcon);
            } else {
                name = itemView.findViewById(R.id.personName);
                info = null;
                icon = itemView.findViewById(R.id.genderIcon);
            }

        }

        private void bind(Event event){
            this.event = event;
            this.person = dataCache.getPerson(event.getPersonID());
            name.setText(person.getFirstName() + " " + person.getLastName());
            info.setText(event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")");
            icon.setImageDrawable(new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.teal_200).sizeDp(64));
        }

        private void bind(FamilyMember person){
            this.person = person;
            name.setText(person.getFirstName() + " " + person.getLastName());
            if(person.getGender().toLowerCase().equals("m")){
                icon.setImageDrawable(new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male).colorRes(R.color.male_icon).sizeDp(64));
            }
            else{
                icon.setImageDrawable(new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female).colorRes(R.color.female_icon).sizeDp(64));
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            Bundle bundle = new Bundle();
            if(viewType == EVENT_VIEW){
                intent = new Intent(SearchActivity.this, EventActivity.class);
                bundle.putString("EventID", event.getEventID());
            } else {
                intent = new Intent(SearchActivity.this, PersonActivity.class);
                bundle.putString("PersonID", person.getPersonID());
            }
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}