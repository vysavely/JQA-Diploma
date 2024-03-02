package data;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class CardInfo {
        public String cardNumber;
        public String cardStatus;
    }

    public static CardInfo getCardApproved() {
        return new CardInfo("4444 4444 4444 4441", "APPROVED");
    }

    public static CardInfo getCardDeclined() {
        return new CardInfo("4444 4444 4444 4442", "DECLINED");
    }

    private static Faker faker = new Faker(new Locale("en"));
    private static Faker fakerRus = new Faker(new Locale("ru"));

    public static String getRandomCorrectCardNumber() {
        return faker.business().creditCardNumber();
    }

    public static String getRandomShorterCardNumber() {
        int shortNumber = faker.random().nextInt(16);
        return faker.number().digits(shortNumber);
    }

    public static String getRandomMonth(int month) {
        return LocalDate.now().plusMonths(month).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getRandomYear(int year) {
        return LocalDate.now().plusYears(year).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getInvalidMonth() {
        return "00";
    }

    public static String getRandomName() {
        return faker.name().fullName();
    }

    public static String getRandomNameRus() {
        return fakerRus.name().fullName();
    }

    public static String getNumberName() {
        return faker.number().digit();
    }

    public static String getHundredSymbolsName() {
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en"), new RandomService());
        return fakeValuesService.letterify("??????????????????????????????????????????????????????????????????????????????????????????????????????");
    }

    public static String getNumberCVV(int code) {
        return faker.number().digits(code);
    }

    public static String getZeroCVV() {
        return "000";
    }

    public static String getSpecSymbolName() {
        return "!@#№$%^&??&*()+";
    }

    public static String getEmptyField() {
        return "";
    }
}
