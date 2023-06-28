INSERT INTO map_info (id, name, valid, public, playable)
VALUES ('0212f42d-bb92-4c26-8059-5ca7a5d7cfb6', 'MAP A', true, true, true),
       ('a3943be7-7203-46e3-92dd-6b3f7e374023', 'MAP B', true, true, true),
       ('2286236f-060f-44ef-a0c6-bde1866ad30a', 'MAP C', true, true, true);

INSERT INTO player_accounts (id, display_name)
VALUES ('ed14ac85-1252-4cc7-8efd-49cd72938f9d', 'PLAYER A'),
       ('4bb5de94-1093-4b17-8776-afe9773fee4a', 'PLAYER B'),
       ('2343ca67-a77c-47c8-83bd-74f99c6f0a37', 'PLAYER C'),
       ('2a13aa7d-992d-4a7c-a3c5-d29b08b7f8cb', 'PLAYER D');

INSERT INTO map_record(id, account_id, map_info_id, medal, score, time, respawn_count, removed)
VALUES ('0212f42d-bb92-4c26-8059-5ca7a5d7cfb7', 'ed14ac85-1252-4cc7-8efd-49cd72938f9d', '0212f42d-bb92-4c26-8059-5ca7a5d7cfb6', 4, 0, 70000, 0, false),
       ('0212f42d-bb92-4c26-8059-5ca7a5d7cfb8', 'ed14ac85-1252-4cc7-8efd-49cd72938f9d', '0212f42d-bb92-4c26-8059-5ca7a5d7cfb6', 4, 0, 69900, 0, false),
       ('0212f42d-bb92-4c26-8059-5ca7a5d7cfb9', '4bb5de94-1093-4b17-8776-afe9773fee4a', '0212f42d-bb92-4c26-8059-5ca7a5d7cfb6', 4, 0, 71123, 0, false),
       ('0212f42d-bb92-4c26-8059-5ca7a5d7cfc0', '4bb5de94-1093-4b17-8776-afe9773fee4a', '0212f42d-bb92-4c26-8059-5ca7a5d7cfb6', 4, 0, 71200, 0, false),
       ('a3943be7-7203-46e3-92dd-6b3f7e374024', '2343ca67-a77c-47c8-83bd-74f99c6f0a37', 'a3943be7-7203-46e3-92dd-6b3f7e374023', 4, 0, 42701, 0, false),
       ('a3943be7-7203-46e3-92dd-6b3f7e374025', '4bb5de94-1093-4b17-8776-afe9773fee4a', 'a3943be7-7203-46e3-92dd-6b3f7e374023', 4, 0, 42700, 0, false);

INSERT INTO subscribers(id, is_private, locale)
VALUES (101, false, 'en_GB'),
       (100, false, 'en_GB');

INSERT INTO custom_leaderboards(id, subscriber_id)
VALUES (101, 101),
       (100, 100);

INSERT INTO custom_leaderboards_player_accounts(custom_leaderboard_id, player_account_id)
VALUES (101, 'ed14ac85-1252-4cc7-8efd-49cd72938f9d'),
       (101, '4bb5de94-1093-4b17-8776-afe9773fee4a'),
       (100, '4bb5de94-1093-4b17-8776-afe9773fee4a'),
       (100, '2343ca67-a77c-47c8-83bd-74f99c6f0a37');

INSERT INTO custom_leaderboards_map_info(custom_leaderboards_id, map_info_id)
VALUES (100, '0212f42d-bb92-4c26-8059-5ca7a5d7cfb6'),
       (100, 'a3943be7-7203-46e3-92dd-6b3f7e374023'),
       (101, '0212f42d-bb92-4c26-8059-5ca7a5d7cfb6'),
       (101, 'a3943be7-7203-46e3-92dd-6b3f7e374023');
