package WMS;

import lombok.Data;

@Data

public class OutboundDTO {
    private String outbound_id;
    private String product_id;
    private int outbound_quantity;
    private String req_outbound_day;
    private String status;
    private String warehouse_id;

    public OutboundDTO(String productId, int outboundQuantity, String reqOutboundDay, String outboundWareId){
        this.product_id = productId;
        this.outbound_quantity = outboundQuantity;
        this.req_outbound_day = reqOutboundDay;
        this.warehouse_id = outboundWareId;

    }
    public OutboundDTO(String outboundId ,String productId, int outboundQuantity, String reqOutboundDay,String status ,String outboundWareId) {
        this.outbound_id = outboundId;
        this.product_id = productId;
        this.outbound_quantity = outboundQuantity;
        this.req_outbound_day = reqOutboundDay;
        this.status = status;
        this.warehouse_id = outboundWareId;

    }





}
