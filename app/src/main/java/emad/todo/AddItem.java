package emad.todo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import emad.todo.DB.DBHelper;
import emad.todo.Model.Item;

public class AddItem extends AppCompatActivity {

    ImageView img;
    TextView addTitle;
    TextView addDescription;
    TextView cancel;
    ImageButton pickImage;
    ImageButton btnSave;

    DBHelper helper;
    String imagePath;

    int REQUEST_GALLERY = 1000;
    private static final String TAG = "AddItem";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() == null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Note");
        helper = new DBHelper(getApplicationContext());
        setUpViews();

        try {
            InputStream ims = getAssets().open("placeholder.png");
            Drawable d = Drawable.createFromStream(ims, null);
            Log.d(TAG, "onCreate: ims "+ ims);
            Log.d(TAG, "onCreate: d "+ d);
//            test.setImageDrawable(d);
        }
        catch(IOException ex) {
            return;
        }
    }

    public void setUpViews(){
        img = findViewById(R.id.img);
        addTitle = findViewById(R.id.addTitle);
        addDescription = findViewById(R.id.addDescription);
        cancel = findViewById(R.id.cancel);
        pickImage = findViewById(R.id.pickImage);
        btnSave = findViewById(R.id.btnSave);
    }

    public void cancel(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public void pickImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_GALLERY);
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
    public void Save(View view) {

        if (TextUtils.isEmpty(addTitle.getText().toString())||TextUtils.isEmpty(addDescription.getText().toString())){
            Toast.makeText(this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
        }else {
            boolean result = helper.addItem(addTitle.getText().toString(), addDescription.getText().toString(),getDate(), imagePath);
            if (result == false) {
                Toast.makeText(this, "Sorry there are a problem , Try Later", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }else {
                Toast.makeText(this, "Note Saved Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }
    }

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
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                imagePath = picturePath;
                try{
                    File imgFile = new  File(imagePath);
                    if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        img.setImageBitmap(myBitmap);
                    }
                }catch (Exception ex){
                    Log.d(TAG, "onCreate: " + ex.getMessage());
                }

            }
        }
    }
}