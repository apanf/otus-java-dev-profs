package homework.ioc;

import java.util.Set;

public interface Ioc {
    Object createClass(Class<?> clazz);

    Set<Class<?>> getProxiedClasses();
}
