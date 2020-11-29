SET REFERENTIAL_INTEGRITY FALSE;
truncate table double_advance_offers restart identity;
truncate table item_photos restart identity;
truncate table messages restart identity;
truncate table conversations restart identity;
truncate table items restart identity;
truncate table categories restart identity;
truncate table plain_advance_offers restart identity;
truncate table offers restart identity;
truncate table user_roles restart identity;
truncate table roles restart identity;
truncate table users restart identity;
SET REFERENTIAL_INTEGRITY TRUE;

-- roles
INSERT INTO roles
VALUES (1, 'ROLE_USER');

-- users
-- jasmine03/maybE4$$$
INSERT INTO public.users (id, ethereum_address, joined, password, username)
VALUES (1, '0xaa996165f901FEd36247D6606FC44D9e588e5bCB', '2020-11-15 18:13:08.025977',
        '$2a$10$pEFeMyMNs1rU8XVzzrKSoedwfzRSbhtmL8jCZHYfq/5HtraUb/sQ2', 'jasmine03');
-- maryjann/Maryjann1!
INSERT INTO public.users (id, ethereum_address, joined, password, username)
VALUES (2, '0x00ae8B2b0e60444b22cdDbF1C355562f1E227e45', '2020-11-15 18:24:40.486842',
        '$2a$10$8BomO1v2CWDa66HcF8Jo2OHS22jX5NUvks09vpmfC7p7uSquQr8D2', 'maryjann');
-- jerrybumbleberry/Jerrry1!
INSERT INTO public.users (id, ethereum_address, joined, password, username)
VALUES (3, '0x1fD25f0C2d74c39b509174a354506cbb33B5A368', '2020-11-15 18:05:42.404004',
        '$2a$10$I49.bkzU3oCr64.7NLXalO78H0B2j5VGKnehneCdcqIfD5TNaYu3e', 'jerrybumbleberry');
-- NewKidOnTheBlock/NewKidd0)
INSERT INTO public.users (id, ethereum_address, joined, password, username)
VALUES (4, '0x50f6Ea4198a24358A10CCcb16D8E58f91769bC87', '2020-11-29 03:41:33.219393',
        '$2a$10$Bv3dB.6CSThhFBl0J/HYVuluI2FeCEeabPmQLhNNAAAxU4h2QDQxC', 'NewKidOnTheBlock');

-- user roles
INSERT INTO public.user_roles (user_id, role_id)
VALUES (1, 1);
INSERT INTO public.user_roles (user_id, role_id)
VALUES (2, 1);
INSERT INTO public.user_roles (user_id, role_id)
VALUES (3, 1);
INSERT INTO public.user_roles (user_id, role_id)
VALUES (4, 1);

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
VALUES (1, '2020-11-29 03:48:31.425240', null, 'only used twice, perfect condition (no pun intended!)', 0.4760,
        'Portable air conditioner, brand new', 'NEW', 4, 1);
INSERT INTO public.items (id, added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES (2, '2020-11-29 03:52:16.221150', null, '120 cm x 80 cm x 100 cm
it''s possible to send the kennel by post!', 0.5930, 'Dog kennel', 'USED', 4, 4);
INSERT INTO public.items (id, added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES (3, '2020-11-15 18:11:30.584057', null, 'a real deal, an original piano from 1880 - all keys working great!',
        1.7444, 'SWISS Piano BURGER&JAKOBY, 1880', 'USED', 1, 7);
INSERT INTO public.items (id, added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES (4, '2020-11-15 18:17:32.099153', null, 'we bought the game with the console for children, but they got bored quickly ...
unfortunately we dont have the box too', 0.0582, 'Nintendo Switch Just Dance 2020', 'NEW', 2, 2);
INSERT INTO public.items (id, added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES (5, '2020-11-15 18:22:00.219090', null, '720 litres (200x60x60)/alu diversa cover, module lighting, metal cupboard
available after fish sold or together with fish
', 1.4780, 'Aquarium set (720 litres)', 'USED', 2, 4);
INSERT INTO public.items (id, added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES (6, '2020-11-15 18:25:48.767058', null, 'Very comfortable socks with bears
Small size 36-38
NEW!', 0.0052, 'Cute socks with bears', 'NEW', 2, 3);
INSERT INTO public.items (id, added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES (7, '2020-11-29 04:22:17.870117', null,
        'a bit worn out, but still in a good condition. very hard to find nowadays!', 0.1220,
        'New Balance Shoes, Size 39', 'USED', 4, 3);


-- item photos
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1606618107364?alt=media&token=5b3fedb8-c292-4040-8027-b3a5410b08b7',
        1);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1606618333377?alt=media&token=a0969322-1757-4381-ac7b-82c2324dfb43',
        2);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1605460620715?alt=media&token=87b243dc-05cb-41a0-a017-66c1837a2aac',
        4);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1605460912648?alt=media&token=39053190-ca35-479e-928b-2b86b8ec4fd8',
        5);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1605460912651?alt=media&token=c690543e-a4c3-4860-a057-3f1611e921ef',
        5);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1605460912650?alt=media&token=13d4e900-6bdd-4ebc-ab21-cb05953db461',
        5);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1605461144016?alt=media&token=11caee65-a9b2-426f-baca-853fddb52147',
        6);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1605461144024?alt=media&token=7ef8a7f0-7761-450f-8a34-f202d453cea6',
        6);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1606620134985?alt=media&token=482cef7a-2b39-47ec-9643-eae2cec859f8',
        7);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1606620134980?alt=media&token=51100dc9-257b-4e01-a16f-f5b55be53daf',
        7);

-- conversations
INSERT INTO public.conversations (interested_user_id, item_id)
VALUES (4, 6);
INSERT INTO public.conversations (interested_user_id, item_id)
VALUES (4, 5);
INSERT INTO public.conversations (interested_user_id, item_id)
VALUES (1, 7);
INSERT INTO public.conversations (interested_user_id, item_id)
VALUES (3, 1);
INSERT INTO public.conversations (interested_user_id, item_id)
VALUES (1, 1);

-- offers
INSERT INTO public.offers (contract_address, offer_status, price)
VALUES (null, 'DECLINED', 1.4780);
INSERT INTO public.offers (contract_address, offer_status, price)
VALUES ('0x6FDecbFb659b3680B5ab576B0d70c495d6374269', 'ACCEPTED', 1.5430);
INSERT INTO public.offers (contract_address, offer_status, price)
VALUES (null, 'DECLINED', 0.1420);
INSERT INTO public.offers (contract_address, offer_status, price)
VALUES ('0xB1Bd1AffDd5eD74F8d09Ce5e9fC51d064C8f8497', 'ACCEPTED', 0.1200);
INSERT INTO public.offers (contract_address, offer_status, price)
VALUES (null, 'AWAITING', 0.5000);
INSERT INTO public.offers (contract_address, offer_status, price)
VALUES (null, 'AWAITING', 0.4500);
INSERT INTO public.offers (contract_address, offer_status, price)
VALUES (null, 'CANCELLED', 0.4600);

-- plain advance offers
INSERT INTO public.plain_advance_offers (advance, id)
VALUES (0.0600, 4);
INSERT INTO public.plain_advance_offers (advance, id)
VALUES (0.3600, 7);

-- double advance offers
INSERT INTO public.double_advance_offers (id)
VALUES (1);
INSERT INTO public.double_advance_offers (id)
VALUES (2);
INSERT INTO public.double_advance_offers (id)
VALUES (3);
INSERT INTO public.double_advance_offers (id)
VALUES (5);
INSERT INTO public.double_advance_offers (id)
VALUES (6);

-- messages
INSERT INTO public.messages (content, sent_on, conversation_id, offer_id, sender_id)
VALUES ('beautiful socks! do you deliver abroad?', '2020-11-29 04:09:31.057259', 1, null, 4);
INSERT INTO public.messages (content, sent_on, conversation_id, offer_id, sender_id)
VALUES ('looks great, I''m interested!', '2020-11-29 04:10:41.261060', 2, 1, 4);
INSERT INTO public.messages (content, sent_on, conversation_id, offer_id, sender_id)
VALUES ('i forgot abt the delivery - that will be a bit more expensive', '2020-11-29 04:15:36.504441', 2, 2, 2);
INSERT INTO public.messages (content, sent_on, conversation_id, offer_id, sender_id)
VALUES ('no, unfortunately not :(', '2020-11-29 04:15:59.528206', 1, null, 2);
INSERT INTO public.messages (content, sent_on, conversation_id, offer_id, sender_id)
VALUES ('how much is the delivery?', '2020-11-29 04:25:05.725972', 3, null, 1);
INSERT INTO public.messages (content, sent_on, conversation_id, offer_id, sender_id)
VALUES ('let''s say 0.02 eth', '2020-11-29 04:26:08.552570', 3, 3, 4);
INSERT INTO public.messages (content, sent_on, conversation_id, offer_id, sender_id)
VALUES ('so i cant spend that much. i suggest 0.12, and i can send half of the price in advance.',
        '2020-11-29 04:27:22.449639', 3, 4, 1);
INSERT INTO public.messages (content, sent_on, conversation_id, offer_id, sender_id)
VALUES ('what''s the power output? and when was it produced?', '2020-11-29 04:28:46.798703', 4, null, 3);
INSERT INTO public.messages (content, sent_on, conversation_id, offer_id, sender_id)
VALUES ('it was made last year, it has 300 W of power.', '2020-11-29 04:29:38.930818', 4, null, 4);
INSERT INTO public.messages (content, sent_on, conversation_id, offer_id, sender_id)
VALUES ('looks fantastic, I want it right now!', '2020-11-29 04:30:16.024157', 5, 5, 1);
INSERT INTO public.messages (content, sent_on, conversation_id, offer_id, sender_id)
VALUES ('hmm, doesn''t sound impressive. i can suggest 0.45 eth.', '2020-11-29 04:31:31.446558', 4, 6, 3);
INSERT INTO public.messages (content, sent_on, conversation_id, offer_id, sender_id)
VALUES ('how about 0.46? and with advance?', '2020-11-29 04:35:27.150867', 4, 7, 4);
INSERT INTO public.messages (content, sent_on, conversation_id, offer_id, sender_id)
VALUES ('nevermind...', '2020-11-29 04:35:53.313728', 4, null, 4);
