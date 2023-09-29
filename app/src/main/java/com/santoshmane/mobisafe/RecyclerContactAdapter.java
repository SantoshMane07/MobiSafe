package com.santoshmane.mobisafe;

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

import com.example.mobisafe.R;

import java.util.ArrayList;

public class RecyclerContactAdapter extends RecyclerView.Adapter<RecyclerContactAdapter.ViewHolder> {
    Context context;
    ArrayList<ContactModel> arrContacts;
    DataBaseHelper dataBaseHelper;
    public RecyclerContactAdapter(Context context, ArrayList<ContactModel> arrContacts,DataBaseHelper dataBaseHelper){
        this.context = context;
        this.arrContacts = arrContacts;
        this.dataBaseHelper = dataBaseHelper;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_row,parent,false);
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        holder.txtname.setText(arrContacts.get(holder.getAdapterPosition()).getUserName());
        holder.txtphone.setText(arrContacts.get(holder.getAdapterPosition()).getUserPhone());
        //Update the Contact
        holder.contacts_llrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_contacts_dialog);
                //
                TextView title = dialog.findViewById(R.id.dialog_title_tv);
                EditText edtName = dialog.findViewById(R.id.newName_edt);
                EditText edtPhone = dialog.findViewById(R.id.newNumber_edt);
                Button updateBtn = dialog.findViewById(R.id.addContact_btn);
                updateBtn.setText("Update Contact");
                title.setText("Update Contact");
                edtName.setText(arrContacts.get(holder.getAdapterPosition()).getUserName());
                edtPhone.setText(arrContacts.get(holder.getAdapterPosition()).getUserPhone());
                //
                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = "", phone = "";
                        Integer oldId=10;
                        if (!edtName.getText().toString().equals("") && !edtPhone.getText().toString().equals("")) {
                            //Updating Details to Database
                            name = edtName.getText().toString();
                            phone = edtPhone.getText().toString();
                            oldId = arrContacts.get(holder.getAdapterPosition()).getId();
                            //
                            Log.d("DD", "onClick: "+name+" "+phone+" "+oldId);
                            dataBaseHelper.contactsDao().updateContact(name,phone,oldId);
                            arrContacts.set(holder.getAdapterPosition(),new ContactModel(oldId,name,phone));
                            notifyItemChanged(holder.getAdapterPosition());
                            //
                            dialog.dismiss();

                        } else {
                            Toast.makeText(context, "Please Enter all fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
        // Deleting Contact from DataBase
        holder.contacts_llrow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Delete Contact")
                        .setMessage("Are you sure you want to delete")
                        .setIcon(R.drawable.baseline_delete_24)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dataBaseHelper.contactsDao().deleteContact(arrContacts.get(holder.getAdapterPosition()).getId());
                                arrContacts.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.show();
                return true;
            }
        });
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
    public void contactListChanged(ArrayList<ContactModel> newContactarr){
        arrContacts.clear();
        arrContacts.addAll(newContactarr);
        notifyDataSetChanged();

    }
}
