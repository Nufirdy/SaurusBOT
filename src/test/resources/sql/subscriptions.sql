INSERT INTO subscribers (id, is_private, locale)
VALUES (1, true, 'en_US'),
       (2, false, 'ru_RU');
INSERT INTO telegram_chat (chat_id, id)
VALUES (100, 1),
       (101, 2);
INSERT INTO subscriptions (id, subscription_type, enabled)
VALUES (1, 0, true),
       (2, 0, true);