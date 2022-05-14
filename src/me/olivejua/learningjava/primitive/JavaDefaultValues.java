package me.olivejua.learningjava.primitive;

public class JavaDefaultValues {
    char DeclaredVariable;  //Declaring variable 'DeclaredVariable'
    public static void main(String[] args) {
        char InitialisedVariable = '\u0000';
        JavaDefaultValues DefaultValues = new JavaDefaultValues();  //Creating object of class JavaDefaultValues
        System.out.println("Value of DeclaredVariable = " + DefaultValues.DeclaredVariable);   //Printing value of DeclaredVariable
        System.out.println("Value of InitialisedVariable = " + InitialisedVariable);     //Printing value ppf InitialisedVariable
        System.out.println(DefaultValues.DeclaredVariable == InitialisedVariable);
        char a = '\uffff';
        System.out.println("a = " + a);
        long l = 123____456_____789;
        int x6 = 0x5_2;
        System.out.println("x6 = " + x6);
    }
}