package com.wenjian.commonskill;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

/**
 * ExampleService local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        List<String> strings = Arrays.asList("1", "1", "2", "2", "4");

        assertEquals(getPageCount(strings, 3), 2);
        assertEquals(getPageCount(strings, 2), 3);
        assertEquals(getPageCount(strings, 1), 5);


    }

    private int getPageCount(List<String> mData, int n) {
        int left = mData.size() % n;
        if (left > 0) {
            return mData.size() / n + 1;
        } else {
            return Math.round(mData.size() / n);
        }
    }

    @Test
    public void test_equals() {

        Person p1 = new Person("wenjian", "nan");
        Person p2 = new Person("wenjian", "nan");
        System.out.println(p1.equals(p2));
    }

    @Test
    public void test_constant_pool() {
        Integer i1 = new Integer(2);
        Integer i2 = new Integer(2);
        System.out.println(i1 == i2);

        //在常量池内
        int i3 = 2;
        int i4 = 2;
        System.out.println(i3 == i4);

        //不在常量池内
        int i5 = 129;
        int i6 = 129;
        System.out.println(i5 == i6);

        //不在常量池内
        Integer i7 = 129;
        Integer i8 = 129;
        System.out.println(i7 == i8);

        //在常量池内
        Integer i9 = 127;
        Integer i10 = 127;
        System.out.println(i9 == i10);


    }


    public static void quickSort(int[] arr) {
        qsort(arr, 0, arr.length - 1);
    }

    private static void qsort(int[] arr, int low, int high) {
        if (low < high) {
            partion();

        }
    }

    private static void partion() {

    }


    class Person {
        String name;
        String sex;

        public Person(String name, String sex) {
            this.name = name;
            this.sex = sex;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return Objects.equals(name, person.name) &&
                    Objects.equals(sex, person.sex);
        }

    }


}