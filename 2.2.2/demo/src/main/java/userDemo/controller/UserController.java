package userDemo.controller;

import genMVC.Inject;
import genMVC.Utility;
import genMVC.controller.BaseController;
import genMVC.controller.Controller;
import genMVC.controller.InputCommand;
import userDemo.model.BookModel;
import userDemo.model.UserModel;
import userDemo.service.BookService;
import userDemo.service.UserService;

import java.util.List;

// 加上 @Controller 才能被扫描到
@Controller
public class UserController {
    // 不用初始化, 初始化器会自动初始化 Service (需要加上 @Inject)
    @Inject
    UserService userService;
    @Inject
    BookService bookService;

    // 当初始化器启动后, 初始化器将自动获取输入值, 传入对应函数的参数 (注释中需要加上 arg = "参数名1 || 参数名2")
    // 输入的指令要和 @InputCommand(command = xxx) 匹配
    // 输入相同就会执行 @InputCommand 注释的方法
    @InputCommand(command = "help", args = "para1")
    public void help(String para1) {
        // para1 将会自动被获取赋值
        Utility.log("%s", para1);
        Utility.log("1, show; 2, add; 3, findById; 4, findByName");
    }

    @InputCommand(command = "1")
    public void hello() {
        List<UserModel> allUsers = userService.all();
        List<BookModel> allBooks = bookService.all();
        Utility.show(allUsers);
        Utility.show(allBooks);
    }

    @InputCommand(command = "2", args = "username || password")
    public void add(String username, String password) {
        UserModel u = new UserModel();
        u.username = username;
        u.password = password;
        userService.add(u);
    }

    @InputCommand(command = "3", args = "id")
    public void find(Integer id) {
        System.out.println(userService.findById(id));
    }

    @InputCommand(command = "4", args = "name")
    public void find2(String name) {
        List<UserModel> userModels = userService.findByUsername(name);
        Utility.show(userModels);
    }
}
