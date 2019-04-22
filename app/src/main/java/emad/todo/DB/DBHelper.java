package emad.todo.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import emad.todo.Model.Item;

public class DBHelper extends SQLiteOpenHelper {

    public static String DBNAME ="Items";
    public static int DBVERSION =1;
    Context context;

    public DBHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
        this.context =context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ItemsContract.CREATE_TABLE);
        sqLiteDatabase.execSQL(RemovedItemsContract.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(ItemsContract.REMOVE_TABLE);
        sqLiteDatabase.execSQL(RemovedItemsContract.REMOVE_TABLE);
    }

    public boolean addItem(String title, String description,String date,String imagePath){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemsContract.TITLE, title);
        contentValues.put(ItemsContract.DESCRIPTION, description);
        contentValues.put(ItemsContract.DATE_ADDED, date);
        contentValues.put(ItemsContract.ITEMSTATUS, "new");
        contentValues.put(ItemsContract.FIREBASEID, "");
        contentValues.put(ItemsContract.ITEMIMAGEPATH,imagePath);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long n = sqLiteDatabase.insert(ItemsContract.TABEL_NAME, null,contentValues);
        if (n==-1){
            return false;
        }else {
            return true;
        }
    }

    public int updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemsContract.TITLE, item.getTitle());
        contentValues.put(ItemsContract.DESCRIPTION, item.getDescription());
        contentValues.put(ItemsContract.DATE_ADDED, item.getDateAdded());
        contentValues.put(ItemsContract.ITEMSTATUS, "updated");
        contentValues.put(ItemsContract.ITEMIMAGEPATH, item.getImagePath());
        if (item.getFirebaseID() !="")
            contentValues.put(ItemsContract.FIREBASEID, item.getFirebaseID());
        else
            contentValues.put(ItemsContract.FIREBASEID, "");

        return db.update(ItemsContract.TABEL_NAME, contentValues,
                ItemsContract.ID + " = ?", /// & firebaseID
                new String[] { String.valueOf(item.getId()) });
    }

    public ArrayList<Item> removeItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ItemsContract.TABEL_NAME,ItemsContract.ID + " = ?",
                new String[] { String.valueOf(item.getId()) });
        ArrayList<Item> items = getAllItems();
        // add to Removed Items Table need when we sync
        if (item.getFirebaseID() != ""){
            addToRemovedItems(new Item(item.getId(),item.getFirebaseID(),item.getTitle(),item.getItemStatus()));
        }
        return items;
    }

    public boolean addToRemovedItems(Item item){
        ContentValues contentValues = new ContentValues();
        contentValues.put(RemovedItemsContract.TITLE, item.getTitle());
        contentValues.put(RemovedItemsContract.ITEMSTATUS, item.getItemStatus());
        contentValues.put(RemovedItemsContract.FIREBASEID, item.getFirebaseID());
        contentValues.put(RemovedItemsContract.ID, item.getId());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long n = sqLiteDatabase.insert(RemovedItemsContract.TABEL_NAME, null,contentValues);
        if (n==-1){
            return false;
        }else {
            return true;
        }
    }

    public void emptyRemovedItems(){ // need this method while syncronization
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + RemovedItemsContract.TABEL_NAME );
     }

    public ArrayList<Item> getAllItems(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Item> arrayList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(ItemsContract.TABEL_NAME, new String[]
                        {
                                ItemsContract.ID,
                                ItemsContract.FIREBASEID,
                                ItemsContract.TITLE,
                                ItemsContract.DESCRIPTION,
                                ItemsContract.DATE_ADDED,
                                ItemsContract.ITEMSTATUS,
                                ItemsContract.ITEMIMAGEPATH
                        }
                , null, null,
                null, null, null);

        while (cursor.moveToNext()) {
            arrayList.add(new Item(
                    cursor.getInt(0)
                    ,cursor.getString(1)
                    , cursor.getString(2)
                    , cursor.getString(3)
                    , cursor.getString(4)
                    , cursor.getString(5)
                    ,cursor.getString(6)));
        }
        return arrayList;
    }
}
