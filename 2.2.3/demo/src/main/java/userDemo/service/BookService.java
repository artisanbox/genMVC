package userDemo.service;

import genMVC.Inject;
import genMVC.service.Service;
import userDemo.mapper.BookMapper;
import userDemo.model.BookModel;

import java.util.List;

// Service 层需要加上 @Service
// 也可以加上 @Aspect(id = xxx), 所有的方法都加上 xxx 拦截器 (多个拦截器 用 || 分割)
@Service
public class BookService {
    // 不用初始化, 初始化器会自动初始化 Mapper (需要加上 @Inject)
    @Inject
    BookMapper bookMapper;

    public List<BookModel> all() {
        return this.bookMapper.all();
    }
}
