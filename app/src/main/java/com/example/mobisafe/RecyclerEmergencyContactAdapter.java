package com.example.mobisafe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerEmergencyContactAdapter extends RecyclerView.Adapter<RecyclerEmergencyContactAdapter.ViewHolder>{
    ArrayList<ContactModel> arrContacts;
    Context context;
    public RecyclerEmergencyContactAdapter(ArrayList<ContactModel> arrContacts,Context context){
        this.arrContacts = arrContacts;
        this.context = context;
    }
    //
    @NonNull
    @Override
    public RecyclerEmergencyContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_row,parent,false);
        RecyclerEmergencyContactAdapter.ViewHolder viewholder = new RecyclerEmergencyContactAdapter.ViewHolder(view);
        return viewholder;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerEmergencyContactAdapter.ViewHolder holder, int position) {
        holder.txtname.setText(arrContacts.get(holder.getAdapterPosition()).getUserName());
        holder.txtphone.setText(arrContacts.get(holder.getAdapterPosition()).getUserPhone());
    }

    @Override
    public int getItemCount() {
        return arrContacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtname,txtphone;
        LinearLayoutCompat contacts_llrow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.contact_user_name);
            txtphone = itemView.findViewById(R.id.contact_user_number);
            contacts_llrow = itemView.findViewById(R.id.contacts_llrow);
        }
    }
}
