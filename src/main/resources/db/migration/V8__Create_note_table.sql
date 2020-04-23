create table note
(
    id bigint auto_increment primary key,
    video_id bigint,
    title varchar(50),
    content text,
    gmt_create bigint,
    gmt_modified bigint,
    creator bigint
);