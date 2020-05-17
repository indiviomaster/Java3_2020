package ru.geekbrains.lesson6;

public class First {

    public int[] arrayAfterNumber(int [] array){

        int fNumber = 4;
        int lastNumber = -1;
        boolean findNumber = false;
        for (int i = 0; i < array.length; i++) {
            if (array[i]==fNumber){
                lastNumber = i;
                findNumber = true;
            }
        }
        System.out.println(lastNumber);
        if(findNumber&&(array.length-lastNumber-1>0)) {

            int outArray[] =new int[array.length-lastNumber-1];
            for (int i = lastNumber+1, j=0; i <array.length ; i++,j++) {
                outArray[j] = array[i];
            }

            return outArray;
        }
        else
        {
            throw new RuntimeException("Массива после цифры 4 нет или цифры 4 не существует");
        }
    }
}
