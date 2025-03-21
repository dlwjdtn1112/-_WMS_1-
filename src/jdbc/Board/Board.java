package jdbc.Board;
import lombok.Data;

import java.sql.Blob;

/**
 * packageName   : jdbc.users
 * fileName      : User
 * author        : a
 * date          : 2025-02-27
 * description   :
 * =================================================
 * DATE             AUTHOR             NOTE
 * -------------------------------------------------
 * 2025-02-27        a          한 사용자의 정보를 담는 객체
 */
@Data
public class Board {
    private int bno;
    private String bititle;
    private String bcontent;
    private String bwriter;
    private String bdate;
    private String bfilename;
    private Blob bfiledata;

    public void setBtitle(String btitle) {
    }
}
