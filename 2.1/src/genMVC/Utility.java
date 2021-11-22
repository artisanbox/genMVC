package genMVC;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utility {
    public static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    public static void ensure(boolean condition, String message) {
        if (!condition) {
            log("%s", message);
        } else {
            log("测试成功");
        }
    }

    public static String getType(Object o) {
        return o.getClass().toString();
    }

    public static Scanner input(String message) {
        String m = String.format("%s: ", message);
        System.out.print(m);
        Scanner scanner = new Scanner(System.in);
        return scanner;
    }

    public static int randomBetween(int start, int end) {
        // start - end (end 取不到)
        Random rand = new Random();
        // rand.nextInt(100) => 0 - 99
        return rand.nextInt(end - start) + start;
    }

    public static int[] randomArray(int size, int startIndex, int endIndex) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = randomBetween(startIndex, endIndex);
        }
        return arr;
    }

    public static <T> void shuffleArray(ArrayList<T> list) {
        ArrayList<T> s = list;
        int len = s.size();
        for (int i = len - 1; i >= 0; i--) {
            int randomIndex = randomBetween(0, i + 1);
            T temp = s.get(randomIndex);
            s.set(randomIndex, s.get(i));
            s.set(i, temp);
        }
    }

    public static String shuffleString(String str) {
        String[] strArray = str.split("");
        ArrayList<String> list = new ArrayList<>(Arrays.asList(strArray));
        shuffleArray(list);
        return list.toString().replace(", ", "");
    }

    public static String randomString(int length) {
        String rawData = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+{}|:\"<>?`-=[]\\;',./'";
        // 洗牌
        String str = shuffleString(rawData);
        int len = str.length();
        StringBuilder verifyCode = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randNumber = randomBetween(0, len);
            verifyCode.append(str.charAt(randNumber));
        }
        return verifyCode.toString();
    }

    public static Long unixTime() {
        return System.currentTimeMillis() / 1000L;
    }

    public static String formatTime(Long unixTime) {
        String pattern = "yyyy/mm/dd hh:mm:ss";
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date date = new Date(unixTime * 1000);
        String dateString = df.format(date);
        return dateString;
    }

    public static <T> Type[] TClass(T self) {
        Type type = self.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            // 参数化类型中可能有多个泛型参数
            Type[] types = parameterizedType.getActualTypeArguments();
            return types;
        }
        return null;
    }

    public static <T> void show(List<T> obj) {
        for (T o : obj) {
            String s = o.toString();
            Utility.log("%s", s);
        }
    }

    public static List<Class> getSonClass(Class fatherClass) {
        List<Class> sonClassList = new ArrayList<Class>();
        String packageName = fatherClass.getPackage().getName();
        List<Class> packageClassList = getPackageClass(packageName);
        for (Class clazz : packageClassList) {
            if (fatherClass.isAssignableFrom(clazz) && !fatherClass.equals(clazz)) {
                sonClassList.add(clazz);
            }
        }
        return sonClassList;
    }

    public static List<Class> getPackageClass(String packageName) {
        ClassLoader loader = Utility.class.getClassLoader();
        String path = packageName.replace(".", "/");
        Enumeration<URL> resources = null;
        try {
            resources = loader.getResources(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<File> fileList = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            fileList.add(new File(resource.getFile()));
        }
        ArrayList<Class> classList = new ArrayList<Class>();
        for (File file : fileList) {
            classList.addAll(findClass(file, packageName));
        }
        return classList;
    }

    public static HashMap<String, Class> getPackageClassMap(String packageName) {
        ClassLoader loader = Utility.class.getClassLoader();
        String path = packageName.replace(".", "/");
        Enumeration<URL> resources = null;
        try {
            resources = loader.getResources(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<File> fileList = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            fileList.add(new File(resource.getFile()));
        }
        HashMap<String, Class> classMap = new HashMap<>();
        for (File file : fileList) {
            List<Class> aClass = findClass(file, packageName);
            for (Class c : aClass) {
                classMap.put(c.getName(), c);
            }
        }
        return classMap;
    }

    private static List<Class> findClass(File file, String packageName) {
        List<Class> classList = new ArrayList<>();
        if (!file.exists()) {
            return classList;
        }
        File[] fileArray = file.listFiles();
        for (File subFile : fileArray) {
            if (subFile.isDirectory()) {
                assert !file.getName().contains(".");
                classList.addAll(findClass(subFile, packageName + "." + subFile.getName()));
            } else if (subFile.getName().endsWith(".class")) {
                try {
                    classList.add(Class.forName(packageName + "." + subFile.getName().split(".class")[0]));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return classList;
    }

    public static Properties loadResource(String path) throws IOException {
        Properties properties = new Properties();
        InputStreamReader reader = new InputStreamReader(Initializer.class.getClassLoader().getResourceAsStream(path));
        properties.load(reader);
        return properties;
    }

}
