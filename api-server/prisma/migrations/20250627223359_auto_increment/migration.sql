-- AlterTable
CREATE SEQUENCE chat_settings_id_seq;
ALTER TABLE "chat_settings" ALTER COLUMN "id" SET DEFAULT nextval('chat_settings_id_seq');
ALTER SEQUENCE chat_settings_id_seq OWNED BY "chat_settings"."id";

-- AlterTable
CREATE SEQUENCE chats_id_seq;
ALTER TABLE "chats" ALTER COLUMN "id" SET DEFAULT nextval('chats_id_seq');
ALTER SEQUENCE chats_id_seq OWNED BY "chats"."id";

-- AlterTable
CREATE SEQUENCE members_member_id_seq;
ALTER TABLE "members" ALTER COLUMN "member_id" SET DEFAULT nextval('members_member_id_seq');
ALTER SEQUENCE members_member_id_seq OWNED BY "members"."member_id";

-- AlterTable
CREATE SEQUENCE roles_id_seq;
ALTER TABLE "roles" ALTER COLUMN "id" SET DEFAULT nextval('roles_id_seq');
ALTER SEQUENCE roles_id_seq OWNED BY "roles"."id";

-- AlterTable
CREATE SEQUENCE sheduled_tasks_chat_id_seq;
ALTER TABLE "sheduled_tasks_chat" ALTER COLUMN "id" SET DEFAULT nextval('sheduled_tasks_chat_id_seq');
ALTER SEQUENCE sheduled_tasks_chat_id_seq OWNED BY "sheduled_tasks_chat"."id";

-- AlterTable
CREATE SEQUENCE users_id_seq;
ALTER TABLE "users" ALTER COLUMN "id" SET DEFAULT nextval('users_id_seq');
ALTER SEQUENCE users_id_seq OWNED BY "users"."id";
