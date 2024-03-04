package implementationDemos;

public class ListWatcher implements Observer
{
    @Override
    public void update(Observable target)
    {
        System.out.println("List has been changed!");
    }
}