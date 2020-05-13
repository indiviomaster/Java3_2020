package ru.geekbrains.lesson6;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SecondTest {

    Second second;

    @Before
    public void initSecondTest(){
        second = new Second();
    }
    @Test
    public void isNotOneOrFourTest(){
        int [] inArray = {1,2,4,4, 2,3,4,1,7};
        Assert.assertFalse(second.isOneOrFour(inArray));
    }
    @Test
    public void isOneOrFourTest1(){
        int [] inArray = {1,4,4,4,1,4,4};
        Assert.assertTrue(second.isOneOrFour(inArray));
    }
    @Test
    public void isNotOneOrFourTest2(){
        int [] inArray = {4,4,4,4,4,4,4};
        Assert.assertFalse(second.isOneOrFour(inArray));
    }
}
