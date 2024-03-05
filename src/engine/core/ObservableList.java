package engine.core;

import engine.patternInterfaces.Observable;
import engine.patternInterfaces.Observer;

import java.util.ArrayList;
import java.util.List;

public class ObservableList<E> extends ArrayList<E> implements Observable
{
    private List<Observer> observers = new ArrayList<>();

    @Override
    public boolean add(E e)
    {
        notifyObservers();
        return super.add(e);
    }

    @Override
    public void add(int index, E e)
    {
        notifyObservers();
        super.add(index, e);
    }

    @Override
    public E remove(int index)
    {
        notifyObservers();
        return super.remove(index);
    }

    @Override
    public boolean remove(Object e)
    {
        notifyObservers();
        return super.remove(e);
    }

    @Override
    public void notifyObservers()
    {
        for (Observer observer : observers)
            observer.update(this);
    }

    @Override
    public void addObserver(Observer target)
    {
        observers.add(target);
    }

    @Override
    public void removeObserver(Observer target)
    {
        observers.remove(target);
    }
}