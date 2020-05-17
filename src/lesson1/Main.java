package lesson1;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static <T> T[] swap(T[] array, int num1,int num2){
        T temp;
        if(num1<=array.length&&num2<=array.length&&num1!=num2){

            temp = array[num1];
           array[num1] = array[num2];
            array[num2] = temp;
        }
        return array;
    }
    public static <T> ArrayList toArrayList(T[] myArray){
        ArrayList arrList = new ArrayList();
        for (int i=0; i<myArray.length; i++){
            arrList.add(myArray[i]);
        }
        return arrList;
    }

    public static void main(String[] args) {
        String[] myArr = {"First","Second","Third","Forth", "Fifth"};
        Integer[] myArrInteger = {1,2,3,4,5};

        // Q2
        myArr=swap(myArr,2,4);
        myArrInteger=swap(myArrInteger,2,4);
        System.out.println(Arrays.toString(myArr));
        System.out.println(Arrays.toString(myArrInteger));
        // Q2
        ArrayList myArrayList;
        myArrayList = toArrayList(myArr);
        System.out.println(myArrayList);

        ArrayList myArrayList2;
        myArrayList2 = toArrayList(myArrInteger);
        System.out.println(myArrayList2);

        // Q 3
        Orange orr1 = new Orange();
        System.out.println(orr1.getWeight());
        Apple app1 = new Apple();
        Box<Apple> appleBox = new Box<>();
        Box<Orange> orangeBox = new Box<>();
        Box<Apple> appleBox2 = new Box<>();
        Box<Orange> orangeBox2 = new Box<>();
        for (int i=0;i<=9;i++){
            appleBox.add(new Apple());
            orangeBox.add(new Orange());
            appleBox2.add(new Apple());
        }


        System.out.println("Коробка яблок и вторая коробка яблок:" + appleBox.compare(appleBox2));
        System.out.println("Коробка яблок и коробка апельсин:" + appleBox.compare(orangeBox));
        System.out.println("Добавим 1 яблоко во вторую коробку");
        appleBox2.add(new Apple());
        System.out.println("Коробка яблок и вторая коробка яблок:" + appleBox.compare(appleBox2));

        System.out.println("Коробка яблок весит: " + appleBox.getWeight());
        System.out.println("Коробка апельсин весит: " + orangeBox.getWeight());
        System.out.println("Пересыпим яблоки из коробки 1 в коробку 2");
        appleBox.pourFrom(appleBox2);
        System.out.println("Коробка яблок весит: " + appleBox.getWeight());
        System.out.println("Коробка яблок2 весит: " + appleBox2.getWeight());
    }
}
