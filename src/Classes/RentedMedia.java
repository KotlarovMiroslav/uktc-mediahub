package Classes;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

public class RentedMedia {
    private int rentID;
    private int mediaID;
    private String userName;
    private String mediaName;
    private String courseNum;
    private int mediaQuantity;
    private CheckBox checkBox;
    private String startDate;
    private String endDate;
    private String qrCode;

    public RentedMedia(int rentID,int mediaID, String userName, String mediaName, String courseNum, int mediaQuantity, CheckBox checkBox, String startDate, String endDate, String qrCode) {
        this.rentID = rentID;
        this.mediaID = mediaID;
        this.userName = userName;
        this.mediaName = mediaName;
        this.courseNum = courseNum;
        this.mediaQuantity = mediaQuantity;
        this.checkBox = checkBox;
        this.startDate = startDate;
        this.endDate = endDate;
        this.qrCode = qrCode;
    }

    public RentedMedia() {

    }

    public int getMediaQuantity() {
        return mediaQuantity;
    }

    public void setMediaQuantity(int mediaQuantity) {
        this.mediaQuantity = mediaQuantity;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public int getMediaID() {
        return mediaID;
    }

    public void setMediaID(int mediaID) {
        this.mediaID = mediaID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getRentID() {
        return rentID;
    }

    public void setRentID(int rentID) {
        this.rentID = rentID;
    }
}
