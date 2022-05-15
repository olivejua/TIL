package me.olivejua.learningjava.autoboxing;

class GFG {

    public static void main(String[] args)
    {

        // Getting an integer value
        Integer i = Integer.valueOf(12);

        // Printing the same integer value
        System.out.println(i);

        // Calling method 2
        modify(i);

        // Now printing the value stored in above integer
        System.out.println(i);
    }

    // Method 2
    // To modify integer value
    private static void modify(Integer i) { i = i + 1; }
}
