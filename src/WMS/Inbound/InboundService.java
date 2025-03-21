package WMS.Inbound;

import WMS.InboundDTO;
import WMS.WarehouseDTO;

import java.util.List;

public class InboundService {
    private final InboundRepository inboundRepository = new InboundRepository();

    public void InboundRequsetService(InboundDTO inboundDTO){
        inboundRepository.InboundRequestRepository(inboundDTO);

    }

    public void InboundDeleteService(){
        inboundRepository.InboundDeleteRepository();

    }
    public void InboundApproveService(int id){
        inboundRepository.InboundApproveRepository(id);
    }

    public void InboundRejectService(int id){
        inboundRepository.InboundRejectRepository(id);
    }
    public List<InboundDTO> InboundReadAllService(){
        return inboundRepository.InboundReadAllRepository();
    }

    public List<WarehouseDTO> ReadAllWarehouseService(){
        return inboundRepository.ReadAllWarehouseRepository();

    }




}
