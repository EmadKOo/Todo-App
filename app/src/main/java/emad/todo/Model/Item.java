package emad.todo.Model;

import java.io.Serializable;
import java.util.Date;

public class Item implements Serializable {
    int id;
    String firebaseID;
    String title;
    String description;
    String dateAdded;
    String itemStatus;      // updated , synced before to firebase, new
    String imagePath;

    public Item(int id, String firebaseID, String title, String description, String dateAdded, String itemStatus, String imagePath) {
        this.id = id;
        this.firebaseID = firebaseID;
        this.title = title;
        this.description = description;
        this.dateAdded = dateAdded;
        this.itemStatus = itemStatus;
        this.imagePath = imagePath;
    }

    public Item(int id, String firebaseID, String title, String itemStatus) {
        this.id = id;
        this.firebaseID = firebaseID;
        this.title = title;
        this.itemStatus = itemStatus;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getFirebaseID() {
        return firebaseID;
    }

    public void setFirebaseID(String firebaseID) {
        this.firebaseID = firebaseID;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
