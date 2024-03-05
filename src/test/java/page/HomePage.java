package page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class HomePage {
    private SelenideElement heading = $(withText("Путешествие дня"));
    private SelenideElement buyButton = $(withText("Купить"));
    private SelenideElement creditButton = $(withText("Купить в кредит"));

    public DebitPage debitPayment() {
        buyButton.click();
        return new DebitPage();
    }

    public CreditPage creditPayment() {
        creditButton.click();
        return new CreditPage();
    }
}
