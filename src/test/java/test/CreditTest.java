package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.HomePage;

import static com.codeborne.selenide.Selenide.open;

public class CreditTest {
    String approvedCardNumber = DataHelper.getCardApproved().getCardNumber();
    String declinedCardNumber = DataHelper.getCardDeclined().getCardNumber();
    String randomCardNumber = DataHelper.getRandomCorrectCardNumber();
    String validMonth = DataHelper.getRandomMonth(1);
    String validYear = DataHelper.getRandomYear(1);
    String validOwnerName = DataHelper.getRandomName();
    String validCode = DataHelper.getNumberCVV(3);

    @BeforeEach
    public void setUp() {
        open("http://localhost:8080");
    }

    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @AfterAll
    public static void shouldCleanBase() {
        SQLHelper.cleanBase();
    }

    @Test
    @DisplayName("Кредит по карте со статусом 'APPROVED'")
    public void shouldApprovedCreditPayment() {
        HomePage home = new HomePage();
        home.homePage();
        var creditPage = home.creditPayment();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditPage.bankApprovedOperation();
        Assertions.assertEquals("APPROVED", SQLHelper.getCreditPayment());
    }

    @Test
    @DisplayName("Кредит по карте со статусом 'DECLINED'")
    public void shouldDeclinedCreditPayment() {
        HomePage home = new HomePage();
        home.homePage();
        var creditPage = home.creditPayment();
        creditPage.fillCardPaymentForm(declinedCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditPage.bankDeclinedOperation();
        Assertions.assertEquals("DECLINED", SQLHelper.getCreditPayment());
    }

    @Test
    @DisplayName("Кредит с несуществующим корректным номером")
    public void shouldRandomCardNumberCreditPayment() {
        HomePage home = new HomePage();
        home.homePage();
        var creditPage = home.creditPayment();
        creditPage.fillCardPaymentForm(randomCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditPage.bankDeclinedOperation();
    }

    @Test
    @DisplayName("Кредит с некорректным коротким номером карты")
    public void shouldInvalidCardNumberCreditPayment() {
        HomePage home = new HomePage();
        home.homePage();
        var creditPage = home.creditPayment();
        var invalidCardNumber = DataHelper.getRandomShorterCardNumber();
        creditPage.fillCardPaymentForm(invalidCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит с незаполненным значением номера карты")
    public void shouldEmptyCardNumberCreditPayment() {
        HomePage home = new HomePage();
        home.homePage();
        var creditPage = home.creditPayment();
        var emptyCardNumber = DataHelper.getEmptyField();
        creditPage.fillCardPaymentForm(emptyCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по данным карты с истекшим сроком действия (Месяц)")
    public void shouldCreditPaymentWithMonthTermValidityExpired() {
        HomePage home = new HomePage();
        home.homePage();
        var creditPage = home.creditPayment();
        var currentYear = DataHelper.getRandomYear(0);
        var monthTermValidityExpired = DataHelper.getRandomMonth(-1);
        creditPage.fillCardPaymentForm(approvedCardNumber, monthTermValidityExpired, currentYear, validOwnerName, validCode);
        creditPage.errorCardTermValidity();
    }

    @Test
    @DisplayName("Кредит с некорректным значением карты - месяц 00")
    public void shouldCreditPaymentWithInvalidMonth() {
        HomePage home = new HomePage();
        home.homePage();
        var creditPage = home.creditPayment();
        var invalidMonth = DataHelper.getInvalidMonth();
        creditPage.fillCardPaymentForm(approvedCardNumber, invalidMonth, validYear, validOwnerName, validCode);
        creditPage.errorCardTermValidity();
    }

    @Test
    @DisplayName("Кредит с некорректным значением карты - поле месяц пустое")
    public void shouldCardPaymentWithEmptyMonth() {
        HomePage home = new HomePage();
        home.homePage();
        var creditPage = home.creditPayment();
        var emptyMonth = DataHelper.getEmptyField();
        creditPage.fillCardPaymentForm(approvedCardNumber, emptyMonth, validYear, validOwnerName, validCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит с истекшим сроком действия карты - год")
    public void shouldCreditPaymentWithYearTermValidityExpired() {
        HomePage home = new HomePage();
        home.homePage();
        var creditPage = home.creditPayment();
        var yearTermValidityExpired = DataHelper.getRandomYear(-1);
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, yearTermValidityExpired, validOwnerName, validCode);
        creditPage.termValidityExpired();
    }

    @Test
    @DisplayName("Кредит с некорректным значением карты - год")
    public void shouldCreditPaymentWithInvalidYear() {
        HomePage home = new HomePage();
        home.homePage();
        var creditPage = home.creditPayment();
        var invalidYear = DataHelper.getRandomYear(6);
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, invalidYear, validOwnerName, validCode);
        creditPage.errorCardTermValidity();
    }

    @Test
    @DisplayName("Кредит с некорректным значением карты - поле год пустое")
    public void shouldCreditPaymentWithEmptyYear() {
        HomePage home = new HomePage();
        home.homePage();
        var creditPage = home.creditPayment();
        var emptyYear = DataHelper.getEmptyField();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, emptyYear, validOwnerName, validCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по карте с именем владельца на кириллице")
    public void shouldRusLangNameCreditPayment() {
        HomePage home = new HomePage();
        home.homePage();
        var creditPage = home.creditPayment();
        var rusLangName = DataHelper.getRandomNameRus();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, rusLangName, validCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по карте с именем владельца, содержащим цифры")
    public void shouldDigitsNameCreditPayment() {
        HomePage home = new HomePage();
        home.homePage();
        var creditPage = home.creditPayment();
        var digitsName = DataHelper.getNumberName();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, digitsName, validCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по карте с именем владельца, содержащим спецсимволы")
    public void shouldSpecSymbolsNameCreditPayment() {
        HomePage home = new HomePage();
        home.homePage();
        var creditPage = home.creditPayment();
        var specSymbolsName = DataHelper.getSpecSymbolName();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, specSymbolsName, validCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по карте с именем владельца, содержащим более 100 символов")
    public void shouldMoreHundredSymbolsNameCreditPayment() {
        HomePage home = new HomePage();
        home.homePage();
        var creditPage = home.creditPayment();
        var hundredSymbolsName = DataHelper.getHundredSymbolsName();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, hundredSymbolsName, validCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по карте с некорректным именем владельца - поле пустое")
    public void shouldEmptyNameCreditPayment() {
        HomePage home = new HomePage();
        home.homePage();
        var creditPage = home.creditPayment();
        var emptyName = DataHelper.getEmptyField();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, emptyName, validCode);
        creditPage.emptyField();
    }

    @Test
    @DisplayName("Кредит по карте с некорректным CVV кодом - две цифры")
    public void shouldTwoDigitsCVVCreditPayment() {
        HomePage home = new HomePage();
        home.homePage();
        var creditPage = home.creditPayment();
        var twoDigitsCode = DataHelper.getNumberCVV(2);
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, twoDigitsCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по карте с некорректным CVV кодом - буквы")
    public void shouldLettersCVVCreditPayment() {
        HomePage home = new HomePage();
        home.homePage();
        var creditPage = home.creditPayment();
        var lettersCode = DataHelper.getRandomName();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, lettersCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по карте с некорректным CVV кодом - спецсимволы")
    public void shouldSpecSymbolsCVVCreditPayment() {
        HomePage home = new HomePage();
        home.homePage();
        var creditPage = home.creditPayment();
        var specSymbolsCode = DataHelper.getSpecSymbolName();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, specSymbolsCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по карте с некорректным CVV кодом - введено 000")
    public void shouldZerosCVVCreditPayment() {
        HomePage home = new HomePage();
        home.homePage();
        var creditPage = home.creditPayment();
        var zeroCode = DataHelper.getZeroCVV();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, zeroCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по карте с некорректным CVV кодом - поле пустое")
    public void shouldNullCVVCreditPayment() {
        HomePage home = new HomePage();
        home.homePage();
        var creditPage = home.creditPayment();
        var emptyCode = DataHelper.getEmptyField();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, emptyCode);
        creditPage.errorFormat();
    }

    @Test
    @DisplayName("Кредит по карте с пустыми полями формы оплаты")
    public void shouldEmptyAllFieldsCreditPayment() {
        HomePage home = new HomePage();
        home.homePage();
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
