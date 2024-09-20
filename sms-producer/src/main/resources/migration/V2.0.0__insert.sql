insert into role(id,name) values (1,'ROLE_ADMIN'), (2,'ROLE_USER');
insert into users(id,username,password,enabled) values
(1,'example@domain.com','$2a$10$PjsnjloEaprXef69rVKtwONlmzsTR4yqDzhkEeqI6TQKjCzy/ywqm',true);
insert into user_role(user_id,role_id) values (1,1), (1,2);
