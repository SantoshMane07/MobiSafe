package com.santoshmane.mobisafe;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactsDao {

    @Query("Select * from contacts")
    List<ContactModel> getAllContacts();

    @Insert
    void addContact(ContactModel contactModel);

    @Query("Update contacts set userName =:newName , userPhone =:newPhone where id=:id")
    void updateContact(String newName,String newPhone, Integer id);


    @Query("Delete from contacts where id=:id")
    void deleteContact(Integer id);

}
