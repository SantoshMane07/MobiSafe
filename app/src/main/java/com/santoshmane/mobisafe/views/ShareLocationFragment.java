package com.santoshmane.mobisafe.views;

import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.santoshmane.mobisafe.ContactModel;
import com.example.mobisafe.R;
import com.example.mobisafe.databinding.FragmentShareLocationBinding;
import com.santoshmane.mobisafe.viewmodels.ShareLocationViewModel;

import java.util.ArrayList;

public class ShareLocationFragment extends Fragment {

    private ShareLocationViewModel mViewModel;
    public FragmentShareLocationBinding mbinding;
    CardView ShareLocationtoAll_btn, ShareEmergencyMssgtoSpecific_btn, ShareCustomeMssgtoSpecific_btn;
    Button EditEmergMssg_btn;
    TextView EmergMssg_txtview;

    public static ShareLocationFragment newInstance() {
        return new ShareLocationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //
        mViewModel = new ShareLocationViewModel();
        //
        View rootView = inflater.inflate(R.layout.fragment_share_location, container, false);
        //Binding ContactsListFragment
        mbinding = FragmentShareLocationBinding.bind(rootView);
        //Getting all views from binding
        ShareLocationtoAll_btn = mbinding.ShareLocationtoAllBtn;
        ShareEmergencyMssgtoSpecific_btn = mbinding.ShareEmergencyMssgtoSpecificBtn;
        ShareCustomeMssgtoSpecific_btn = mbinding.ShareCustomeMssgtoSpecificBtn;
        EditEmergMssg_btn = mbinding.EditEmergMssgBtn;
        EmergMssg_txtview = mbinding.EmergMssgTxtview;
        //
        //Getting Emergency Mssg default
        String defaultEmergMssg = "";
        SharedPreferences pref = getContext().getSharedPreferences("emergMssg", Context.MODE_PRIVATE);
        defaultEmergMssg = pref.getString("mssg", "Iam in Trouble").trim();
        EmergMssg_txtview.setText(defaultEmergMssg);
        //
        //Setting on Click listener to EditEmergMssg_btn
        EditEmergMssg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Opening Dialog to edit mssg
                Dialog dialog = new Dialog(ShareLocationFragment.this.getContext());
                dialog.setContentView(R.layout.edit_emerg_mssg_dialog);
                EditText emg_mssg_edt = dialog.findViewById(R.id.emg_mssg_edt);
                Button emgeditMssg_btn = dialog.findViewById(R.id.emgeditMssg_btn);
                emg_mssg_edt.setText(EmergMssg_txtview.getText().toString());
                //On Edit button Click
                emgeditMssg_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String emergMssg = emg_mssg_edt.getText().toString().trim();
                        EmergMssg_txtview.setText(emergMssg);
                        //Storing Emergency mssg to shared Preference
                        SharedPreferences pref2 = getContext().getSharedPreferences("emergMssg", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref2.edit();
                        editor.putString("mssg", emergMssg);
                        editor.apply();
                        dialog.dismiss();
                    }
                });
                dialog.show();
                //
            }
        });

        //Setting on Click listener to ShareEmergencyMssgtoSpecific_btn
        ShareEmergencyMssgtoSpecific_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //.. Opening Dialog to add
                Dialog dialog = new Dialog(ShareLocationFragment.this.getContext());
                dialog.setContentView(R.layout.add_contacts_dialog);
                //
                EditText edtMssg = dialog.findViewById(R.id.newName_edt);
                EditText edtPhone = dialog.findViewById(R.id.newNumber_edt);
                Button sendMssgBtn = dialog.findViewById(R.id.addContact_btn);
                // Changing name of Dialog Views
                TextView title = dialog.findViewById(R.id.dialog_title_tv);
                title.setText("Send Emergency Message to Any Contact");
                edtMssg.setVisibility(View.GONE);
                sendMssgBtn.setText("Send Emergency Message and Location");
                //
                sendMssgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String emgMssg = "", phone = "";
                        if (!edtPhone.getText().toString().equals("")) {
                            //Sending Emergency mssg... to contact
                            emgMssg = EmergMssg_txtview.getText().toString();
                            phone = edtPhone.getText().toString();
                            //
                            boolean result = mViewModel.shareOnlyEmergMssgAndLocationToSpecificContact(phone, emgMssg, ShareLocationFragment.this.getContext());
                            if (result) {
                                Toast.makeText(getContext(), "Emergency Message and Location Shared Successfully to " + phone, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Failed to Send check your Internet Connection and Location Permission and SMS permission is given or not ", Toast.LENGTH_SHORT).show();
                            }
                            //
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "Please Enter all fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
                //..
            }
        });
        //

        //Setting on Click listener to ShareCustomeMssgtoSpecific_btn
        ShareCustomeMssgtoSpecific_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //.. Opening Dialog to add
                Dialog dialog = new Dialog(ShareLocationFragment.this.getContext());
                dialog.setContentView(R.layout.add_contacts_dialog);
                //
                EditText edtMssg = dialog.findViewById(R.id.newName_edt);
                EditText edtPhone = dialog.findViewById(R.id.newNumber_edt);
                Button sendMssgBtn = dialog.findViewById(R.id.addContact_btn);
                // Changing name of Dialog Views
                TextView title = dialog.findViewById(R.id.dialog_title_tv);
                title.setText("Send Custom Message to Any Contact");
                edtMssg.setHint("Enter Custom Message");
                sendMssgBtn.setText("Send Message and Location");
                //

                sendMssgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mssg = "", phone = "";
                        if (!edtMssg.getText().toString().equals("") && !edtPhone.getText().toString().equals("")) {
                            //Sending mssg... to contact
                            mssg = edtMssg.getText().toString();
                            phone = edtPhone.getText().toString();
                            //
                            boolean result = mViewModel.shareCustomMssgAndLocationToSpecificContact(phone, mssg, ShareLocationFragment.this.getContext());
                            if (result) {
                                Toast.makeText(getContext(), "Emergency Message and Location Shared Successfully to " + phone, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Failed to Send check your Internet Connection and Location Permission and SMS permission is given or not ", Toast.LENGTH_SHORT).show();
                            }
                            //
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "Please Enter all fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
                //..
            }
        });
        //

        //Setting on Click listener to ShareLocationtoAll_btn
        ShareLocationtoAll_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ContactModel> arrEmergContacts = mViewModel.getAllEmergContacts(getContext());
                if (!arrEmergContacts.isEmpty()) {
                    boolean isSend = mViewModel.shareEmergLocationAndMessageToAllContacts(arrEmergContacts, EmergMssg_txtview.getText().toString(), getContext());
                    if (isSend) {
                        Toast.makeText(getContext(), "Emergency Message and Location Shared Successfully to All contacts in list", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to Send check your Internet Connection and Location Permission and SMS permission is given or not ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "No Contacts in Emergency Contact List", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //

        return rootView;
    }
}