package genMVC.mapper.dao;


import genMVC.Utility;
import genMVC.model.Column;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BaseDao<T> {
    static {
        loadProperties();
    }

    public static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();
    public static String url;
    public static String admin;
    public static String adminPassword;
    public static String driver;

    public Class<T> rawClass;

    public BaseDao() {
        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            // 参数化类型中可能有多个泛型参数
            Type[] types = parameterizedType.getActualTypeArguments();
            this.rawClass = (Class<T>) types[0];
        }
    }

    public static void loadProperties() {
        try {
            Properties properties = Utility.loadResource("application.properties");
            url = properties.getProperty("url");
            admin = properties.getProperty("admin");
            adminPassword = properties.getProperty("adminPassword");
            driver = properties.getProperty("driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection connection() throws SQLException {
        Connection conn = null;
        try {
            conn = threadLocal.get();
            if (conn != null && !conn.isClosed()) {
                return conn;
            }

//            Utility.log("first conn: %s", conn);
//            Class.forName(driver);
            conn = DriverManager.getConnection(url, admin, adminPassword);
            if (conn != null && threadLocal.get() == null) {
                threadLocal.set(conn);
            }
        } catch (Exception e) {
            Utility.log("获取数据库连接异常");
            e.printStackTrace();
        }

        return conn;
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                threadLocal.set(null);
                threadLocal.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void beginTransaction(Connection connection) {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void commitTransaction(Connection connection) {
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rollbackTransaction(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // baseDao
    public <M> M factory(SQL sqlModel, Modify<M> Modify) {
        PreparedStatement prep = null;
        try {
            Connection conn = connection();
//            Utility.log("factory conn: %s", conn);
            String sql = sqlModel.content;
            prep = conn.prepareStatement(sql);

            ArrayList<Object> parameters = sqlModel.parameters;
//            Utility.log("Factory para: %s", parameters);
            if (parameters != null) {
                for (int i = 0; i < parameters.size(); i++) {
                    int index = i + 1;
                    Object ele = parameters.get(i);
                    prep.setObject(index, ele);
                }
            }
//            Utility.log("prep: %s", prep.toString());
            M res = Modify.modify(prep);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                prep.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return null;
    }

    public List<T> executeQuery(SQL SQLModel) {
        if (SQLModel == null) {
            return null;
        }
        return this.factory(SQLModel, (preparedStatement) -> {
            ResultSet res = preparedStatement.executeQuery();
            List<T> list = new ArrayList<>();
            while (res.next()) {
                T obj = (T) this.rawClass.getDeclaredConstructor().newInstance();
                Field[] fields = this.rawClass.getDeclaredFields();
                String columnName = "";
                for (Field field : fields) {
                    boolean noted = field.isAnnotationPresent(Column.class);
                    if (noted) {
                        Column annotation = field.getAnnotation(Column.class);
                        columnName = annotation.value();
                    } else {
                        columnName = field.getName();
                    }
                    field.setAccessible(true);
                    field.set(obj, res.getObject(columnName));
                }
                list.add(obj);
            }
            return list;
        });
    }

    public int executeUpdate(SQL SQLModel) {
        if (SQLModel == null) {
            return -1;
        }
        return this.factory(SQLModel, (preparedStatement) -> {
            int updatedColumns = preparedStatement.executeUpdate();
            return updatedColumns;
        });
    }
}
