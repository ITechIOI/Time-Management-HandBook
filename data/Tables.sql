﻿CREATE DATABASE TIME_MANAGEMENT_HANDBOOK

GO

USE TIME_MANAGEMENT_HANDBOOK

GO

CREATE TABLE _USER(
	USERID VARCHAR(30) PRIMARY KEY,
	FULLNAME NVARCHAR(50),
	EMAIL VARCHAR(50),
	IS_DELETED BIT DEFAULT 0 --1: ĐÃ XÓA, 0: TỔN TẠI
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

insert into PROLONGED_EVENT values ('PR1', 'SV2', 'Code mobile', 'Truong',
'2024/04/12', '2024/06/23', 'PT2H30M', 'Khong co gi', 1, 0)

CREATE TABLE EVENT_OF_THE_DAY(
	EVENT_ID VARCHAR(30) PRIMARY KEY,
	USERID VARCHAR(30),
	SUMMARY NVARCHAR(100),
	LOCATION NVARCHAR(100),
	_START_TIME DATETIME,
	_END_TIME DATETIME,
	NOTIFICATION_PERIOD VARCHAR(100),
	--RECURRENCE VARCHAR(100), --REPEATABILITY 1: DAILY, 2: WEEKLY, 3: MONTHLY, 4: ANUALLY
	DESCRIPTION NVARCHAR(200),
	COLOR INT,
	IS_DELETED BIT DEFAULT 0 --1: DELETED, 0: EXIST
)


INSERT INTO EVENT_OF_THE_DAY VALUES ('EV1', 'SV2', N'Đi chơi', 
'Truong', '2024/04/12 14:44:44', '2024/12/13 14:59:44', 'PT2H30M', 'Yeah', 1, 0)
INSERT INTO EVENT_OF_THE_DAY VALUES ('EV3', 'SV2', N'Đi chơi', 
'Truong', '2024/05/10 07:44:44', '2024/5/10 23:59:44', 'PT2H30M', 'Yeah', 1, 0)
INSERT INTO TASK VALUES ('TA1', 'SV2', 'Code app', 'Truong', '2024/05/10 07:44:44', 
'2024/5/10 23:59:44', 'PT2H30M', 'Khong co gi', '2024/5/12 23:59:44', 0)
select * from EVENT_OF_THE_DAY WHERE _START_TIME <= '2024/12/04 14:44:12' AND _END_TIME >= '2024/12/13 14:59:44'

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
	IS_DELETED BIT DEFAULT 0 --1: DELETED, 0: EXIST
)

/* CREATE TABLE NOTI_EVENT(
	NOTI_ID VARCHAR(30),
	EVENT_ID VARCHAR(30),
	NOTIFICATE TIME,
	CONSTRAINT PK_EVENT_NOTI PRIMARY KEY (EVENT_ID,NOTI_ID)
)

CREATE TABLE NOTI_TASK(
	NOTI_ID VARCHAR(30),
	TASK_ID VARCHAR(30),
	NOTIFICATE TIME
	CONSTRAINT PK_TASK_NOTI PRIMARY KEY (TASK_ID,NOTI_ID)
)
*/

GO

-- ADD FOREIGN KEY CONSTRAINT

/*
ALTER TABLE NOTI_EVENT ADD
CONSTRAINT FK_PROLONGED_EVENT_NOTI FOREIGN KEY (EVENT_ID) REFERENCES PROLONGED_EVENT(EVENT_ID)

ALTER TABLE NOTI_EVENT ADD
CONSTRAINT FK_EVENT_OF_THE_DAY_NOTI FOREIGN KEY (EVENT_ID) REFERENCES EVENT_OF_THE_DAY(EVENT_ID)

ALTER TABLE NOTI_TASK ADD
CONSTRAINT FK_TASK_NOTI FOREIGN KEY (TASK_ID) REFERENCES TASK(TASK_ID)

*/
ALTER TABLE PROLONGED_EVENT ADD
CONSTRAINT FK_USER_EVE_PROLONGED FOREIGN KEY (USERID) REFERENCES _USER(USERID)

ALTER TABLE EVENT_OF_THE_DAY ADD
CONSTRAINT FK_USER_EVE_OF_THE_DAY FOREIGN KEY (USERID) REFERENCES _USER(USERID)

ALTER TABLE TASK ADD
CONSTRAINT FK_USER_TASK FOREIGN KEY (USERID) REFERENCES _USER(USERID)

