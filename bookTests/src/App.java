public class App {
    public static void main(String[] args) {
        A a = new A();
        B b = new B();

        a.f("aaa");
        b.f("aa");
        b.f(4);
    }
}

class A{
    void f(String a){
        System.out.println("A");
    }
}


class B extends A{
    void f(int a){
        System.out.println("B");
    }
    void f(String a) {
        System.out.println("B");
    }
}
