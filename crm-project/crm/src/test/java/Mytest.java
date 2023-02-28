import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class Mytest {

    public static void main(String[] args) {
        ArrayList<Dog> dogs = new ArrayList<Dog>();
        Dog dog = new Dog();
        for (int i = 0; i < 10; i++) {
            dog.setName("小狗:" + i);
            dogs.add(dog);
        }
        System.out.println(dogs);
    }
}

class Dog{
    private String name;

    public String getName() {
        return name;
    }

    public Dog() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dog(String name) {
        this.name = name;
    }
}
