package cache;

import entities.ApiResponse;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Krasnikov Roman on 21.02.17.
 */
public class CacheService {
    private static CacheService instance;
    private static final String pathToCache = "src/main/java/cache/cache.txt";
    private SimpleDateFormat dateFormat;

    private CacheService() {
        dateFormat = new SimpleDateFormat("dd:MM:yyyy");
    }

    public static CacheService getInstance() {
        if (instance == null) {
            instance = new CacheService();
        }
        return instance;
    }

    /**
     * Сохраняет запись в кэш.
     */
    public void saveEntry(Date date, ApiResponse response) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(pathToCache, true));
            StringBuilder builder = new StringBuilder();
            builder.append(dateFormat.format(date))
                    .append(" ")
                    .append(response.getBase())
                    .append("=>")
                    .append(response.getRates().getName())
                    .append(" ")
                    .append(response.getRates().getRate())
                    .append("\n");
            writer.write(builder.toString());
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получает запись из кэша и возвращает значение курса валют.
     */
    public double getRateFromCache(Date date, String baseCurrency, String otherCurrency) {
        StringBuilder builder = new StringBuilder();
        builder.append(dateFormat.format(date))
                .append(" ")
                .append(baseCurrency)
                .append("=>")
                .append(otherCurrency);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToCache));
            String line;
            double rate = 0;
            while ((line = reader.readLine()) != null) {
                if (line.contains(builder.toString())) {
                    String[] tmp = line.split(" ");
                    rate = Double.parseDouble(tmp[2]);
                }
            }
            reader.close();
            return rate;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}