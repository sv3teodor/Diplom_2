package utils;

import com.github.javafaker.Faker;

import java.util.Locale;

import static config.BaseConfig.LOCALE_FOR_TEST;

public class BasePojo {
    public static Faker faker = new Faker(new Locale(LOCALE_FOR_TEST));
}
