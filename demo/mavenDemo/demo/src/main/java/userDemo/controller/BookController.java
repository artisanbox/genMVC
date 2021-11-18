package userDemo.controller;

import genMVC.Utility;
import genMVC.controller.Controller;
import genMVC.controller.InputCommand;
import userDemo.model.BookModel;
import userDemo.service.BookService;

import java.util.List;

@Controller
public class BookController {
    // 不用初始化, 初始化器会自动初始化 Service (类型需要加上 @Service)
    BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @InputCommand(command = "ok")
    public void all() {
        List<BookModel> all = bookService.all();
        Utility.show(all);
    }
}
