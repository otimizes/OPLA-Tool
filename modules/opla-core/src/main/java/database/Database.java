package database;

import exceptions.MissingConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final Logger LOGGER = Logger.getLogger(Database.class);
    private static String pathDatabase;

    private Database() {
    }

    /**
     * Create a connection with database and returns a Statement to working
     * with.
     *
     * @return
     * @throws Exception
     */
    public static Connection getConnection() throws Exception {

        if (StringUtils.EMPTY.equals(pathDatabase)) {
            LOGGER.info("Path to database should not be blank");
            throw new MissingConfigurationException("Path to database should not be blank");
        }

        return makeConnection();
    }

    private static Connection makeConnection() throws Exception {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:" + pathDatabase);
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.info(e);
            throw new Exception(e);
        }
    }

    public static void setPathToDB(String path) {
        pathDatabase = path;
        LOGGER.info("Path to Data Base: " + pathDatabase);
    }

}
