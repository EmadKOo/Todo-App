package emad.todo;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

import emad.todo.DB.DBHelper;
import emad.todo.Model.Item;

public class ViewItem extends AppCompatActivity {

    ImageView imgViewItem;
    EditText viewTitle;
    EditText viewDescription;
    FloatingActionButton editButton;

    Dialog dialog;
    Item receivedItem;
    Item updatedItem = null;
    private static final String TAG = "ViewItem";
    DBHelper dbHelper;
    String picturePath = null;

    int REQUEST_GALLERY = 1000;
    int flagEdit = 0; // if 0 EditIdDisabled else edit is enabled
    int enableUpdate = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
   //     if (getSupportActionBar() == null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Edit Note");
        //}


        dbHelper = new DBHelper(getApplicationContext());
        receivedItem = (Item) getIntent().getSerializableExtra("item");
        setUpViews();
        try{
        File imgFile = new  File(receivedItem.getImagePath());
            Log.d(TAG, "onCreate: " + receivedItem.getImagePath());
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgViewItem.setImageBitmap(myBitmap);
      //      imgViewItem.setImageURI(Uri.parse("android.content.res.AssetManager$AssetInputStream@beada4"));
        }
        }catch (Exception ex){
            Log.d(TAG, "onCreate: " + ex.getMessage());
        }
    }

    public void setUpViews(){
        imgViewItem = findViewById(R.id.imgViewItem);
        viewTitle = findViewById(R.id.viewTitle);
        viewDescription = findViewById(R.id.viewDescription);
        editButton = findViewById(R.id.editButton);
        viewTitle.setText(receivedItem.getTitle());
        viewDescription.setText(receivedItem.getDescription());
    }

    public void cancel(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public void pickImag(View view) {
       if (viewTitle.isEnabled()){

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_GALLERY);
       }else {
           Toast.makeText(this, "Enable Edit Mode First", Toast.LENGTH_SHORT).show();
       }
    }

    private String getDate(){
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        String myDate = date.toString();
        //Mon Mar 04 16:41:58 GMT+02:00 2019
        String day = myDate.substring(8,10) +" "+ myDate.substring(4,7)+" "+ myDate.substring(30,34);
        String time = myDate.substring(11, 16);
        return day+ ","+time;
    }

    public void updateItem(View view) {
        if (picturePath == null){
            picturePath = receivedItem.getImagePath();
        }
        updatedItem = new Item(receivedItem.getId(),receivedItem.getFirebaseID(),viewTitle.getText().toString(),viewDescription.getText().toString(),getDate(),"updated",picturePath);

        if (enableUpdate ==0){
        dialog = new Dialog(ViewItem.this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.update_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);

        dialog.show();

        LinearLayout updateIcon = dialog.findViewById(R.id.updateYesIcon);
        LinearLayout cancelUpdate = dialog.findViewById(R.id.updateNoIcon);

        updateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.updateItem(updatedItem);
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        cancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.updateExitIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        }else {
            Snackbar.make(view, "Note has No Changes", Snackbar.LENGTH_LONG)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                    .show();
        }
    }

    public void deleteItem(View view) {

        dialog = new Dialog(ViewItem.this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);

        dialog.show();

        LinearLayout deleteIcon = dialog.findViewById(R.id.deleteYesIcon);
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.removeItem(receivedItem);
                dbHelper.addToRemovedItems(receivedItem);
                Toast.makeText(ViewItem.this, "Note Removed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ViewItem.this,MainActivity.class));
            }
        });

        dialog.findViewById(R.id.deleteNoIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.findViewById(R.id.deleteExitIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        finish();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent1;
        //noinspection SimplifiableIfStatement
        if (item.getItemId() == android.R.id.home){
            intent1 = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent1);
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY) {
             Uri selectedImage = data.getData();
                Log.d(TAG, "onActivityResult: " + selectedImage);
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                Log.d(TAG, "onActivityResult: "+ picturePath);
                File imgFile = new  File(picturePath);

                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imgViewItem.setImageBitmap(myBitmap);
                }
               }
        }
    }

    public void EnableEditMode(View view) {
        if (flagEdit == 0){

            flagEdit = 1;
            viewDescription.setEnabled(true);
            viewTitle.setEnabled(true);
            editButton.setImageResource(R.drawable.ok);
        }else if (flagEdit == 1){
            flagEdit = 0;
            viewDescription.setEnabled(false);
            viewTitle.setEnabled(false);
            editButton.setImageResource(R.drawable.edit);
            updateItem(view);
        }


    }
}