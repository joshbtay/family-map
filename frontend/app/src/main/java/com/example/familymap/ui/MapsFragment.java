package com.example.familymap.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.familymap.core.FamilyMember;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import com.example.familymap.R;
import com.example.familymap.core.DataCache;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Event;
import model.Person;

public class MapsFragment extends Fragment {
    private DataCache dataCache = DataCache.getInstance();
    private GoogleMap map = null;
    private Map<Marker, String> eventMap;
    private Marker focused;
    private Person person;
    private List<Polyline> lines;
    private TextView eventName;
    private TextView eventInfo;
    private ImageView mapIcon;
    private RelativeLayout toPerson;
    private String eventID;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;

            addMarkers();
            if(eventID != null) {
                for (Map.Entry<Marker, String> event : eventMap.entrySet()) {
                    if (eventID.equals(event.getValue())){
                        setFocus(event.getKey());
                        break;
                    }
                }
            }


        }
    };

    public void setEventID(String eventID){
        this.eventID = eventID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        eventName = (TextView)v.findViewById(R.id.eventName);
        eventInfo = (TextView)v.findViewById(R.id.eventInfo);
        mapIcon = (ImageView)v.findViewById(R.id.mapIcon);
        toPerson = (RelativeLayout)v.findViewById(R.id.toPerson);
        lines = new ArrayList<>();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public void addMarkers(){
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return setFocus(marker);
            }
        });
        toPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(focused != null)
                    goToPerson();
            }
        });

        eventMap = new HashMap<>();
        Map<String, Event> events = dataCache.getFilteredEvents();
        if(events == null){
            return;
        }
        for (Map.Entry<String,Event> event: events.entrySet()){
            LatLng coord = new LatLng(event.getValue().getLatitude(), event.getValue().getLongitude());
            Marker marker = map.addMarker(new MarkerOptions().position(coord).icon(
                    BitmapDescriptorFactory.defaultMarker(dataCache.getColor(
                    event.getValue().getEventType())))
                    .title(event.getValue().getEventType() + " in " + event.getValue().getCity()));
            eventMap.put(marker, event.getKey());
        }


    }

    private boolean setFocus(Marker marker) {

        focused = marker;
        map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        Event event = dataCache.getEvent(eventMap.get(marker));
        drawLines(event);
        person = dataCache.getPerson(event.getPersonID());

        if(person.getGender().toLowerCase().equals("m"))
            mapIcon.setImageDrawable(new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.male_icon).sizeDp(64));
        else
            mapIcon.setImageDrawable(new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.female_icon).sizeDp(64));
        eventName.setText(person.getFirstName() + " " + person.getLastName());
        eventInfo.setText(event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")");
        return true;
    }

    private void drawLines(Event targetEvent) {
        for(Polyline line : lines){
            line.remove();
        }
        lines = new ArrayList<>();
        if(dataCache.getLifeLine()){
            List<Event> personEvents = dataCache.getEvents(targetEvent.getPersonID());
            for(int i = 1; i < personEvents.size(); i++){
                LatLng first = new LatLng(personEvents.get(i-1).getLatitude(),
                        personEvents.get(i-1).getLongitude());
                LatLng second = new LatLng(personEvents.get(i).getLatitude(),
                        personEvents.get(i).getLongitude());

                Polyline line = map.addPolyline(new PolylineOptions()
                        .add(first,second).width(15)
                        .color(ContextCompat.getColor(getActivity(), R.color.life_events)));
                lines.add(line);
            }
        }
        if(dataCache.getFamilyLine()){
            ArrayList<ArrayList<Pair<LatLng,LatLng>>> firstEvents = dataCache.getFirstEvents(targetEvent);
            int generations = firstEvents.size();
            for(int i = 0; i < firstEvents.size(); i++){
                for(int j = 0; j < firstEvents.get(i).size(); j++){
                    LatLng first = firstEvents.get(i).get(j).first;
                    LatLng second = firstEvents.get(i).get(j).second;
                    Polyline line = map.addPolyline(new PolylineOptions()
                            .add(first,second).width((generations - i) * 10)
                            .color(ContextCompat.getColor(getActivity(), R.color.family_events)));
                    lines.add(line);
                }
            }
        }

        if(dataCache.getSpouseLine()){
            FamilyMember spouse = dataCache.getPerson(
                    dataCache.getPerson(targetEvent.getPersonID()).getSpouseID());
            if(spouse == null){
                return;
            }
            List<Event> spouseEvents = dataCache.getEvents(spouse.getPersonID());
            if(spouseEvents.size() > 0){
                LatLng first = new LatLng(targetEvent.getLatitude(),
                        targetEvent.getLongitude());
                LatLng second = new LatLng(spouseEvents.get(0).getLatitude(),
                        spouseEvents.get(0).getLongitude());
                Polyline line = map.addPolyline(new PolylineOptions()
                        .add(first,second).width(15)
                        .color(ContextCompat.getColor(getActivity(), R.color.spouse_events)));
                lines.add(line);
            }
        }

    }

    private boolean goToPerson(){
        if(person == null)
            return false;
        Intent intent = new Intent(getActivity(), PersonActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("PersonID", person.getPersonID());
        intent.putExtras(bundle);
        startActivity(intent);
        return true;
    }

}