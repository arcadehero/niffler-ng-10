package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class AllPeoplePage {

    private final ElementsCollection allPeopleTable = $$("#all > tr");

    public AllPeoplePage verifyOutcomeInvitationIsPresent(String username) {
        SelenideElement first = allPeopleTable.stream().findFirst().orElseThrow();
        first.shouldHave(text(username));
        first.shouldHave(text("Waiting..."));
        return this;
    }
}
