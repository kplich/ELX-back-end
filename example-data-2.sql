-- roles
INSERT INTO roles
VALUES (1, 'ROLE_USER');

-- users
-- jasmine03/maybE4$$$
INSERT INTO public.users (id, ethereum_address, joined, password, username)
VALUES (5, '0x0C9944bf5150c20263bb1c9eA51509a94AAE61A0', '2020-11-15 18:13:08.025977',
        '$2a$10$pEFeMyMNs1rU8XVzzrKSoedwfzRSbhtmL8jCZHYfq/5HtraUb/sQ2', 'jasmine03');
-- maryjann/Maryjann1!
INSERT INTO public.users (id, ethereum_address, joined, password, username)
VALUES (6, '0x5d69Bb29ACb5d65218E4Ffc11a748d557e5eF1E4', '2020-11-15 18:24:40.486842',
        '$2a$10$8BomO1v2CWDa66HcF8Jo2OHS22jX5NUvks09vpmfC7p7uSquQr8D2', 'maryjann');
-- jerrybumbleberry/Jerrry1!
INSERT INTO public.users (id, ethereum_address, joined, password, username)
VALUES (4, '0xE400c7222FfDE8134c5b4162431A4906668935c7', '2020-11-15 18:05:42.404004',
        '$2a$10$I49.bkzU3oCr64.7NLXalO78H0B2j5VGKnehneCdcqIfD5TNaYu3e', 'jerrybumbleberry');

-- user roles
INSERT INTO public.user_roles (user_id, role_id)
VALUES (4, 1);
INSERT INTO public.user_roles (user_id, role_id)
VALUES (5, 1);
INSERT INTO public.user_roles (user_id, role_id)
VALUES (6, 1);

-- categories
INSERT INTO categories
VALUES (1, 'House and Garden');
INSERT INTO categories
VALUES (2, 'Electronics');
INSERT INTO categories
VALUES (3, 'Fashion');
INSERT INTO categories
VALUES (4, 'Pets and Animals');
INSERT INTO categories
VALUES (5, 'Children');
INSERT INTO categories
VALUES (6, 'Sports and Outdoors');
INSERT INTO categories
VALUES (7, 'Music and Film');
INSERT INTO categories
VALUES (8, 'Education');

-- items
INSERT INTO public.items (id, added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES (3, '2020-11-15 18:11:30.584057', null, 'a real deal, an original piano from 1880 - all keys working great!',
        1.7444, 'SWISS Piano BURGER&JAKOBY, 1880', 'USED', 4, 7);
INSERT INTO public.items (id, added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES (4, '2020-11-15 18:17:32.099153', null, 'we bought the game with the console for children, but they got bored quickly ...
unfortunately we dont have the box too', 0.0582, 'Nintendo Switch Just Dance 2020', 'NEW', 5, 2);
INSERT INTO public.items (id, added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES (5, '2020-11-15 18:22:00.219090', null, '720 litres (200x60x60)/alu diversa cover, module lighting, metal cupboard
available after fish sold or together with fish
', 1.4780, 'Aquarium set (720 litres)', 'USED', 5, 4);
INSERT INTO public.items (id, added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES (6, '2020-11-15 18:25:48.767058', null, 'Very comfortable socks with bears
Small size 36-38
NEW!', 0.0052, 'Cute socks with bears', 'NEW', 6, 3);

-- item photos
INSERT INTO public.item_photos (id, url, item_id)
VALUES (7,
        'https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1605460620715?alt=media&token=87b243dc-05cb-41a0-a017-66c1837a2aac',
        4);
INSERT INTO public.item_photos (id, url, item_id)
VALUES (8,
        'https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1605460912648?alt=media&token=39053190-ca35-479e-928b-2b86b8ec4fd8',
        5);
INSERT INTO public.item_photos (id, url, item_id)
VALUES (9,
        'https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1605460912651?alt=media&token=c690543e-a4c3-4860-a057-3f1611e921ef',
        5);
INSERT INTO public.item_photos (id, url, item_id)
VALUES (10,
        'https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1605460912650?alt=media&token=13d4e900-6bdd-4ebc-ab21-cb05953db461',
        5);
INSERT INTO public.item_photos (id, url, item_id)
VALUES (11,
        'https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1605461144016?alt=media&token=11caee65-a9b2-426f-baca-853fddb52147',
        6);
INSERT INTO public.item_photos (id, url, item_id)
VALUES (12,
        'https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1605461144024?alt=media&token=7ef8a7f0-7761-450f-8a34-f202d453cea6',
        6);
