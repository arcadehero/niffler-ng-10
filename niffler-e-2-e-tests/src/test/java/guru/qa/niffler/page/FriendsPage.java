package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {

    private final SelenideElement friends = $("#friends");
    private final SelenideElement requests = $("#requests");

    public FriendsPage verifyFriendIsPresentInFriendsList(String username) {
        friends.$$("tr td").find(text(username)).shouldBe(visible);
        return this;
    }

    public FriendsPage verifyFriendsListIsEmpty() {
        friends.shouldNotBe(exist);
        return this;
    }

    public FriendsPage verifyIncomeFriendRequestIsPresent(String username) {
        requests.$$("tr td").find(text(username)).shouldBe(visible);
        return this;
    }
}
