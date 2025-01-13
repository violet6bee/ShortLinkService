package org.example;

import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.UUID;

import static org.example.RandomShortLink.generateRandomString;

public class Main {
    private static Database database = new Database();
    private static Scanner scanner = new Scanner(System.in);
    private static int liveTimeLinkHour = 1;
    public static void main(String[] args) {


        System.out.println("create user - создать пользователя\n" +
                "insert link - вставить ссылку для сокращение\n" +
                "go to - перейти по короткой ссылке");

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("create user")) {
                UUID uuid  = database.createUser();
                System.out.println("Пользователь создан: " + uuid);
            } else if (input.equals("insert link")) {
                insertLink();
            } else if (input.equals("go to")) {
                LinkTo();
            } else {
                System.out.println("Моя твоя не понимать");
            }
        }
    }

    public static void insertLink() {
        try {
            System.out.println("Введите ссылку");
            String longLink = scanner.nextLine();

            String shortLink = "clck.ru/" + generateRandomString();

            System.out.println("Введите UUID");
            UUID userUuid = UUID.fromString(scanner.nextLine());

            long time = System.currentTimeMillis();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String formattedDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).format(formatter);
            System.out.println("Дата создания: " + formattedDate);

            System.out.println("Введите лимит переходов");
            int limit = scanner.nextInt();

            database.saveLink(longLink, shortLink, userUuid, time, limit);

            System.out.println("Данные сохранены. Короткая ссылка: " + shortLink);
        } catch (Exception e) {
            System.out.println("Вы сделали что-то неправильно.Попробуйте снова");
        }
    }

    public static void LinkTo() {
        try {
            System.out.println("Введите UUID");
            UUID userUuid = UUID.fromString(scanner.nextLine());
            System.out.println("Введите короткую ссылку");
            String shortLink = scanner.nextLine();
            Object[] link = database.getLink(userUuid, shortLink);

            long timeCreate = (long) link[1];
            long lifeTime =  System.currentTimeMillis() - timeCreate;
            long linkLiveTime = (long) liveTimeLinkHour * 60 * 60 * 1000;
            if (linkLiveTime < lifeTime) {
                System.out.println("Срок действия ссылки истек");
                database.deleteLink(userUuid, shortLink);
                return;
            }

            int limit = (int) link[2];
            if (limit > 1) {
                int newLimit = limit - 1;
                System.out.println("Осталось" + newLimit + "переходов");
                database.updateLimit(userUuid, newLimit, shortLink);
            } else if (limit == 1) {
                int newLimit = -1;
                System.out.println("Осталось 0 переходов");
                database.updateLimit(userUuid, newLimit, shortLink);
            } else if (limit == -1) {
                System.out.println("Посмотри мне в глаза...не бойся, я друг... " +
                        "но ты больше никогда не получишь свою ссылку");
                return;
            }

            String longLink = (String) link[0];
            try {
                Desktop.getDesktop().browse(new URI(longLink));
            } catch (IOException | URISyntaxException e) {
                System.out.println("Не удалось перейти по ссылке");
            }
        } catch (Exception e) {
            System.out.println("Вы сделали что-то неправильно.Попробуйте снова");
        }
    }
}