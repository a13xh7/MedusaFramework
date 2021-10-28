import medusa.Medusa;
import org.testng.annotations.Test;

public class TestExamples extends A_BaseTest {

    @Test
    public void testFullPage() {
        app.indexPage.open();
        app.preparePageForScreenshot();
        Medusa.testPage("index_page");
    }

    @Test
    public void testFullPageOnlySelectedBreakpoints() {
        app.indexPage.open();
        app.preparePageForScreenshot();
        Medusa.testPage("index_page_breakpoints", "1920", "768"); // only values from "breakpoints" setting
    }

    @Test
    public void testElement() {
        app.indexPage.open();
        Medusa.testElement("search_field", app.indexPage.searchField.getWrappedElement(), "1920");
    }

}
