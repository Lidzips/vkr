CREATE TABLE "user"(
	"id" SERIAL PRIMARY KEY,
	"login" TEXT DEFAULT '' NOT NULL,
	"password" TEXT DEFAULT '' NOT NULL,
	"is_admin" BOOLEAN DEFAULT false
);

CREATE TABLE "task"(
	"id" SERIAL PRIMARY KEY,
	"complexity" INT DEFAULT 1 NOT NULL,
	"task_text" TEXT,
	"file" TEXT DEFAULT '' NOT NULL,
	"prompt" TEXT,
	"topic" TEXT,
	"out1" TEXT DEFAULT '' NOT NULL,
	"out2" TEXT DEFAULT '' NOT NULL,
	"out3" TEXT DEFAULT '' NOT NULL
);

CREATE TABLE "progress"(
	"id" SERIAL PRIMARY KEY,
	"user_id" INT REFERENCES "user"("id"),
	"task_id" INT REFERENCES "task"("id"),
	"completed" BOOLEAN,
	"available" BOOLEAN
);
