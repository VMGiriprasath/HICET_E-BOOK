package com.giriprasath.vm.hicet.adminhicet.Model;

public class ModelCategory {
    String id,category,uid,rollnumber,year,department;
    long timestamp;

    public ModelCategory() {
    }

    public ModelCategory(String id, String category, String uid, String rollnumber, String year, String department, long timestamp) {
        this.id = id;
        this.category = category;
        this.uid = uid;
        this.rollnumber = rollnumber;
        this.year = year;
        this.department = department;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRollnumber() {
        return rollnumber;
    }

    public void setRollnumber(String rollnumber) {
        this.rollnumber = rollnumber;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
