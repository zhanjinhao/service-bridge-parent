package sb.rest.springcloud;

import org.junit.Test;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class ComparableAndComparatorTest implements Serializable {

    @Test
    public void test1() {

        /**
         * This interface imposes a total ordering on the objects of each class that implements it.
         * This ordering is referred to as the class's natural ordering, and the class's compareTo
         * method is referred to as its natural comparison method.
         *
         * Lists (and arrays) of objects that implement this interface can be sorted automatically
         * by Collections.sort (and Arrays.sort). Objects that implement this interface can be used
         * as keys in a sorted map or as elements in a sorted set, without the need to specify a comparator.
         *
         * The natural ordering for a class C is said to be consistent with equals if and only if
         * e1.compareTo(e2) == 0 has the same boolean value as e1.equals(e2) for every e1 and e2 of class C.
         * Note that null is not an instance of any class, and e.compareTo(null) should throw a NullPointerException
         * even though e.equals(null) returns false.
         *
         * It is strongly recommended (though not required) that natural orderings be consistent with equals.
         * This is so because sorted sets (and sorted maps) without explicit comparators behave "strangely"
         * when they are used with elements (or keys) whose natural ordering is inconsistent with equals.
         * In particular, such a sorted set (or sorted map) violates the general contract for set (or map),
         * which is defined in terms of the equals method.
         *
         * For example, if one adds two keys a and b such that (!a.equals(b) && a.compareTo(b) == 0) to a sorted set
         * that does not use an explicit comparator, the second add operation returns false (and the size of
         * the sorted set does not increase) because a and b are equivalent from the sorted set's perspective.
         *
         * Virtually all Java core classes that implement Comparable have natural orderings that are consistent
         * with equals. One exception is java.math.BigDecimal, whose natural ordering equates BigDecimal objects
         * with equal values and different precisions (such as 4.0 and 4.00).
         *
         * For the mathematically inclined, the relation that defines the natural ordering on a given class C is:
         *          {(x, y) such that x.compareTo(y) <= 0}.
         * The quotient for this total order is:
         *          {(x, y) such that x.compareTo(y) == 0}.
         * It follows immediately from the contract for compareTo that the quotient is an equivalence relation on C,
         * and that the natural ordering is a total order on C. When we say that a class's natural ordering is consistent
         * with equals, we mean that the quotient for the natural ordering is the equivalence relation defined by the class's
         * equals(Object) method:
         *        {(x, y) such that x.equals(y)}.
         */

        /**
         * 实现 Comparable 接口的类可以被 Arrays.sort() 和 Colletions.sort() 直接配许
         */
        Person[] persons = {new Person(120), new Person(22), new Person(33)};
        Arrays.sort(persons);
        System.out.println(Arrays.deepToString(persons));

        List<Person> people = Arrays.asList(new Person(120), new Person(22), new Person(33));
        Collections.sort(people);
        System.out.println(people);
    }

    /**
     * 对于 null 应该抛出 NullPointerException
     */
    @Test
    public void test2() {
        Person[] persons = {new Person(120), new Person(22), new Person(33), null};
        Arrays.sort(persons);
        System.out.println(Arrays.deepToString(persons));
    }

    /**
     * equals() 和 compareTo() 在表示相等的时候，行为应当一致，否则会出现奇怪的问题。
     */
    @Test
    public void test3() {
        SortedSet<Person> set = new TreeSet<>();
        set.add(new Person(10));
        set.add(new Person(20));
        System.out.println(set); // 之加入一个元素
    }


    /**
     * JDK 提供的类中，除了 BigDecimal，其他的全部保证 equals() 和 compareTo() 在表示相等的时候行为一致，
     * 因为 BigDecimal 需要考虑精度问题
     */
    @Test
    public void test4() {
        BigDecimal bigDecimal1 = new BigDecimal("4.00");
        BigDecimal bigDecimal2 = new BigDecimal("4.0");
        System.out.println(bigDecimal1.equals(bigDecimal2));
        System.out.println(bigDecimal1.compareTo(bigDecimal2));
    }

    /**
     * 自然排序满足如下条件时是升序排序，应当保证此时语义和现实语义相符：
     * o.compareTo() < 0
     * o.compareTo() > 0
     * o.compareTo() == 0
     */
    @Test
    public void test5() {
        Person[] persons = {new Person(120), new Person(22), new Person(33)};
        Arrays.sort(persons);
        System.out.println(Arrays.deepToString(persons));       // 升序
    }

    @Test
    public void test6() {
        /**
         * A comparison function, which imposes a total ordering on some collection of objects. Comparators can be
         * passed to a sort method (such as Collections.sort or Arrays.sort) to allow precise control over the sort
         * order. Comparators can also be used to control the order of certain data structures (such as sorted sets
         * or sorted maps), or to provide an ordering for collections of objects that don't have a natural ordering.
         *
         * The ordering imposed by a comparator c on a set of elements S is said to be consistent with equals if and
         * only if c.compare(e1, e2)==0 has the same boolean value as e1.equals(e2) for every e1 and e2 in S.
         *
         * Caution should be exercised when using a comparator capable of imposing an ordering inconsistent with equals
         * to order a sorted set (or sorted map). Suppose a sorted set (or sorted map) with an explicit comparator c is
         * used with elements (or keys) drawn from a set S. If the ordering imposed by c on S is inconsistent with equals,
         * the sorted set (or sorted map) will behave "strangely." In particular the sorted set (or sorted map) will
         * violate the general contract for set (or map), which is defined in terms of equals.
         *
         * For example, suppose one adds two elements a and b such that (a.equals(b) && c.compare(a, b) != 0) to an empty
         * TreeSet with comparator c. The second add operation will return true (and the size of the tree set will
         * increase) because a and b are not equivalent from the tree set's perspective, even though this is contrary
         * to the specification of the Set.add method.
         *
         * Note: It is generally a good idea for comparators to also implement java.io.Serializable, as they may be used
         * as ordering methods in serializable data structures (like TreeSet, TreeMap). In order for the data structure
         * to serialize successfully, the comparator (if provided) must implement Serializable.
         *
         * For the mathematically inclined, the relation that defines the imposed ordering that a given comparator
         * c imposes on a given set of objects S is:
         *          {(x, y) such that c.compare(x, y) <= 0}.
         * The quotient for this total order is:
         *          {(x, y) such that c.compare(x, y) == 0}.
         * It follows immediately from the contract for compare that the quotient is an equivalence relation on S, and
         * that the imposed ordering is a total order on S. When we say that the ordering imposed by c on S is consistent
         * with equals, we mean that the quotient for the ordering is the equivalence relation defined by the objects'
         * equals(Object) method(s):
         *        {(x, y) such that x.equals(y)}.
         *
         * Unlike Comparable, a comparator may optionally permit comparison of null arguments, while maintaining
         * the requirements for an equivalence relation.
         */
        Comparator<Person> comparator = new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                if (o1.getAge() < o2.getAge())
                    return 1;
                else if (o1.getAge() > o2.getAge())
                    return -1;
                return 0;
            }
        };

        /**
         * comparator 是精确的排序，也就是说当 comparable 和 comparator 同时存在的时候以comparator为主
         */
        Person[] persons = {new Person(120), new Person(22), new Person(33)};
        Arrays.sort(persons, comparator);
        System.out.println(Arrays.deepToString(persons));       // 降序

    }


    /**
     * 当 comparator 行为和 equals() 不一致时表现得很 strange
     */
    @Test
    public void test7() {
        Comparator<Person> comparator = new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return -1;
            }
        };
        SortedSet<Person> set = new TreeSet<>(comparator);
        set.add(new Person(10));
        set.add(new Person(10));
        System.out.println(set);   // 按照 Person 的语义，应该只有一个元素的
    }

    /**
     * 如果用junit启动，要将 ComparableAndComparatorTest 实现 Serializable
     *
     * @throws Exception
     */
    @Test
    public void test8() throws Exception {

        /**
         * 传入 Comparator 的 SortMap 或 SortSet 如果进行序列化，必须让 Comparator 实现 Serializable 接口
         */
        class SerializableComparator implements Serializable, Comparator<Person> {
            @Override
            public int compare(Person o1, Person o2) {
                if (o1.getAge() < o2.getAge())
                    return 1;
                else if (o1.getAge() > o2.getAge())
                    return -1;
                return 0;
            }
        }

        SortedSet<Person> set = new TreeSet<>(new SerializableComparator());
        set.add(new Person(10));
        set.add(new Person(20));
        set.add(new Person(30));

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(bout);
        objectOutputStream.writeObject(set);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bout.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Set<Person> set2 = (TreeSet<Person>) objectInputStream.readObject();

        System.out.println(set2);

    }

    /**
     * Comparator 可以支持 null
     */
    @Test
    public void test9() {
        Comparator<Person> comparator = new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                // 此时 null 是最小
                if(o1 == null || o1.getAge() < o2.getAge())
                    return -1;
                if (o1.equals(o2))
                    return 0;
                return 1;
            }
        };

        Person[] persons = {new Person(120), new Person(22), new Person(33), null};
        Arrays.sort(persons, comparator);
        System.out.println(Arrays.deepToString(persons));

    }

}

class Person implements Comparable<Person>, Serializable {
    private Integer age;

    public Person(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public int compareTo(Person o) {

        /**
         * 测试 test3() 时使用
         */
//        return 0;

        if (o == null)
            throw new NullPointerException();
        if (age > o.age)
            return 1;
        else if (age < o.age)
            return -1;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(age, person.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age);
    }

    public String toString() {
        return "Person [age=" + age + "]";
    }
}