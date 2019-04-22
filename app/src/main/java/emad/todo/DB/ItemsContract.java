package emad.todo.DB;

public class ItemsContract {
    public static String ID = "ID";
    public static String TITLE = "Title";
    public static String DESCRIPTION = "description";
    public static String DATE_ADDED = "dateAdded";
    public static String FIREBASEID =  "firebaseId";
    public static String ITEMSTATUS = "itemStatus";
    public static String ITEMIMAGEPATH = "itemimagePath";


    public static String TABEL_NAME ="items";


    public static String CREATE_TABLE ="Create table " + TABEL_NAME + "( "
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TITLE + " varchar(30) , "
            + DATE_ADDED + " varchar(30) , "
            + FIREBASEID + " varchar(30) , "
            + ITEMSTATUS + " varchar(30) , "
            + DESCRIPTION + " varchar(30) , "
            + ITEMIMAGEPATH + " varchar(300));";
    public static String REMOVE_TABLE ="drop Table "+ TABEL_NAME + " if exists";

}
