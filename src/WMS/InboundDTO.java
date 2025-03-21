package WMS;

import lombok.Data;

@Data

public class InboundDTO {
    private String inbound_id;
    private String product_id;
    private int inbound_quantity;
    private String req_inbound_day;
    private String status;
    private String warehouse_id;

//    public InboundDTO(String inbound_id, String product_id, int outbound_quantity, String req_inbound_day, String status, String warehouse_id) {
//        this.inbound_id = inbound_id;
//        this.product_id = product_id;
//        this.inbound_quantity = outbound_quantity;
//        this.req_inbound_day = req_inbound_day;
//        this.status = status;
//        this.warehouse_id = warehouse_id;
//    }

//    public InboundDTO(String inboundId, int inboundQuantity) {
//    }

    public InboundDTO( String productId, int inboundQuantity, String reqInboundDay, String inboundWareId) {
        //this.inbound_id = inboundId;
        this.product_id = productId;
        this.inbound_quantity = inboundQuantity;
        this.req_inbound_day = reqInboundDay;
        this.warehouse_id = inboundWareId;

    }
    public InboundDTO(String inboundId ,String productId, int inboundQuantity, String reqInboundDay,String status ,String inboundWareId) {
        this.inbound_id = inboundId;
        this.product_id = productId;
        this.inbound_quantity = inboundQuantity;
        this.req_inbound_day = reqInboundDay;
        this.status = status;
        this.warehouse_id = inboundWareId;

    }


}
