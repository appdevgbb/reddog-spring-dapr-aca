package com.microsoft.gbb.reddog.receiptgenerationservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Order summary.
 */
@Getter
@Setter
public class OrderSummaryDto {
    
    public OrderSummaryDto(String loyaltyId, String firstName, String lastName, String orderId, String storeId, long orderDateInstant, List<OrderItemSummaryDto> orderItems, double orderTotal, String origin, String storeLatitude, String storeLongitude) {
        this.loyaltyId = loyaltyId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.orderId = orderId;
        this.storeId = storeId;
        this.orderDateInstant = orderDateInstant;
        this.orderItems = orderItems;
        this.orderTotal = orderTotal;
        this.origin = origin;
        this.storeLatitude = storeLatitude;
        this.storeLongitude = storeLongitude;
    }
    @JsonProperty("orderCompletedDate")
    private long orderCompletedInstant;

    @JsonProperty("loyaltyId")
    private String loyaltyId;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("storeId")
    private String storeId;


    @JsonProperty("orderDateInstant")
    private long orderDateInstant;

    @JsonProperty("orderItems")
    private List<OrderItemSummaryDto> orderItems;

    @JsonProperty("orderTotal")
    private double orderTotal;

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("storeLatitude")
    private String storeLatitude;

    @JsonProperty("storeLongitude")
    private String storeLongitude;

    @Override
    public String toString() {
        return "OrderSummaryDto{" +
                ", loyaltyId='" + loyaltyId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", orderId=" + orderId +
                ", storeId='" + storeId + '\'' +
                ", orderItems=" + orderItems +
                ", orderTotal=" + orderTotal +
                ", origin='" + origin + '\'' +
                ", storeLatitude='" + storeLatitude + '\'' +
                ", storeLongitude='" + storeLongitude + '\'' +
                '}';
    }
}