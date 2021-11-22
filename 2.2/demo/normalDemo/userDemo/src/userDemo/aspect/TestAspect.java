package userDemo.aspect;

import genMVC.Utility;
import genMVC.aspect.Aspect;
import genMVC.aspect.BaseAspect;

import java.lang.reflect.Method;

// 切面加上 @Aspect 才能识别
// 固定写法 implements BaseAspect.Point
@Aspect
public class TestAspect implements BaseAspect.Point {

    // 实现 proceed 方法
    public Object proceed(BaseAspect baseAspect, Method method, Object[] objects) throws Throwable {
        Utility.log("___begin");
        Object result = baseAspect.proceed(method, objects);
        Utility.log("___end");
        return result;
    }
}
