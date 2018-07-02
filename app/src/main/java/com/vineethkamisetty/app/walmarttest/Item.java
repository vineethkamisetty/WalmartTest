package com.vineethkamisetty.app.walmarttest;

public class Item {
    private long itemId;
    private String itemName;
    private double itemMSRP;
    private double itemPrice;
    private String itemShortDescription;
    private String itemLongDescription;
    private String itemThumbnailImage;
    private String itemURL;
    private boolean itemAvailableOnline;
    private String itemRating;

    Item() {
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemMSRP() {
        return itemMSRP;
    }

    public void setItemMSRP(double itemMSRP) {
        this.itemMSRP = itemMSRP;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemShortDescription() {
        return itemShortDescription;
    }

    public void setItemShortDescription(String itemShortDescription) {
        this.itemShortDescription = itemShortDescription;
    }

    public String getItemLongDescription() {
        return itemLongDescription;
    }

    public void setItemLongDescription(String itemLongDescription) {
        this.itemLongDescription = itemLongDescription;
    }

    public String getItemThumbnailImage() {
        return itemThumbnailImage;
    }

    public void setItemThumbnailImage(String itemThumbnailImage) {
        this.itemThumbnailImage = itemThumbnailImage;
    }

    public String getItemURL() {
        return itemURL;
    }

    public void setItemURL(String itemURL) {
        this.itemURL = itemURL;
    }

    public boolean isItemAvailableOnline() {
        return itemAvailableOnline;
    }

    public void setItemAvailableOnline(boolean itemAvailableOnline) {
        this.itemAvailableOnline = itemAvailableOnline;
    }

    public String getItemRating() {
        return itemRating;
    }

    public void setItemRating(String itemRating) {
        this.itemRating = itemRating;
    }
}
