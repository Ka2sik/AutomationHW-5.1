package ru.netology.delivery.test;

import org.openqa.selenium.Keys;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import ru.netology.delivery.data.DataGenerator;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class PositiveTest {

    @Test
    void ShouldReRegisterClientOnNewDate() {

        var client = DataGenerator.Registration.generateUser("ru");
        var daysBeforeFirstMeeting = 3;
        var firstMeetingDate = DataGenerator.generateDate(daysBeforeFirstMeeting);
        var daysBeforeSecondMeeting = 6;
        var secondMeetingDate = DataGenerator.generateDate(daysBeforeSecondMeeting);

        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue(client.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(client.getName());
        $("[data-test-id=phone] input").setValue(client.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDate))
                .shouldBe(visible);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $(byText("Запланировать")).click();
        $("[data-test-id=replan-notification] .notification__content")
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(visible);
        $("[data-test-id=replan-notification] button").click();
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + secondMeetingDate))
                .shouldBe(visible);
    }
}
