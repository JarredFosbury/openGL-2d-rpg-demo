package engine.patternInterfaces;

public interface Observable
{
    void notifyObservers();
    void addObserver(Observer target);
    void removeObserver(Observer target);
}