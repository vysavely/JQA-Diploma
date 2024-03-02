package page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Condition.visible;

public class HomePage {
    private SelenideElement heading = $(withText("Путешествие дня"));
    private SelenideElement buyButton = $(withText("Купить"));
    private SelenideElement creditButton = $(withText("Купить в кредит"));

    public void homePage() {
        heading.shouldBe(visible);
    }

    public DebitPage debitPayment() {
        buyButton.click();
        return new DebitPage();
    }

    public CreditPage creditPayment() {
        creditButton.click();
        return new CreditPage();
    }
}
