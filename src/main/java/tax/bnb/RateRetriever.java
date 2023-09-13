package tax.bnb;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import java.io.Closeable;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import lombok.Cleanup;
import lombok.SneakyThrows;

public class RateRetriever implements Closeable {

    private final XmlMapper xmlMapper;
    private final OkHttpClient client;
    private final Map<LocalDate, Double> rateCache;

    public RateRetriever() {
        this.client = new OkHttpClient();
        this.rateCache = new HashMap<>();
        this.xmlMapper = new XmlMapper();
        xmlMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @SneakyThrows
    public Double retrieveRate(LocalDate date) {
        Double rate = rateCache.get(date);
        if (rate != null) {
            return rate;
        }

        Double rateFromBnb = retrieveRateFromBnb(date);
        rateCache.put(date, rateFromBnb);
        return rateFromBnb;
    }

    private Double retrieveRateFromBnb(LocalDate date) throws IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("www.bnb.bg")
                .addPathSegment("Statistics")
                .addPathSegment("StExternalSector")
                .addPathSegment("StExchangeRates")
                .addPathSegment("StERForeignCurrencies")
                .addPathSegment("index.htm")
                .addQueryParameter("downloadOper", "true")
                .addQueryParameter("group1", "first")
                .addQueryParameter("firstDays", String.valueOf(date.getDayOfMonth()))
                .addQueryParameter("firstMonths", String.valueOf(date.getMonth()))
                .addQueryParameter("firstYear", String.valueOf(date.getYear()))
                .addQueryParameter("search", "true")
                .addQueryParameter("showChart", "false")
                .addQueryParameter("showChartButton", "false")
                .addQueryParameter("type", "XML")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        //https://github.com/square/okhttp/issues/3957
        @Cleanup
        ResponseBody response = client.newCall(request).execute().body();

        if (!response.contentType().equals(MediaType.parse("text/xml;charset=UTF-8"))) {
            return retrieveRateFromBnb(date.minusDays(1));
        }

        Rates rates = this.xmlMapper.readValue(response.string(), Rates.class);
        return rates.getRate("USD");
    }

    @Override
    public void close() {
        try {
            client.getDispatcher().getExecutorService().shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
