CREATE TABLE users(
    user_id integer PRIMARY KEY,
    username varchar(50),
    user_password varchar(50),
    email varchar(50));

CREATE TABLE user_details (
    user_details_id     integer PRIMARY KEY,
    first_name          varchar(50),
    last_name          	varchar(50),
    street              varchar(100),
    post_code           varchar(20),
    city				varchar(50),
    user_id				integer,
        CONSTRAINT fk_user_id
            FOREIGN KEY(user_id)
            REFERENCES users(user_id)
)