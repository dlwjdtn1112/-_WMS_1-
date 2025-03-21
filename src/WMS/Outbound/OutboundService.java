package WMS.Outbound;

import WMS.OutboundDTO;

import java.util.List;

public class OutboundService {
    private final OutboundRepository outboundRepository = new OutboundRepository();

    public void OutboundRequsetService(OutboundDTO outboundDTO){
        outboundRepository.OutboundRequestRepository(outboundDTO);

    }

    public void OutboundDeleteService(){
        outboundRepository.OutboundDeleteRepository();

    }

    public List<OutboundDTO> OutboundReadAllService(){//출고 내역을 보여준다
        return outboundRepository.OutboundReadAllRepository();
    }

    public void OutboundApproveService(int id){
        outboundRepository.OutboundApproveRepository(id);
    }



}
