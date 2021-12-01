package userDemo.model;

import genMVC.model.BaseModel;
import genMVC.model.Table;

// 模型需要加上 @Table
// name 对应的是 表名, primaryKey 对应的是主键
// 继承 BaseModel 有自动 toString() 方法
@Table(name = "book", primaryKey = "bId")
public class BookModel extends BaseModel {
    public Integer bId;
    public String author;
    public Float price;
}
