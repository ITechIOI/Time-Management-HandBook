CREATE DATABASE MOBILEAPP
USE MOBILEAPP

CREATE TABLE _USER(
	USERID VARCHAR(20) PRIMARY KEY,
	FULLNAME NVARCHAR(50),
	EMAIL VARCHAR(50),
	IS_DELETED BIT DEFAULT 0 --1: ĐÃ XÓA, 0: TỔN TẠI
	)

CREATE TABLE EVENT(
	EVENT_ID VARCHAR(20) PRIMARY KEY,
	USERID VARCHAR(20),
	NAME NVARCHAR(100),
	_DATE SMALLDATETIME,
	CATEGORY VARCHAR(50),
	START_TIME TIME,
	END_TIME TIME,
	REPEAT NUMERIC(2,1), --tính thường xuyên 1: DAILY, 2: WEEKLY, 3: MONTHLY, 4: ANUALLY
	DESCRIPTION NVARCHAR(200),
	IS_DELETED BIT DEFAULT 0 --1: ĐÃ XÓA, 0: TỔN TẠI 
	)


CREATE TABLE TASK(
	TASK_ID VARCHAR(20) PRIMARY KEY,
	USERID VARCHAR(20),
	NAME NVARCHAR(100),
	BEGIN_DATE DATETIME DEFAULT GETDATE(),
	END_DATE DATETIME,
	SUBJECT NVARCHAR(100),
	DESCRIPTION NVARCHAR(200),
	DATE_FINISHED DATETIME,
	IS_DELETED BIT DEFAULT 0 --1: ĐÃ XÓA, 0: TỒN TẠI
	)

CREATE TABLE NOTI_EVENT(
	EVENT_ID VARCHAR(20),
	NOTI_ID VARCHAR(10),
	NOTIFICATE TIME,
	CONSTRAINT PK_EVENT_NOTI PRIMARY KEY (EVENT_ID,NOTI_ID)
	)

CREATE TABLE NOTI_TASK(
	TASK_ID VARCHAR(20),
	NOTI_ID VARCHAR(10),
	NOTIFICATE TIME
	CONSTRAINT PK_TASK_NOTI PRIMARY KEY (TASK_ID,NOTI_ID)
	)

ALTER TABLE NOTI_EVENT ADD
	CONSTRAINT FK_EVENT_NOTI FOREIGN KEY (EVENT_ID) REFERENCES EVENT(EVENT_ID)

ALTER TABLE NOTI_TASK ADD
	CONSTRAINT FK_TASK_NOTI FOREIGN KEY (TASK_ID) REFERENCES TASK(TASK_ID)

ALTER TABLE EVENT ADD
	CONSTRAINT FK_USER_EVE FOREIGN KEY (USERID) REFERENCES _USER(USERID)

ALTER TABLE TASK ADD
	CONSTRAINT FK_USER_TASK FOREIGN KEY (USERID) REFERENCES _USER(USERID)

--Thêm 1 tài khoản mới
create proc USP_insertuser
@id varchar(20), @name nvarchar(50), @email varchar(50)
as
begin
	insert into _USER(USERID, FULLNAME, EMAIL)
	values (@id, @name, @email)
end

--Thêm một event mới
create proc USP_insertevent
@eventid varchar(20), @userid varchar(20), @name nvarchar(100), @date varchar(20), @category varchar(50),
@start varchar(20), @end varchar(20), @repeat numeric(2,1), @descrip nvarchar(200)
as
begin
	set dateformat dmy
	declare @idate smalldatetime, @istart time, @iend time
	set @idate = cast(@date as smalldatetime)
	set @istart = cast(@start as time)
	set @iend = cast(@end as time)
	insert into EVENT(EVENT_ID, USERID, NAME, _DATE, CATEGORY, START_TIME, END_TIME, REPEAT, DESCRIPTION)
	values (@eventid, @userid, @name, @idate, @category, @istart, @iend, @repeat, @descrip)
end

--Thêm một task mới
create proc USP_inserttask
@taskid varchar(20), @userid varchar(20), @name nvarchar(100), @end_date varchar(20),
@subject nvarchar(100), @descrip nvarchar(200)
as
begin
	set dateformat dmy
	declare @iend datetime
	set @iend = cast(@end_date as datetime)
	insert into TASK(TASK_ID, USERID, NAME, END_DATE, SUBJECT, DESCRIPTION)
	values (@taskid, @userid, @name, @iend, @subject, @descrip)
end

--Thêm 1 thông báo cho event
create proc USP_insertnoti_event
@eventid varchar(20), @notiid varchar(10), @notificate varchar(20)
as
begin
	set dateformat dmy
	declare @inotificate time
	set @inotificate = cast(@notificate as time)
	insert into NOTI_EVENT(EVENT_ID, NOTI_ID, NOTIFICATE)
	values (@eventid, @notiid, @inotificate)
end

--Thêm 1 thông báo cho task
create proc USP_insertnoti_task
@tasktid varchar(20), @notiid varchar(10), @notificate varchar(20)
as
begin
	set dateformat dmy
	declare @inotificate time
	set @inotificate = cast(@notificate as time)
	insert into NOTI_TASK(TASK_ID, NOTI_ID, NOTIFICATE)
	values (@tasktid, @notiid, @inotificate)
end

--Xóa 1 tài khoản
create proc USP_deleteuser
@id varchar(20)
as
begin
	update _USER
	set IS_DELETED = 1
	where USERID = @id
end

--Xóa 1 event
create proc USP_deleteevent
@id varchar(20)
as
begin
	update EVENT
	set IS_DELETED = 1
	where EVENT_ID = @id
end

--Xóa 1 task
create proc USP_deletetask
@id varchar(20)
as
begin
	update TASK
	set IS_DELETED = 1
	where TASK_ID = @id
end

--Xóa 1 thông báo của event
create proc USP_deletenoti_event
@eventid varchar(20), @notiid varchar(10)
as
begin
	delete from NOTI_EVENT
	where EVENT_ID = @eventid and NOTI_ID = @notiid
end

--Xóa 1 thông báo của task
create proc USP_deletenoti_task
@taskid varchar(20), @notiid varchar(10)
as
begin
	delete from NOTI_TASK
	where TASK_ID = @taskid and NOTI_ID = @notiid
end

--Chỉnh sửa 1 user
create proc USP_edituser
@userid varchar(20), @name nvarchar(50), @email varchar(50)
as
begin
	update _USER
	set FULLNAME = @name, EMAIL = @email
	where USERID = @userid
end

--Chỉnh sửa 1 event
create proc USP_editevent
@eventid varchar(20), @userid varchar(20), @name nvarchar(100), @date varchar(20), @category varchar(50),
@start varchar(20), @end varchar(20), @repeat numeric(2,1), @descrip nvarchar(200)
as
begin
	set dateformat dmy
	declare @idate smalldatetime, @istart time, @iend time
	set @idate = cast(@date as smalldatetime)
	set @istart = cast(@start as time)
	set @iend = cast(@end as time)
	update EVENT
	set USERID = @userid, NAME = @name, _DATE = @idate, CATEGORY = @category,
		START_TIME = @istart, END_TIME = @iend, REPEAT = @repeat, DESCRIPTION = @descrip
	where EVENT_ID = @eventid
end

--Chỉnh sửa 1 task
create proc USP_edittask
@taskid varchar(20), @userid varchar(20), @name nvarchar(100), @end_date varchar(20),
@subject nvarchar(100), @descrip nvarchar(200)
as
begin
	set dateformat dmy
	declare @iend datetime
	set @iend = cast(@end_date as datetime)
	update TASK
	set USERID = @userid, NAME = @name, END_DATE = @iend, SUBJECT = @subject, DESCRIPTION = @descrip
	where TASK_ID = @taskid
end

--Chỉnh sửa 1 thông báo của event
create proc USP_editnoti_event
@eventid varchar(20), @notiid varchar(10), @notificate time
as
begin
	set dateformat dmy
	update NOTI_EVENT
	set NOTIFICATE = @notificate
	where EVENT_ID = @eventid and NOTI_ID = @notiid
end

--Chỉnh sửa 1 thông báo của task
create proc USP_edittask_event
@taskid varchar(20), @notiid varchar(10), @notificate time
as
begin
	set dateformat dmy
	update NOTI_TASK
	set NOTIFICATE = @notificate
	where TASK_ID = @taskid and NOTI_ID = @notiid
end


--Hoàn thành 1 task
create proc USP_finishtask
@id varchar(20), @finish_date varchar(20)
as
begin
	set dateformat dmy
	declare @date datetime
	set @date = cast(@finish_date as datetime)
	update TASK
	set DATE_FINISHED = @date
	where TASK_ID = @id
end

--Hoàn tác 1 task (Nghĩa là chuyển 1 task đã hoàn thành thành chưa hoàn thành)
create proc USP_returntask
@id varchar(20)
as
begin
	update TASK
	set DATE_FINISHED = null
	where TASK_ID = @id
end

--Hiển thị các tài khoản
create proc Loadlist_user
as
begin
	select * from _USER
end

--Hiển thị các event trong 1 ngày
create proc Loadlist_event
@date varchar(20)
as
begin
	set dateformat dmy
	select EVENT_ID, EMAIL, NAME, CATEGORY, START_TIME, END_TIME, DESCRIPTION
	from EVENT join _USER on EVENT.USERID = _USER.USERID
	where _DATE = cast(@date as smalldatetime)
end

--Hiển thị các deadline sắp tới hạn và trong ngày
create proc Loadlist_task
@date varchar(50)
as
begin
	select TASK_ID, EMAIL, NAME, END_DATE, SUBJECT, DESCRIPTION
	from TASK join _USER on TASK.TASK_ID = _USER.USERID
	where BEGIN_DATE<=cast(@date as datetime) and cast(@date as datetime)<=END_DATE
end