package com.trhsy.sim.sqlite;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.sqlite
 * @ClassName: SQLiteJDBC
 * @Description:
 * @date 2024/9/4 16:28
 */
public class SQLiteJDBC {
    public static void main(String args[]) {
        Connection connection = null;
        try {
            // 加载SQLite驱动程序
            Class.forName("org.sqlite.JDBC");
            String strmc = (new File(".")).getAbsolutePath();
            // 创建数据库连接
            connection = DriverManager.getConnection("jdbc:sqlite:"+strmc+"/sim.db");
            System.out.println("已成功打开数据库");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭数据库连接
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
