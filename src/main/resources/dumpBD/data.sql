INSERT INTO roles (name, description) VALUES ('ROLE_ADMIN', 'admin');
INSERT INTO roles (name, description) VALUES ('ROLE_USER', 'user');

ALTER SEQUENCE my_schema.users_id_seq RESTART WITH 3;
ALTER SEQUENCE my_schema.roles_id_seq RESTART WITH 1;

INSERT INTO users (email, password, first_name, last_name, phone)
VALUES ('den.strelts@gmail.com', '7913782o', 'Denis', 'Streltsov', '+375295839006');
INSERT INTO users (email, password, first_name, last_name, phone)
VALUES ('den.stres@gmail.com', '111', 'And', 'Vor', '+375295839006');

INSERT INTO user_roles (user_id, role_id) VALUES (1,1);
INSERT INTO user_roles (user_id, role_id) VALUES (1,2);
INSERT INTO user_roles (user_id, role_id) VALUES (2,2);

delete from user_roles where id = 3;

INSERT INTO friends (user_id, friend_id) VALUES (1,4);
INSERT INTO friends (user_id, friend_id) VALUES (4,1);
INSERT INTO friends (user_id, friend_id) VALUES (1,7);
INSERT INTO friends (user_id, friend_id) VALUES (7,1);
INSERT INTO friends (user_id, friend_id) VALUES (1,2);