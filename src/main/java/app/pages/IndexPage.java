package app.pages;

import com.codeborne.selenide.SelenideElement;


import static com.codeborne.selenide.Selenide.$;

public class IndexPage extends BasePage {
    public SelenideElement searchField = $("form[action=\"/search\"]");

    public IndexPage(String pageUrl) {
        super(pageUrl);
    }

}
