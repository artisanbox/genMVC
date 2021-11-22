package genMVC.mapper.dao;

import genMVC.Utility;
import genMVC.model.BaseModel;

import java.util.*;

public class SQL extends BaseModel {
    public String content;
    public ArrayList<Object> parameters;

    public SQL() {
        this.parameters = new ArrayList<>();
    }

    public static SQL create(String content, Object... args) {
        SQL sql = new SQL();
        sql.content = content;

        int len = args.length;

        try {
            sql.parameters.addAll((Collection<?>) args[0]);
        } catch (Exception e) {
            sql.parameters.addAll(Arrays.asList(args).subList(0, len));
        }

        try {
            sql.checkParameters();
        } catch (ParameterException e) {
            e.printStackTrace();
            return null;
        }

        Utility.log("sql: %s", sql.toString());
        return sql;
    }

    // 自定义异常
    class ParameterException extends Exception {
        public ParameterException() {

        }

        public ParameterException(String message) {
            super(message);
        }
    }

    public int numberOfParameters() {
        int count = 0;
        for (int i = 0; i < this.content.length(); i++) {
            Character c = this.content.charAt(i);
            if (c.equals('?')) {
                count += 1;
            }
        }
        return count;
    }

    public void checkParameters() throws ParameterException {
        int ps = this.parameters.size();
        if (ps == 0) {
            return;
        }
        int count = numberOfParameters();
//        Utility.log("count: %s, ps: %s", count, ps);
        if (count != ps) {
            throw new ParameterException("参数个数和 <?> 不匹配");
        }
    }

}