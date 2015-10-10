/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataAccessLayer;

import java.sql.*;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.CallableStatement;

import DTO.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Minh Nhat
 */
public class ArticleDAO extends DataSource {

    CallableStatement call = null;

    public ArticleDAO(String username, String password){
            this.username = username;
            this.password = password;
    }

    public boolean insertArticle(ArticleDTO art) {
        try {
            connection = DataSource.getInstance().getConnection();
            while(connection == null)
                connection = DataSource.getInstance().getConnection();
            call = connection.prepareCall("{call insertArticle(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            call.setInt("IDTableArticle", art.getIDTableArticle());

            call.setInt("IDTableUpdateTime", art.getIDTableUpdateTime());
            call.setInt("IDTableMagazine", art.getIDTableMagazine());
            call.setInt("IDTableCategory", art.getIDTableCategory());
            call.setInt("CountOfUpdate", art.getCountOfUpdate());
            call.setTimestamp("ArticleDate",art.getArticleDate());
            call.setString("Title", art.getTitle());
            call.setString("UrlPicture", art.geturlPicture());
            call.setString("Url", art.getUrl());
            call.setInt("ObjectID", art.getObjectID());
            call.setString("Description", art.getDescription());
            call.setInt("FbLike", art.facebook.getFBLike());
            call.setInt("FbCmt", art.facebook.getFBCmt());
            call.setInt("FbShare", art.facebook.getFBShare());
            call.setInt("ArticleLike", art.getArticleLike());

            call.execute();
            return true;

        } catch (Exception e) {
            // TODO: handle exception
            
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                if (call != null) {
                    call.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean updateArticle(ArticleDTO art) {
        try {
            connection = DataSource.getInstance().getConnection();
            while(connection == null)
                connection = DataSource.getInstance().getConnection();
            call = connection.prepareCall("{call updateArticle(?,?,?,?,?,?,?)}");

            // (IDTableArticle int, IDTableUpdateTime int, CountOfUpdate int, FbLike int, FbCmt int,
	//FbShare int, ArticleLikeupdateArticle int
            call.setInt("_IDTableArticle", art.getIDTableArticle());
            call.setInt("_IDTableUpdateTime", art.getIDTableUpdateTime());
            call.setInt("_CountOfUpdate", art.getCountOfUpdate());
            call.setInt("_FbLike", art.facebook.getFBLike());
            call.setInt("_FbCmt", art.facebook.getFBCmt());
            call.setInt("_FbShare", art.facebook.getFBShare());
            call.setInt("_ArticleLike", art.getArticleLike());

            call.execute();
            return true;

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                if (call != null) {
                    call.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }
    
    public int getMaxIDTableArticle ()    {
        try {
            connection = DataSource.getInstance().getConnection();
            while(connection == null)
                connection = DataSource.getInstance().getConnection();
            
            call = connection.prepareCall("{call getMaxIDTableArticle(?)}");
            
            call.registerOutParameter(1,Types.INTEGER );
            
            call.execute();
            
            return call.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                if (call != null) {
                    call.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return -1;
    }

    public void deleteArticle(int _id)
    {
        try {
            connection = DataSource.getInstance().getConnection();
            while(connection == null)
                connection = DataSource.getInstance().getConnection();
            call = connection.prepareCall("delete * from article where article.IDTableArticle = _id");
            call.executeQuery();
        } catch (Exception e) {
        }
    }

    public ArticleDTO GetArticle(int _id)
    {
        ArticleDTO result = new ArticleDTO();
        FacebookDTO rsFB = new FacebookDTO();
        try {
            connection = DataSource.getInstance().getConnection();
            while(connection == null)
                connection = DataSource.getInstance().getConnection();
            
            call = connection.prepareCall("Select * from article where article.IDTableArticle = " + _id);
            ResultSet rs = call.executeQuery();
            while (rs.next()) {                
                result.setIDTableArticle(rs.getInt("IDTableArticle"));
                result.setIDTableUpdateTime(rs.getInt("IDTableUpdateTime"));
                result.setIDTableMagazine(rs.getInt("IDTableMagazine"));
                result.setIDTableCategory(rs.getInt("IDTableCategory"));
                result.setCountOfUpdate(rs.getInt("CountOfUpdate"));
                result.setArticleDate(rs.getTimestamp("ArticleDate"));
                result.setTitle(rs.getNString("Title"));
                result.setUrlPicture(rs.getString("UrlPicture"));
                result.setUrl(rs.getString("Url"));
                result.setObjectID(rs.getInt("ObjectID"));
                result.setDescription(rs.getNString("Description"));
                result.setArticleLike(rs.getInt("ArticleLike"));
                rsFB.setFBCmt(rs.getInt("FBCmt"));
                rsFB.setFBLike(rs.getInt("FBLike"));
                rsFB.setFBShare(rs.getInt("FBShare"));
                result.facebook = rsFB;
            }
            return result;
        } catch (Exception e) {
            
        }
        return null;
    }

    public List<ArticleDTO> getAllArticleByIndex()
    {
        List<ArticleDTO> result = new ArrayList<ArticleDTO>();
//        for (int i = 0; i < getMaxIDTableArticle(_user, _pass) + 2; i++) {
//            result.add(GetArticle(_user, _pass, i));
//        }
        FacebookDTO rsFB = new FacebookDTO();
        try {
            connection = DataSource.getInstance().getConnection();
            while(connection == null)
                connection = DataSource.getInstance().getConnection();
            call = connection.prepareCall("Select * from Article");
            ResultSet rs = call.executeQuery();
            while (rs.next()) {
                ArticleDTO temp = new ArticleDTO();
                temp.setIDTableArticle(rs.getInt("IDTableArticle"));
                temp.setIDTableUpdateTime(rs.getInt("IDTableUpdateTime"));
                temp.setIDTableMagazine(rs.getInt("IDTableMagazine"));
                temp.setIDTableCategory(rs.getInt("IDTableCategory"));
                temp.setCountOfUpdate(rs.getInt("CountOfUpdate"));
                temp.setArticleDate(rs.getTimestamp("ArticleDate"));
                temp.setTitle(rs.getNString("Title"));
                temp.setUrlPicture(rs.getString("UrlPicture"));
                temp.setUrl(rs.getString("Url"));
                temp.setObjectID(rs.getInt("ObjectID"));
                temp.setDescription(rs.getNString("Description"));
                temp.setArticleLike(rs.getInt("ArticleLike"));
                rsFB.setFBCmt(rs.getInt("FBCmt"));
                rsFB.setFBLike(rs.getInt("FBLike"));
                rsFB.setFBShare(rs.getInt("FBShare"));
                temp.facebook = rsFB;
                result.add(temp);
            }
        }
        catch (Exception e) {
        }
        
        return result;
    }
    
    public List<ArticleDTO> getArticleToUpdate( int IDTableUpdateTime , int IDTableMagazine){
        List<ArticleDTO> lart = new ArrayList<ArticleDTO>();
        ArticleDTO art = null;
        FacebookDTO fb = null;
        try {
            connection = DataSource.getInstance().getConnection();
            while(connection == null)
                connection = DataSource.getInstance().getConnection();
            // getArticleToUpdate(IDTableUpdateTime int, IDTableMagazine int)
            call = connection.prepareCall("{call getArticleToUpdate(?,?)}");
            
            call.setInt(1, IDTableUpdateTime);
            call.setInt(2, IDTableMagazine);
            //select IDTableArticle, CountOfUpdate, Url, ArticleLike, FbLike,FbCmt,FbShare, objectID

            ResultSet rart = call.executeQuery();
            while(rart.next())
            {
                art = new ArticleDTO();
                fb = new FacebookDTO();
                art.setIDTableArticle(rart.getInt(1));
                art.setCountOfUpdate(rart.getInt(2));
                art.setUrl(rart.getString(3));
                art.setArticleLike(rart.getInt(4));
                fb.setFBLike(rart.getInt(5));
                fb.setFBCmt(rart.getInt(6));
                fb.setFBShare(rart.getInt(7));
                art.setObjectID(rart.getInt(8));
                
                art.facebook = fb;
                
                lart.add(art);
            }
            
            return lart;
        } catch (SQLException ex) {
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                if (call != null) {
                    call.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }
    
    // isArticleExistsForUpdate
      public int isArticleExistsForUpdate (ArticleDTO art)    {
        try {
            connection = DataSource.getInstance().getConnection();
            while(connection == null)
                connection = DataSource.getInstance().getConnection();
            // getArticleToUpdate(IDTableUpdateTime int, IDTableMagazine int)

            call = connection.prepareCall("{call isArticleExistsForUpdate(?,?)}");
            
            call.setInt(1, art.getIDTableArticle());
            call.registerOutParameter(2,Types.INTEGER );
            
            call.execute();
            
            return call.getInt(2);
        } catch (SQLException ex) {
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                if (call != null) {
                    call.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return -1;
    }
    
    public List<ArticleDTO> getArticleDTOByUpdateType(int _idUpdateType)
    {
        List<ArticleDTO> resultList = new ArrayList<ArticleDTO>();
        try {
            connection = DataSource.getInstance().getConnection();
            while (connection == null) {                
                connection = DataSource.getInstance().getConnection();
            }
            call = connection.prepareCall("select * from article where article.IDTableUpdateTime =" + _idUpdateType);
            ResultSet rs = call.executeQuery();
            while (rs.next()) {
                ArticleDTO temp = new ArticleDTO();
                temp.setIDTableArticle(rs.getInt("IDTableArticle"));
                temp.setIDTableUpdateTime(rs.getInt("IDTableUpdateTime"));
                temp.setIDTableMagazine(rs.getInt("IDTableMagazine"));
                temp.setIDTableCategory(rs.getInt("IDTableCategory"));
                temp.setCountOfUpdate(rs.getInt("CountOfUpdate"));
                temp.setArticleDate(rs.getTimestamp("ArticleDate"));
                temp.setTitle(rs.getNString("Title"));
                temp.setUrlPicture(rs.getString("UrlPicture"));
                temp.setUrl(rs.getString("Url"));
                temp.setObjectID(rs.getInt("ObjectID"));
                temp.setDescription(rs.getNString("Description"));
                temp.setArticleLike(rs.getInt("ArticleLike"));
                FacebookDTO fb = new FacebookDTO();
                fb.setFBCmt(rs.getInt("FBCmt"));
                fb.setFBLike(rs.getInt("FBLike"));
                fb.setFBShare(rs.getInt("FBShare"));
                temp.facebook = fb;
                resultList.add(temp);
            }
        } catch (Exception e) {
        }
        return resultList;
    }
      
      // isArticleExistsForInsert
    public int isArticleExistsForInsert (ArticleDTO art)    {
        try {
            connection = DataSource.getInstance().getConnection();
            while(connection == null)
                connection = DataSource.getInstance().getConnection();
            // isArticleExistsForInsert(magazine int, objectid int, out Result int)

            call = connection.prepareCall("{call isArticleExistsForInsert(?,?,?)}");
            
            call.setInt(1, art.getIDTableMagazine());
            call.setInt(2, art.getObjectID());
            call.registerOutParameter(3,Types.INTEGER );
            
            call.execute();
            
            return call.getInt(3);
        } catch (SQLException ex) {
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                if (call != null) {
                    call.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return -1;
    }
}
