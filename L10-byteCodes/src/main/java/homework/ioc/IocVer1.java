package homework.ioc;

import homework.annotations.AnnotationHandler;
import homework.annotations.AnnotationHandler.AnnotationDto;
import homework.annotations.AnnotationHandler.HandlePhase;
import lombok.Getter;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static homework.annotations.AnnotationHandler.HandlePhase.AFTER;
import static homework.annotations.AnnotationHandler.HandlePhase.AROUND;
import static homework.annotations.AnnotationHandler.HandlePhase.BEFORE;

/*
Класс контейнера до оптимизации (рефлексия).
 */
public class IocVer1 implements Ioc {
    @Getter
    private final Map<Class<?>, InvocationHandler> handlers = new HashMap<>();
    private final Map<Class<? extends Annotation>, AnnotationHandler> annotationHandlers;

    public IocVer1(Map<Class<? extends Annotation>, AnnotationHandler> annotationHandlers) {
        this.annotationHandlers = annotationHandlers;
    }

    public Object createClass(Class<?> clazz) {
        InvocationHandler handler = handlers.computeIfAbsent(clazz, Handler::new);
        Class<?>[] interfaces = clazz.getInterfaces();

        return Proxy.newProxyInstance(this.getClass().getClassLoader(), interfaces, handler);
    }

    @Override
    public Set<Class<?>> getProxiedClasses() {
        return handlers.keySet();
    }

    private class Handler<T> implements InvocationHandler {
        private final T instance;
        /*
        Method - методо содержащий обрабатываемую аннотацию.
        HandlePhase - фаза обработки аннотации.
        List - список обработчиков аннотаций на конкретной фазе.
         */
        private final Map<Method, Map<HandlePhase, List<AnnotationHandler>>> mappedAnnotationHandlers = new HashMap<>();
        private static final List<HandlePhase> BEFORE_METHOD_PHASES = List.of(BEFORE, AROUND);
        private static final List<HandlePhase> AFTER_METHOD_PHASES = List.of(AROUND, AFTER);

        public Handler(Class<T> instanceClass) {
            try {
                this.instance = instanceClass.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            mapAnnotationHandlers();
        }

        private void mapAnnotationHandlers() {
            Method[] methods = instance.getClass().getDeclaredMethods();

            for (var method : methods)
                Arrays.stream(method.getAnnotations())
                        .map(Annotation::annotationType)
                        .filter(annotationHandlers::containsKey)
                        .forEach(a -> putAnnotationHandler(method, a));
        }

        private void putAnnotationHandler(Method method, Class<? extends Annotation> annotationType) {
            AnnotationHandler currAnnotationHandler = annotationHandlers.get(annotationType);
            HandlePhase currHandleType = currAnnotationHandler.getHandlePhase();

            mappedAnnotationHandlers.putIfAbsent(method, new HashMap<>());
            mappedAnnotationHandlers.computeIfPresent(method, (k, v) -> {
                v.putIfAbsent(currHandleType, new ArrayList<>());
                v.get(currHandleType).add(currAnnotationHandler);
                return v;
            });
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method proxiedInstanceMethod =
                    MethodUtils.getMatchingMethod(instance.getClass(), method.getName(), method.getParameterTypes());
            var handleTypeMap =
                    Optional.ofNullable(mappedAnnotationHandlers.get(proxiedInstanceMethod)).orElseGet(HashMap::new);
            AnnotationDto dto = AnnotationDto.builder().proxy(proxy).method(method).args(args).build();
            Object result;

            processAnnotationsForSpecificPhases(handleTypeMap, BEFORE_METHOD_PHASES, dto);
            result = method.invoke(instance, args);
            processAnnotationsForSpecificPhases(handleTypeMap, AFTER_METHOD_PHASES, dto);
            return result;
        }

        private void processAnnotationsForSpecificPhases(Map<HandlePhase, List<AnnotationHandler>> handleTypeMap,
                                                         List<HandlePhase> typeList,
                                                         AnnotationDto dto) {
            for (var type : typeList)
                if (handleTypeMap.containsKey(type))
                    for (var handler : handleTypeMap.get(type))
                        handler.handle(dto);
        }
    }
}