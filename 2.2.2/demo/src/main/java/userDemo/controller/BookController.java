package userDemo.controller;

import genMVC.Inject;
import genMVC.Utility;
import genMVC.controller.Controller;
import genMVC.controller.InputCommand;
import userDemo.model.BookModel;
import userDemo.service.BookService;

import java.util.List;

// 加上 @Controller 才能被扫描到
@Controller
public class BookController {
    // 不用初始化, 初始化器会自动初始化 Service (需要加上 @Inject)
    @Inject
    BookService bookService;

    @InputCommand(command = "ok")
    public void all() {
        List<BookModel> all = bookService.all();
        Utility.show(all);
    }
}
