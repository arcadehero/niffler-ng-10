package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class LoginPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement submitBtn = $("#login-button");
    public final SelenideElement registerBtn = $("#register-button");
    public final SelenideElement errorMessage = $(".form__error");

    public LoginPage login(String username, String password) {
        usernameInput.val(username);
        passwordInput.val(password);
        submitBtn.click();
        return this;
    }

    public SignUpPage clickCreateNewAccountButton() {
        registerBtn.shouldBe(visible).click();
        return page(SignUpPage.class);
    }

    public LoginPage verifyErrorMessageIsDisplayed() {
        errorMessage.shouldBe(visible);
        return this;
    }


}
