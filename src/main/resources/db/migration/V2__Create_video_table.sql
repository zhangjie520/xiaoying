create table video(
  id bigint auto_increment primary key,
  account_id varchar(100),
  video_id varchar(100),
  video_title varchar(100),
  video_tag varchar(100),
  video_bio varchar(100),
  video_cover varchar(100),
  video_url varchar(100),
  status varchar(100),
  gmt_create bigint,
  gmt_modified bigint
)