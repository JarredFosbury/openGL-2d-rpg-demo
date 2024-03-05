package engine.core;

public class EntityListAction
{
    enum ActionType
    {
        ADD_OBJECT,
        ADD_INDEXED,
        REMOVE_OBJECT,
        REMOVE_INDEXED;
    }

    public Entity target;
    public int index;
    public ActionType action;

    public EntityListAction(Entity target, int index, ActionType action)
    {
        this.target = target;
        this.index = index;
        this.action = action;
    }
}