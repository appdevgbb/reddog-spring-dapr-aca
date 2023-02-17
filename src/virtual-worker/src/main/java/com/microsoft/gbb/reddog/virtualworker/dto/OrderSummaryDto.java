package com.microsoft.gbb.reddog.virtualworker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * The type Order summary.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryDto{

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
                "orderCompletedInstant=" + orderCompletedInstant +
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