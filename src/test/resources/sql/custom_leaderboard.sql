INSERT INTO subscribers(id, is_private, locale)
VALUES (101, false, 'en_GB'),
       (100, false, 'en_GB');

INSERT INTO custom_leaderboards(id, subscriber_id)
VALUES (101, 101),
       (100, 100);

INSERT INTO player_accounts (id, display_name)
VALUES ('ed14ac85-1252-4cc7-8efd-49cd72938f9d', 'PLAYER A'),
       ('4bb5de94-1093-4b17-8776-afe9773fee4a', 'PLAYER B'),
       ('2343ca67-a77c-47c8-83bd-74f99c6f0a37', 'PLAYER C'),
       ('2a13aa7d-992d-4a7c-a3c5-d29b08b7f8cb', 'PLAYER D');

INSERT INTO custom_leaderboards_player_accounts(custom_leaderboard_id, player_account_id)
VALUES (101, 'ed14ac85-1252-4cc7-8efd-49cd72938f9d'),
       (101, '4bb5de94-1093-4b17-8776-afe9773fee4a'),
       (100, '4bb5de94-1093-4b17-8776-afe9773fee4a'),
       (100, '2343ca67-a77c-47c8-83bd-74f99c6f0a37'),
       (100, '2a13aa7d-992d-4a7c-a3c5-d29b08b7f8cb');
