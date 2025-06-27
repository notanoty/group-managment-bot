-- CreateTable
CREATE TABLE "chats" (
    "id" INTEGER NOT NULL,
    "name" VARCHAR(50),
    "members_amount" INTEGER NOT NULL,
    "bots_amount" INTEGER NOT NULL,

    CONSTRAINT "chats_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "roles" (
    "id" INTEGER NOT NULL,
    "name" VARCHAR(50),
    "is_admin" BOOLEAN,
    "can_add_tasks" BOOLEAN,
    "can_make_polls" BOOLEAN,
    "can_give_strikes_without_poll" BOOLEAN,

    CONSTRAINT "roles_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "sheduled_tasks_chat" (
    "id" INTEGER NOT NULL,
    "chat_id" INTEGER NOT NULL,
    "task_name" VARCHAR(50) NOT NULL,
    "task_description" TEXT NOT NULL,
    "date_of_expire" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "sheduled_tasks_chat_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "users" (
    "id" INTEGER NOT NULL,
    "login" VARCHAR(50),
    "password" VARCHAR(50),
    "firstname" VARCHAR(50),
    "lastname" VARCHAR(50),

    CONSTRAINT "users_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "chat_settings" (
    "id" INTEGER NOT NULL,
    "chat_id" INTEGER,
    "tasks_reminder_friquency" INTEGER,

    CONSTRAINT "chat_settings_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "members" (
    "member_id" INTEGER NOT NULL,
    "user_id" INTEGER,
    "role_id" CHAR(10),

    CONSTRAINT "members_pkey" PRIMARY KEY ("member_id")
);

-- CreateTable
CREATE TABLE "strikes" (
    "member_id" INTEGER NOT NULL,
    "chat_id" INTEGER NOT NULL,

    CONSTRAINT "strikes_pkey" PRIMARY KEY ("member_id","chat_id")
);

-- CreateTable
CREATE TABLE "chat_members" (
    "chat_id" INTEGER NOT NULL,
    "member_id" INTEGER NOT NULL,

    CONSTRAINT "chat_members_pkey" PRIMARY KEY ("chat_id","member_id")
);

-- AddForeignKey
ALTER TABLE "sheduled_tasks_chat" ADD CONSTRAINT "sheduled_tasks_chat_chat_id_fkey" FOREIGN KEY ("chat_id") REFERENCES "chats"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "chat_settings" ADD CONSTRAINT "chat_settings_chat_id_fkey" FOREIGN KEY ("chat_id") REFERENCES "chats"("id") ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "members" ADD CONSTRAINT "members_user_id_fkey" FOREIGN KEY ("user_id") REFERENCES "users"("id") ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "strikes" ADD CONSTRAINT "strikes_member_id_fkey" FOREIGN KEY ("member_id") REFERENCES "members"("member_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "strikes" ADD CONSTRAINT "strikes_chat_id_fkey" FOREIGN KEY ("chat_id") REFERENCES "chats"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "chat_members" ADD CONSTRAINT "chat_members_chat_id_fkey" FOREIGN KEY ("chat_id") REFERENCES "chats"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "chat_members" ADD CONSTRAINT "chat_members_member_id_fkey" FOREIGN KEY ("member_id") REFERENCES "members"("member_id") ON DELETE RESTRICT ON UPDATE CASCADE;
