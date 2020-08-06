INSERT INTO roles (id, name)
VALUES (1, 'ROLE_USER');

-- users
INSERT INTO public.users (id, joined, password, username) -- kplich/P@ssw0rd2
VALUES (1, '2020-02-02 21:34:57.010243', '$2a$10$8AB9GaDXq7Cwq34l8qIp3OpZRAV.3IAYSy13yZ8KNhoL/H9FNusoW', 'kplich');
INSERT INTO public.users (id, joined, password, username) -- kplich2/P@ssw0rd2
VALUES (2, '2020-03-03 21:43:20.213819', '$2a$10$5p4ymMhRnReLXe5qxBx6letQ8AGatuN2QSBiCNU2sC7TAHCypYUWW', 'kplich2');
INSERT INTO public.users (id, joined, password, username) -- kplich3/P@ssw0rd3
VALUES (3, '2020-04-04 21:35:30.082832', '$2a$10$oCE.MaPe3SnqURnAHqUuq.Sl/U7/OSXLEQo3olfOaoW4MvTwIbqn6', 'kplich3');

-- user roles
INSERT INTO public.user_roles (user_id, role_id)
VALUES (1, 1);
INSERT INTO public.user_roles (user_id, role_id)
VALUES (2, 1);
INSERT INTO public.user_roles (user_id, role_id)
VALUES (3, 1);

INSERT INTO categories (id, name)
VALUES (1, 'House and Garden');
INSERT INTO categories (id, name)
VALUES (2, 'Electronics');
INSERT INTO categories (id, name)
VALUES (3, 'Fashion');

INSERT INTO public.items (id, added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES (1, '2020-05-05 21:40:10.096853', null, '1 Quick description of an open item in category House and Garden',
        1.2345, '1 Quick title that will have more than 10 characters', 'NEW', 1, 1);

INSERT INTO public.items (id, added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES (2, '2020-06-06 21:40:10.096853', '2020-06-07 21:37:00.420069',
        '2 Quick description of a closed item! in category Electronics',
        2.0000, '2 Quick title that will have more than 10 characters', 'USED', 2, 2);

INSERT INTO public.items (id, added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES (3, '2020-07-07 21:40:10.096853', null, '3 Quick description of an in category Fashion',
        3.3333, '3 Quick title that will have more than 10 characters', 'NOT_APPLICABLE', 3, 3);
