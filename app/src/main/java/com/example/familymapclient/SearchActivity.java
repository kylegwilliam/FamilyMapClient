package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import model.Event;
import model.Person;

public class SearchActivity extends AppCompatActivity {

    public static final int EVENT_TYPE = 0;
    public static final int FAMILY_TYPE = 1;

    public List<Person> globalPerson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        DataCache dataCache;
        dataCache = DataCache.getInstance();

        List<Event> allEvents = dataCache.getEvents();
        List<Person> allPeople = dataCache.getPeople();

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(allEvents, allPeople);
        recyclerView.setAdapter(adapter);

        EditText editText = findViewById(R.id.edittext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }



    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> implements Filterable {

        private final List<Event> allEvents;
        private final List<Person> allPeople;

        private List<Event> allEventsFull;
        private List<Person> allPeopleFull;



        RecyclerViewAdapter(List<Event> allEvents, List<Person> allPeople) {
            this.allEvents = allEvents;
            this.allPeople = allPeople;
            allEventsFull = new ArrayList<>(allEvents);
            allPeopleFull = new ArrayList<>(allPeople);

            globalPerson = allPeople;
        }

        @Override
        public int getItemViewType(int position) {
            return position < allEvents.size() ? EVENT_TYPE : FAMILY_TYPE;
        }


        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if (viewType == EVENT_TYPE) {
                view = getLayoutInflater().inflate(R.layout.inflater_event, parent, false);
            }
            else {
                view = getLayoutInflater().inflate(R.layout.inflater_family, parent, false);
            }

            return new RecyclerViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
            if (position < allEvents.size()) {
                holder.bind(allEvents.get(position));
            }
            else {
                holder.bind(allPeople.get(position - allEvents.size()));
            }

        }

        @Override
        public int getItemCount() {
            return allEvents.size() + allPeople.size();
        }

        @Override
        public Filter getFilter() {
            return exampleFilter;
        }

        private Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Event> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(allEvents);
                }
                else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Event events : allEvents) {
                        if (events.getEventType().toString().contains(constraint.toString().toLowerCase())) {
                            filteredList.add(events);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                allEvents.clear();
                allEvents.addAll((Collection<? extends Event>) results.values);
                notifyDataSetChanged();
            }
        };
    }



    private class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView name;
        private final TextView location;
        private final TextView difficulty;

        private final int viewType;
        private Event event;
        private Person person;


        public RecyclerViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if (viewType == EVENT_TYPE) {
                name = itemView.findViewById(R.id.eventTitle);
                location = null;
                difficulty = null;

            }
            else {
                name = itemView.findViewById(R.id.familyTitle);
                location = null;
                difficulty = null;

            }
        }

        private void bind(Event event) {
            this.event = event;
            String personName = null;
            for (Person person : globalPerson) {
                if (person.getPersonID().equals(event.getPersonID())) {
                    personName = person.getFirstName() + " " + person.getLastName();
                }
            }
            String cityInfo = event.getEventType() + ": " +event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")"
                    + "\n" + personName;
            name.setText(cityInfo);

        }

        private void bind(Person person) {
            this.person = person;
            String fullName = person.getFirstName() + " " + person.getLastName();
            name.setText(fullName);

            Drawable myDrawable = getResources().getDrawable(R.drawable.girl_image);

            if (person.getGender().equals("f")) {
                name.setCompoundDrawablesWithIntrinsicBounds(myDrawable, null, null, null);
            }


        }

//        private void bind(List<Person> allPeople) {
//
//        }


        @Override
        public void onClick(View v) {

            if (viewType == EVENT_TYPE) {

                Intent intent = new Intent(SearchActivity.this, EventActivity.class);

                intent.putExtra("eventID", event.getEventID());
                intent.putExtra("personID", event.getPersonID());

                startActivity(intent);
            }

            else {

                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);

                intent.putExtra("firstName", person.getFirstName());
                intent.putExtra("lastName", person.getLastName());
                intent.putExtra("gender", person.getGender());
                intent.putExtra("ID", person.getPersonID());

                startActivity(intent);
            }
        }
    }
}
