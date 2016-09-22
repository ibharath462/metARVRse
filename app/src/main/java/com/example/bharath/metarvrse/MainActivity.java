package com.example.bharath.metarvrse;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    ArrayList<Property> books;
    static Context c=null;
    static ArrayAdapter<Property> adapter=null;
    static SweetAlertDialog pDialog=null;
    static data data;
    ListView listView=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, addBooks.class);
                startActivity(i);
                finish();
            }
        });


        c=MainActivity.this;


        books = new ArrayList<>();

        data=new data(this);
        ArrayList<ArrayList<String>> iterator=data.get();
        if(iterator.size()==0){
            Toast.makeText(getApplicationContext(),"Make new entries buddy!",Toast.LENGTH_SHORT).show();
        }
        else {
            for (ArrayList<String> t : iterator) {
                books.add(new Property(t.get(0), t.get(1), t.get(2), t.get(3)));
                //Log.d("Get", "Name:" + t[0] + " Author:" + t[1] + " ISBN:" + t[2]);
            }

            adapter = new propertyArrayAdapter(this, 0, books);
            listView = (ListView) findViewById(R.id.customListView);

            listView.setAdapter(adapter);


        }
    }

    public static void delete(final int pos){


        pDialog = new SweetAlertDialog(c, SweetAlertDialog.WARNING_TYPE);
        pDialog.setTitleText("Are you sure?");
        pDialog.setContentText("Won't be able to recover this file!");
        pDialog.setConfirmText("Yes,delete it!");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                //Toast.makeText(getApplicationContext(), "Delete " + pos, Toast.LENGTH_SHORT).show();

                Property p = adapter.getItem(pos);
                data.delete(p.getIsbn());
                adapter.remove(adapter.getItem(pos));
                adapter.notifyDataSetChanged();
                ArrayList<ArrayList<String>> iterator=data.get();
                if(iterator.size()==0){
                    Toast.makeText(c,"Make new entries buddy!",Toast.LENGTH_SHORT).show();
                }
                sDialog
                        .setTitleText("Deleted!")
                        .setContentText("Your book has been deleted!")
                        .setConfirmText("OK")
                        .setConfirmClickListener(null)
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
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






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
