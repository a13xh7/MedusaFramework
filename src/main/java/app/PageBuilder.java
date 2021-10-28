package app;

import app.pages.*;

public class PageBuilder {

    public static IndexPage buildIndexPage() {
        return new IndexPage("/");
    }

}
