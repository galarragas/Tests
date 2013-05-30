package com.pragmasoft.test.springws.model;

import java.util.Date;

public class Appointment {
    private Date startDate;
    private Date endDate;
    private String comment;
    private int id;

    public Appointment(int id, Date startDate, Date endDate, String comment) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.comment = comment;
        this.id = id;
    }

    public Appointment(Date startDate, Date endDate, String comment) {
        this(-1, startDate, endDate, comment);
    }

    public Appointment(int id, Appointment from) {
        this(id, from.getStartDate(), from.getEndDate(), from.getComment());
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getComment() {
        return comment;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Appointment that = (Appointment) o;

        if (id != that.id) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = startDate != null ? startDate.hashCode() : 0;
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + id;
        return result;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setId(int id) {
        this.id = id;
    }
}
