package ru.simdelivery.sdcourier.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Point implements Comparable<Point> {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("address")
    @Expose
    private Address address;

    @SerializedName("person")
    @Expose
    private Person person;

    @SerializedName("number")
    @Expose
    private Integer number;

    @SerializedName("arrivalAtFrom")
    @Expose
    private String arrivalAtFrom;

    @SerializedName("arrivalAtTo")
    @Expose
    private String arrivalAtTo;

    @SerializedName("commentary")
    @Expose
    private Object commentary;

    @SerializedName("paymentObject")
    @Expose
    private String paymentObject;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getArrivalAtFrom() {
        return arrivalAtFrom;
    }

    public void setArrivalAtFrom(String arrivalAtFrom) {
        this.arrivalAtFrom = arrivalAtFrom;
    }

    public String getArrivalAtTo() {
        return arrivalAtTo;
    }

    public void setArrivalAtTo(String arrivalAtTo) {
        if (arrivalAtTo != null) {
            this.arrivalAtTo = arrivalAtTo;
        } else {
            this.arrivalAtTo = "ASAP";
        }
    }

    public Object getCommentary() {
        return commentary;
    }

    public void setCommentary(Object commentary) {
        this.commentary = commentary;
    }

    public String getPaymentObject() {
        return paymentObject;
    }

    public void setPaymentObject(String paymentObject) {
        this.paymentObject = paymentObject;
    }


    @Override
    public int compareTo(Point p) {
        if(getNumber() == null || p.getNumber() == null) {
            return 0;
        }
        return getNumber().compareTo(p.getNumber());
    }

}