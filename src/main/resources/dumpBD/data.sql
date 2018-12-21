INSERT INTO my_schema.roles (name, description) VALUES ('ROLE_ADMIN', 'admin');
INSERT INTO my_schema.roles (name, description) VALUES ('ROLE_USER', 'user');

ALTER SEQUENCE my_schema.users_id_seq RESTART WITH 3;
ALTER SEQUENCE my_schema.roles_id_seq RESTART WITH 1;

INSERT INTO my_schema.users (email, password, first_name, last_name, phone)
VALUES ('den.strelts@gmail.com', '7913782o', 'Denis', 'Streltsov', '+375295839006');
INSERT INTO my_schema.users (email, password, first_name, last_name, phone)
VALUES ('den.stres@gmail.com', '111', 'And', 'Vor', '+375295839006');

INSERT INTO my_schema.user_roles (user_id, role_id) VALUES (1,1);
INSERT INTO my_schema.user_roles (user_id, role_id) VALUES (1,2);
INSERT INTO my_schema.user_roles (user_id, role_id) VALUES (2,2);

delete from my_schema.user_roles where id = 3;