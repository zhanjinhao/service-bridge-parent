package sb.client.proxy;

import com.alibaba.fastjson.JSON;
import sb.client.pojo.URLPojo;
import sb.client.utils.HttpAccessUtil;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class SBHttpClientProxy {

    private URLPojo urlPojo;

    public SBHttpClientProxy(URLPojo urlPojo) {
        this.urlPojo = urlPojo;
    }

    public Object createHttpClient(Class interfaceClass) throws Exception {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        /*
         * proxy：传入代理对象。
         * method：被执行的方法。
         * args：传入的参数。
         * 例子：  newProxyInstance.ShootFilm("我不是药神");
         *      proxy：newProxyInstance；      method：ShootFilm；       args："我不是药神"
         */
        Object httpClient = Proxy.newProxyInstance(
                //代理类的类加载器，获取目标类加载器即可
                loader,
                //代理类应该实现的接口，由于代理类和目标类需要继承相同的接口，使用目标类的接口即可
                new Class[]{interfaceClass},
                //使用匿名内部类传入InvocationHandler的实例

                (proxy, method, args) -> {
                    //执行前的操作
                    List list = new ArrayList();
                    int length = args.length;
                    for (int i = 0; i < length; i++) {
                        list.add(args[i]);
                    }
                    String s1 = method.toString().replaceAll(" ", "-");
                    String result = HttpAccessUtil.httpPostSerialObject(urlPojo.getFullPath() + s1, urlPojo.getConnectTimeOut(), urlPojo.getReadTimeOut(), list);
                    //执行后的操作

                    return JSON.parseObject(result, method.getReturnType());
                });
        return httpClient;
    }
}