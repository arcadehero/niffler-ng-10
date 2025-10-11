package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class RegistrationTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @DisplayName("Проверка успешной регистрации нового пользователя")
    void shouldRegisterNewUser() {
        String username = new Faker().name().username();
        String password = new Faker().internet().password(3, 12);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccountButton()
                .createNewUser(username, password)
                .clickOnSignInButton()
                .login(username, password);
        new MainPage()
                .verifyMainPageIsOpened();
    }

    @Test
    @DisplayName("Проверка создания пользователя с именем меньше минимального значения")
    void shouldFailShortUsername() {
        String username = "12";
        String password = new Faker().internet().password(3, 12);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccountButton()
                .createNewUser(username, password)
                .verifyUsernameErrorMessageIsDisplayed();
    }

    @Test
    @DisplayName("Проверка создания пользователя с именем больше максимального значения")
    void shouldFailLongtUsername() {
        String username = "Doexxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        String password = new Faker().internet().password(3, 12);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccountButton()
                .createNewUser(username, password)
                .verifyUsernameErrorMessageIsDisplayed();
    }

    @Test
    @DisplayName("Проверка создания пользователя с паролем меньше минимального значения")
    void shouldFailShortPass() {
        String username = new Faker().name().username();
        String password = new Faker().internet().password(1, 2);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccountButton()
                .createNewUser(username, password)
                .verifyPasswordErrorMessageIsDisplayed();
    }

    @Test
    @DisplayName("Проверка создания пользователя с паролем больше максимального значения")
    void shouldFailLongPass() {
        String username = new Faker().name().username();
        String password = new Faker().internet().password(13, 14);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccountButton()
                .createNewUser(username, password)
                .verifyPasswordErrorMessageIsDisplayed();
    }

    @Test
    @DisplayName("Проверка создания пользователя с уже существующим username")
    void shouldFailWithAlreadyExistingUsername() {
        String username = new Faker().name().username();
        String password = new Faker().internet().password(3, 12);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccountButton()
                .createNewUser(username, password)
                .clickOnSignInButton()
                .clickCreateNewAccountButton()
                .createNewUser(username, password)
                .verifyUsernameErrorMessageIsDisplayed();
    }

    @Test
    @DisplayName("Проверка логина с неверными кредами")
    void shouldStayOnLoginPageIfUsernameIsWrong() {
        String username = new Faker().name().username();
        String password = new Faker().internet().password(3, 12);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccountButton()
                .createNewUser(username, password)
                .clickOnSignInButton()
                .login(username + "1", password + "1")
                .verifyErrorMessageIsDisplayed();
    }
}
