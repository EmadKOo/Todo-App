package emad.todo;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import emad.todo.Adapters.HomeAdapter;
import emad.todo.DB.DBHelper;
import emad.todo.Model.Item;

public class MainActivity extends AppCompatActivity {

    public TextView noNotes;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    Dialog dialog;
    HomeAdapter homeAdapter;
    ArrayList<Item> items = new ArrayList<>();
    DBHelper helper ;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new Dialog(MainActivity.this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);

        noNotes = findViewById(R.id.noNotes);
        helper = new DBHelper(getApplicationContext());
        // get data from sqlite or firebase
        setUpViews();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("NoItems"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        items = helper.getAllItems();
        if (items.size()==0)
            noNotes.setVisibility(View.VISIBLE);
        else
            noNotes.setVisibility(View.GONE);
        homeAdapter.notifyDataSetChanged();
    }

    public void setUpViews(){
       items = helper.getAllItems();
        homeAdapter = new HomeAdapter(items,getApplicationContext(),dialog);
        floatingActionButton = findViewById(R.id.addItem);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddItem.class));
            }
        });
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(homeAdapter);
    }
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                noNotes.setVisibility(View.VISIBLE);
//                recyclerView.setVisibility(View.GONE);
        }
    };
}