# Medusa Framework - visual regression testing (TestNG, Selenide) 

**Features:**

* Capturing the entire page
* Capture the Web Element
* Screenshot comparison
* Report with expected, actual, difference and gif images

## Test execution

You can run tests using next command:

`mvn test -Dclean=0 -Dheadless=0`

parameters:
- **-Dheadless** - 0 or 1. run browser in headless mode. Default value == **0**
- **-Dclean** - 0 or 1. remove expected screenshots and save actual ones as expected screenshots. Default value == **0**

All parameters are optional.

**Configuration**

- `config/visual.properties` - configuration file
- `default breakpoints` - 1920,768,360 
- `allowableDiffSize` - allowable number of different pixels between two screenshots

**Default breakpoints**

- **Desktop:** breakpoint >= 1024px
- **Tablet:** breakpoint >= 768px AND breakpoint < 1024px
- **Mobile**: breakpoint = < 768px

### Screenshot name format:

- format - **test_name__env_browser_breakpoint.png**
- example - **index_page__dev_chrome_1920.png**

**Some code examples:**

```
Medusa.testPage("balance_page");
Medusa.testElement("rating_filters", selenide_element);
Medusa.testElement("copied_tip", selenide_element, "1920"); 
```
  
```
Screenshoter.shootPage(driver);
Screenshoter.shootElement(driver, element);
```

```
Screenshot diff = Screenshoter.generateDifferenceImage(
        "/home/alex/at/bettervisual/screenshots/test_exp.png",
        "/home/alex/at/bettervisual/screenshots/test_act.png"
);

diff.getDiffSize();
diff.save("/home/alex/at/bettervisual/screenshots/a__result_diff.png");
```

```
Screenshoter.generateGifImage(
        "/home/alex/at/bettervisual/screenshots/test_exp.png",
       "/home/alex/at/bettervisual/screenshots/test_act.png",
        "/home/alex/at/bettervisual/screenshots/a__result_diff.png",
        "/home/alex/at/bettervisual/screenshots/a_gif.gif"
);
```    
    
