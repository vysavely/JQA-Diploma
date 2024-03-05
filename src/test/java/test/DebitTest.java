package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.HomePage;

import static com.codeborne.selenide.Selenide.open;

public class DebitTest {
    private HomePage home = new HomePage();

    String approvedCardNumber = DataHelper.getCardApproved().getCardNumber();
    String declinedCardNumber = DataHelper.getCardDeclined().getCardNumber();
    String randomCardNumber = DataHelper.getRandomCorrectCardNumber();
    String validMonth = DataHelper.getSpecificMonth(1);
    String validYear = DataHelper.getSpecificYear(1);
    String validOwnerName = DataHelper.getRandomName();
    String validCode = DataHelper.getNumberCVV(3);

    @BeforeEach
    public void setUp() {
        open(System.getProperty("sut.url"));
    }

    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @AfterEach
    public void shouldCleanBase() {
        SQLHelper.cleanBase();
    }

    @Test
    @DisplayName("Оплата картой со статусом 'APPROVED'")
    public void shouldApprovedPayment() {
        var debitPage = home.debitPayment();
        debitPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, validCode);
        debitPage.bankApprovedOperation();
        Assertions.assertEquals("APPROVED", SQLHelper.getCardPayment());
    }

    @Test
    @DisplayName("Оплата картой со статусом 'DECLINED'")
    public void shouldDeclinedPayment() {
        var debitPage = home.debitPayment();
        debitPage.fillCardPaymentForm(declinedCardNumber, validMonth, validYear, validOwnerName, validCode);
        debitPage.bankDeclinedOperation();
        Assertions.assertEquals("DECLINED", SQLHelper.getCardPayment());
    }

    @Test
    @DisplayName("Оплата с несуществующим корректным номером")
    public void shouldRandomCardNumberDebitPayment() {
        var debitPage = home.debitPayment();
        debitPage.fillCardPaymentForm(randomCardNumber, validMonth, validYear, validOwnerName, validCode);
        debitPage.bankDeclinedOperation();
    }

    @Test
    @DisplayName("Оплата с некорректным коротким номером карты")
    public void shouldInvalidCardNumberDebitPayment() {
        var debitPage = home.debitPayment();
        var invalidCardNumber = DataHelper.getRandomShorterCardNumber();
        debitPage.fillCardPaymentForm(invalidCardNumber, validMonth, validYear, validOwnerName, validCode);
        debitPage.errorFormat();
    }

    @Test
    @DisplayName("Оплата с незаполненным значением номера карты")
    public void shouldEmptyCardNumberDebitPayment() {
        var debitPage = home.debitPayment();
        var emptyCardNumber = DataHelper.getEmptyField();
        debitPage.fillCardPaymentForm(emptyCardNumber, validMonth, validYear, validOwnerName, validCode);
        debitPage.errorFormat();
    }

    @Test
    @DisplayName("Оплата картой с истекшим сроком действия (Месяц)")
    public void shouldDebitPaymentWithMonthTermValidityExpired() {
        var debitPage = home.debitPayment();
        var currentYear = DataHelper.getSpecificYear(0);
        var monthTermValidityExpired = DataHelper.getSpecificMonth(-1);
        debitPage.fillCardPaymentForm(approvedCardNumber, monthTermValidityExpired, currentYear, validOwnerName, validCode);
        debitPage.errorCardTermValidity();
    }

    @Test
    @DisplayName("Оплата с некорректным значением карты - месяц 00")
    public void shouldDebitPaymentWithInvalidMonth() {
        var debitPage = home.debitPayment();
        var invalidMonth = DataHelper.getInvalidMonth();
        debitPage.fillCardPaymentForm(approvedCardNumber, invalidMonth, validYear, validOwnerName, validCode);
        debitPage.errorCardTermValidity();
    }

    @Test
    @DisplayName("Оплата с некорректным значением карты - поле месяц пустое")
    public void shouldDebitPaymentWithEmptyMonth() {
        var debitPage = home.debitPayment();
        var emptyMonth = DataHelper.getEmptyField();
        debitPage.fillCardPaymentForm(approvedCardNumber, emptyMonth, validYear, validOwnerName, validCode);
        debitPage.errorFormat();
    }

    @Test
    @DisplayName("Оплата с истекшим сроком действия карты - год")
    public void shouldDebitPaymentWithYearTermValidityExpired() {
        var debitPage = home.debitPayment();
        var yearTermValidityExpired = DataHelper.getSpecificYear(-1);
        debitPage.fillCardPaymentForm(approvedCardNumber, validMonth, yearTermValidityExpired, validOwnerName, validCode);
        debitPage.termValidityExpired();
    }

    @Test
    @DisplayName("Оплата с некорректным значением карты - год")
    public void shouldDebitPaymentWithInvalidYear() {
        var debitPage = home.debitPayment();
        var invalidYear = DataHelper.getSpecificYear(6);
        debitPage.fillCardPaymentForm(approvedCardNumber, validMonth, invalidYear, validOwnerName, validCode);
        debitPage.errorCardTermValidity();
    }

    @Test
    @DisplayName("Оплата с некорректным значением карты - поле год пустое")
    public void shouldDebitPaymentWithEmptyYear() {
        var debitPage = home.debitPayment();
        var emptyYear = DataHelper.getEmptyField();
        debitPage.fillCardPaymentForm(approvedCardNumber, validMonth, emptyYear, validOwnerName, validCode);
        debitPage.errorFormat();
    }

    @Test
    @DisplayName("Оплата по карте с именем владельца на кириллице")
    public void shouldRusLangNameDebitPayment() {
        var debitPage = home.debitPayment();
        var rusLangName = DataHelper.getRandomNameRus();
        debitPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, rusLangName, validCode);
        debitPage.errorFormat();
    }

    @Test
    @DisplayName("Оплата по карте с именем владельца, содержащим цифры")
    public void shouldDigitsNameDebitPayment() {
        var debitPage = home.debitPayment();
        var digitsName = DataHelper.getNumberName();
        debitPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, digitsName, validCode);
        debitPage.errorFormat();
    }

    @Test
    @DisplayName("Оплата по карте с именем владельца, содержащим спецсимволы")
    public void shouldSpecSymbolsNameDebitPayment() {
        var debitPage = home.debitPayment();
        var specSymbolsName = DataHelper.getSpecSymbolName();
        debitPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, specSymbolsName, validCode);
        debitPage.errorFormat();
    }

    @Test
    @DisplayName("Оплата по карте с именем владельца более 100 символов")
    public void shouldMoreHundredSymbolsNamePayment() {
        var debitPage = home.debitPayment();
        var hundredSymbolsName = DataHelper.getHundredSymbolsName();
        debitPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, hundredSymbolsName, validCode);
        debitPage.errorFormat();
    }

    @Test
    @DisplayName("Оплата по карте с некорректным именем владельца - поле пустое")
    public void shouldEmptyNameDebitPayment() {
        var debitPage = home.debitPayment();
        var emptyName = DataHelper.getEmptyField();
        debitPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, emptyName, validCode);
        debitPage.emptyField();
    }

    @Test
    @DisplayName("Оплата по карте с некорректным CVV кодом - две цифры")
    public void shouldTwoDigitsCVVDebitPayment() {
        var debitPage = home.debitPayment();
        var twoDigitsCode = DataHelper.getNumberCVV(2);
        debitPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, twoDigitsCode);
        debitPage.errorFormat();
    }

    @Test
    @DisplayName("Оплата по карте с некорректным CVV кодом - буквы")
    public void shouldLettersCVVDebitPayment() {
        var debitPage = home.debitPayment();
        var lettersCode = DataHelper.getRandomName();
        debitPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, lettersCode);
        debitPage.errorFormat();
    }

    @Test
    @DisplayName("Оплата по карте с некорректным CVV кодом - спецсимволы")
    public void shouldSpecSymbolsCVVDebitPayment() {
        var debitPage = home.debitPayment();
        var specSymbolsCode = DataHelper.getSpecSymbolName();
        debitPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, specSymbolsCode);
        debitPage.errorFormat();
    }

    @Test
    @DisplayName("Оплата по карте с некорректным CVV кодом - введено 000")
    public void shouldZerosCVVDebitPayment() {
        var debitPage = home.debitPayment();
        var zeroCode = DataHelper.getZeroCVV();
        debitPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, zeroCode);
        debitPage.errorFormat();
    }

    @Test
    @DisplayName("Оплата по карте с некорректным CVV кодом - поле пустое")
    public void shouldEmptyCVVDebitPayment() {
        var debitPage = home.debitPayment();
        var emptyCode = DataHelper.getEmptyField();
        debitPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, emptyCode);
        debitPage.errorFormat();
    }

    @Test
    @DisplayName("Оплата по карте с пустыми полями формы")
    public void shouldEmptyAllFieldsDebitPayment() {
        var debitPage = home.debitPayment();
        var emptyCardNumber = DataHelper.getEmptyField();
        var emptyMonth = DataHelper.getEmptyField();
        var emptyYear = DataHelper.getEmptyField();
        var emptyName = DataHelper.getEmptyField();
        var emptyCode = DataHelper.getEmptyField();
        debitPage.fillCardPaymentForm(emptyCardNumber, emptyMonth, emptyYear, emptyName, emptyCode);
        debitPage.errorFormat();
    }
}