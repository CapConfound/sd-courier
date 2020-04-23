package ru.simdelivery.sdcourier.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("locality")
    @Expose
    private Locality locality;
    @SerializedName("formatted")
    @Expose
    private String formatted;
    @SerializedName("route")
    @Expose
    private String route;
    @SerializedName("house")
    @Expose
    private String house;
    @SerializedName("entrance")
    @Expose
    private Object entrance;
    @SerializedName("floor")
    @Expose
    private Object floor;
    @SerializedName("apartment")
    @Expose
    private Object apartment;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }

    public String getFormatted() {
        return formatted;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public Object getEntrance() {
        return entrance;
    }

    public void setEntrance(Object entrance) {
        this.entrance = entrance;
    }

    public Object getFloor() {
        return floor;
    }

    public void setFloor(Object floor) {
        this.floor = floor;
    }

    public Object getApartment() {
        return apartment;
    }

    public void setApartment(Object apartment) {
        this.apartment = apartment;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}