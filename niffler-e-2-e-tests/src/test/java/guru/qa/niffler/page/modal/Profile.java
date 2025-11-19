package guru.qa.niffler.page.modal;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.AllPeoplePage;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.ProfilePage;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class Profile {

    private final SelenideElement profile = $("[href='/profile']");
    private final SelenideElement friends = $("[href='/people/friends']");
    private final SelenideElement people = $("[href='/people/all']");
    private final SelenideElement signOut = $x("//*[contains(text(), 'Sign out')]");

    public ProfilePage clickOnProfile() {
        profile.click();
        return Selenide.page(ProfilePage.class);
    }

    public FriendsPage clickOnFriends() {
        friends.click();
        return Selenide.page(FriendsPage.class);
    }

    public AllPeoplePage clickOnAllPeople() {
        people.click();
        return Selenide.page(AllPeoplePage.class);
    }
}
