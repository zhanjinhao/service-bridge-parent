package sb.core.operation.read.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * 用指定的类加载器序列化对象
 */
public class CurrentThreadLoaderObjectInputStream extends ObjectInputStream {

    public CurrentThreadLoaderObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    protected CurrentThreadLoaderObjectInputStream() throws IOException, SecurityException {
    }

    // 覆盖原有的加载类的机制，从当前线程的loader进行加载
    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc)
            throws ClassNotFoundException {
        String name = desc.getName();
        return Thread.currentThread().getContextClassLoader().loadClass(name);
    }

}
