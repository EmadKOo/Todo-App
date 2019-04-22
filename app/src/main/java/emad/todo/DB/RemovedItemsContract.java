package emad.todo.DB;

public class RemovedItemsContract {
    public static String ID = "ID";
    public static String TITLE = "Title";
    public static String FIREBASEID =  "firebaseId";
    public static String ITEMSTATUS = "itemStatus";

    public static String TABEL_NAME ="RemovedItems";


    public static String CREATE_TABLE ="Create table " + TABEL_NAME + "( "
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TITLE + " varchar(30) , "
            + ITEMSTATUS + " varchar(30) , "
            + FIREBASEID + " varchar(40));";
    public static String REMOVE_TABLE ="drop Table "+ TABEL_NAME + " if exists";

}
