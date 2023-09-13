package tax.bnb;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "ROWSET")
public class Rates {

    @JacksonXmlElementWrapper(localName = "ROWSET", useWrapping = false)
    @JacksonXmlProperty(localName = "ROW")
    public List<Rate> rates;

    public Double getRate(String code) {
        if (rates == null || code == null) {
            return null;
        }
        return rates.stream()
                .filter(rate -> code.equals(rate.code))
                .findFirst()
                .map(rate -> rate.rate)
                .map(Double::parseDouble)
                .orElse(null);
    }

    @JacksonXmlRootElement(localName = "ROW")
    public static class Rate {
        @JacksonXmlProperty(localName = "CODE")
        public String code;
        @JacksonXmlProperty(localName = "RATE")
        public String rate;
    }
}
