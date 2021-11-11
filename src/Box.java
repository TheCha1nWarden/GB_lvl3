import java.util.ArrayList;
import java.util.Arrays;

public class Box<T extends Fruit>{
    private ArrayList<T> storage;

    public Box(ArrayList<T> storage) {
        this.storage = storage;
    }

    public float getWeight() {
        return storage.get(0).getWeight() * storage.size();
    }
    public boolean compare(Box<?> box){
        return  (getWeight() == box.getWeight());
    }

    public void move(Box<T> box) {
        box.storage.addAll(storage);
        storage.clear();
    }
    public void add(T... fruits) {
        storage.addAll(Arrays.asList(fruits));
    }


}
