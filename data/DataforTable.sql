﻿USE TIME_MANAGEMENT_HANDBOOK

SELECT * FROM TASK
SET DATEFORMAT DMY

INSERT INTO _USER(USERID,FULLNAME,EMAIL)
VALUES ('SV001',N'LÊ MINH KHA','22520596@gm.uit.edu.vn')
INSERT INTO _USER(USERID,FULLNAME,EMAIL)
VALUES ('SV002',N'NGUYỄN THỊ TUYẾT LOAN','22520783@gm.uit.edu.vn')
INSERT INTO _USER(USERID,FULLNAME,EMAIL)
VALUES ('SV003',N'HỒ THỊ BÍCH PHƯỢNG','22521160@gm.uit.edu.vn')
INSERT INTO _USER(USERID,FULLNAME,EMAIL)
VALUES ('SV004',N'PHẠM ĐỨC DUY','22520338@gm.uit.edu.vn')
INSERT INTO _USER(USERID,FULLNAME,EMAIL)
VALUES ('SV005',N'TRẦN TUẤN PHONG','22521094@gm.uit.edu.vn')

INSERT INTO EVENT(EVENT_ID,USERID,NAME,_DATE,CATEGORY,START_TIME,END_TIME,REPEAT)
VALUES ('E0001','SV003',N'PHƯƠNG PHÁP MÔ HÌNH HÓA','23/04/2024','STUDY','07:30:00','09:45:00',1.1)
INSERT INTO EVENT(EVENT_ID,USERID,NAME,_DATE,CATEGORY,START_TIME,END_TIME,REPEAT)
VALUES ('E0002','SV003',N'TƯ TƯỞNG HỒ CHÍ MINH','23/04/2024','STUDY','10:00:00','11:30:00',1.1)
INSERT INTO EVENT(EVENT_ID,USERID,NAME,_DATE,CATEGORY,START_TIME,END_TIME,REPEAT)
VALUES ('E0003','SV003',N'NHẬP MÔN CÔNG NGHỆ PHẦN MỀM','24/04/2024','STUDY','07:30:00','09:45:00',1.1)
INSERT INTO EVENT(EVENT_ID,USERID,NAME,_DATE,CATEGORY,START_TIME,END_TIME,REPEAT)
VALUES ('E0004','SV003',N'KỸ NĂNG NGHỀ NGHIỆP','24/04/2024','STUDY','10:00:00','11:30:00',1.1)
INSERT INTO EVENT(EVENT_ID,USERID,NAME,_DATE,CATEGORY,START_TIME,END_TIME,REPEAT)
VALUES ('E0005','SV003',N'NHẬP MÔN ỨNG DỤNG DI ĐỘNG','25/04/2024','STUDY','07:30:00','10:45:00',2.1)
INSERT INTO EVENT(EVENT_ID,USERID,NAME,_DATE,CATEGORY,START_TIME,END_TIME,REPEAT)
VALUES ('E0006','SV003',N'CÁC PHƯƠNG PHÁP LẬP TRÌNH','27/04/2024','STUDY','07:30:00','10:45:00',2.1)
--
INSERT INTO EVENT(EVENT_ID,USERID,NAME,_DATE,CATEGORY,START_TIME,END_TIME,REPEAT)
VALUES ('E0007','SV002',N'TRIẾT HỌC MÁC LÊNIN','22/04/2024','STUDY','07:30:00','09:45:00',1.1)
INSERT INTO EVENT(EVENT_ID,USERID,NAME,_DATE,CATEGORY,START_TIME,END_TIME,REPEAT)
VALUES ('E0008','SV002',N'PHÂN TÍCH THIẾT KẾ HỆ THỐNG THÔNG TIN','26/04/2024','STUDY','07:30:00','10:45:00',1.1)

INSERT INTO TASK(TASK_ID,USERID,NAME,END_DATE,SUBJECT,DESCRIPTION)
VALUES ('T0001','SV003',N'THIẾT KẾ PP KNNN','04/05/2024','STUDY',N'THIẾT KẾ CÙNG VỚI PHÚC')
INSERT INTO TASK(TASK_ID,USERID,NAME,END_DATE,SUBJECT,DESCRIPTION)
VALUES ('T0002','SV003',N'THAM GIA TỔNG DUYỆT PP','05/05/2024','STUDY',N'HOÀN THÀNH CÔNG VIỆC CÙNG CẢ NHÓM')
INSERT INTO TASK(TASK_ID,USERID,NAME,END_DATE,SUBJECT,DESCRIPTION)
VALUES ('T0003','SV003',N'TÌM TƯ LIỆU CHO MHH','10/05/2024','STUDY',N'DÙNG CHAT GPT VÀ TÀI LIỆU ĐÃ CÓ SẴN')
INSERT INTO TASK(TASK_ID,USERID,NAME,END_DATE,SUBJECT,DESCRIPTION)
VALUES ('T0004','SV003',N'TÌM TEMPLATE PP','12/05/2024','STUDY',N'MẪU MANG TÍNH CHUYÊN NGHIỆP, ĐƠN GIẢN')
INSERT INTO TASK(TASK_ID,USERID,NAME,END_DATE,SUBJECT,DESCRIPTION)
VALUES ('T0005','SV003',N'DUYỆT LẠI BỔ SUNG VIDEO','04/05/2024','STUDY',N'THỰC HIỆN CÙNG LOAN')
--
INSERT INTO TASK(TASK_ID,USERID,NAME,END_DATE,SUBJECT,DESCRIPTION)
VALUES ('T0006','SV002',N'TẠO MỤC LỤC CHO BÁO CÁO MHH','04/05/2024','STUDY',N'KIỂM TRA CẨN THẬN, KHÔNG ĐỂ LỖI')
INSERT INTO TASK(TASK_ID,USERID,NAME,END_DATE,SUBJECT,DESCRIPTION)
VALUES ('T0007','SV002',N'TEST DỮ LIỆU CHO MÔN DI ĐỘNG','06/05/2024','STUDY',N'NHANH GỌN LẸ')
INSERT INTO TASK(TASK_ID,USERID,NAME,END_DATE,SUBJECT,DESCRIPTION)
VALUES ('T0008','SV002',N'DUYỆT LẠI BỔ SUNG VIDEO','04/05/2024','STUDY',N'THỰC HIỆN CÙNG PHƯỢNG')

INSERT INTO NOTI_EVENT(NOTI_ID,EVENT_ID,NOTIFICATE)
VALUES ('NE01','E0001','00:30:00')
INSERT INTO NOTI_EVENT(NOTI_ID,EVENT_ID,NOTIFICATE)
VALUES ('NE02','E0001','01:00:00')
INSERT INTO NOTI_EVENT(NOTI_ID,EVENT_ID,NOTIFICATE)
VALUES ('NE01','E0003','00:30:00')
INSERT INTO NOTI_EVENT(NOTI_ID,EVENT_ID,NOTIFICATE)
VALUES ('NE02','E0003','01:00:00')
INSERT INTO NOTI_EVENT(NOTI_ID,EVENT_ID,NOTIFICATE)
VALUES ('NE01','E0007','01:00:00')
INSERT INTO NOTI_EVENT(NOTI_ID,EVENT_ID,NOTIFICATE)
VALUES ('NE01','E0008','01:30:00')
INSERT INTO NOTI_EVENT(NOTI_ID,EVENT_ID,NOTIFICATE)
VALUES ('NE02','E0008','00:30:00')

INSERT INTO NOTI_TASK(NOTI_ID,TASK_ID,NOTIFICATE)
VALUES ('NT01','T0004','01:00:00')
INSERT INTO NOTI_TASK(NOTI_ID,TASK_ID,NOTIFICATE)
VALUES ('NT02','T0004','03:00:00')
INSERT INTO NOTI_TASK(NOTI_ID,TASK_ID,NOTIFICATE)
VALUES ('NT01','T0008','01:30:00')
INSERT INTO NOTI_TASK(NOTI_ID,TASK_ID,NOTIFICATE)
VALUES ('NT02','T0008','03:00:00')
INSERT INTO NOTI_TASK(NOTI_ID,TASK_ID,NOTIFICATE)
VALUES ('NT03','T0008','06:00:00')
INSERT INTO NOTI_TASK(NOTI_ID,TASK_ID,NOTIFICATE)
VALUES ('NT01','T0007','02:30:00')
