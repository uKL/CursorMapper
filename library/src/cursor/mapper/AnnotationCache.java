package cursor.mapper;

import static java.util.Collections.emptyList;

import java.lang.reflect.Field;
import java.util.List;

public class AnnotationCache {

    private static class SingletonHolder {

        private static final AnnotationCache INSTANCE = new AnnotationCache();
    }

    private AnnotationCache() {
    }

    public static AnnotationCache getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public boolean contains(Class clazz) {
        return false;
    }

    public void evict() {

    }

    public List<Field> get(Class clazz) {
        return emptyList();
    }

    public void put(Class clazz, List<Field> declaredFields) {

    }
}
