package com.pratilipi.mycontacts;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;


import de.hdodenhof.circleimageview.CircleImageView;

public class Contact extends AppCompatActivity {

    CircleImageView imageView;
    TextView name,phone,email;
    String n,p,e,img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        n=getIntent().getStringExtra("name");
        p=getIntent().getStringExtra("phone");
        e=getIntent().getStringExtra("email");
        img=getIntent().getStringExtra("image");

        this.setTitle(n);
        name=findViewById(R.id.name1);
        phone=findViewById(R.id.phone1);
        email=findViewById(R.id.email1);
        imageView=findViewById(R.id.profilePicture);

        if(img!=null){
            Uri image= Uri.parse(img);
            imageView.setImageURI(null);
            imageView.setImageURI(image);
        }

        name.setText(n);
        phone.setText(p);
        email.setText(e);

    }
    public boolean onOptionsItemSelected(MenuItem menu){
        onBackPressed();
        return true;
    }
}
