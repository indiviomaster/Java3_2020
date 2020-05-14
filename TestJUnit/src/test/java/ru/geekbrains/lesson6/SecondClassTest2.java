package ru.geekbrains.lesson6;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SecondClassTest2 {

    @Parameterized.Parameters
    public static Collection<Object []> data(){
        return Arrays.asList(new Object [][]{
                {new int[]{1,4,4,4, 1,1,4,1,4}},
                {new int[]{4,1,4,4, 4,4,1,4,4}},
                {new int[]{4,4,4,4, 4,4,4,4,4}}
        });}

    static Second second;

    int[] inArray;

    public SecondClassTest2(int [] inArray) {
        this.inArray = inArray;
    }

    @BeforeClass
    public static void initSecondTest(){
        second = new Second();
    }

    @Test
    public void isOneOrFourTest1(){
        Assert.assertTrue(second.isOneOrFour(inArray));
    }
}


