package me.olivejua.learningjava.gc;

public class G1GCNewDemo {

    public static void main(String[] args) {
        A a = new A();
    }

    static class A {
        B b;

        public A() {
            this.b = new B();
        }
    }

    static class B {
        C c;

        public B() {
            c = new C("foo");
        }
    }

    static class C {
        String name;

        public C(String name) {
            this.name = name;
        }
    }
}
