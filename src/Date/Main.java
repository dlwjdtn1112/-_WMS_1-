package Date;
import java.util.Date;
public class Main {
    public static void main(String[] args) {
        Date now = new Date();
        Date pastDate = new Date(1672531200000L); // 2023-01-01 00:00:00 GMT

        System.out.println("현재 시간: " + now);
        System.out.println("이전 날짜: " + pastDate);

        System.out.println("현재가 과거보다 이후인가? " + now.after(pastDate));
        System.out.println("현재가 과거보다 이전인가? " + now.before(pastDate));
        System.out.println("날짜 비교 결과: " + now.compareTo(pastDate));
    }
}
