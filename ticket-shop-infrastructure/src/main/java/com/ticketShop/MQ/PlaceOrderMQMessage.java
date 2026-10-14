package com.ticketShop.MQ;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderMQMessage {
    private String token;
    private Long ticketEventId;
    private int quantity;
    private int userId;
    private long unitPrice;
    private long timestamp;
}
