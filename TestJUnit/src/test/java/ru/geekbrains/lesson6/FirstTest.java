package ru.geekbrains.lesson6;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FirstTest {
    First first;
    @Before
    public void initTest(){
        first = new First();
    }


    @Test
    public void arrayAfterNumberTest(){

        int [] inArray = {1,2,4,4, 2,3,4,1,7};
        int [] outArr= {1,7};
        Assert.assertArrayEquals(outArr,first.arrayAfterNumber(inArray));
    }

    @Test
    public void arrayAfterNumberTest2(){

        int [] inArray = {1,2,4,4, 2,3,1,1,7};
        int [] outArr= {2,3,1,1,7};
        Assert.assertArrayEquals(outArr,first.arrayAfterNumber(inArray));
    }

    @Test(expected = RuntimeException.class)
    public void noArrayAfterNumberTest(){
        int [] inArray = {1,2,4,4, 2,3,4,1,4};
        int [] outArr= {1,7};
        Assert.assertArrayEquals(outArr,first.arrayAfterNumber(inArray));
    }
}
