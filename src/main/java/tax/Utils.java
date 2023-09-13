package tax;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utils {

    private Utils(){
    }

    public static BigDecimal scaledBigDecimal(double d) {
        return new BigDecimal(d).setScale(2, RoundingMode.HALF_UP);
    }
}
