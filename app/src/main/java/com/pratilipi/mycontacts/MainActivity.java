package com.pratilipi.mycontacts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView listView ;
    ArrayList<String> StoreContacts ;
    ArrayList<String> phone;
    ArrayAdapter<String> arrayAdapter ;
    SwipeRefreshLayout swipeRefreshLayout;
    Cursor cursor ;
    String name, phonenumber,image,email ;
    Map<String, String> map_phone,map_image,map_email;
    public  static final int RequestPermissionCode  = 1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listview1);
        swipeRefreshLayout=findViewById(R.id.refresh);
        StoreContacts = new ArrayList<String>();
        phone=new ArrayList<String>();
        map_phone =new HashMap<String, String>();
        map_image =new HashMap<String, String>();
        map_email =new HashMap<String, String>();
        EnableRuntimePermission();
        GetContactsIntoArrayList();

        arrayAdapter = new ArrayAdapter<String>(
                MainActivity.this,
                R.layout.list_row,
                R.id.name, StoreContacts
        );
        Collections.sort(StoreContacts);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                StoreContacts.clear();
                GetContactsIntoArrayList();
                arrayAdapter = new ArrayAdapter<String>(
                        MainActivity.this,
                        R.layout.list_row,
                        R.id.name, StoreContacts
                );
                Collections.sort(StoreContacts);
                listView.setAdapter(arrayAdapter);
                swipeRefreshLayout.setRefreshing(false);
                arrayAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Contacts Refreshed", Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(getApplicationContext(),Contact.class);
                i.putExtra("name",StoreContacts.get(position));
                i.putExtra("phone", map_phone.get(StoreContacts.get(position)));
                i.putExtra("image",map_image.get(StoreContacts.get(position)));
                i.putExtra("email",map_email.get(StoreContacts.get(position)));
                startActivity(i);
            }
        });
    }

    public void GetContactsIntoArrayList(){


        cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,null, null, null);
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                image = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                Cursor pCur = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
                while (pCur.moveToNext()) {
                    phonenumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                pCur.close();

                Cursor emailCur = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[] { id }, null);
                if(emailCur.getCount()>0) {
                    while (emailCur.moveToNext()) {
                        email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }
                }else{
                    email="---";
                }
                emailCur.close();
                StoreContacts.add(name);
                map_phone.put(name, phonenumber);
                map_image.put(name, image);
                map_email.put(name, email);
            }

            cursor.close();


        }

    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                MainActivity.this,
                Manifest.permission.READ_CONTACTS))
        {

            Toast.makeText(MainActivity.this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MainActivity.this,"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(MainActivity.this,"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


}
