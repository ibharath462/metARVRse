package com.example.bharath.metarvrse;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class addBooks extends AppCompatActivity {


    EditText name,author,isbn;
    private final int RESULT_CROP = 400;
    com.hanks.htextview.HTextView text;
    Button image,submit;
    LinearLayout ll;
    int i=0;
    long s=0;
    Handler mHandler = null;
    data data=null;
    String imageURl=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name=(EditText)findViewById(R.id.name);
        author=(EditText)findViewById(R.id.author);
        isbn=(EditText)findViewById(R.id.isbn);

        image=(Button)findViewById(R.id.im);
        submit=(Button)findViewById(R.id.submit);
        mHandler = new Handler();
        ll=(LinearLayout)findViewById(R.id.ll);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 1);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(addBooks.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        data=new data(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name!=null && name.getText().toString().isEmpty() || author!=null && author.getText().toString().isEmpty() || isbn!=null && isbn.getText().toString().isEmpty()|| imageURl!=null && imageURl.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fillup all the details buddy!",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(isbn.getText().toString().length()<3){
                        Toast.makeText(getApplicationContext(),"Invalid ISBN buddy!",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        boolean flag = data.searchDuplicate(isbn.getText().toString());
                        if (flag == true) {
                            error();
                        } else {
                            show();
                        }
                    }
                }
            }
        });

    }


    public void show(){
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
        pDialog.setTitleText("Confrim "+name.getText().toString()+"?");
        pDialog.setContentText("Ok?");
        pDialog.setConfirmText("Add it!");
        pDialog.setCustomImage(Drawable.createFromPath(imageURl));
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog
                        .setTitleText("Added!")
                        .setContentText("Your book has been added!")
                        .setConfirmText("OK")
                        .setConfirmClickListener(null)
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                data.add(name.getText().toString(), author.getText().toString(), isbn.getText().toString(), imageURl);
                Toast.makeText(getApplicationContext(), "Added successfully..", Toast.LENGTH_SHORT).show();
                name.setText("");
                imageURl=null;
                author.setText("");
                isbn.setText("");
            }
        });
        pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.cancel();
            }
        });
        pDialog.show();

    }

    public void error(){
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
        pDialog.setTitleText("Confrim "+name.getText().toString()+"?");
        pDialog.setContentText("Ok?");
        pDialog.setConfirmText("Add it!");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog
                        .setTitleText("Error")
                        .setContentText("Duplicate entry found!")
                        .setConfirmText("OK")
                        .setConfirmClickListener(null)
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                Toast.makeText(getApplicationContext(), "Re-enter ISBN", Toast.LENGTH_SHORT).show();
                isbn.setText("");
            }
        });
        pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.cancel();
            }
        });
        pDialog.show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imageURl = cursor.getString(columnIndex);
            File file=new File(imageURl);
            Bitmap bmp= BitmapFactory.decodeFile(imageURl);
            //threshold size is fixed to 2MB
            s=file.length()/1048576;
            //Change to the required threshold size..
            if(s>=2 || bmp.getWidth()>800 || bmp.getHeight()>600){
                Toast.makeText(getApplicationContext(),"Image size is too high, please choose another low resolution image.",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 1);
                imageURl=null;
            }
            else{
                imageURl = cursor.getString(columnIndex);
            }
            cursor.close();

        }



    }

}
