package connector;

import cache.CacheService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.ApiResponse;
import entities.RateObject;
import parser.RatesDeserializer;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by Krasnikov Roman on 20.02.17.
 */
public class Connector {
    private String baseCurrency;
    private String otherCurrency;
    private Date date;

    public Connector(String baseCurrency, String otherCurrency) {
        this.baseCurrency = baseCurrency;
        this.otherCurrency = otherCurrency;
        date = new Date();
    }

    /**
     * В зависимости от baseCurrency и otherCurrency формирует get-запрос
     */
    private URL getURL() throws MalformedURLException {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("http://api.fixer.io/latest?base=")
                .append(baseCurrency)
                .append("&symbols=")
                .append(otherCurrency);
        URL url = new URL(urlBuilder.toString());

        return url;
    }

    /**
     * Получает значение курса валют, которое подгружается из локального хранилища или get-запросом
     */
    public double getRate() {
            // Получаем instance кэш-сервиса
            CacheService cacheService = CacheService.getInstance();
            // Получаем значение курса валют из кэша. Если оно не равно нулю, то возвращаем его. Иначе, отправляем get-запрос.
            // rate == 0 означает, что в кэше нет записи для данного курса валют
            double rate = cacheService.getRateFromCache(date, baseCurrency, otherCurrency);
            if (rate != 0) {
                return rate;
            } else {
                try {
                    URL url = getURL();
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                        Gson gson = new GsonBuilder()
                                .registerTypeAdapter(RateObject.class, new RatesDeserializer())
                                .create();
                        ApiResponse response = gson.fromJson(reader, ApiResponse.class);
                        cacheService.saveEntry(date, response);

                        reader.close();
                        connection.disconnect();
                        return response.getRates().getRate();
                    }

                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        return 0;
    }
}