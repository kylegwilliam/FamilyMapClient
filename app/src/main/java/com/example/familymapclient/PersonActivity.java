package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import model.Event;
import model.Person;

public class PersonActivity extends AppCompatActivity {

    private TextView firstNameField;
    private TextView lastNameField;
    private TextView genderField;

    public String UsersName;

    public List<String> titleList;
    public List<Person> peopleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);


        String firstName = getIntent().getExtras().getString("firstName");
        String lastName = getIntent().getExtras().getString("lastName");
        String gender = getIntent().getExtras().getString("gender");
        String personID = getIntent().getExtras().getString("ID");

        UsersName = firstName + " " + lastName;

        firstNameField = (TextView) findViewById(R.id.firstNameField);
        lastNameField = (TextView) findViewById(R.id.lastNameField);
        genderField = (TextView) findViewById(R.id.genderField);

        String genderString;

        if (gender.equals("m")) {
            genderString = "Male";
        } else {
            genderString = "Female";
        }

        firstNameField.setText(firstName);
        lastNameField.setText(lastName);
        genderField.setText(genderString);

        ExpandableListView expandableListView = findViewById(R.id.expandableListView);

        DataCache dataCache;
        dataCache = DataCache.getInstance();

        List<Event> eventsAssociatedToUser = dataCache.getPersonsEvent(personID);
        List<List> connectedFamilyMembers = dataCache.getFamilyMembers(personID);

        titleList = connectedFamilyMembers.get(0);
        peopleName = connectedFamilyMembers.get(1);

        //should pass in a person object. Better choice.

        expandableListView.setAdapter(new ExpandableListAdapter(eventsAssociatedToUser, titleList, peopleName));


    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int Events_Position = 0;
        private static final int Family_Position = 1;

        private final List<Event> eventsAssociatedToUser;
        private final List<String> titleList;
        private final List<Person> peopleName;

        ExpandableListAdapter(List<Event> eventsAssociatedToUser, List<String> titleList, List<Person> peopleName) {
            this.eventsAssociatedToUser = eventsAssociatedToUser;
            this.titleList = titleList;
            this.peopleName = peopleName;
            
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case Events_Position:
                    return eventsAssociatedToUser.size();
                case Family_Position:
                    return peopleName.size();
                default:
                    throw new IllegalArgumentException("Unrecognized groupPosition");
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case Events_Position:
                    return getString(R.string.lifeEvents);
                case Family_Position:
                    return getString(R.string.familyEvents);
                default:
                    throw new IllegalArgumentException("Unrecognized groupPosition");
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case Events_Position:
                    return eventsAssociatedToUser.get(childPosition);
                case Family_Position:
                    return peopleName.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized groupPosition");
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
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case Events_Position:
                    titleView.setText(R.string.lifeEventsTitle);
                    break;
                case Family_Position:
                    titleView.setText(R.string.familyMembersTitle);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);

            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            View itemView;

            switch(groupPosition) {
                case Events_Position:
                    itemView = getLayoutInflater().inflate(R.layout.life_events, parent, false);
                    initializeLifeEventsView(itemView, childPosition);
                    break;
                case Family_Position:
                    itemView = getLayoutInflater().inflate(R.layout.family_members, parent, false);
                    initializePersonFamilyView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
            return itemView;
        }

        private void initializeLifeEventsView(View LifeEventView, final int childPosition) {
            TextView EventLocation = LifeEventView.findViewById(R.id.EventLocation);
            String eventLocation = eventsAssociatedToUser.get(childPosition).getEventType() + ": " +
                    eventsAssociatedToUser.get(childPosition).getCity() + ", " + eventsAssociatedToUser.get(childPosition).getCountry()
                    + " (" + eventsAssociatedToUser.get(childPosition).getYear() + ")";


            EventLocation.setText(eventLocation);


            String personID = eventsAssociatedToUser.get(childPosition).getPersonID();

            TextView EventPerson = LifeEventView.findViewById(R.id.EventPerson);
            //EventPerson.setText(eventsAssociatedToUser.get(childPosition).getAssociatedUsername());
            EventPerson.setText(UsersName);



            //EventPerson.setCompoundDrawables(null, null, null, null);
            //Id want to replace the left with the drawable object.

            LifeEventView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Toast.makeText(PersonActivity.this, "It clicked!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);

                    intent.putExtra("eventID", eventsAssociatedToUser.get(childPosition).getEventID());
                    intent.putExtra("personID", eventsAssociatedToUser.get(childPosition).getPersonID());

                    startActivity(intent);

                }
            });
        }

        private void initializePersonFamilyView(View LifeEventView, final int childPosition) {

            TextView PersonName = LifeEventView.findViewById(R.id.individualsName);
            String personsName = peopleName.get(childPosition).getFirstName() + " " + peopleName.get(childPosition).getLastName();
            PersonName.setText(personsName);

            TextView EventPerson = LifeEventView.findViewById(R.id.individualsTitle);
            EventPerson.setText(titleList.get(childPosition));


            Drawable myDrawable = getResources().getDrawable(R.drawable.girl_image);

            if (peopleName.get(childPosition).getGender().equals("f")) {
                EventPerson.setCompoundDrawablesWithIntrinsicBounds(myDrawable, null, null, null);
            }

            else {
                Drawable boyDrawable = getResources().getDrawable(R.drawable.boy_image);
                EventPerson.setCompoundDrawablesWithIntrinsicBounds(boyDrawable, null, null, null);
            }

            //EventPerson.setCompoundDrawables(null, null, null, null);
            //Id want to replace the left with the drawable object.

            LifeEventView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Toast.makeText(PersonActivity.this, "It clicked!", Toast.LENGTH_LONG).show();

                    //Intent refresh = new Intent(v.getContext(), PersonActivity.class);
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);

                    intent.putExtra("firstName", peopleName.get(childPosition).getFirstName());
                    intent.putExtra("lastName", peopleName.get(childPosition).getLastName());
                    intent.putExtra("gender", peopleName.get(childPosition).getGender());
                    intent.putExtra("ID", peopleName.get(childPosition).getPersonID());

                    startActivity(intent);


                }

            });

        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    private class EventLocation {

        private final String EventLocation;
        private final String EventPerson;

        private EventLocation(String eventLocation, String eventPerson) {
            this.EventLocation = eventLocation;
            this.EventPerson = eventPerson;
        }

        //public String getName() {
        //    return
        //}
    }




}