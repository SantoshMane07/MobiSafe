package com.santoshmane.mobisafe.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.santoshmane.mobisafe.ContactModel;
import com.santoshmane.mobisafe.DataBaseHelper;
import com.example.mobisafe.R;
import com.santoshmane.mobisafe.RecyclerContactAdapter;
import com.example.mobisafe.databinding.FragmentContactsListBinding;
import com.santoshmane.mobisafe.viewmodels.ContactsListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ContactsListFragment extends Fragment {

    Toolbar toolbar;
    ArrayList<ContactModel> arrContacts = new ArrayList<>();
    private ContactsListViewModel mViewModel;
    public FragmentContactsListBinding mbinding;
    RecyclerContactAdapter adapter;
    private FloatingActionButton openAddDialogBtn;

    public static ContactsListFragment newInstance() {
        return new ContactsListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        DataBaseHelper dataBaseHelper = DataBaseHelper.getDB(container.getContext());
        //
        //Binding ContactsListFragment
        mbinding = FragmentContactsListBinding.inflate(inflater, container, false);
        //getting floating action button
        openAddDialogBtn = mbinding.addContactsFloatingBtn;
        //Setting Recyclerview
        RecyclerView recycler_contact = mbinding.recyclerContact;
        recycler_contact.setLayoutManager(new LinearLayoutManager(getContext()));

        //ToolBar
        toolbar = mbinding.contactListToolBar;
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Emergency Contact List");

        //Array of Contacts getting from DB and setting it to recview
        arrContacts = (ArrayList<ContactModel>)dataBaseHelper.contactsDao().getAllContacts();

        adapter = new RecyclerContactAdapter(getContext(), arrContacts,dataBaseHelper);
        recycler_contact.setAdapter(adapter);
        //

        //On Floating Button Clicked
        openAddDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(ContactsListFragment.this.getContext());
                dialog.setContentView(R.layout.add_contacts_dialog);
                //
                EditText edtName = dialog.findViewById(R.id.newName_edt);
                EditText edtPhone = dialog.findViewById(R.id.newNumber_edt);
                Button addBtn = dialog.findViewById(R.id.addContact_btn);
                //

                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = "", phone = "";
                        if (!edtName.getText().toString().equals("") && !edtPhone.getText().toString().equals("")) {
                            //Adding new Details to Database
                            name = edtName.getText().toString();
                            phone = edtPhone.getText().toString();
                            //
                            dataBaseHelper.contactsDao().addContact(new ContactModel(name,phone));
                            ArrayList<ContactModel> newContactarr = (ArrayList<ContactModel>)dataBaseHelper.contactsDao().getAllContacts();
                            adapter.contactListChanged(newContactarr);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "Please Enter all fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
        //
        return mbinding.getRoot();
    }
}