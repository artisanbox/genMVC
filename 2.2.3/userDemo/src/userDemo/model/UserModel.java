package userDemo.model;

import genMVC.model.BaseModel;
import genMVC.model.Column;
import genMVC.model.Table;

// 模型需要加上 @Table
// name 对应的是 表名, primaryKey 对应的是主键
// 继承 BaseModel 有自动 toString() 方法
@Table(name = "user", primaryKey = "id")
public class UserModel extends BaseModel {
    public Integer id;
    // 名字和数据库里 不一样时 需要加上 @Column
    // 需要 Mapper 自动化搜索时 value 和 action 都要加
    // action 只有 search 这个字段
    @Column(value = "username", action = "search")
    public String username;
    public String password;
    public String role;
}
