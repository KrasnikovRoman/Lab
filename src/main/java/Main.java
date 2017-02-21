import connector.Connector;
import entities.Currency;
import java.util.Scanner;

/**
 * Created by Krasnikov Roman on 20.02.17.
 */
public class Main {
    public static void main(String[] args) {
        String baseCurrency;
        String otherCurrency;
        Connector connector;

        System.out.println("Available currencies\n");
        System.out.println("AUD  GBR  KRW  SEK\nBGN  HKD  MXN  SGB\nBRL  HRK  MYR  THB\nCAD  HUF  NOK  TRY\n");
        System.out.println("CHF  IDR  NZD  USD\nCNY  ILS  PHP  ZAR\nCZK  INR  PLN  EUR\nDKK  JPY  RON  RUB\n");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter from currency:");
            baseCurrency = scanner.next();
            try {
                Currency.valueOf(baseCurrency);
            } catch (IllegalArgumentException e) {
                System.out.println("Fail! The name of the currency is typed wrong or it doesn't exist on the list. Try again!");
                continue;
            }
            break;
        }

        while (true) {
            System.out.println("Enter to currency:");
            otherCurrency = scanner.next();
            try {
                Currency.valueOf(otherCurrency);
            } catch (IllegalArgumentException e) {
                System.out.println("Fail! The name of the currency is typed wrong or it doesn't exist on the list. Try again!");
                continue;
            }
            break;
        }

        scanner.close();
        connector = new Connector(baseCurrency, otherCurrency);
        System.out.println(baseCurrency + " => " + otherCurrency + " : " + connector.getRate());

    }
}