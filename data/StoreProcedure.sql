﻿USE TIME_MANAGEMENT_HANDBOOK

GO

-- FUNCTION IS USED TO INCREASE ID_USER AUTOMATICALLY

CREATE OR ALTER FUNCTION dbo.GET_MAX_ID_USER()
RETURNS INT
AS
BEGIN
	DECLARE 
		@MAX_ID_USER VARCHAR(30), 
		@MAX_NUM INT

	IF ((SELECT COUNT(*) FROM _USER WHERE IS_DELETED = 0) = 0)
		RETURN 0

	SELECT TOP 1 @MAX_ID_USER = USERID
	FROM _USER
	ORDER BY USERID DESC;
	
	DECLARE @SUB_MAX VARCHAR(30) = SUBSTRING(@MAX_ID_USER, 3, LEN(@MAX_ID_USER))
	SET @MAX_NUM = CAST(@SUB_MAX AS INT)

	RETURN @MAX_NUM
END

GO

-- INSERT A NEW ACCOUNT
CREATE OR ALTER PROC USP_INSERT_NEW_USER
@EMAIL VARCHAR(50)
AS
BEGIN
	DECLARE @ID VARCHAR(30)
	DECLARE @MAX_COUNT INT
	SET @MAX_COUNT = dbo.GET_MAX_ID_USER()
	SET @MAX_COUNT = @MAX_COUNT + 1
	SET @ID = 'SV' + CAST(@MAX_COUNT AS VARCHAR(30))
	INSERT INTO _USER(USERID, EMAIL)
	VALUES (@ID, @EMAIL)
END

GO

-- SELECT EVENT_OF_THE_DAY BY EMAIL

CREATE OR ALTER PROC USP_GET_EVENT_OF_THE_DAY_BY_ID
@EMAIL VARCHAR(50), @DATETIME VARCHAR(25)
AS
BEGIN
	DECLARE @USERID VARCHAR(30), @TIME_NOW DATE
	SET @TIME_NOW = CAST(@DATETIME AS DATE)
	SELECT @USERID = USERID FROM _USER WHERE EMAIL = @EMAIL
	AND IS_DELETED = 0  
	SELECT * FROM EVENT_OF_THE_DAY WHERE USERID = @USERID
	AND IS_DELETED = 0 AND @TIME_NOW >= _START_TIME AND 
	@TIME_NOW <= _END_TIME AND IS_DELETED = 0

	SELECT * FROM EVENT_OF_THE_DAY WHERE USERID = @USERID
	AND IS_DELETED = 0 AND @TIME_NOW = CAST(_START_TIME AS DATE)
END

GO

CREATE OR ALTER PROC USP_GET_EVENT_OF_THE_DAY_BY_ID_FOR_NOTIFICATION
@EMAIL VARCHAR(50), @DATETIME VARCHAR(25)
AS
BEGIN
	DECLARE @USERID VARCHAR(30), @TIME_NOW DATE
	SET @TIME_NOW = CAST(@DATETIME AS DATE)
	SELECT @USERID = USERID FROM _USER WHERE EMAIL = @EMAIL
	AND IS_DELETED = 0  
	SELECT * FROM EVENT_OF_THE_DAY WHERE USERID = @USERID
	AND IS_DELETED = 0 AND 
	@TIME_NOW <= _END_TIME AND IS_DELETED = 0

END

GO

CREATE OR ALTER PROC USP_GET_EVENT_OF_THE_DAY_BY_ID_FOR_NOTIFICATION
@EMAIL VARCHAR(50), @DATETIME VARCHAR(25)
AS
BEGIN
	DECLARE @USERID VARCHAR(30), @TIME_NOW DATETIME
	SET @TIME_NOW = CAST(@DATETIME AS DATETIME)
	SELECT @USERID = USERID FROM _USER WHERE EMAIL = @EMAIL
	AND IS_DELETED = 0  
	SELECT * FROM EVENT_OF_THE_DAY WHERE USERID = @USERID
	AND IS_DELETED = 0 AND @TIME_NOW <= _END_TIME 
	AND @TIME_NOW >= _START_TIME
END

GO

-- FUNCTION IS USED TO INCREASE EVENT_ID OF THE DAY AUTOMATICALLY

CREATE OR ALTER FUNCTION dbo.GET_MAX_ID_EVENT_OF_THE_DAY()
RETURNS INT
AS
BEGIN
	DECLARE 
		@MAX_ID_EVENT VARCHAR(30), 
		@MAX_NUM INT

	IF ((SELECT COUNT(*) FROM EVENT_OF_THE_DAY) = 0 )
		RETURN 0

	SELECT TOP 1 @MAX_ID_EVENT = EVENT_ID
	FROM EVENT_OF_THE_DAY
	ORDER BY CAST(SUBSTRING(EVENT_ID, 3, LEN(EVENT_ID)) AS INT) DESC;
	
	
	DECLARE @SUB_MAX VARCHAR(30) = SUBSTRING(@MAX_ID_EVENT, 3, LEN(@MAX_ID_EVENT))
	SET @MAX_NUM = CAST(@SUB_MAX AS INT) 

	RETURN @MAX_NUM
END

GO

-- INSERT NEW EVENT

CREATE OR ALTER PROC INSERT_NEW_EVENT_OF_THE_DAY
@EMAIL VARCHAR(50), @SUMMARY NVARCHAR(100), @LOCATION NVARCHAR(100),
@START VARCHAR(20), @END VARCHAR(20), @NOTIFICATION_PERIOD VARCHAR(100),
@DESCRIPTION NVARCHAR (200), @COLOR INT
AS 
BEGIN
	DECLARE @USER_ID VARCHAR(30), @EVENT_ID VARCHAR(30)
	SELECT @USER_ID = USERID FROM _USER WHERE EMAIL = @EMAIL AND IS_DELETED = 0
	DECLARE @MAX_COUNT INT
	SET @MAX_COUNT = dbo.GET_MAX_ID_EVENT_OF_THE_DAY()
	SET @MAX_COUNT = @MAX_COUNT + 1
	SET @EVENT_ID = 'EV' + CAST(@MAX_COUNT AS VARCHAR(30))

	DECLARE @START_TIME DATETIME, @END_TIME DATETIME
	SET @START_TIME = CAST(@START AS DATETIME)
	SET @END_TIME = CAST(@END AS DATETIME)

	INSERT INTO EVENT_OF_THE_DAY 
	VALUES (@EVENT_ID, @USER_ID, @SUMMARY, @LOCATION, @START_TIME, @END_TIME, 
	@NOTIFICATION_PERIOD, @DESCRIPTION, @COLOR, 0)
END

GO

-- SELECT PROLONGED_EVENT BY EMAIL BY EMAIL

CREATE OR ALTER PROC USP_GET_PROLONGED_EVENT_BY_EMAIL
@EMAIL VARCHAR(50), @DATETIME VARCHAR(25)
AS
BEGIN
	DECLARE @ID_USER VARCHAR(30),  @DATE_NOW DATE
	SELECT @ID_USER = USERID FROM _USER 
	WHERE EMAIL = @EMAIL AND IS_DELETED = 0
	SET @DATE_NOW = CAST(@DATETIME AS DATE)
	SELECT * FROM PROLONGED_EVENT WHERE USERID = @ID_USER
	AND IS_DELETED = 0 AND @DATE_NOW >= _START_DATE AND 
	@DATE_NOW <= _END_DATE AND IS_DELETED = 0
END

GO

-- SELECT PROLONGED_EVENT BY EMAIL BY EMAIL	FOR NOTIFICATION

CREATE OR ALTER PROC USP_GET_PROLONGED_EVENT_BY_EMAIL_FOR_NOTIFICATION
@EMAIL VARCHAR(50), @DATETIME VARCHAR(25)
AS
BEGIN
	DECLARE @ID_USER VARCHAR(30),  @DATE_NOW DATE
	SELECT @ID_USER = USERID FROM _USER 
	WHERE EMAIL = @EMAIL AND IS_DELETED = 0
	SET @DATE_NOW = CAST(@DATETIME AS DATE)
	SELECT * FROM PROLONGED_EVENT WHERE USERID = @ID_USER
	AND IS_DELETED = 0 AND @DATE_NOW >= _START_DATE AND 
	@DATE_NOW <= _END_DATE AND IS_DELETED = 0
END

GO

-- GET LIST TASK BY EMAIL

CREATE OR ALTER PROC USP_GET_TASK_BY_EMAIL
@EMAIL VARCHAR(50), @DATETIME VARCHAR(30)
AS
BEGIN
	DECLARE @USERID VARCHAR(30), @TIME_NOW DATE
	SET @TIME_NOW = CAST(@DATETIME AS DATE)
	SELECT @USERID = USERID FROM _USER WHERE EMAIL = @EMAIL
	AND IS_DELETED = 0  
	SELECT * FROM TASK WHERE USERID = @USERID 
	AND IS_DELETED = 0 AND (@TIME_NOW BETWEEN CAST(CREATING_TIME AS DATE)
	AND CAST(END_TIME AS DATE) OR (CAST(CREATING_TIME AS DATE) <= @TIME_NOW
	AND FINISHED_TIME IS NULL))
	/*
	SELECT * FROM TASK WHERE USERID = @USERID 
	AND IS_DELETED = 0 AND (@TIME_NOW BETWEEN CAST(CREATING_TIME AS DATE)
	AND CAST(END_TIME AS DATE) OR @TIME_NOW BETWEEN CAST(CREATING_TIME AS DATE)
	AND CAST(FINISHED_TIME AS DATE))
	*/
END

EXEC  USP_GET_TASK_BY_EMAIL '22520783@gm.uit.edu.vn', '2024/5/20 11:41:00'

GO

-- GET LIST TASK BY EMAIL FOR NOTIFICATION

CREATE OR ALTER PROC USP_GET_TASK_BY_EMAIL_FOR_NOTIFICATION
@EMAIL VARCHAR(50), @DATETIME VARCHAR(30)
AS
BEGIN
	DECLARE @USERID VARCHAR(30), @TIME_NOW DATETIME
	SET @TIME_NOW = CAST(@DATETIME AS DATETIME)
	SELECT @USERID = USERID FROM _USER WHERE EMAIL = @EMAIL
	AND IS_DELETED = 0  
	SELECT * FROM TASK WHERE USERID = @USERID 
	AND IS_DELETED = 0 AND (@TIME_NOW BETWEEN CREATING_TIME
	AND END_TIME OR @TIME_NOW BETWEEN CREATING_TIME
	AND FINISHED_TIME)
END
exec USP_GET_TASK_BY_EMAIL_FOR_NOTIFICATION '22520783@gm.uit.edu.vn' , '2024/05/19 9:07:00'
GO

-- FUNCTION IS USED TO INCREASE EVENT_ID OF PROLONGED_EVENT AUTOMATICALLY

CREATE OR ALTER FUNCTION dbo.GET_MAX_ID_PROLONGED_EVENT()
RETURNS INT
AS
BEGIN
	DECLARE 
		@MAX_ID_EVENT VARCHAR(30), 
		@MAX_NUM INT

	IF ((SELECT COUNT(*) FROM PROLONGED_EVENT) = 0 )
		RETURN 0

	SELECT TOP 1 @MAX_ID_EVENT = EVENT_ID
	FROM PROLONGED_EVENT
	ORDER BY CAST(SUBSTRING(EVENT_ID, 3, LEN(EVENT_ID)) AS INT) DESC;
	
	DECLARE @SUB_MAX VARCHAR(30)
	SET @SUB_MAX = SUBSTRING(@MAX_ID_EVENT, 3, LEN(@MAX_ID_EVENT))
	SET @MAX_NUM = CAST(@SUB_MAX AS INT)

	RETURN @MAX_NUM
END

GO

-- INSERT NEW PROLONGED EVENT

CREATE OR ALTER PROC USP_INSERT_NEW_PROLONGED_OF_THE_DAY
@EMAIL VARCHAR(50), @SUMMARY NVARCHAR(100), @LOCATION NVARCHAR(100),
@START VARCHAR(20), @END VARCHAR(20), @NOTIFICATION_PERIOD VARCHAR(100),
@DESCRIPTION NVARCHAR (200), @COLOR INT
AS
BEGIN
	DECLARE @USER_ID VARCHAR(30), @EVENT_ID VARCHAR(30)
	SELECT @USER_ID = USERID FROM _USER WHERE EMAIL = @EMAIL AND IS_DELETED = 0
	DECLARE @MAX_COUNT INT
	SET @MAX_COUNT = dbo.GET_MAX_ID_PROLONGED_EVENT()
	SET @MAX_COUNT = @MAX_COUNT + 1
	SET @EVENT_ID = 'PR' + CAST(@MAX_COUNT AS VARCHAR(30))

	DECLARE @START_TIME DATE, @END_TIME DATE
	SET @START_TIME = CAST(@START AS DATE)
	SET @END_TIME = CAST(@END AS DATE)

	INSERT INTO PROLONGED_EVENT
	VALUES (@EVENT_ID, @USER_ID, @SUMMARY, @LOCATION, @START_TIME, @END_TIME, 
	@NOTIFICATION_PERIOD, @DESCRIPTION, @COLOR, 0)
END

GO

-- FUNCTION IS USED TO INCREASE TASK_ID OF TASK AUTOMATICALLY

CREATE OR ALTER FUNCTION dbo.GET_MAX_ID_TASK()
RETURNS INT
AS
BEGIN
	DECLARE 
		@MAX_ID_EVENT VARCHAR(30), 
		@MAX_NUM INT

	IF ((SELECT COUNT(*) FROM TASK) = 0 )
		RETURN 0

	SELECT TOP 1 @MAX_ID_EVENT = TASK_ID
	FROM TASK
	ORDER BY CAST(SUBSTRING(TASK_ID, 3, LEN(TASK_ID)) AS INT) DESC;
	
	DECLARE @SUB_MAX VARCHAR(30) = SUBSTRING(@MAX_ID_EVENT, 3, LEN(@MAX_ID_EVENT))
	SET @MAX_NUM = CAST(@SUB_MAX AS INT)

	RETURN @MAX_NUM
END

GO

-- INSERT NEW TASK BY EMAIL

CREATE OR ALTER PROC USP_INSERT_NEW_TASK
@EMAIL VARCHAR(50), @NAME NVARCHAR(100), @LOCATION NVARCHAR(100),
@CREATING_TIME VARCHAR(20), @END_TIME VARCHAR(20), @NOTIFICATION_PERIOD VARCHAR(100),
@DESCRIPTION NVARCHAR (200), @COLOR INT
AS
BEGIN
	DECLARE @USER_ID VARCHAR(30), @EVENT_ID VARCHAR(30)
	SELECT @USER_ID = USERID FROM _USER WHERE EMAIL = @EMAIL AND IS_DELETED = 0

	DECLARE @MAX_COUNT INT
	SET @MAX_COUNT = dbo.GET_MAX_ID_TASK()
	SET @MAX_COUNT = @MAX_COUNT + 1
	SET @EVENT_ID = 'TA' + CAST(@MAX_COUNT AS VARCHAR(30))


	DECLARE @FINISH DATETIME, @END DATETIME, @CREATING DATETIME
	SET @CREATING = CAST(@CREATING_TIME AS DATETIME)
	SET @END = CAST(@END_TIME AS DATETIME)

	INSERT INTO TASK VALUES (@EVENT_ID, @USER_ID, @NAME,
	@LOCATION, @CREATING, @END, @NOTIFICATION_PERIOD,
	@DESCRIPTION, NULL, @COLOR, 0)
END

GO

-- DELETE EVENT_OF_THE_DAY BY EMAIL, START_TIME, END_TIME AND SUMMARY

CREATE OR ALTER PROC USP_DELETE_EVENT_OF_THE_DAY
@EMAIL VARCHAR(50), @SUMMARY VARCHAR(100), 
@START VARCHAR(20), @END VARCHAR(20)
AS
BEGIN
	DECLARE @USER_ID VARCHAR(30), @EVENT_ID VARCHAR(30)
	SELECT @USER_ID = USERID FROM _USER WHERE EMAIL = @EMAIL AND IS_DELETED = 0

	DECLARE @START_TIME DATETIME, @END_TIME DATETIME
	SET @START_TIME = CAST(@START AS DATETIME)
	SET @END_TIME = CAST(@END AS DATETIME)

	UPDATE EVENT_OF_THE_DAY SET IS_DELETED = 1
	WHERE USERID = @USER_ID AND SUMMARY = @SUMMARY 
	AND _START_TIME = @START_TIME AND _END_TIME = @END_TIME
	AND IS_DELETED = 0
END

GO

-- DELETE PROLONGED_END BY EMAIL, START_TIME, END_TIME AND SUMMARY

CREATE OR ALTER PROC USP_DELETE_PROLONGED_EVENT
@EMAIL VARCHAR(50), @SUMMARY VARCHAR(100), 
@START VARCHAR(20), @END VARCHAR(20)
AS
BEGIN
	DECLARE @USER_ID VARCHAR(30), @EVENT_ID VARCHAR(30)
	SELECT @USER_ID = USERID FROM _USER WHERE EMAIL = @EMAIL AND IS_DELETED = 0

	DECLARE @START_TIME DATE, @END_TIME DATE
	SET @START_TIME = CAST(@START AS DATE)
	SET @END_TIME = CAST(@END AS DATE)

	UPDATE PROLONGED_EVENT SET IS_DELETED = 1
	WHERE USERID = @USER_ID AND SUMMARY = @SUMMARY 
	AND _START_DATE = @START_TIME AND _END_DATE = @END_TIME
	AND IS_DELETED = 0
END

GO

CREATE OR ALTER PROC USP_DELETE_TASK
@EMAIL VARCHAR(50), @NAME VARCHAR(100), 
@END VARCHAR(20)
AS
BEGIN
	DECLARE @USER_ID VARCHAR(30), @EVENT_ID VARCHAR(30)
	SELECT @USER_ID = USERID FROM _USER WHERE EMAIL = @EMAIL AND IS_DELETED = 0

	DECLARE @END_TIME DATETIME
	SET @END_TIME = CAST(@END AS DATETIME)

	UPDATE TASK SET IS_DELETED = 1
	WHERE USERID = @USER_ID AND NAME = @NAME 
	AND END_TIME = @END_TIME AND IS_DELETED = 0
END

GO

-- INSERT NEW EVENT OF THE DAY FROM CALENDAR

CREATE OR ALTER PROC USP_INSERT_EVENT_OF_THE_DAY_FROM_CALENDAR
@EMAIL VARCHAR(50), @SUMMARY NVARCHAR(100), @LOCATION NVARCHAR(100),
@START VARCHAR(20), @END VARCHAR(20), 
@NOTIFICATION_PERIOD VARCHAR(100), @DESCRIPTION NVARCHAR (200),
@COLOR INT
AS
BEGIN
	DECLARE @USER_ID VARCHAR(30), @COUNT INT, @EVENT_ID VARCHAR(30)
	SELECT @USER_ID = USERID FROM _USER WHERE EMAIL = @EMAIL AND IS_DELETED = 0

	DECLARE @START_TIME DATETIME, @END_TIME DATETIME
	SET @START_TIME = CAST(@START AS DATETIME)
	SET @END_TIME = CAST(@END AS DATETIME)

	SELECT @COUNT = COUNT(*) FROM EVENT_OF_THE_DAY 
	WHERE SUMMARY = @SUMMARY AND LOCATION = @LOCATION
	AND _START_TIME = @START_TIME AND _END_TIME = @END_TIME
	AND DESCRIPTION = @DESCRIPTION AND IS_DELETED = 0
	AND USERID = @USER_ID

	IF (@COUNT = 0)
	BEGIN
		DECLARE @MAX_COUNT INT
		SET @MAX_COUNT = dbo.GET_MAX_ID_EVENT_OF_THE_DAY()
		SET @MAX_COUNT = @MAX_COUNT + 1
		SET @EVENT_ID = 'EV' + CAST(@MAX_COUNT AS VARCHAR(30))
		INSERT INTO EVENT_OF_THE_DAY 
		VALUES (@EVENT_ID, @USER_ID, @SUMMARY, @LOCATION, @START_TIME, @END_TIME, 
		@NOTIFICATION_PERIOD, @DESCRIPTION, @COLOR, 0)
	END
END

GO

-- INSERT NEW PROLONGED EVENT FROM CALENDAR

CREATE OR ALTER PROC USP_INSERT_PROLONGED_EVENT_FROM_CALENDAR
@EMAIL VARCHAR(50), @SUMMARY NVARCHAR(100), @LOCATION NVARCHAR(100),
@START VARCHAR(20), @END VARCHAR(20), @NOTIFICATION_PERIOD VARCHAR(100),
@DESCRIPTION NVARCHAR (200), @COLOR INT
AS
BEGIN
	DECLARE @USER_ID VARCHAR(30), @EVENT_ID VARCHAR(30), @COUNT INT
	SELECT @USER_ID = USERID FROM _USER WHERE EMAIL = @EMAIL AND IS_DELETED = 0

	DECLARE @START_TIME DATE, @END_TIME DATE
	SET @START_TIME = CAST(@START AS DATE) 
	SET @END_TIME = CAST(@END AS DATE)

	SELECT @COUNT = COUNT(*) FROM PROLONGED_EVENT 
	WHERE USERID = @USER_ID AND SUMMARY = @SUMMARY 
	AND DESCRIPTION = @DESCRIPTION 
		/*
	AND LOCATION = @LOCATION AND _START_DATE = @START_TIME
	AND _END_DATE = @START_TIME 
	AND IS_DELETED = 0
	*/

	IF (@COUNT = 0)
	BEGIN
		DECLARE @MAX_COUNT INT
		SET @MAX_COUNT = dbo.GET_MAX_ID_PROLONGED_EVENT()
		SET @MAX_COUNT = @MAX_COUNT + 1
		SET @EVENT_ID = 'PR' + CAST(@MAX_COUNT AS VARCHAR(30))

		INSERT INTO PROLONGED_EVENT
		VALUES (@EVENT_ID, @USER_ID, @SUMMARY, @LOCATION, @START_TIME, @END_TIME, 
		@NOTIFICATION_PERIOD, @DESCRIPTION, @COLOR, 0)
	END
END

GO

CREATE OR ALTER PROC USP_UPDATE_EVENT_OF_THE_DAY
@EVENT_ID VARCHAR(30),
@SUMMARY NVARCHAR(100), @LOCATION NVARCHAR(100),
@START_TIME DATETIME, @END_TIME DATETIME,
@NOTIFICATION VARCHAR(100), @DESCRIPTION NVARCHAR(200),
@COLOR INT
AS
BEGIN
	DECLARE @START DATETIME, @END DATETIME
	SET @START = CAST(@START_TIME AS DATETIME)
	SET @END = CAST(@END_TIME AS DATETIME)

	UPDATE EVENT_OF_THE_DAY SET SUMMARY = @SUMMARY,
	LOCATION = @LOCATION, _START_TIME = @START,
	_END_TIME = @END, NOTIFICATION_PERIOD = @NOTIFICATION,
	DESCRIPTION = @DESCRIPTION, COLOR = @COLOR
	WHERE EVENT_ID = @EVENT_ID 
END
select * from TASK
GO

CREATE OR ALTER PROC USP_UPDATE_PROLONGED_EVENT
@EVENT_ID VARCHAR(30),
@SUMMARY NVARCHAR(100), @LOCATION NVARCHAR(100),
@START_TIME DATE, @END_TIME DATE,
@NOTIFICATION VARCHAR(100), @DESCRIPTION NVARCHAR(200),
@COLOR INT
AS
BEGIN
	DECLARE @START DATE, @END DATE
	SET @START = CAST(@START_TIME AS DATE)
	SET @END = CAST(@END_TIME AS DATE)

	UPDATE PROLONGED_EVENT SET SUMMARY = @SUMMARY,
	LOCATION = @LOCATION, _START_DATE = @START,
	_END_DATE = @END, NOTIFICATION_PERIOD = @NOTIFICATION,
	DESCRIPTION = @DESCRIPTION, COLOR = @COLOR
	WHERE EVENT_ID = @EVENT_ID 
END

GO

CREATE OR ALTER PROC USP_UPDATE_TASK
@EVENT_ID VARCHAR(30),
@SUMMARY NVARCHAR(100), @LOCATION NVARCHAR(100),
@CREATING_TIME DATE, @END_TIME DATE,
@NOTIFICATION VARCHAR(100), @DESCRIPTION NVARCHAR(200),
@COLOR INT
AS
BEGIN
	UPDATE TASK SET NAME = @SUMMARY,
	LOCATION = @LOCATION, CREATING_TIME = @CREATING_TIME,
	END_TIME = @END_TIME, NOTIFICATION_PERIOD = @NOTIFICATION,
	DESCRIPTION = @DESCRIPTION, 
	COLOR = @COLOR WHERE TASK_ID = @EVENT_ID
END

GO

CREATE PROC USP_UPDATE_FINISH_TIME_FOR_TASK
AS
BEGIN
	
END
