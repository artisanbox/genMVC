package userDemo.service;

import genMVC.service.Service;
import userDemo.mapper.BookMapper;
import userDemo.model.BookModel;

import java.util.List;

@Service
public class BookService {
    BookMapper bookMapper = new BookMapper();

    public List<BookModel> all() {
        return this.bookMapper.all();
    }
}
