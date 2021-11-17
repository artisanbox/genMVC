package userDemo.service;

import genMVC.aspect.Aspect;
import genMVC.service.Service;
import userDemo.mapper.UserMapper;
import userDemo.model.UserModel;

import java.util.List;

// Service 层需要加上 @Service, 不然在 Controller 无法注入
// 也可以加上 @Aspect(id = xxx), 所有的方法都加上 xxx 拦截器 (多个拦截器 用 || 分割)
@Service
public class UserService {
    UserMapper userMapper = new UserMapper();

    // 给方法加上 拦截器 (多个拦截器 用 || 分割)
    // @Aspect(id = xxx)
    @Aspect(id = "TestAspect")
    public List<UserModel> all() {
        return this.userMapper.all();
    }

    public void add(UserModel userModel) {
        this.userMapper.add(userModel);
    }

    public UserModel findById(Integer id) {
        List<UserModel> res = this.userMapper.findByPrimaryKey(id);
        if (res.size() == 1) {
            return res.get(0);
        } else {
            return null;
        }
    }

    public List<UserModel> findByUsername(String name) {
        List<UserModel> userModels = this.userMapper.findByColumn("username", name);
        return userModels;
    }
}
