<div align="center">

<h3 align="center">Дипломный проект часть 2</h3>
</div>

<!-- ABOUT THE PROJECT -->

## Описание проекта

В проекте реализованы следующие тесты API:

- Создание уникального пользователя;
- Создание пользователя, который уже зарегистрирован;
- Создать пользователя и не заполнить одно из обязательных полей.
- Логин под существующим пользователем,
- Логин с неверным логином и паролем.
- Изменение данных пользователя с авторизацией и без.
- Создание заказа с авторизацией и без.
- Создание заказа с ингредиентами и без.
- Создание заказа с неверным хешем ингредиентов.
- Получение заказов конкретного пользователя с авторизацией и без:

Создан отчет allure.

## Установка

Для установки необходимо клонировать дистрибутив

   ```sh
   git clone https://github.com/sv3teodor/Diplom_2.git
   ```

## Запуск

ДЛя запуска тестов выполните следующую команду:

```sh
mvn clean test
```

## Зависимости

В проекте используются следующие технологии:

- Java 11
- JUnit 4.13.2
- maven 3.8.6
- rest-assured 5.3.8
- allure 2.9.0
- JavaFaker 0.15

