package homework;

public interface UnitOfWork<T> {
    void doWork();

    void doWork(T o);

    void doWork(T... args);

    void doWork(int first, int last, T... args);
}
