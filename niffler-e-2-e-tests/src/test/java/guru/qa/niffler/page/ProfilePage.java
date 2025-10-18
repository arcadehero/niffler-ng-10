package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {

    private final SelenideElement showArchivedCheckbox = $("[type=checkbox]");
    private final ElementsCollection archivedCategories = $$(".MuiChip-colorDefault>.MuiChip-label");
    private final ElementsCollection activeCategories = $$(".MuiChip-colorPrimary>.MuiChip-label");

    public ProfilePage clickOnShowArchivedCheckbox() {
        showArchivedCheckbox.click();
        return this;
    }

    public ProfilePage verifyActiveCategoryIsDisplayed(String categoryName) {
        activeCategories.findBy(Condition.text(categoryName))
                .shouldBe(visible);
        return this;
    }

    public ProfilePage verifyArchivedCategoryIsDisplayed(String categoryName) {
        archivedCategories.findBy(Condition.text(categoryName))
                .shouldBe(visible);
        return this;
    }

    public ProfilePage verifyArchivedCategoryIsNotDisplayed(String categoryName) {
        archivedCategories.filter(text(categoryName)).shouldHave(size(0));
        return this;
    }
}
