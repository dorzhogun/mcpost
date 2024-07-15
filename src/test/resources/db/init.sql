DROP TABLE IF EXISTS posts, comments, post_likes, comment_likes, post_tags, tags;

CREATE TABLE posts
(
    id serial primary key,
    author_id uuid not null,
    title varchar(255) not null,
    post_text text not null,
    publish_date timestamp not null,
    image_path varchar(255),
    time timestamp not null,
    time_changed timestamp not null,
    type varchar(50) not null,
    is_blocked boolean not null,
    is_deleted boolean not null
);

CREATE INDEX post_author_id_index ON posts (author_id);

CREATE TABLE comments
(
    id serial primary key,
    time timestamp not null,
    time_changed timestamp not null,
    author_id uuid not null,
    comment_text text not null,
    post_id bigint not null,
    parent_id bigint,
    is_blocked boolean not null,
    is_deleted boolean not null,
    FOREIGN KEY (post_id) REFERENCES posts (id)
);

CREATE INDEX comment_author_id_index ON comments (author_id);

CREATE INDEX comment_post_id_index ON comments (post_id);

CREATE INDEX comment_parent_id_index ON comments (parent_id);

CREATE TABLE post_likes
(
    id serial primary key,
    author_id uuid not null,
    reaction_type varchar(50) not null,
    post_id bigint not null,
    is_deleted boolean not null,
    FOREIGN KEY (post_id) REFERENCES posts (id)
);

CREATE INDEX post_like_author_id_index ON post_likes (author_id);

CREATE INDEX post_like_post_id_index ON post_likes (post_id);

CREATE TABLE comment_likes
(
    id serial primary key,
    author_id uuid not null,
    reaction_type varchar(50) not null,
    comment_id bigint not null,
    is_deleted boolean not null,
    FOREIGN KEY (comment_id) REFERENCES comments (id)
);

CREATE INDEX comment_like_author_id_index ON comment_likes (author_id);

CREATE INDEX comment_like_comment_id_index ON comment_likes (comment_id);

CREATE TABLE tags
(
    id serial primary key,
    name varchar(255) not null
);

CREATE TABLE post_tags
(
    post_id bigint not null,
    tag_id bigint not null,
    FOREIGN KEY (post_id) REFERENCES posts (id),
    FOREIGN KEY (tag_id) REFERENCES tags (id)
);

INSERT INTO POSTS (author_id, title, post_text, publish_date, image_path, time, time_changed, type, is_blocked, is_deleted)
VALUES ('01784bc4-4dcc-4c91-b085-a5c71ae0184c', 'title1', 'post_text1', '2024-06-04 12:50:36', 'image_path1', '2024-06-04 12:50:36', '2024-06-04 12:50:36', 'POSTED', false, false),
('01784bc4-4dcc-4c91-b085-a5c71ae0184c', 'title2', 'post_text2', '2024-06-04 12:55:36', 'image_path2', '2024-06-04 12:55:36', '2024-06-04 12:55:36', 'POSTED', false, false),
('f4d5539c-ed78-4817-be12-b1bdf80040a3', 'title3', 'post_text3', '2024-06-04 12:57:36', 'image_path3', '2024-06-04 12:57:36', '2024-06-04 12:57:36', 'POSTED', false, false);

INSERT INTO COMMENTS (author_id, comment_text, time, time_changed, post_id, parent_id, is_blocked, is_deleted)
VALUES ('f4d5539c-ed78-4817-be12-b1bdf80040a3', 'comment_text1', '2024-06-04 12:52:36', '2024-06-04 12:52:36', 1, null, false, false),
('01784bc4-4dcc-4c91-b085-a5c71ae0184c', 'comment_text2', '2024-06-04 12:53:36', '2024-06-04 12:53:36', 1, 1, false, false),
('f4d5539c-ed78-4817-be12-b1bdf80040a3', 'comment_text3', '2024-06-04 12:58:36', '2024-06-04 12:58:36', 2, null, false, false);

INSERT INTO POST_LIKES (author_id, reaction_type, post_id, is_deleted)
VALUES ('f4d5539c-ed78-4817-be12-b1bdf80040a3', 'HEART', 1, false), ('7099e6e4-f0b9-4aa6-a263-05e7eeae1604', 'HEART', 2, false);

INSERT INTO COMMENT_LIKES (author_id, reaction_type, comment_id, is_deleted)
VALUES ('01784bc4-4dcc-4c91-b085-a5c71ae0184c', 'HEART', 1, false), ('f4d5539c-ed78-4817-be12-b1bdf80040a3', 'HEART', 2, false);

