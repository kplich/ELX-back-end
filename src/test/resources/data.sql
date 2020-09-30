INSERT INTO roles (id, name)
VALUES (1, 'ROLE_USER');

-- users
INSERT INTO public.users (id, ethereum_address, joined, password, username)
VALUES
    -- kplich/P@ssw0rd2
    (1, '0xc1912fee45d61c87cc5ea59dae31190fffff232d', '2020-02-02 21:34:57.010243', '$2a$10$8AB9GaDXq7Cwq34l8qIp3OpZRAV.3IAYSy13yZ8KNhoL/H9FNusoW', 'kplich'),
    -- kplich2/P@ssw0rd2
    (2, '0x06012c8cf97bead5deae237070f9587f8e7a266d', '2020-03-03 21:43:20.213819', '$2a$10$5p4ymMhRnReLXe5qxBx6letQ8AGatuN2QSBiCNU2sC7TAHCypYUWW', 'kplich2'),
    -- kplich3/P@ssw0rd3
    (3, '0x5e97870f263700f46aa00d967821199b9bc5a120', '2020-04-04 21:35:30.082832', '$2a$10$oCE.MaPe3SnqURnAHqUuq.Sl/U7/OSXLEQo3olfOaoW4MvTwIbqn6', 'kplich3'),
    -- kplich4/P@sswo0rd4; no ethereum address provided!
    (4, null, '2020-05-05 00:00:00', '$2a$10$oCE.MaPe3SnqURnAHqUuq.Sl/U7/OSXLEQo3olfOaoW4MvTwIbqn6', 'kplich4');

-- user roles
INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1),
       (2, 1),
       (3, 1);

INSERT INTO categories (id, name)
VALUES (1, 'House and Garden'),
       (2, 'Electronics'),
       (3, 'Fashion');

INSERT INTO items (id, added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES (1, '2020-05-05 21:40:10.096853', null, '1 Quick description of an open item in category House and Garden',
        1.2345, '1 Quick title that will have more than 10 characters', 'NEW', 1, 1),
       (2, '2020-06-06 21:40:10.096853', '2020-06-07 21:37:00.420069',
        '2 Quick description of a closed item! in category Electronics',
        2.0000, '2 Quick title that will have more than 10 characters', 'USED', 2, 2),
       (3, '2020-07-07 21:40:10.096853', null, '3 Quick description of an in category Fashion',
        3.3333, '3 Quick title that will have more than 10 characters', 'NOT_APPLICABLE', 3, 3);

-- items for conversation tests, added by user with id 1 (kplich1)
INSERT INTO items (id, added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES (4, '2020-05-05 21:40:10.096853', null, 'First item for testing conversations, item 4',
        1.01, 'Testing conversations, item 4', 'NEW', 1, 1),
       (5, '2020-05-05 21:40:10.096853', null, 'Second item for testing conversations, item 5',
        1.01, 'Testing conversations, item 5', 'NEW', 1, 1),
       (6, '2020-05-05 21:40:10.096853', null, 'Third item for testing conversations, item 6',
        1.01, 'Testing conversations, item 6', 'NEW', 1, 1),
       (7, '2020-05-05 21:40:10.096853', null, 'Fourth item for testing conversations, item 7',
        1.01, 'Testing conversations, item 7', 'NEW', 1, 1),
       (8, '2020-05-05 21:40:10.096853', '2020-05-05 21:45:10.096853', 'Fifth item for testing conversations, item 8',
        1.01, 'Testing conversations, item 8', 'NEW', 1, 1),
       (9, '2020-05-05 21:40:10.096853', null, 'Sixth item for testing conversations, item 9',
        1.01, 'Testing conversations, item 9', 'NEW', 1, 1);


-- conversation for tests, most with user with id 2 (kplich2)
INSERT INTO conversations (id, interested_user_id, item_id)
VALUES (1, 2, 4),
       (2, 2, 6),
       (3, 2, 7),
       (4, 3, 7),
       (5, 2, 8),
       (6, 2, 9);

-- conversation about first item
INSERT INTO messages (id, conversation_id, content, sender_id, sent_on, offer_id)
VALUES (1, 1, 'First message', 2, '2020-05-05 23:40:10.096853', null),
       (2, 1, 'First reply', 1, '2020-05-05 23:45:10.096853', null),
       (3, 1, 'Another message', 2, '2020-05-05 23:50:10.096853', null);

-- conversation about third item
INSERT INTO messages (id, conversation_id, content, sender_id, sent_on, offer_id)
VALUES (4, 2, 'Can I send an offer?', 2, '2020-05-05 23:40:10.096853', null),
       (5, 2, 'Sure thing!', 1, '2020-05-05 23:45:10.096853', null);

INSERT INTO offers (id, price, offer_status, contract_address) VALUES (1, 1.0, 'AWAITING', null);
INSERT INTO plain_advance_offers (advance, id) VALUES (0.50, 1);

INSERT INTO messages (id, conversation_id, content, sender_id, sent_on, offer_id)
VALUES (6, 2, 'There I go!', 2, '2020-05-05 23:40:10.096853', 1);

-- conversations about fourth item
INSERT INTO offers (id, price, offer_status, contract_address) VALUES (2, 1.0, 'AWAITING', null);
INSERT INTO plain_advance_offers(advance, id) VALUES (0.67, 2);

INSERT INTO offers (id, price, offer_status, contract_address) VALUES (3, 1.5, 'AWAITING', null);
INSERT INTO plain_advance_offers (advance, id) VALUES (1.0, 3);

INSERT INTO messages (id, conversation_id, content, sender_id, sent_on, offer_id)
VALUES (7, 3, 'My offer', 2, '2020-05-05 23:40:10.096853', 2),
       (8, 4, 'I''m sure mine''s better :P', 3, '2020-05-05 23:45:10.096853', 3);

-- conversation about fifth item
INSERT INTO messages (id, conversation_id, content, sender_id, sent_on, offer_id)
VALUES (9, 5, 'Message to a closed item', 2, '2020-05-05 21:42:10.096853', null);

-- conversation about sixth item
INSERT INTO offers (id, price, offer_status, contract_address) VALUES (4, 1.0, 'CANCELLED', null);
INSERT INTO plain_advance_offers(advance, id) VALUES (0.67, 4);

INSERT INTO offers (id, price, offer_status, contract_address) VALUES (5, 1.5, 'DECLINED', null);
INSERT INTO plain_advance_offers(advance, id) VALUES (1.0, 5);

-- conversation about sixth item
INSERT INTO messages (id, conversation_id, content, sender_id, sent_on, offer_id)
VALUES (10, 6, 'A cancelled offer', 2, '2020-05-05 21:42:10.096853', 4),
       (11, 6, 'A declined offer', 2, '2020-05-05 21:43:10.096853', 5)

