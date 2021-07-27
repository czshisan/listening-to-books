package util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBUtil {
    private static DataSource dataSource = null;

    private static void initDataSource() {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setServerName("127.0.0.1");
        mysqlDataSource.setPort(3306);
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("");
        mysqlDataSource.setDatabaseName("listeningtobooks");
        mysqlDataSource.setCharacterEncoding("utf-8");
        mysqlDataSource.setUseSSL(false);
        mysqlDataSource.setServerTimezone("Asia/Shanghai");

        dataSource = mysqlDataSource;
    }

    public static Connection getConnection() throws SQLException {
        if(dataSource == null){
            synchronized (DBUtil.class){
                if(dataSource == null){
                    initDataSource();
                }
            }
        }

        return dataSource.getConnection();
    }
}
