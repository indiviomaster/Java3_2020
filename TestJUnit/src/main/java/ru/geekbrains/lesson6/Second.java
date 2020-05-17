package ru.geekbrains.lesson6;

public class Second {

    public boolean isOneOrFour(int [] array){
        boolean findOne = false;
        boolean findFour = false;
        boolean notOneOrFour = false;

        for (int i = 0; i < array.length; i++) {
            if (array[i]==1){
                findOne = true;
            } else if(array[i]==4){
                findFour = true;
            }else{
                notOneOrFour = true;
            }

        }
        return findOne&findFour&!notOneOrFour;

    }
}