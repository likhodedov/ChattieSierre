package application.xxmp.chattiesierre;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.lihodedov on 05.05.2017.
 */

public class ContactModel {
    private static ContactModel ContactModel;
    private List<Contact> mContacts;

    public ContactModel(Context context) {
        mContacts = new ArrayList<>();
    }

    public static ContactModel get(Context context)
    {
        if(ContactModel == null)
        {
            ContactModel = new ContactModel(context);
        }
        return  ContactModel;
    }

    public void AddModelToList(Contact contact){
        mContacts.add(contact);
    }
    public List<Contact> getContacts()
    {
        return mContacts;
    }

    public void RemoveFromList (int id){
        mContacts.remove(id);
    }

}
