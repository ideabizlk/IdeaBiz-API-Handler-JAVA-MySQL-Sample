package lk.ideabiz.library.APICall.DataProviders.impl;


import com.google.gson.Gson;
import lk.dialog.ideabiz.library.APICall.DataProvider.IdeabizOAuthDataProviderInterface;
import lk.dialog.ideabiz.library.model.APICall.OAuth2Model;
import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Malinda_07654 on 2/9/2016.
 */
public class MySQLIdeabizOAuthDataProviderImpl implements IdeabizOAuthDataProviderInterface {
    Logger logger;
    Gson gson;

    private DataSource authDataSource;

    public DataSource getAuthDataSource() {
        return authDataSource;
    }

    public void setAuthDataSource(DataSource authDataSource) {
        this.authDataSource = authDataSource;
    }

    /***
     * Read Token form DB
     *
     * @param id id of the app in DB
     * @return
     */
    public OAuth2Model getToken(int id) {

        String sql = "SELECT * FROM `oauth2` WHERE `id`  = ?";
        OAuth2Model oAuth2Model = null;
        Connection dbConnection = null;

        try {

            dbConnection = authDataSource.getConnection();

            PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                oAuth2Model = new OAuth2Model();
                oAuth2Model.setId(result.getInt("id"));
                oAuth2Model.setAccessToken(result.getString("accessToken"));
                oAuth2Model.setRefreshToken(result.getString("refreshToken"));
                oAuth2Model.setConsumerKey(result.getString("consumerKey"));
                oAuth2Model.setConsumerSecret(result.getString("consumerSecret"));
                oAuth2Model.setUsername(result.getString("username"));
                oAuth2Model.setPassword(result.getString("password"));
                oAuth2Model.setScope(result.getString("scope"));
                oAuth2Model.setExpire(result.getLong("expire"));
                oAuth2Model.setTokenURL(result.getString("tokenURL"));
                oAuth2Model.setLastUpdated(result.getString("lastUpdated"));
                oAuth2Model.setNote(result.getString("note"));
            }
        } catch (SQLException e) {

            logger.error("DB get Oauth : " + e.getMessage());

        } finally {
            try {
                if (dbConnection != null && !dbConnection.isClosed())
                    dbConnection.close();
            } catch (SQLException e) {
            }
        }

        return oAuth2Model;
    }

    /***
     * Update token once it refreshed
     *
     * @param id           app DB ID
     * @param accessToken
     * @param refreshToken
     * @param expire
     */
    public void updateToken(int id, String accessToken, String refreshToken, String expire) {
        String sql = "UPDATE `oauth2` SET `accessToken` = ?, `refreshToken` = ?, `expire` = ?,lastUpdated=CURRENT_TIMESTAMP() " +
                "WHERE `oauth2`.`id` = ?;";

        OAuth2Model oAuth2Model = null;
        Connection dbConnection = null;

        try {

            dbConnection = authDataSource.getConnection();

            PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
            preparedStatement.setString(1, accessToken);
            preparedStatement.setString(2, refreshToken);
            preparedStatement.setString(3, expire);
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {

            logger.error("DB Update Oauth : " + e.getMessage());

        } finally {
            try {
                if (dbConnection != null && !dbConnection.isClosed())
                    dbConnection.close();
            } catch (SQLException e) {
            }
        }

    }

    public MySQLIdeabizOAuthDataProviderImpl(DataSource authDataSource) {
        this.authDataSource = authDataSource;
        logger = Logger.getLogger(MySQLIdeabizOAuthDataProviderImpl.class);
        gson = new Gson();
    }

    public MySQLIdeabizOAuthDataProviderImpl(String classname, String url, String username, String password) {

        logger = Logger.getLogger(MySQLIdeabizOAuthDataProviderImpl.class);
        gson = new Gson();

        logger.info("Creating DB from : " + classname + " " + url);
        DriverManagerDataSource managerDataSource = new DriverManagerDataSource();
        managerDataSource.setDriverClassName(classname);
        managerDataSource.setUrl(url);
        managerDataSource.setUsername(username);
        managerDataSource.setPassword(password);

        this.authDataSource = managerDataSource;

    }
    public MySQLIdeabizOAuthDataProviderImpl() {
        logger = Logger.getLogger(MySQLIdeabizOAuthDataProviderImpl.class);
        gson = new Gson();
    }

}
