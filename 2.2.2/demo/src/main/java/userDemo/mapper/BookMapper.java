package userDemo.mapper;

import genMVC.mapper.BaseMapper;
import genMVC.mapper.Mapper;
import userDemo.model.BookModel;

import java.awt.print.Book;

@Mapper
public class BookMapper extends BaseMapper<BookModel> {
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
}
