CREATE TABLE chats (
 chat_id INT NOT NULL,
 name CHAR(10),
 members_count CHAR(10),
 bots_count CHAR(10)
);

ALTER TABLE chats ADD CONSTRAINT PK_chats PRIMARY KEY (chat_id);


CREATE TABLE roles (
 role_id CHAR(10) NOT NULL,
 name VARCHAR(10),
 is_admin BIT(10),
 can_add_tasks BIT(10),
 can_make_polls BIT(10),
 can_give_strikes_without_poll BIT(10)
);

ALTER TABLE roles ADD CONSTRAINT PK_roles PRIMARY KEY (role_id);


CREATE TABLE sheduled_tasks (
 sheduled_tasks_chat_id INT NOT NULL,
 chat_id INT NOT NULL,
 task_name VARCHAR(10) NOT NULL,
 task_description VARCHAR(10) NOT NULL,
 date_of_expire TIMESTAMP(10) NOT NULL
);

ALTER TABLE sheduled_tasks ADD CONSTRAINT PK_sheduled_tasks PRIMARY KEY (sheduled_tasks_chat_id);


CREATE TABLE users (
 user_id INT NOT NULL,
 login VARCHAR(10),
 password VARCHAR(10),
 firstname VARCHAR(10),
 lastname VARCHAR(10)
);

ALTER TABLE users ADD CONSTRAINT PK_users PRIMARY KEY (user_id);


CREATE TABLE chat_settings (
 chat_settings_id INT NOT NULL,
 chat_id INT,
 tasks_reminder_friquency INT
);

ALTER TABLE chat_settings ADD CONSTRAINT PK_chat_settings PRIMARY KEY (chat_settings_id);


CREATE TABLE members (
 member_id INT NOT NULL,
 user_id INT,
 role_id CHAR(10)
);

ALTER TABLE members ADD CONSTRAINT PK_members PRIMARY KEY (member_id);


CREATE TABLE strikes (
 strike_id CHAR(10) NOT NULL,
 member_id INT,
 chat_id INT
);

ALTER TABLE strikes ADD CONSTRAINT PK_strikes PRIMARY KEY (strike_id);


CREATE TABLE chat_members (
 chat_id INT NOT NULL,
 member_id INT NOT NULL
);

ALTER TABLE chat_members ADD CONSTRAINT PK_chat_members PRIMARY KEY (chat_id,member_id);


ALTER TABLE sheduled_tasks ADD CONSTRAINT FK_sheduled_tasks_0 FOREIGN KEY (chat_id) REFERENCES chats (chat_id);


ALTER TABLE chat_settings ADD CONSTRAINT FK_chat_settings_0 FOREIGN KEY (chat_id) REFERENCES chats (chat_id);


ALTER TABLE members ADD CONSTRAINT FK_members_0 FOREIGN KEY (user_id) REFERENCES users (user_id);
ALTER TABLE members ADD CONSTRAINT FK_members_1 FOREIGN KEY (role_id) REFERENCES roles (role_id);


ALTER TABLE strikes ADD CONSTRAINT FK_strikes_0 FOREIGN KEY (member_id) REFERENCES members (member_id);
ALTER TABLE strikes ADD CONSTRAINT FK_strikes_1 FOREIGN KEY (chat_id) REFERENCES chats (chat_id);


ALTER TABLE chat_members ADD CONSTRAINT FK_chat_members_0 FOREIGN KEY (chat_id) REFERENCES chats (chat_id);
ALTER TABLE chat_members ADD CONSTRAINT FK_chat_members_1 FOREIGN KEY (member_id) REFERENCES members (member_id);


