public class Sphere extends Figure implements Comparable<Sphere> {
    private Sphere other;

    public Sphere(String name) {
        super(name);
    }

    public String getDescription() {
        return name;
    }

    @Deprecated public String test(String... args) {
        if (args.length == 0){
            return "Empty";
        }
        String[] ar = (String[]) args;
        for (int i = 1; i < args.length; ++i){
            ar[0] += ar[i];
        }
        return ar[0];
    }

    public int compareTo(Sphere other){
        return name.compareTo(other.name);
    }

}
