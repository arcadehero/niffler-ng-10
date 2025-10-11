package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class SignUpPage {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement signUpBtn = $("#register-button");
    private final SelenideElement signInBnt = $(".form_sign-in");
    private final SelenideElement passwordErrorMessage = $x("//input[@id='password']/following-sibling::span");
    private final SelenideElement usernameErrorMessage = $x("//input[@id='username']/following-sibling::span");

    public SignUpPage createNewUser(String username, String password){
        usernameInput.val(username);
        passwordInput.val(password);
        passwordSubmitInput.val(password);
        signUpBtn.shouldBe(visible).click();
        return this;
    }
    public LoginPage clickOnSignInButton(){
        signInBnt.shouldBe(visible).click();
        return Selenide.page(LoginPage.class);
    }

    public SignUpPage verifyUsernameErrorMessageIsDisplayed(){
        usernameErrorMessage.shouldBe(visible);
        return this;
    }

    public SignUpPage verifyPasswordErrorMessageIsDisplayed(){
        passwordErrorMessage.shouldBe(visible);
        return this;
    }
}
