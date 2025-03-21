package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
// 혹시 몰라 생성했습니다 필요 시 삭제 가능
public class InventoryDto {
    private String prod_id;
    private String client_id;
    private int quantity;
    private String ware_id;
    private String prod_name;

}
