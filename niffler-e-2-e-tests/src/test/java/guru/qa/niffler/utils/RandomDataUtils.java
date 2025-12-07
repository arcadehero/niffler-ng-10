package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {

    private static final Faker faker = new Faker();

    public static String randomUsername() {
        return faker.name().firstName();
    }

    public static String randomName() {
        return faker.name().firstName();
    }

    public static String randomPassword(int min, int max) {
        return faker.internet().password(min, max);
    }

    public static String randomSurname() {
        return faker.name().lastName();
    }

    public static String randomCategoryName() {
        return faker.book().title();
    }

    public static String randomSentence(int wordCount) {
        return faker.commerce().promotionCode(wordCount);
    }
}
