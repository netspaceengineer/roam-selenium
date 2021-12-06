package app.roam.se.models.test.webentities;

import app.roam.se.models.reports.Result;
import app.roam.se.models.test.exceptions.TestFailureException;
import org.openqa.selenium.WebDriver;

public class CheckBox extends Clickable {
    public CheckBox(WebDriver driver, String name, String location) {
        super(driver, name, location);
    }

    public Result verifyChecked() {
        try {
            if (getElement().isSelected()) {
                return new Result("verifychecked", name, "");

            } else {
                return new Result("verifychecked", new TestFailureException(String.format("Expecting %s to be checked but it is unchecked", name)));
            }
        } catch (Exception ex) {
            return new Result("verifychecked", ex);
        }
    }

    public Result verifyUnchecked() {
        try {
            if (!getElement().isSelected()) {
                return new Result("verifyunchecked", name, "");

            } else {
                return new Result("verifyunchecked", new TestFailureException(String.format("Expecting %s to be checked but it is unchecked", name)));
            }
        } catch (Exception ex) {
            return new Result("verifyunchecked", ex);
        }

    }
}
