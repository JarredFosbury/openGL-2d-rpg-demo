package engine.core;

public class GenericListAction
{
    enum ActionType
    {
        ADD_OBJECT,
        ADD_INDEXED,
        REMOVE_OBJECT,
        REMOVE_INDEXED,
        CLEAR_ALL_ENTITIES;
    }

    public Object target;
    public int index;
    public ActionType action;

    public GenericListAction(Object target, int index, ActionType action)
    {
        this.target = target;
        this.index = index;
        this.action = action;
    }
}