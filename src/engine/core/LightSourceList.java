package engine.core;

import engine.lighting.DirectionalLight;
import engine.lighting.PointLight;
import engine.patternInterfaces.Observable;
import engine.patternInterfaces.Observer;

import java.util.ArrayList;
import java.util.List;

public class LightSourceList implements Observable
{
    private List<Observer> observers = new ArrayList<>();
    private DirectionalLight mainLight;
    private List<PointLight> pointLights;

    public LightSourceList()
    {
        mainLight = null;
        pointLights = new ArrayList<>();
    }

    public void addSource(DirectionalLight source)
    {
        mainLight = source;
        notifyObservers();
    }

    public void addSource(PointLight source)
    {
        pointLights.add(source);
        notifyObservers();
    }

    public void removeMainLightSource(DirectionalLight targetSource)
    {
        mainLight = null;
        notifyObservers();
    }

    public void remove(int index)
    {
        pointLights.remove(index);
        notifyObservers();
    }

    public void remove(PointLight targetSource)
    {
        pointLights.remove(targetSource);
        notifyObservers();
    }

    public DirectionalLight getMainLightSource()
    {
        return mainLight;
    }

    public PointLight getPointLightSource(int index)
    {
        return pointLights.get(index);
    }

    public PointLight[] getPointLightSources()
    {
        PointLight[] sourcesOut = new PointLight[pointLights.size()];
        for (int i = 0; i < sourcesOut.length; i++)
            sourcesOut[i] = pointLights.get(i);
        return sourcesOut;
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