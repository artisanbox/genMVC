package genMVC;

import genMVC.aspect.BaseAspect;
import genMVC.controller.BaseController;
import genMVC.controller.Controller;
import genMVC.service.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;



public class Initializer {

    public static String project;
    public static String beginNotice;
    public static String endNotice;
    public static String errNotice;
    public BaseController baseController;

    public Initializer() {
        init();
    }

    public void init() {
        try {
            Properties properties = Utility.loadResource("application.properties");
            project = properties.getProperty("project");
            beginNotice = properties.getProperty("beginNotice");
            endNotice = properties.getProperty("endNotice");
            errNotice = properties.getProperty("errNotice");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String packageName(String name) {
        name = name.toLowerCase().trim();
        return String.format("%s.%s", project, name);
    }

    public List packageClass(String name) {
        String s = packageName(name);
        return Utility.getPackageClass(s);
    }

    public Object newClass(Class c) throws Exception {
        Object o = c.getDeclaredConstructor().newInstance();
        return o;
    }

    public void serviceIntoController(Class c) throws Exception {
        // 每一个 controller 的 字段 准备注入
        int size = c.getDeclaredFields().length;
        Object[] os = new Object[size];
        Class<?>[] serviceClasses = new Class[size];
        for (int i = 0; i < size; i++) {
            Field declaredField = c.getDeclaredFields()[i];
            Object service = this.newClass(declaredField.getType());
            Class serviceClass = service.getClass();
            // 满足注入条件
            if (serviceClass.isAnnotationPresent(Service.class)) {
                // 实例化 baseAspect
                Object baseAspectObj = this.newClass(BaseAspect.class);
                Method newProxyInstance = baseAspectObj.getClass().getDeclaredMethod("newProxyInstance", Object.class);
                // 实例化 service
                Object serviceObj = this.newClass(serviceClass);
                Object proxyObj = newProxyInstance.invoke(baseAspectObj, serviceObj);
                //
                serviceClasses[i] = serviceClass;
                os[i] = proxyObj;
            } else {
                Utility.log("Service 注入 需要 @Service");
            }
        }
        // 注入
        Object controllerObj = c.getDeclaredConstructor(serviceClasses).newInstance(os);
        this.baseController.controllerObjs.add(controllerObj);
    }

    public void start() {
        try {
            // base controller
            this.baseController = BaseController.class.getDeclaredConstructor().newInstance();
            //
            List<Class> controllerList = this.packageClass("controller");
//            Utility.log("con: %s", controllerList);
            this.baseController.scanControllers(controllerList);

//            Utility.log("con: %s", controllerList);
            // 遍历所有的 controller
            for (Class c : controllerList) {
                if (c.isAnnotationPresent(Controller.class)) {
                    if (c.getDeclaredFields().length == 0) {
                        Object noServiceController = c.getDeclaredConstructor().newInstance();
                        this.baseController.controllerObjs.add(noServiceController);
                    } else {
                        // service 注入 controller
                        serviceIntoController(c);
                    }
                }
            }
            this.baseController.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
