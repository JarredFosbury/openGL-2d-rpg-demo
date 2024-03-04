package implementationDemos;

public class Main
{
    public static void main(String[] args)
    {
        ObservableList<Integer> myList = new ObservableList<>();
        ListWatcher notify = new ListWatcher();
        myList.addObserver(notify);
        myList.add(1);
        myList.add(1, 22);
        myList.remove(0);
        myList.remove(myList.get(0));
    }
}