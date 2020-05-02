import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;


public class app {
    public static void main(String[] args) {
        String[] names = {"Sasha", "Vanya", "Yura", "Marina"};
        Person[] people = Arrays.stream(names).map(Person::new).toArray(Person[]::new);

        Arrays.sort(people, Comparator.comparing(Person::getName, (s, t)-> Integer.compare(s.length(), t.length())));
        for (Person p: people){
            System.out.println(p.getName());
        }


    }
}

class Person {
    private String name;

    public Person(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

/*    @Override
    public int compareTo(Object o) {
        Person other = (Person) o;
        return this.name.compareTo(other.name);
    }*/


}
