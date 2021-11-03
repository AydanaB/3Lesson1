package com.example.a3lesson1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnShowContacts;
    public static final int PermissionRequestContact = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnShowContacts = findViewById(R.id.btn_show_contacts);

        btnShowContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnShowContacts.setVisibility(View.INVISIBLE);
                askForContactPermission();
            }
        });
    }


    ArrayList<Contacts> contactList = new ArrayList<>();

    private static final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    private void getContacts() {
        ContentResolver cr = getContentResolver();

        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor != null) {
            HashSet<String> mobileNoSet = new HashSet<String>();
            try {
                final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                String name, number;
                while (cursor.moveToNext()) {
                    name = cursor.getString(nameIndex);
                    number = cursor.getString(numberIndex);
                    number = number.replace(" ", "");
                    if (!mobileNoSet.contains(number)) {
                        contactList.add(new Contacts(name, number));
                        mobileNoSet.add(number);
                        Log.d("hvy", "Name: " + name
                                + " number: " + number);
                    }
                }
            } finally {
                cursor.close();
            }
        }
    }


    public void askForContactPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please confirm Contacts access");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                                    PermissionRequestContact);
                        }
                    });
                    builder.show();
                }else {
                    ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.READ_CONTACTS}, PermissionRequestContact);
                }

            }else {
                getContacts();
            }
        }else {
            getContacts();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts();
                    Toast.makeText(getApplicationContext(), "Permission is done", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "No permission for contacts", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}