package com.treskunov.editor.functional;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.By.id;

public final class Collaborator {

    private WebDriver client;

    public Collaborator() {
        //TODO Use Chrome driver
        this.client = new HtmlUnitDriver(true);
        client.get("http://localhost:8080");
    }

    public void typeText(String text) {
        client.findElement(id("editor")).sendKeys(text);
    }

    public void quit() {
        client.quit();
    }

    public void ensureTextBecame(final String expected) {
        (new WebDriverWait(client, 1)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(id("editor")).getText().equals(expected);
            }
        });
    }
}
