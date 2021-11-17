package genMVC.aspect;

import genMVC.Initializer;
import genMVC.Utility;
import genMVC.service.Service;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class BaseAspect implements MethodInterceptor {
    public interface Point {
        Object proceed(BaseAspect baseAspect, Method method, Object[] objects) throws Throwable;
    }

    private List<BaseAspect.Point> list;
    private int index;
    private Object target;

    public BaseAspect() {

    }

    public void loadAllAspect() {
        this.list = new ArrayList<>();
        String name = Initializer.packageName("aspect");
        List<Class> as = Utility.getPackageClass(name);
//        Utility.log("as: %s", as);
        try {
            for (Class a : as) {
                if (a.isAnnotationPresent(Aspect.class)) {
                    BaseAspect.Point aspectInstance = (BaseAspect.Point) a.getDeclaredConstructor().newInstance();
                    this.list.add(aspectInstance);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Object newProxyInstance(Object target) {
        this.target = target;
        this.loadAllAspect();
        //
        final Enhancer en = new Enhancer();
        en.setSuperclass(target.getClass());
        en.setCallback(this);
        return en.create();
    }

    public boolean condition(BaseAspect.Point point, Method invokeMethod) {
        String selfName = point.getClass().getSimpleName();
//        Utility.log("aspect: %s", selfName);
        Class clazz = (Class) this.target.getClass();
        boolean notedService = clazz.isAnnotationPresent(Service.class);

        if (notedService) {
            // Service 和 Aspect 修饰类, 类下的所有都走 Aspect 里面的 AOP
            // Aspect 里面没写 , 同样也只看 method 的 Aspect
            if (clazz.isAnnotationPresent(Aspect.class)) {
                Aspect a = this.target.getClass().getAnnotation(Aspect.class);
                if (a.id().contains(selfName)) {
                    return true;
                }
            }
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Aspect.class)) {
//                    Utility.log("method: %s", method.getName());
                    Aspect aspect = method.getAnnotation(Aspect.class);
                    String ids = aspect.id();
//                    Utility.log("id: %s", ids);
//                    Utility.log("equals: %s, %s", invokeMethod.getName(), method.getName());
//                    Utility.log("contains: %s", ids.contains(selfName));
                    if (invokeMethod.getName().equals(method.getName()) && ids.contains(selfName)) {
                        return true;
                    }
                }
            }
        } else {
            Utility.log("主类 没有 @Service 注释");
        }

        return false;
    }

    public Object proceed(Method method, Object[] objects) throws Throwable {
        Object result = null;
//        Utility.log("index: %s", index);
        if (++index == list.size()) {
            result = method.invoke(this.target, objects);
        } else {
            BaseAspect.Point point = list.get(index);
            if (condition(point, method)) {
                result = point.proceed(this, method, objects);
            } else {
                result = this.proceed(method, objects);
            }
        }
//        Utility.log("result:%s", result);
        return result;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        this.index = -1;
        return this.proceed(method, objects);
    }
}
