package genMVC.controller;

import genMVC.Initializer;
import genMVC.Utility;

import javax.xml.bind.SchemaOutputResolver;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class BaseController {
    public static HashMap<String, Method> actionMap = new HashMap<>();
    public ArrayList<Object> controllerObjs = new ArrayList<>();

    public BaseController() {}

    public Object inputFromType(String noticeString, String type) throws Exception {
        Object in;
        String name = type;
        Scanner scanner = Utility.input(noticeString);
        if (name.equals("String") || name.equals("Character")) {
            in = scanner.next();
        } else if (name.equals("Integer")) {
            in = scanner.nextInt();
        } else if (name.equals("Double")) {
            in = scanner.nextDouble();
        } else if (name.equals("Float")) {
            in = scanner.nextFloat();
        } else if (name.equals("Boolean")) {
            in = scanner.nextBoolean();
        } else {
            throw new Exception("变量类型只能是 <包装类> String Integer Double Float Boolean");
        }
        return in;
    }

    public void dispatcher(Object id) throws Exception {
        Method method = BaseController.actionMap.getOrDefault(String.valueOf(id), null);
//        Utility.log("conobj: %s", this.controllerObjs);
//        Utility.log("method: %s", method);
        if (method == null) {
            Utility.log(Initializer.errNotice);
            return;
        }
        for (Object o : this.controllerObjs) {
            for (Method m : o.getClass().getMethods()) {
                // 判断方法是否对应
                if (m.equals(method)) {
                    String[] args = method.getAnnotation(InputCommand.class).args().split("\\|\\|");
                    Parameter[] parameters = m.getParameters();
                    Object[] parameterObjects = new Object[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        Parameter parameter = parameters[i];
                        String name = parameter.getType().getSimpleName();
                        String noticeString = args[0].trim();
                        parameterObjects[i] = this.inputFromType(noticeString, name);
                    }
                    method.invoke(o, parameterObjects);
                    break;
                }
            }
        }
    }

    public void init() {
        String beginNotice = Initializer.beginNotice;
        String endNotice = Initializer.endNotice;
        String errNotice = Initializer.errNotice;
        while (true) {
            try {
                String command = Utility.input(beginNotice).next();
                if (command.equals("0")) {
                    Utility.log(endNotice);
                    break;
                }
                this.dispatcher(command);
            } catch (InputMismatchException e) {
                System.err.println(errNotice);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void scanControllers(List<Class> controllers) {
        for (Class controller : controllers) {
            if (controller.isAnnotationPresent(Controller.class)) {
                Class clazz = controller;
                //        Utility.log("scanConfig: %s", clazz);
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(InputCommand.class)) {
                        InputCommand inputCommand = method.getAnnotation(InputCommand.class);
//                        actionMap.put(inputCommand.command(), method);
                        String command = inputCommand.command();
                        if (actionMap.containsKey(command)) {
                            Utility.log("<%s> inputCommand 重复了", command);
                        } else {
                            actionMap.put(command, method);
                        }
                    }
                }
            }
        }
//        Utility.log("actionMap:%s", this.actionMap);
    }
}
