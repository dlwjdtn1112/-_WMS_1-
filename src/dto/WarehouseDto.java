package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)


public class WarehouseDto {

    private String ware_id;
    private String client_id;
    private String prod_id;
    private int quantity;
    private Date last_inbound_day;
    private Date last_outbount_day;

    public String getWare_id() {
        return ware_id;
    }

    public void setWare_id(String ware_id) {
        this.ware_id = ware_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getLast_inbound_day() {
        return last_inbound_day;
    }

    public void setLast_inbound_day(Date last_inbound_day) {
        this.last_inbound_day = last_inbound_day;
    }

    public Date getLast_outbount_day() {
        return last_outbount_day;
    }

    public void setLast_outbount_day(Date last_outbount_day) {
        this.last_outbount_day = last_outbount_day;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    private String prod_name; // inventory_readAll() 전체조회 위해 상품명 추가



}
