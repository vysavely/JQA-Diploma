## Дипломный проект. Автоматизация тестирования приложения "Путешествие дня"

План автоматизации тестирования - https://github.com/vysavely/JQA-Diploma/blob/main/Plan.md

## Запуск автотестов

Для запуска автотестов потребуются следующие установленные на ПК программы:
* IntelliJ IDEA
* Docker Desktop

## С использованием БД MySQL:
1. Склонировать репозиторий командой `git clone` https://github.com/vysavely/JQA-Diploma
2. Открыть проект в `IntelliJ IDEA`
3. Запустить docker контейнеры командой `docker-compose up --build`
4. Запустить SUT командой `java  "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar .\artifacts\aqa-shop.jar`
5. Запустить автотесты командой `./gradlew clean test '-Ddb.url=jdbc:mysql://localhost:3306/app'`
6. Открыть Allure отчёт в браузере командой `./gradlew allureServe`
7. Остановить docker контейнеры командой `docker-compose down`
8. Остановить работу **allureServe** в терминале сочетанием клавиш CTRL + C

## С использованием БД PostgreSQL:
1. Склонировать репозиторий командой `git clone` https://github.com/vysavely/JQA-Diploma
2. Открыть проект в `IntelliJ IDEA`
3. Запустить docker контейнеры командой `docker-compose up --build`
4. Запустить SUT командой `java  "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar .\artifacts\aqa-shop.jar`
5. Запустить автотесты командой `./gradlew clean test '-Ddb.url=jdbc:postgresql://localhost:5432/app'`
6. Открыть Allure отчёт в браузере командой `./gradlew allureServe`
7. Остановить docker контейнеры командой `docker-compose down`
8. Остановить работу **allureServe** в терминале сочетанием клавиш CTRL + C