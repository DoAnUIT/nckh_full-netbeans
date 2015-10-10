-- drop database nckh;
create database nckh;
use nckh;

create table MAGAZINE(
	IDTableMagazine int,
    Magazine varchar(40),
    primary key (IDTableMagazine)
);

create table UPDATETIME(
	IDTableUpdateTime int not null,
	QuantumTime time,
    MaxRepeated int,
	primary key (IDTableUpdateTime)
);

create table CATEGORY(
	IDTableCategory int,
    Category varchar(30),
    primary key (IDTableCategory)
);

create table ARTICLE(
	IDTableArticle int not null,
    IDTableUpdateTime int,
    IDTableMagazine int,
    IDTableCategory int,
    CountOfUpdate int,
	ArticleDate TIMESTAMP,
	Title nvarchar(300) ,
	UrlPicture varchar(300),
	Url varchar(300),
	ObjectID int ,
	Description text ,
	FbLike int,
	FbCmt int,
	FbShare int,
	ArticleLike int,
	primary key (IDTableArticle),
    constraint foreign key FK_Article_UpdateTime(IDTableUpdateTime) references UPDATETIME(IDTableUpdateTime),
				foreign key FK_Article_Magazine (IDTableMagazine) references MAGAZINE(IDTableMagazine),
                foreign key FK_Article_Category (IDTableCategory) references CATEGORY(IDTableCategory)
)character set utf8;

create table PARENTCMT(
	IDTableParentCmt int not null,  
	IDTableArticle int not null,
	ParentID int , 
	CmtLike int not null,
	Content text,
	primary key (IDTableParentCmt),
	constraint foreign key FK_parentCmt_Article (IDTableArticle) references ARTICLE(IDTableArticle)
);

create table SUBCMT(
	IDTableSubCmt int,
	IDTableParentCmt int not null, 
	ChildID int not null unique, 
	CmtLike int ,
	Content text,
	primary key (IDTableSubCmt),
	constraint foreign key FK_subcomment_parentcmt(IDTableParentCmt) references PARENTCMT(IDTableParentCmt)
);


-- Insert value of these table : category, magazine, updatetime
insert into MAGAZINE values (1, "Thanh niên");
insert into MAGAZINE values (2, "VnExpress");
insert into MAGAZINE values (3, "Tuổi trẻ");


insert into updatetime values (1, "0:10:0", 144);
insert into updatetime values (2, "0:30:0", 48);
insert into updatetime values (3, "1:0:0", 120);
insert into updatetime values (4, "6:0:0", 28);
insert into updatetime values (5, "12:0:0", 28);

insert into category values (1, "Thời sự");
insert into category values (2, "Thế giới");
insert into category values (3, "Kinh doanh");
insert into category values (4, "Giáo dục");
insert into category values (5, "Thể thao");
insert into category values (6, "Giải trí");
insert into category values (7, "Khoa học công nghệ");