package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.UserType;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.UserType.Type.*;

@ExtendWith({BrowserExtension.class, UsersQueueExtension.class})
public class FriendsTest {

    private static final Config CFG = Config.getInstance();
    private LoginPage loginPage;

    @BeforeEach
    void setUp() {
        loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);
    }

    @Test
    @DisplayName("Проверка наличия друга в таблице друзей")
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {
        new LoginPage()
                .login(user.username(), user.password());
        new MainPage()
                .clickOnProfileIcon()
                .clickOnFriends()
                .verifyFriendIsPresentInFriendsList(user.friend());
    }

    @Test
    @DisplayName("Проверка отсутствия друзей у нового пользователя")
    void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) UsersQueueExtension.StaticUser user) {
        new LoginPage()
                .login(user.username(), user.password());
        new MainPage()
                .clickOnProfileIcon()
                .clickOnFriends()
                .verifyFriendsListIsEmpty();
    }

    @Test
    @DisplayName("Проверка наличия входящего запроса на дружбу y пользователя")
    void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) UsersQueueExtension.StaticUser user) {
        new LoginPage()
                .login(user.username(), user.password());
        new MainPage()
                .clickOnProfileIcon()
                .clickOnFriends()
                .verifyIncomeFriendRequestIsPresent(user.income());
    }

    @Test
    @DisplayName("Проверка наличия исходящего запроса на дружбу y пользователя")
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) UsersQueueExtension.StaticUser user) {
        new LoginPage()
                .login(user.username(), user.password());
        new MainPage()
                .clickOnProfileIcon()
                .clickOnAllPeople()
                .verifyOutcomeInvitationIsPresent(user.outcome());
    }
}
