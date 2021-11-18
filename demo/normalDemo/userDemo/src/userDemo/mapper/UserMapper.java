package userDemo.mapper;

import genMVC.mapper.BaseMapper;
import userDemo.model.UserModel;

public class UserMapper extends BaseMapper<UserModel> {
    public static UserMapper getInstance() {
        return new UserMapper();
    }
}
