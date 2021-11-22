package userDemo.mapper;

import genMVC.mapper.BaseMapper;
import genMVC.mapper.Mapper;
import userDemo.model.UserModel;

@Mapper
public class UserMapper extends BaseMapper<UserModel> {
    // BookMapper 会继承 BaseMapper 的所有方法
    /*
        all()
        add()
        deleteById()
        updateById()
        findByPrimaryKey()
        findByColumn()
        findByColumnWithLike()
    * */
    // 如果需要自定义的 SQL 方法, 可以自己实现
    // this.executeQuery();
    // this.executeUpdate();
    /*
        例如
        String format = "select * from %s where id = ?";
        String content = String.format(format, this.tableName);
        SQL sql = SQL.create(content, 1);
        return this.executeQuery(sql);
    * */
}
