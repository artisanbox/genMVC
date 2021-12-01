package userDemo;

import genMVC.Initializer;

public class Main {
    public static void main(String[] args) {
        // 项目入口
        // 初始化器启动后, 会扫描项目下的所有类
        // 对所有 controller 进行监听输入
        Initializer initializer = new Initializer();
        initializer.start();
    }
}
