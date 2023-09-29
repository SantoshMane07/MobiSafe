package com.santoshmane.mobisafe.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.santoshmane.mobisafe.ContactModel;
import com.example.mobisafe.R;
import com.santoshmane.mobisafe.RecyclerEmergencyContactAdapter;
import com.example.mobisafe.databinding.FragmentEmergencyContactsFragmentsBinding;
import com.santoshmane.mobisafe.viewmodels.EmergencyContactsFragmentsViewModel;

import java.util.ArrayList;

public class EmergencyContactsFragments extends Fragment {

    private EmergencyContactsFragmentsViewModel mViewModel;
    ArrayList<ContactModel> arrEmergContacts = new ArrayList<>();
    RecyclerEmergencyContactAdapter adapter;
    public FragmentEmergencyContactsFragmentsBinding mbinding;
    Toolbar toolbar;
    //

    public static EmergencyContactsFragments newInstance() {
        return new EmergencyContactsFragments();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_emergency_contacts_fragments, container, false);
        //Binding ContactsListFragment
        mbinding = FragmentEmergencyContactsFragmentsBinding.bind(rootView);
        //Setting Recyclerview
        RecyclerView listView = mbinding.recyclerEmergContact;
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        //
        //ToolBar
        toolbar = mbinding.emergContactListToolBar;
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Maharashtra Emergency Contacts");
        // Creating Array of Emergency contacts
        arrEmergContacts.add(new ContactModel(1,"Emergency and Disaster Manegement","108 / 1077"));
        arrEmergContacts.add(new ContactModel(2,"Police","100"));
        arrEmergContacts.add(new ContactModel(3,"Fire Service","101"));
        arrEmergContacts.add(new ContactModel(4,"Ambulance","102"));
        arrEmergContacts.add(new ContactModel(5,"Railway Accident","1072"));
        arrEmergContacts.add(new ContactModel(6,"Medical Advice Service, Govt. of Maharashtra","104"));
        arrEmergContacts.add(new ContactModel(7,"Women Crisis Response Center","1091"));
        arrEmergContacts.add(new ContactModel(8,"Coastal Security","1093"));
        arrEmergContacts.add(new ContactModel(9,"Kisan Call Centre","1551"));
        arrEmergContacts.add(new ContactModel(10,"Maritime SAR (Search and Rescue operation) Emergency services to Indian coast guards","1554"));
        //
        adapter = new RecyclerEmergencyContactAdapter(arrEmergContacts,getContext());
        listView.setAdapter(adapter);
        //
        return rootView;
    }
}