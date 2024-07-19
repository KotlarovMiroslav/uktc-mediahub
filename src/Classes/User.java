package Classes;

import javafx.scene.control.CheckBox;

public class User {
    private int id;
    private String name;
    private String courseNum;
    private String egn;
    private String phoneNum;
    private String description;
    private CheckBox checkBox;

    public User(int id, String name, String courseNum, String egn, String phoneNum, String description, CheckBox checkBox) {
        this.id = id;
        this.name = name;
        this.courseNum = courseNum;
        this.egn = egn;
        this.phoneNum = phoneNum;
        this.description = description;
        this.checkBox = checkBox;
    }

    public User() {

    }

    public String getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEgn() {
        return egn;
    }

    public void setEgn(String egn) {
        this.egn = egn;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
