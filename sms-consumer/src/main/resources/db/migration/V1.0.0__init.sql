CREATE TABLE IF NOT EXISTS users (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(256) NOT NULL,
  password VARCHAR(256) NOT NULL,
  enabled boolean not null  default  true,
  UNIQUE KEY unique_username(username)
);

CREATE TABLE IF NOT EXISTS role (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(256) NOT NULL,
  UNIQUE KEY unique_name(name)
);

CREATE TABLE user_role (
  user_id bigint NOT NULL,
  role_id bigint NOT NULL,
  PRIMARY KEY (user_id,role_id),
  CONSTRAINT FK_ROLE_ID FOREIGN KEY (role_id) REFERENCES role (id),
  CONSTRAINT FK_USER_ID FOREIGN KEY (user_id) REFERENCES users (id)
);

create table sms (
    id bigint auto_increment primary key,
    message text not null ,
    recepient varchar(13) not null,
    sender varchar (20) not null ,
    status varchar (20) not null ,
    status_comments text,
    external_id varchar (55),
    created_on timestamp not null default now(),
    created_by bigint not null,
    transmission_time timestamp not null default now(),
    cost numeric (16,4) not null default 0,
    CONSTRAINT FK_S_CREATED_BY FOREIGN KEY (created_by) references users (id)
);
