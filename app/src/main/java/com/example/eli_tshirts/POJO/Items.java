package com.example.eli_tshirts.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

/**
 * Model Class to get details from firestore to Items
 */
public class Items implements Parcelable {
    @Exclude private String id;
    String name, description, image;
    double price;
    boolean favourite;

    /**
     * Parameterized Constructor
     * @param name - name parameter
     * @param description - description parameter
     * @param image - image parameter
     * @param price - price parameter
     * @param favourite - favourite parameter
     */
    public Items(String name, String description, String image, double price, boolean favourite) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.favourite = favourite;
    }

    /**
     * Non-parameterized constructor
     */
    public Items() {
    }

    /**
     * Constructor with parcel parameter
     * @param in - parameter of type Parcel
     */
    protected Items(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        image = in.readString();
        price = in.readDouble();
        favourite = in.readByte() != 0;
    }

    public static final Creator<Items> CREATOR = new Creator<Items>() {
        @Override
        public Items createFromParcel(Parcel in) {
            return new Items(in);
        }

        @Override
        public Items[] newArray(int size) {
            return new Items[size];
        }
    };

    /**
     * Getter for id
     * @return - return id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for id
     * @param id - parameter for id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for name
     * @return - return name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name
     * @param name - parameter for name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for description
     * @return - return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for description
     * @param description - parameter for description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for image
     * @return - return image
     */
    public String getImage() {
        return image;
    }

    /**
     * Setter for image
     * @param image - parameter for image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Getter for price
     * @return - return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Setter for price
     * @param price - parameter for price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Getter for favourite
     * @return - return favourite
     */
    public boolean isFavourite() {
        return favourite;
    }

    /**
     * Setter for favourite
     * @param favourite - parameter for favourite
     */
    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(image);
        parcel.writeDouble(price);
        parcel.writeByte((byte) (favourite ? 1 : 0));
    }
}