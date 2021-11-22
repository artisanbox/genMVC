package userDemo.service;

import genMVC.service.Service;
import userDemo.mapper.BookMapper;
import userDemo.model.BookModel;

import java.util.List;

@Service
public class BookService {
    // 不用初始化, 初始化器会自动初始化 Mapper (类型需要加上 @Mapper)
    BookMapper bookMapper;

    public List<BookModel> all() {
        return this.bookMapper.all();
    }
}
