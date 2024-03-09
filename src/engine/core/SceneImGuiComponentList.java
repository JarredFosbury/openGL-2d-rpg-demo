package engine.core;

import engine.imGui.ImGuiRootComponent;
import engine.patternInterfaces.Observable;
import engine.patternInterfaces.Observer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SceneImGuiComponentList extends ArrayList<ImGuiRootComponent> implements Observable
{
    private List<Observer> observers = new ArrayList<>();
    private Queue<GenericListAction> entityListActionQueue = new LinkedList<>();
    private boolean inInitialization = true;

    public boolean add(ImGuiRootComponent component)
    {
        boolean out = entityListActionQueue.offer(new GenericListAction(component, 0, GenericListAction.ActionType.ADD_OBJECT));
        if (inInitialization)
            executeActionsInQueue();

        return out;
    }

    public void add(int index, ImGuiRootComponent component)
    {
        entityListActionQueue.offer(new GenericListAction(component, index, GenericListAction.ActionType.ADD_INDEXED));
        if (inInitialization)
            executeActionsInQueue();
    }

    public ImGuiRootComponent remove(int index)
    {
        entityListActionQueue.offer(new GenericListAction(null, index, GenericListAction.ActionType.REMOVE_INDEXED));
        if (inInitialization)
            executeActionsInQueue();
        return super.get(index);
    }

    public void remove(ImGuiRootComponent component)
    {
        entityListActionQueue.offer(new GenericListAction(component, 0, GenericListAction.ActionType.REMOVE_OBJECT));
        if (inInitialization)
            executeActionsInQueue();
    }

    public void clear()
    {
        entityListActionQueue.offer(new GenericListAction(null, 0, GenericListAction.ActionType.CLEAR_ALL_ENTITIES));
        if (inInitialization)
            executeActionsInQueue();
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

    public void executeActionsInQueue()
    {
        // run through queue and add or remove entities at the end of the frame to prevent changing List while it's being looped through
        boolean updatedEntityList = false;
        while (!entityListActionQueue.isEmpty())
        {
            GenericListAction entityAction = entityListActionQueue.poll();
            updatedEntityList = true;
            switch (entityAction.action)
            {
                case ADD_OBJECT -> super.add((ImGuiRootComponent) entityAction.target);
                case ADD_INDEXED -> super.add(entityAction.index, (ImGuiRootComponent) entityAction.target);
                case REMOVE_INDEXED -> super.remove(entityAction.index);
                case REMOVE_OBJECT -> super.remove(entityAction.target);
                case CLEAR_ALL_ENTITIES -> super.clear();
            }
        }

        if (updatedEntityList)
            notifyObservers();
    }

    public void endOfInit()
    {
        inInitialization = false;
        notifyObservers();
    }
}