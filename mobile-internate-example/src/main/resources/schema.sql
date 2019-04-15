create table users (
  id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(256),
  password varchar(256),
  enabled boolean,
  PRIMARY KEY (`id`)
);

create table authorities (
  username varchar(256),
  authority varchar(256)
);

create table oauth_client_details (
  client_id VARCHAR(256) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256)
);

create table oauth_client_token (
  token_id VARCHAR(256),
  token LONGVARBINARY,
  authentication_id VARCHAR(256),
  user_name VARCHAR(256),
  client_id VARCHAR(256)
);

create table oauth_access_token (
  token_id VARCHAR(256),
  token LONGVARBINARY,
  authentication_id VARCHAR(256),
  user_name VARCHAR(256),
  client_id VARCHAR(256),
  authentication LONGVARBINARY,
  refresh_token VARCHAR(256)
);

create table oauth_refresh_token (
  token_id VARCHAR(256),
  token LONGVARBINARY,
  authentication LONGVARBINARY
);

create table verification_code (
  id varchar(32),
  target varchar(15),
  code varchar(32),
  type varchar(32),
  expire_time datetime,
  disabled tinyint(1),
  PRIMARY KEY (id)
);

create table captcha_code (
  id varchar(32),
  code varchar(32),
  type varchar(32),
  expire_time datetime,
  verify tinyint(1),
  PRIMARY KEY (id)
);


