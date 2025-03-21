package Outbound.Service;

import Outbound.Repository.ShipmentRepository;
import Outbound.Shipment;

import java.util.List;

public class ShipmentService {
    private final ShipmentRepository shipmentRepository = new ShipmentRepository();

    public void outboundRequestService(Shipment shipment){
        shipmentRepository.outboundRequestRepository(shipment);
    }
    public List<Shipment> outboundTotalReadService(){
        return shipmentRepository.outboundTotalReadRepository();
    }
//    public void Delete(Shipment shipment){
//        shipmentRepository.deleteByid(shipment.getId());
//        // shipment는 출고데이터를 가지고 있다. 따라서 shipment.getId를 통해서 출고 일련변호를 받아서
//        // 삭제를 하도록 한다.
//    }

    public void outboundDeleteService(int id) {
        shipmentRepository.outboundDeleteRepository(id);
    }





}
