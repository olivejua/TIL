package me.olivejua.learningjava.callbyreference;

class Test2
{
    int x;
    Test2(int i) { x = i; }
    Test2()      { x = 0; }
}

class Main2
{
    public static void main(String[] args)
    {
        // t is a reference
        Test2 t = new Test2(5);

        // Reference is passed and a copy of reference
        // is created in change()
        change(t);

        // New value of x is printed
        System.out.println(t.x);
    }

    // This change() doesn't change the reference, it only
    // changes member of object referred by reference
    public static void change(Test2 t)
    {
        t.x = 10;
    }
}
