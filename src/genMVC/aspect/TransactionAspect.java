package genMVC.aspect;

import genMVC.Utility;
import genMVC.mapper.dao.BaseDao;

import java.lang.reflect.*;
import java.sql.Connection;

@Aspect
public class TransactionAspect extends BaseAspect implements BaseAspect.Point {

    public Object proceed(BaseAspect baseAspect, Method method, Object[] objects) throws Throwable {
        Object result = null;
        Connection conn = BaseDao.connection();
        Utility.log("___begin invoke transaction: %s", conn);
        try {
            BaseDao.beginTransaction(conn);
            result = baseAspect.proceed(method, objects);
            BaseDao.commitTransaction(conn);
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                BaseDao.rollbackTransaction(conn);
            }
        } finally {
            BaseDao.closeConnection(conn);
            Utility.log("___end invoke transaction: %s", conn);
        }
        return result;
    }
}
