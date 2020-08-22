-- roles
INSERT INTO roles
VALUES (1, 'ROLE_USER');

-- users
INSERT INTO public.users (joined, password, username)
VALUES ('2020-07-30 21:34:57.010243', '$2a$10$8AB9GaDXq7Cwq34l8qIp3OpZRAV.3IAYSy13yZ8KNhoL/H9FNusoW', 'kplich');
INSERT INTO public.users (joined, password, username)
VALUES ('2020-07-30 21:35:30.082832', '$2a$10$oCE.MaPe3SnqURnAHqUuq.Sl/U7/OSXLEQo3olfOaoW4MvTwIbqn6', 'kplich3');
INSERT INTO public.users (joined, password, username)
VALUES ('2020-07-30 21:43:20.213819', '$2a$10$5p4ymMhRnReLXe5qxBx6letQ8AGatuN2QSBiCNU2sC7TAHCypYUWW', 'kplich2');

-- user roles
INSERT INTO public.user_roles (user_id, role_id)
VALUES (1, 1);
INSERT INTO public.user_roles (user_id, role_id)
VALUES (2, 1);
INSERT INTO public.user_roles (user_id, role_id)
VALUES (3, 1);

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
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:40:10.096853', null,
        'Pellentesque eget libero et tellus condimentum suscipit. Aenean lacus lorem, consectetur non quam in, molestie euismod tellus. Fusce faucibus odio et ante volutpat, ut aliquet mi ullamcorper. Donec eget porttitor quam. Donec ut pulvinar lacus. Nam vestibulum, elit sed sagittis suscipit, augue ex tincidunt elit, at congue eros elit id felis. Fusce fringilla consequat risus eu hendrerit. Integer id interdum est.',
        1.2356, 'Nullam convallis, dolor eget tempor sodales', 'NEW', 1, 1);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-27 23:25:25.919112', '2020-07-30 21:40:29.534620',
        'hello world, this is my new item and its description!', 1.2356, 'Another fantastic item for sale!',
        'NOT_APPLICABLE', 1, 1);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:51:04.478907', '2020-07-30 21:51:47.363755',
        'Pellentesque eget libero et tellus condimentum suscipit. Aenean lacus lorem, consectetur non quam in, molestie euismod tellus. Fusce faucibus odio et ante volutpat, ut aliquet mi ullamcorper. Donec eget porttitor quam. Donec ut pulvinar lacus. Nam vestibulum, elit sed sagittis suscipit, augue ex tincidunt elit, at congue eros elit id felis. Fusce fringilla consequat risus eu hendrerit. Integer id interdum est.',
        199.9650, 'Nullam convallis, dolor eget tempor sodales', 'USED', 1, 2);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:49:03.179152', null,
        'Pellentesque eget libero et tellus condimentum suscipit. Aenean lacus lorem, consectetur non quam in, molestie euismod tellus. Fusce faucibus odio et ante volutpat, ut aliquet mi ullamcorper. Donec eget porttitor quam. Donec ut pulvinar lacus. Nam vestibulum, elit sed sagittis suscipit, augue ex tincidunt elit, at congue eros elit id felis. Fusce fringilla consequat risus eu hendrerit. Integer id interdum est.',
        7.1650, 'Nullam convallis, dolor eget tempor sodales', 'USED', 1, 8);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:46:25.508783', null,
        'Pellentesque eget libero et tellus condimentum suscipit. Aenean lacus lorem, consectetur non quam in, molestie euismod tellus. Fusce faucibus odio et ante volutpat, ut aliquet mi ullamcorper. Donec eget porttitor quam. Donec ut pulvinar lacus. Nam vestibulum, elit sed sagittis suscipit, augue ex tincidunt elit, at congue eros elit id felis. Fusce fringilla consequat risus eu hendrerit. Integer id interdum est.',
        1.2356, 'Nullam convallis, dolor eget tempor sodales', 'NEW', 1, 1);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:48:47.633072', null,
        'Pellentesque eget libero et tellus condimentum suscipit. Aenean lacus lorem, consectetur non quam in, molestie euismod tellus. Fusce faucibus odio et ante volutpat, ut aliquet mi ullamcorper. Donec eget porttitor quam. Donec ut pulvinar lacus. Nam vestibulum, elit sed sagittis suscipit, augue ex tincidunt elit, at congue eros elit id felis. Fusce fringilla consequat risus eu hendrerit. Integer id interdum est.',
        89999.7890, 'Nullam convallis, dolor eget tempor sodales', 'USED', 1, 5);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:48:55.132861', null,
        'Pellentesque eget libero et tellus condimentum suscipit. Aenean lacus lorem, consectetur non quam in, molestie euismod tellus. Fusce faucibus odio et ante volutpat, ut aliquet mi ullamcorper. Donec eget porttitor quam. Donec ut pulvinar lacus. Nam vestibulum, elit sed sagittis suscipit, augue ex tincidunt elit, at congue eros elit id felis. Fusce fringilla consequat risus eu hendrerit. Integer id interdum est.',
        3.7890, 'Nullam convallis, dolor eget tempor sodales', 'USED', 1, 5);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:46:40.467517', null,
        'Pellentesque eget libero et tellus condimentum suscipit. Aenean lacus lorem, consectetur non quam in, molestie euismod tellus. Fusce faucibus odio et ante volutpat, ut aliquet mi ullamcorper. Donec eget porttitor quam. Donec ut pulvinar lacus. Nam vestibulum, elit sed sagittis suscipit, augue ex tincidunt elit, at congue eros elit id felis. Fusce fringilla consequat risus eu hendrerit. Integer id interdum est.',
        124.5550, 'Nullam convallis, dolor eget tempor sodales', 'USED', 1, 6);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:47:11.658113', '2020-07-30 21:51:35.515324',
        'Pellentesque eget libero et tellus condimentum suscipit. Aenean lacus lorem, consectetur non quam in, molestie euismod tellus. Fusce faucibus odio et ante volutpat, ut aliquet mi ullamcorper. Donec eget porttitor quam. Donec ut pulvinar lacus. Nam vestibulum, elit sed sagittis suscipit, augue ex tincidunt elit, at congue eros elit id felis. Fusce fringilla consequat risus eu hendrerit. Integer id interdum est.',
        14.5000, 'Nullam convallis, dolor eget tempor sodales', 'NOT_APPLICABLE', 1, 3);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:47:32.121926', null,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla tortor urna, lobortis a lobortis ac, pulvinar luctus risus. Nulla tristique lectus leo. Integer pharetra dui elit, id bibendum felis tristique suscipit. Aenean elementum odio odio, non iaculis libero semper nec. Sed ullamcorper urna non purus luctus, at fermentum sapien ultrices. Nulla interdum pretium congue. Vivamus ultricies vulputate varius.',
        3.3500, 'Lorem ipsum dolor sit amet', 'NEW', 2, 1);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:38:20.109577', null,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla tortor urna, lobortis a lobortis ac, pulvinar luctus risus. Nulla tristique lectus leo. Integer pharetra dui elit, id bibendum felis tristique suscipit. Aenean elementum odio odio, non iaculis libero semper nec. Sed ullamcorper urna non purus luctus, at fermentum sapien ultrices. Nulla interdum pretium congue. Vivamus ultricies vulputate varius.',
        15.0000, 'Lorem ipsum dolor sit amet', 'USED', 2, 3);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:39:08.729669', null,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla tortor urna, lobortis a lobortis ac, pulvinar luctus risus. Nulla tristique lectus leo. Integer pharetra dui elit, id bibendum felis tristique suscipit. Aenean elementum odio odio, non iaculis libero semper nec. Sed ullamcorper urna non purus luctus, at fermentum sapien ultrices. Nulla interdum pretium congue. Vivamus ultricies vulputate varius.',
        3.3400, 'Lorem ipsum dolor sit amet', 'NOT_APPLICABLE', 2, 4);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:47:19.832031', null,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla tortor urna, lobortis a lobortis ac, pulvinar luctus risus. Nulla tristique lectus leo. Integer pharetra dui elit, id bibendum felis tristique suscipit. Aenean elementum odio odio, non iaculis libero semper nec. Sed ullamcorper urna non purus luctus, at fermentum sapien ultrices. Nulla interdum pretium congue. Vivamus ultricies vulputate varius.',
        3.3450, 'Lorem ipsum dolor sit amet', 'NOT_APPLICABLE', 2, 2);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:47:31.309209', null,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla tortor urna, lobortis a lobortis ac, pulvinar luctus risus. Nulla tristique lectus leo. Integer pharetra dui elit, id bibendum felis tristique suscipit. Aenean elementum odio odio, non iaculis libero semper nec. Sed ullamcorper urna non purus luctus, at fermentum sapien ultrices. Nulla interdum pretium congue. Vivamus ultricies vulputate varius.',
        3.3500, 'Lorem ipsum dolor sit amet', 'NEW', 2, 1);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:36:52.838540', null, 'Lorem ipsum set dolorem gracias muchachos', 0.0000,
        'New perfectly saved this thing super item', 'USED', 2, 6);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:48:08.135460', null,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla tortor urna, lobortis a lobortis ac, pulvinar luctus risus. Nulla tristique lectus leo. Integer pharetra dui elit, id bibendum felis tristique suscipit. Aenean elementum odio odio, non iaculis libero semper nec. Sed ullamcorper urna non purus luctus, at fermentum sapien ultrices. Nulla interdum pretium congue. Vivamus ultricies vulputate varius.',
        1.2500, 'Lorem ipsum dolor sit amet', 'NEW', 2, 3);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:50:18.311413', null,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla tortor urna, lobortis a lobortis ac, pulvinar luctus risus. Nulla tristique lectus leo. Integer pharetra dui elit, id bibendum felis tristique suscipit. Aenean elementum odio odio, non iaculis libero semper nec. Sed ullamcorper urna non purus luctus, at fermentum sapien ultrices. Nulla interdum pretium congue. Vivamus ultricies vulputate varius.',
        49.9900, 'Lorem ipsum dolor sit amet', 'NEW', 2, 2);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:50:22.224468', null,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla tortor urna, lobortis a lobortis ac, pulvinar luctus risus. Nulla tristique lectus leo. Integer pharetra dui elit, id bibendum felis tristique suscipit. Aenean elementum odio odio, non iaculis libero semper nec. Sed ullamcorper urna non purus luctus, at fermentum sapien ultrices. Nulla interdum pretium congue. Vivamus ultricies vulputate varius.',
        49.9900, 'Lorem ipsum dolor sit amet', 'NEW', 2, 2);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:46:57.657600', null, 'hello world, this is my new item and its description!', 0.0540,
        'Another fantastic item for sale!', 'USED', 3, 7);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:45:43.972389', null, 'hello world, this is my new item and its description!', 1.2356,
        'Another fantastic item for sale!', 'NOT_APPLICABLE', 3, 1);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:50:45.928322', null, 'hello world, this is my new item and its description!', 1.7390,
        'Another item for sale!', 'USED', 3, 7);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:47:51.587884', null, 'hello world, this is my new item and its description!', 2.2222,
        'Another fantastic item for sale!', 'NEW', 3, 8);
INSERT INTO public.items (added_on, closed_on, description, price, title, used_status, added_by_id, category_id)
VALUES ('2020-07-30 21:49:22.670836', null, 'hello world, this is my new item and its description!', 17.7890,
        'Another fantastic item for sale!', 'NEW', 3, 4);

-- item photos
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        1);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657097?alt=media&token=227c3f29-b706-4a66-9908-a63e8d687716',
        2);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657070?alt=media&token=ddd995ed-6512-4e07-8634-9bd479de8f35',
        2);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657070?alt=media&token=ddd995ed-6512-4e07-8634-9bd479de8f35',
        3);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        4);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657070?alt=media&token=ddd995ed-6512-4e07-8634-9bd479de8f35',
        4);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        5);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657070?alt=media&token=ddd995ed-6512-4e07-8634-9bd479de8f35',
        5);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657070?alt=media&token=ddd995ed-6512-4e07-8634-9bd479de8f35',
        5);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        6);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        7);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657070?alt=media&token=ddd995ed-6512-4e07-8634-9bd479de8f35',
        7);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657070?alt=media&token=ddd995ed-6512-4e07-8634-9bd479de8f35',
        7);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        8);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657070?alt=media&token=ddd995ed-6512-4e07-8634-9bd479de8f35',
        8);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657070?alt=media&token=ddd995ed-6512-4e07-8634-9bd479de8f35',
        8);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        9);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        10);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657070?alt=media&token=ddd995ed-6512-4e07-8634-9bd479de8f35',
        10);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657070?alt=media&token=ddd995ed-6512-4e07-8634-9bd479de8f35',
        10);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        11);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657070?alt=media&token=ddd995ed-6512-4e07-8634-9bd479de8f35',
        11);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        12);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657070?alt=media&token=ddd995ed-6512-4e07-8634-9bd479de8f35',
        12);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        13);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657070?alt=media&token=ddd995ed-6512-4e07-8634-9bd479de8f35',
        13);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        14);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657070?alt=media&token=ddd995ed-6512-4e07-8634-9bd479de8f35',
        15);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        16);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657070?alt=media&token=ddd995ed-6512-4e07-8634-9bd479de8f35',
        16);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657070?alt=media&token=ddd995ed-6512-4e07-8634-9bd479de8f35',
        16);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        16);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        17);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657070?alt=media&token=ddd995ed-6512-4e07-8634-9bd479de8f35',
        17);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657070?alt=media&token=ddd995ed-6512-4e07-8634-9bd479de8f35',
        17);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        17);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        18);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657070?alt=media&token=ddd995ed-6512-4e07-8634-9bd479de8f35',
        18);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657070?alt=media&token=ddd995ed-6512-4e07-8634-9bd479de8f35',
        18);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        18);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        19);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657097?alt=media&token=227c3f29-b706-4a66-9908-a63e8d687716',
        20);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657097?alt=media&token=d31bb958-0679-4413-b136-d5166817b75f',
        20);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596138597823?alt=media&token=30937d31-23bc-43bf-b517-31fbb521cb6c',
        20);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657097?alt=media&token=227c3f29-b706-4a66-9908-a63e8d687716',
        21);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596134657097?alt=media&token=d31bb958-0679-4413-b136-d5166817b75f',
        21);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1596138597823?alt=media&token=30937d31-23bc-43bf-b517-31fbb521cb6c',
        21);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        22);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        23);
INSERT INTO public.item_photos (url, item_id)
VALUES ('https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1595785828992?alt=media&token=20606105-e2e8-4b54-94fc-ed0517a6bfe8',
        23);

-- conversations
INSERT INTO public.conversations (interested_user_id, item_id)
VALUES (2, 1);

-- messages
INSERT INTO public.messages (sent_on, content, conversation_id, sender_id)
VALUES ('2020-08-15 15:23:16.000000', 'Hello world!', 1, 2);
INSERT INTO public.messages (sent_on, content, conversation_id, sender_id)
VALUES ('2020-08-15 15:25:34.000000', 'What up?', 1, 2);
INSERT INTO public.messages (sent_on, content, conversation_id, sender_id)
VALUES ('2020-08-15 16:19:17.000000', 'Good evening, how can I help?', 1, 1);
