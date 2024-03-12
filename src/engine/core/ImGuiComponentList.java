package engine.core;

import engine.imGui.ImGuiRootComponent;
import engine.patternInterfaces.Observable;
import engine.patternInterfaces.Observer;

import java.util.ArrayList;
import java.util.List;

public class ImGuiComponentList extends ArrayList<ImGuiRootComponent> implements Observable
{
    private List<Observer> observers = new ArrayList<>();

    public boolean add(ImGuiRootComponent component)
    {
        boolean out = super.add(component);
        notifyObservers();

        return out;
    }

    public void add(int index, ImGuiRootComponent component)
    {
        super.add(index, component);
        notifyObservers();
    }

    public ImGuiRootComponent remove(int index)
    {
        ImGuiRootComponent component = super.get(index);
        super.remove(index);
        notifyObservers();
        return component;
    }

    public void remove(ImGuiRootComponent component)
    {
        super.remove(component);
        notifyObservers();
    }

    public void clear()
    {
        super.clear();
        notifyObservers();
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