package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.HomePage;

import static com.codeborne.selenide.Selenide.open;

public class CreditTest {
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
    @DisplayName("Кредит по карте со статусом 'APPROVED'")
    public void shouldApprovedCreditPayment() {
        var creditPage = home.creditPayment();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditPage.bankApprovedOperation();
        Assertions.assertEquals("APPROVED", SQLHelper.getCreditPayment());
    }

    @Test
    @DisplayName("Кредит по карте со статусом 'DECLINED'")
    public void shouldDeclinedCreditPayment() {
        var creditPage = home.creditPayment();
        creditPage.fillCardPaymentForm(declinedCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditPage.bankDeclinedOperation();
        Assertions.assertEquals("DECLINED", SQLHelper.getCreditPayment());
    }

    @Test
    @DisplayName("Кредит с несуществующим корректным номером")
    public void shouldRandomCardNumberCreditPayment() {
        var creditPage = home.creditPayment();
        creditPage.fillCardPaymentForm(randomCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditPage.bankDeclinedOperation();
    }

    @Test
    @DisplayName("Кредит с некорректным коротким номером карты")
    public void shouldInvalidCardNumberCreditPayment() {
        var creditPage = home.creditPayment();
        var invalidCardNumber = DataHelper.getRandomShorterCardNumber();
        creditPage.fillCardPaymentForm(invalidCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит с незаполненным значением номера карты")
    public void shouldEmptyCardNumberCreditPayment() {
        var creditPage = home.creditPayment();
        var emptyCardNumber = DataHelper.getEmptyField();
        creditPage.fillCardPaymentForm(emptyCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по данным карты с истекшим сроком действия (Месяц)")
    public void shouldCreditPaymentWithMonthTermValidityExpired() {
        var creditPage = home.creditPayment();
        var currentYear = DataHelper.getSpecificYear(0);
        var monthTermValidityExpired = DataHelper.getSpecificMonth(-1);
        creditPage.fillCardPaymentForm(approvedCardNumber, monthTermValidityExpired, currentYear, validOwnerName, validCode);
        creditPage.errorCardTermValidity();
    }

    @Test
    @DisplayName("Кредит с некорректным значением карты - месяц 00")
    public void shouldCreditPaymentWithInvalidMonth() {
        var creditPage = home.creditPayment();
        var invalidMonth = DataHelper.getInvalidMonth();
        creditPage.fillCardPaymentForm(approvedCardNumber, invalidMonth, validYear, validOwnerName, validCode);
        creditPage.errorCardTermValidity();
    }

    @Test
    @DisplayName("Кредит с некорректным значением карты - поле месяц пустое")
    public void shouldCardPaymentWithEmptyMonth() {
        var creditPage = home.creditPayment();
        var emptyMonth = DataHelper.getEmptyField();
        creditPage.fillCardPaymentForm(approvedCardNumber, emptyMonth, validYear, validOwnerName, validCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит с истекшим сроком действия карты - год")
    public void shouldCreditPaymentWithYearTermValidityExpired() {
        var creditPage = home.creditPayment();
        var yearTermValidityExpired = DataHelper.getSpecificYear(-1);
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, yearTermValidityExpired, validOwnerName, validCode);
        creditPage.termValidityExpired();
    }

    @Test
    @DisplayName("Кредит с некорректным значением карты - год")
    public void shouldCreditPaymentWithInvalidYear() {
        var creditPage = home.creditPayment();
        var invalidYear = DataHelper.getSpecificYear(6);
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, invalidYear, validOwnerName, validCode);
        creditPage.errorCardTermValidity();
    }

    @Test
    @DisplayName("Кредит с некорректным значением карты - поле год пустое")
    public void shouldCreditPaymentWithEmptyYear() {
        var creditPage = home.creditPayment();
        var emptyYear = DataHelper.getEmptyField();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, emptyYear, validOwnerName, validCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по карте с именем владельца на кириллице")
    public void shouldRusLangNameCreditPayment() {
        var creditPage = home.creditPayment();
        var rusLangName = DataHelper.getRandomNameRus();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, rusLangName, validCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по карте с именем владельца, содержащим цифры")
    public void shouldDigitsNameCreditPayment() {
        var creditPage = home.creditPayment();
        var digitsName = DataHelper.getNumberName();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, digitsName, validCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по карте с именем владельца, содержащим спецсимволы")
    public void shouldSpecSymbolsNameCreditPayment() {
        var creditPage = home.creditPayment();
        var specSymbolsName = DataHelper.getSpecSymbolName();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, specSymbolsName, validCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по карте с именем владельца, содержащим более 100 символов")
    public void shouldMoreHundredSymbolsNameCreditPayment() {
        var creditPage = home.creditPayment();
        var hundredSymbolsName = DataHelper.getHundredSymbolsName();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, hundredSymbolsName, validCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по карте с некорректным именем владельца - поле пустое")
    public void shouldEmptyNameCreditPayment() {
        var creditPage = home.creditPayment();
        var emptyName = DataHelper.getEmptyField();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, emptyName, validCode);
        creditPage.emptyField();
    }

    @Test
    @DisplayName("Кредит по карте с некорректным CVV кодом - две цифры")
    public void shouldTwoDigitsCVVCreditPayment() {
        var creditPage = home.creditPayment();
        var twoDigitsCode = DataHelper.getNumberCVV(2);
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, twoDigitsCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по карте с некорректным CVV кодом - буквы")
    public void shouldLettersCVVCreditPayment() {
        var creditPage = home.creditPayment();
        var lettersCode = DataHelper.getRandomName();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, lettersCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по карте с некорректным CVV кодом - спецсимволы")
    public void shouldSpecSymbolsCVVCreditPayment() {
        var creditPage = home.creditPayment();
        var specSymbolsCode = DataHelper.getSpecSymbolName();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, specSymbolsCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по карте с некорректным CVV кодом - введено 000")
    public void shouldZerosCVVCreditPayment() {
        var creditPage = home.creditPayment();
        var zeroCode = DataHelper.getZeroCVV();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, zeroCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по карте с некорректным CVV кодом - поле пустое")
    public void shouldNullCVVCreditPayment() {
        var creditPage = home.creditPayment();
        var emptyCode = DataHelper.getEmptyField();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, emptyCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по карте с пустыми полями формы оплаты")
    public void shouldEmptyAllFieldsCreditPayment() {
        var creditPage = home.creditPayment();
        var emptyCardNumber = DataHelper.getEmptyField();
        var emptyMonth = DataHelper.getEmptyField();
        var emptyYear = DataHelper.getEmptyField();
        var emptyName = DataHelper.getEmptyField();
        var emptyCode = DataHelper.getEmptyField();
        creditPage.fillCardPaymentForm(emptyCardNumber, emptyMonth, emptyYear, emptyName, emptyCode);
        creditPage.errorFormat();
    }
}
