package lesson1;

import java.util.ArrayList;

public class Box<T extends Fruit> {
    ArrayList <T> inbox  = new ArrayList <T>();;
    float weight=0;

    public float getWeight() {
        return this.weight*this.inbox.size();
    }

    public void add(T obj){
        this.inbox.add(obj);
        this.weight=obj.weight;
    }

    public boolean compare(Box<? extends Fruit> obj) {
        return Math.abs(this.getWeight()-obj.getWeight())<0.0001;
    }
    public void pourFrom(Box<T> obj) {

        for(int i=obj.inbox.size();i>0;i--){
            this.inbox.add((T) obj.inbox.remove(i-1));
        }
    }
}
