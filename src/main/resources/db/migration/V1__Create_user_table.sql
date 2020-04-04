create table user(
  id bigint auto_increment primary key,
  account_id varchar(100),
  name varchar(50),
  password varchar(50),
  bio varchar(100),
  avatar_url varchar(256),
  gmt_create bigint,
  gmt_modified bigint
)