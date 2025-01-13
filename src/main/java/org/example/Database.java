package org.example;

import java.util.HashMap;
import java.util.UUID;

public class Database {
    // Таблица<Идентификация пользователя, таблица<короткая ссылка, Массив[длинная ссылка, время жизни ссылки, лимит переходов]>>
    private HashMap<UUID, HashMap<String, Object[]>> information = new HashMap<>();

    // создать пользователя
    public UUID createUser () {
        UUID uuid = UUID.randomUUID();
        HashMap<String, Object[]> userLinks = new HashMap<>();
        information.put(uuid, userLinks);
        return uuid;
    }

    // сохранение ссылки
    public void saveLink (String longLink, String shortLink, UUID userUuid, long linkCreatedDate, int limit) {
        HashMap<String, Object[]> links = information.get(userUuid);
        Object[] link = {longLink, linkCreatedDate, limit};
        links.put(shortLink, link);
    }

    // получить ссылку
    public Object[] getLink (UUID userUuid, String shortLink) {
        return information.get(userUuid).get(shortLink);
    }

    // удалить ссылку
    public boolean deleteLink (UUID userUuid, String shortLink) {
        HashMap<String, Object[]> links = information.get(userUuid);
        if (links.get(shortLink) == null) {
            return false;
        }
        links.remove(shortLink);
        return true;
    }

     // обновление и редактирование значения лимита ссылки
    public void updateLimit (UUID userUuid, int limit, String shortLink) {
        Object[] link = information.get(userUuid).get(shortLink);
        link[2] = limit;
    }

}

