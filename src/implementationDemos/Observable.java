package implementationDemos;

public interface Observable
{
    void notifyObservers();
    void addObserver(Observer target);
}