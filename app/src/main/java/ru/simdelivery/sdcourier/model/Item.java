package ru.simdelivery.sdcourier.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("product")
    @Expose
    private Product product;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("discount")
    @Expose
    private Integer discount;
    @SerializedName("payment_count")
    @Expose
    private Integer paymentCount;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getPaymentCount() {
        return paymentCount;
    }

    public void setPaymentCount(Integer paymentCount) {
        this.paymentCount = paymentCount;
    }

}
