create table sms (
    id bigint auto_increment primary key,
    message text not null ,
    recipient varchar(13) not null,
    sender varchar (20) not null ,
    status varchar (20) not null ,
    status_comments text,
    external_id varchar (55),
    created_on timestamp not null default now(),
    created_by varchar2(100) not null,
    transmission_time timestamp not null default now(),
    cost numeric (16,4) not null default 0
);
