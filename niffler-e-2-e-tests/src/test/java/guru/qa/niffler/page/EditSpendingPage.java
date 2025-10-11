package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class EditSpendingPage {
  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement saveBtn = $("#save");

  public MainPage setNewSpendingDescription(String description) {
    descriptionInput.val(description);
    saveBtn.click();
    return new MainPage();
  }

  public MainPage save() {
    saveBtn.click();
    return new MainPage();
  }
}
