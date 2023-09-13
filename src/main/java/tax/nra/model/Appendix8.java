package tax.nra.model;

import static tax.Utils.scaledBigDecimal;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@Builder
@JacksonXmlRootElement(localName = "app8")
public class Appendix8 {

    @JacksonXmlElementWrapper(localName = "stocks")
    @JacksonXmlProperty(localName = "stocksenum")
    @Singular
    public List<StocksEnum> stocks;

    @JacksonXmlProperty(localName = "joinstocks")
    public final JoinStocks joinstocks = new JoinStocks();
    @JacksonXmlProperty(localName = "place")
    public final Place place = new Place();
    @JacksonXmlProperty(localName = "base")
    public final Place base = new Place();
    @JacksonXmlElementWrapper(localName = "prop")
    @JacksonXmlProperty(localName = "propenum")
    public final List<PropEnum> prop = List.of(new PropEnum());
    @JacksonXmlProperty(localName = "part3")
    public final App8Part3 part3 = new App8Part3();
    @JacksonXmlProperty(localName = "row4")
    public final BigDecimal row4 = scaledBigDecimal(0);
    @JacksonXmlProperty(localName = "part38al1")
    public final App8Part38al1 part38al1 = new App8Part38al1();
    @JacksonXmlProperty(localName = "sum81al1")
    public final Object sum81al1 = scaledBigDecimal(0);
    @JacksonXmlProperty(localName = "part38al58")
    public final App8Part38al58 part38al58 = new App8Part38al58();
    @JacksonXmlProperty(localName = "sum81al58")
    public final Object sum81al58 = scaledBigDecimal(0);
    @JacksonXmlProperty(localName = "part38al13")
    public final App8Part38al13 part38al13 = new App8Part38al13();
    @JacksonXmlProperty(localName = "sum38al13")
    public final Object sum38al13 = scaledBigDecimal(0);
    @JacksonXmlProperty(localName = "partstatus")
    public final int partstatus = 1;

    @Getter
    @Builder
    @JacksonXmlRootElement(localName = "stocksenum")
    public static class StocksEnum {
        @JacksonXmlProperty(localName = "country")
        private String country;
        @JacksonXmlProperty(localName = "count")
        private BigDecimal count;
        @JacksonXmlProperty(localName = "acquiredate")
        private Date acquireDate;
        @JacksonXmlProperty(localName = "priceincurrency")
        private BigDecimal priceInCurrency;
        @JacksonXmlProperty(localName = "price")
        private BigDecimal price;

        @Override
        public String toString() {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(acquireDate);
            return String.format(
                    "Country: %s \t Amount: %s \t Date: %s \t In USD: %s \t In BGN: %s", country,
                    count.toPlainString(), date, priceInCurrency.toPlainString(),
                    price.toPlainString());
        }

        public static class StocksEnumBuilder {

            public StocksEnumBuilder count(double count) {
                this.count = scaledBigDecimal(count);
                return this;
            }

            public StocksEnumBuilder priceInCurrency(double priceInCurrency) {
                this.priceInCurrency = scaledBigDecimal(priceInCurrency);
                return this;
            }

            public StocksEnumBuilder price(double price) {
                this.price = scaledBigDecimal(price);
                return this;
            }

            public StocksEnumBuilder acquireDate(LocalDate date) {
                this.acquireDate = java.util.Date.from(date.atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
                return this;
            }
        }
    }

    @Override
    public String toString() {
        return "Appendix 8: \n" + this.stocks.stream().map(StocksEnum::toString)
                .collect(Collectors.joining("\n"));
    }

    @JacksonXmlRootElement
    private static class Place {
        @JacksonXmlProperty
        private Object country;
        @JacksonXmlProperty
        private Object addres;
    }

    @JacksonXmlRootElement
    private static class PropEnum {
        @JacksonXmlProperty
        private Object country;
        @JacksonXmlProperty
        private Object addres;
        @JacksonXmlProperty
        private Object type;
        @JacksonXmlProperty
        private Object acquiredate;
    }

    @JacksonXmlRootElement
    private static class App8RowEnum {
        @JacksonXmlProperty
        private Object name;
        @JacksonXmlProperty
        private Object country;
        @JacksonXmlProperty
        private Object incomecode;
        @JacksonXmlProperty
        private Object methodcode;
        @JacksonXmlProperty
        private Object sum;
        @JacksonXmlProperty
        private Object value;
        @JacksonXmlProperty
        private Object diff;
        @JacksonXmlProperty
        private Object paidtax;
        @JacksonXmlProperty
        private Object permitedtax;
        @JacksonXmlProperty
        private Object tax;
        @JacksonXmlProperty
        private Object owetax;
    }

    @JacksonXmlRootElement
    private class JoinStocks {

        @JacksonXmlProperty(localName = "stocksenum")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<StocksEnum> stocksEnums;

        public JoinStocks() {
            this.stocksEnums = List.of(StocksEnum.builder().build());
        }
    }

    @JacksonXmlRootElement
    private static class App8Part3 {
        @JacksonXmlProperty(localName = "rowenum")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<App8Part3RowEnum> rowenum;
        public App8Part3() {
            this.rowenum = List.of(new App8Part3RowEnum());
        }

        @JacksonXmlRootElement
        private class App8Part3RowEnum {
            @JacksonXmlProperty
            private Object name;
            @JacksonXmlProperty
            private Object country;
            @JacksonXmlProperty
            private Object owetax;
        }
    }

    @JacksonXmlRootElement
    private static class App8Part38al1 {
        @JacksonXmlProperty(localName = "rowenum")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<App8RowEnum> rowenum;
        public App8Part38al1() {
            this.rowenum = List.of(new App8RowEnum());
        }
    }

    @JacksonXmlRootElement
    private static class App8Part38al58 {
        @JacksonXmlProperty(localName = "rowenum")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<App8RowEnum> rowenum;
        public App8Part38al58() {
            this.rowenum = List.of(new App8RowEnum());
        }
    }

    @JacksonXmlRootElement
    private static class App8Part38al13 {
        @JacksonXmlProperty(localName = "rowenum")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<App8RowEnum> rowenum;
        public App8Part38al13() {
            this.rowenum = List.of(new App8RowEnum());
        }
    }
}
