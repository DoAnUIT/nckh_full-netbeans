delimiter //
-- article --------
create procedure insertArticle(IDTableArticle int , IDTableUpdateTime int, IDTableMagazine int,
    IDTableCategory int, CountOfUpdate int, ArticleDate TIMESTAMP,	Title nvarchar(200) ,UrlPicture varchar(200),
	Url varchar(200),	ObjectID int ,	Description text ,	FbLike int,
	FbCmt int,	FbShare int,	ArticleLike int)
begin
insert into ARTICLE (IDTableArticle ,   IDTableUpdateTime ,  IDTableMagazine ,
    IDTableCategory ,CountOfUpdate,	ArticleDate ,	Title ,	UrlPicture ,Url ,	ObjectID ,	Description ,
	FbLike ,FbCmt ,FbShare ,ArticleLike ) values (IDTableArticle ,   IDTableUpdateTime ,  IDTableMagazine ,
    IDTableCategory ,CountOfUpdate,	ArticleDate ,	Title ,	UrlPicture ,Url ,	ObjectID ,	Description ,
	FbLike ,FbCmt ,FbShare ,ArticleLike );
end//

delimiter //
create procedure getMaxIDTableArticle (out maxIDTableArticle int)
begin
declare a int;
select max(IDTableArticle) into a from ARTICLE;
set maxIDTableArticle :=a ;
end//

delimiter //
create procedure updateArticle(IDTableArticle int, IDTableUpdateTime int, CountOfUpdate int, FbLike int, FbCmt int,
	FbShare int, ArticleLike int)
begin
update ARTICLE as a
set a.IDTableUpdateTime = if(a.IDTableUpdateTime <> IDTableUpdateTime,IDTableUpdateTime,a.IDTableUpdateTime), 
	a.CountOfUpdate = if(a.CountOfUpdate <> CountOfUpdate,CountOfUpdate,a.CountOfUpdate), 
    a.FbLike = if(a.FbLike <> FbLike, FbLike,a.FbLike),
	a.FbCmt = if(a.FbCmt <> FbCmt, FbCmt,a.FbCmt), 
    a.FbShare = if(a.FbShare <> FbShare,FbShare,a.FbShare),
    a.ArticleLike = if(a.ArticleLike <> ArticleLike,ArticleLike,a.ArticleLike)
where a.IDTableArticle = IDTableArticle;
end//

delimiter //
create procedure getArticleToUpdate(IDTableUpdateTime int, IDTableMagazine int)
begin
select IDTableArticle, CountOfUpdate, Url, ArticleLike, FbLike,FbCmt,FbShare, ObjectID
from ARTICLE as a
where a.IDTableUpdateTime = IDTableUpdateTime and a.IDTableMagazine = IDTableMagazine;
end//

delimiter //
create procedure isArticleExistsForUpdate (IDTableArticle int, out Result int)
begin
if(exists(select * from ARTICLE as a where a.IDTableArticle = IDTableArticle))
then
set Result = 1;
else
set Result = 0;
end if;

end//

delimiter //
create procedure isArticleExistsForInsert(magazine int, objectid int, out Result int)
begin
if(exists(select * from ARTICLE as a where a.IDTableMagazine = magazine and a.ObjectID = objectid))
then
set Result = 1;
else
set Result = 0;
end if;
end//

-- parentcomenet
delimiter //
create procedure insertParentCmt (IDTableParentCmt int, IDTableArticle int, parentID int, CmtLike int, Content text)
begin
insert into PARENTCMT (IDTableParentCmt, IDTableArticle, parentid, CmtLike, Content)values(IDTableParentCmt, IDTableArticle,
						parentID, CmtLike, Content);
end//
/* check if the parent comment has been existed */
delimiter //
create procedure isParentCmtExists(IDTableArticle int, ParentID int, out Result int)
begin 
if(exists(select * from PARENTCMT as p 	where p.IDTableArticle = IDTableArticle and p.ParentID = ParentID))
            then
	set Result = 1;
    else
    set Result = 0;
    end if;
end//

delimiter //
create procedure updateParentCmt (IDTableArticle int,  parentID int,  CmtLike int, Content text)
begin
update PARENTCMT as p
set p.CmtLike = if(p.CmtLike <> CmtLike,CmtLike,p.CmtLike), 
	p.Content = if(p.Content <> Content, Content, p.Content)
where p.IDTableArticle = IDTableArticle and p.Parentid = Parentid;
end//


create procedure getMaxIDTableParentCmt(out maxid int)
begin
declare a int;
select max(IDTableParentCmt) into a from PARENTCMT;
set maxid := a; 
end//


create procedure getIDTableParentCmtWithArgument(IDTableArticle int, ParentID int, out IDTableParentCmt int)
begin
declare a int;
select p.IDTableParentCmt into a from PARENTCMT as p
where p.IDTableArticle = IDTableArticle and p.ParentID = ParentID;
set IDTableParentCmt := a;
end//
-- SubComment

delimiter //
create procedure insertSubCmt(IDTableSubCmt int, IDTableParentCmt int,  ChildID int, CmtLike int, Content text)
begin
insert into SUBCMT (IDTableSubCmt, IDTableParentCmt, ChildID, CmtLike, Content )
values (IDTableSubCmt, IDTableParentCmt ,	ChildID , CmtLike, Content);
end//

/* check if the sub comment has been existed */

delimiter //
create procedure isSubCmtExists(IDTableArticle int, ParentID int, ChildID int, out Result int)
begin 
if(exists(select * from (SUBCMT as s join PARENTCMT as p 
					ON s.IDTableParentCmt = p.IDTableParentCmt) join ARTICLE as a
                    on a.IDTableArticle = p.IDTableArticle 
			where a.IDTableArticle = IDTableArticle and p.ParentID = ParentID and s.ChildID = ChildID))
            then
	set Result = 1;
    else
    set Result = 0;
    end if;
end//


delimiter //
create procedure updateSubCmt (IDTableParentCmt int, in ChildID int, in CmtLike int, Content text)
begin
update SUBCMT as s
set s.CmtLike = if(s.CmtLike <> CmtLike,CmtLike,s.CmtLike), 
	s.Content = if(s.Content <> Content, Content, s.Content)
where s.IDTableParentCmt = IDTableParentCmt and s.ChildID = ChildID; 
end//

create procedure getMaxIDTableSubCmt(out maxid int)
begin
declare a int;
select max(IDTableSubCmt) into a from SUBCMT;
set maxid := a; 
end//


delimiter ;