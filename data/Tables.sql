﻿CREATE DATABASE TIME_MANAGEMENT_HANDBOOK

GO

USE TIME_MANAGEMENT_HANDBOOK

GO

CREATE TABLE _USER(
	USERID VARCHAR(30) PRIMARY KEY,
	FULLNAME NVARCHAR(50),
	EMAIL VARCHAR(50),
	IS_DELETED BIT DEFAULT 0  --1: DELETED, 0: EXIST
)

CREATE TABLE PROLONGED_EVENT(
	EVENT_ID VARCHAR(30) PRIMARY KEY,
	USERID VARCHAR(30),
	SUMMARY NVARCHAR(100),
	LOCATION NVARCHAR(100),
	_START_DATE DATE,
	_END_DATE DATE,
	NOTIFICATION_PERIOD VARCHAR(100),
	--RECURRENCE VARCHAR(100),
	DESCRIPTION NVARCHAR(200),
	COLOR INT,
	IS_DELETED BIT DEFAULT 0 --1: DELETED, 0: EXIST
)

CREATE TABLE EVENT_OF_THE_DAY(
	EVENT_ID VARCHAR(30) PRIMARY KEY,
	USERID VARCHAR(30),
	SUMMARY NVARCHAR(100),
	LOCATION NVARCHAR(100),
	_START_TIME DATETIME,
	_END_TIME DATETIME,
	NOTIFICATION_PERIOD VARCHAR(100),
	DESCRIPTION NVARCHAR(200),
	COLOR INT,
	IS_DELETED BIT DEFAULT 0 --1: DELETED, 0: EXIST
)

CREATE TABLE TASK(
	TASK_ID VARCHAR(30) PRIMARY KEY,
	USERID VARCHAR(30),
	NAME NVARCHAR(100),
	LOCATION NVARCHAR(100),
	CREATING_TIME DATETIME DEFAULT GETDATE(),
	END_TIME DATETIME,
	NOTIFICATION_PERIOD VARCHAR(100),
	--SUBJECT NVARCHAR(100),
	DESCRIPTION NVARCHAR(200),
	FINISHED_TIME DATETIME,
	COLOR INT,
	IS_DELETED BIT DEFAULT 0 --1: DELETED, 0: EXIST
)

GO

ALTER TABLE PROLONGED_EVENT ADD
CONSTRAINT FK_USER_EVE_PROLONGED FOREIGN KEY (USERID) REFERENCES _USER(USERID)

ALTER TABLE EVENT_OF_THE_DAY ADD
CONSTRAINT FK_USER_EVE_OF_THE_DAY FOREIGN KEY (USERID) REFERENCES _USER(USERID)

ALTER TABLE TASK ADD
CONSTRAINT FK_USER_TASK FOREIGN KEY (USERID) REFERENCES _USER(USERID)

