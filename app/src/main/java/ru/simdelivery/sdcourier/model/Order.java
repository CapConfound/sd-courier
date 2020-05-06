package ru.simdelivery.sdcourier.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("points")
    @Expose
    private List<Point> points = null;

    @SerializedName("items")
    @Expose
    private List<Item> items = null;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("cost")
    @Expose
    private Integer cost;

    @SerializedName("currentPointId")
    @Expose
    private Integer currentPointId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getCurrentPointId() {
        return currentPointId;
    }

    public void setCurrentPointId(Integer currentPointId) {
        this.currentPointId = currentPointId;
    }
}
