package page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DebitPage {
    private SelenideElement heading = $(withText("Оплата по карте"));
    private SelenideElement cardNumberField = $("[placeholder='0000 0000 0000 0000']");
    private SelenideElement monthField = $("[placeholder='08']");
    private SelenideElement yearField = $("[placeholder='22']");
    private SelenideElement ownerField = $$(".input__inner").findBy(text("Владелец")).$(".input__control");
    private SelenideElement codeField = $("[placeholder='999']");
    private SelenideElement contButton = $$(".button__content").findBy(text("Продолжить"));

    private SelenideElement errorFormat = $$(".input__inner").findBy(text("Неверный формат"));
    private SelenideElement emptyField = $$(".input__inner").findBy(text("Поле обязательно для заполнения"));
    private SelenideElement termValidityExpired = $$(".input__inner").findBy(text("Истёк срок действия карты"));
    private SelenideElement errorCardTermValidity = $$(".input__inner").findBy(text("Неверно указан срок действия карты"));
    private SelenideElement bankDeclinedOperation = $$(".notification__content").findBy(text("Ошибка! Банк отказал в проведении операции."));
    private SelenideElement bankApprovedOperation = $$(".notification__content").findBy(text("Операция одобрена Банком."));


    public DebitPage() {
        heading.shouldBe(visible);
    }

    public void fillCardPaymentForm(String cardNumber, String month, String year, String owner, String code) {
        cardNumberField.setValue(cardNumber);
        monthField.setValue(month);
        yearField.setValue(year);
        ownerField.setValue(owner);
        codeField.setValue(code);
        contButton.click();
    }

    public void bankApprovedOperation() {
        bankApprovedOperation.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void bankDeclinedOperation() {
        bankDeclinedOperation.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void errorFormat() {
        errorFormat.shouldBe(visible);
    }

    public void emptyField() {
        emptyField.shouldBe(visible);
    }

    public void termValidityExpired() {
        termValidityExpired.shouldBe(visible);
    }

    public void errorCardTermValidity() {
        errorCardTermValidity.shouldBe(visible);
    }

//    public void cleanFields() {
//        cardNumberField.doubleClick().sendKeys(Keys.BACK_SPACE);
//        monthField.doubleClick().sendKeys(Keys.BACK_SPACE);
//        yearField.doubleClick().sendKeys(Keys.BACK_SPACE);
//        ownerField.doubleClick().sendKeys(Keys.BACK_SPACE);
//        codeField.doubleClick().sendKeys(Keys.BACK_SPACE);
//    }
}
