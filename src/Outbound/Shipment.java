package Outbound;

import lombok.Data;

@Data
public class Shipment {
    private int id;
    private String productName;
    private int quantity;
    private String destination;

    public Shipment(int id,String productName,int quantity,String destination){
        this.id = id;
        this.productName = productName;
        this.quantity  = quantity;
        this.destination = destination;
    }
    public Shipment(String productName,int quantity,String destination){
        this.productName = productName;
        this.quantity = quantity;
        this.destination = destination;
    }

    public String toString(){
        return "id : " + id + "productName : " + productName + "quantity : " + quantity + "destination : " + destination;

    }


}
