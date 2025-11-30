package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.CreateCategoryExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({BrowserExtension.class, CreateCategoryExtension.class})
public class CategoryTest {

    private static final Config CFG = Config.getInstance();

    @User(
            username = "arcadehero",
            categories = @Category(
                    archived = true
            ))
    @Test
    @DisplayName("Проверка успешного отображения заархивированной категории в списке категорий")
    void archivedCategoryShouldBePresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("arcadehero", "12345");
        new MainPage()
                .verifyMainPageIsOpened()
                .clickOnProfileIcon()
                .clickOnProfile()
                .clickOnShowArchivedCheckbox()
                .verifyArchivedCategoryIsDisplayed(category.name());
    }

    @User(
            username = "arcadehero",
            categories = @Category(
                    archived = true
            ))
    @Test
    @DisplayName("Проверка отсутствия отображения заархивированной категории в списке категорий")
    void archivedCategoryShouldNotBePresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("arcadehero", "12345");
        new MainPage()
                .verifyMainPageIsOpened()
                .clickOnProfileIcon()
                .clickOnProfile()
                .verifyArchivedCategoryIsNotDisplayed(category.name());
    }

    @User(
            username = "arcadehero",
            categories = @Category()
    )
    @Test
    @DisplayName("Проверка успешного отображения активной категории в списке категорий")
    void activeCategoryShouldBePresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("arcadehero", "12345");
        new MainPage()
                .verifyMainPageIsOpened()
                .clickOnProfileIcon()
                .clickOnProfile()
                .verifyActiveCategoryIsDisplayed(category.name());
    }
}
