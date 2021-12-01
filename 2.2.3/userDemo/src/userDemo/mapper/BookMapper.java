package userDemo.mapper;

import genMVC.mapper.BaseMapper;
import userDemo.model.BookModel;

import java.awt.print.Book;

public class BookMapper extends BaseMapper<BookModel> {
    public static BookMapper getInstance() {
        return new BookMapper();
    }
}
