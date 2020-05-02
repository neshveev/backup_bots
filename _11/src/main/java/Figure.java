public abstract class Figure{
    protected String name;

    public Figure(String name){this.name = name;}

    public abstract String getDescription();

    public String getName() {
        return name;
    }
}
