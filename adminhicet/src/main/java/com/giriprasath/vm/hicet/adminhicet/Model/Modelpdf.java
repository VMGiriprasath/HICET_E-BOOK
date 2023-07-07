package com.giriprasath.vm.hicet.adminhicet.Model;

public class Modelpdf {
    private String uid, id, title, description, categoryId, Url, facultyname, CurrentYear, Department;

    private long timestamp;

    public Modelpdf() {
    }


    public Modelpdf(String uid, String id, String title, String description, String categoryId, String url, long timestamp, String facultyname, String CurrentYear, String Department) {
        this.uid = uid;
        this.id = id;
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.Url = url;
        this.timestamp = timestamp;
        this.facultyname = facultyname;
        this.CurrentYear = CurrentYear;
        this.Department = Department;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        this.Url = url;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFacultyname() {
        return facultyname;
    }

    public void setFacultyname(String facultyname) {
        this.facultyname = facultyname;
    }

    public String getCurrentYear() {
        return CurrentYear;
    }

    public void setCurrentYear(String currentYear) {
        CurrentYear = currentYear;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }


}
