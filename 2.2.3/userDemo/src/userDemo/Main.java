package userDemo;

import genMVC.Initializer;

public class Main {

    public static void main(String[] args) {
        // 项目入口
        // 初始化器启动后, controller 开始监听输入
        Initializer initializer = new Initializer();
        initializer.start();
    }
}
