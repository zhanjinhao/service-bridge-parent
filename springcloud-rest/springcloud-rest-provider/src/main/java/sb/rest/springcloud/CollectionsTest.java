package sb.rest.springcloud;

import org.junit.Test;

import java.util.*;

public class CollectionsTest {


    @Test
    public void test() {
        // 排序字符串集合
        List<String> lists = new ArrayList<>(Arrays.asList("2", "5", "3", "7", "4"));
        // listS中的对象String本身含有compareTo方法，所以可以直接调用sort方法，按自然顺序排序
        Collections.sort(lists);
        System.out.println(lists);

        // 排序对象元素
        List<Employer> list1 = new ArrayList<>(
                Arrays.asList(
                        new Employer("a1", 44),
                        new Employer("b1", 55),
                        new Employer("b1", 33)));
        Collections.sort(list1, new Comparator<Employer>() {
            @Override
            public int compare(Employer o1, Employer o2) {
                // 比较规则，年龄比较，升序排序(左比右)
                return o1.getAge().compareTo(o2.getAge());
            }
        });
        System.out.println(list1);

        List<Employer> list2 = new ArrayList<>(
                Arrays.asList(
                        new Employer("a1", 44),
                        new Employer("b1", 55),
                        new Employer("b1", 33))
        );
        Collections.sort(list2, new Comparator<Employer>() {
            @Override
            public int compare(Employer o1, Employer o2) {
                // 比较规则，年龄比较，降序排序(右比左)
                // 注意：compareTo方法里面传入Integer类型的比较内容
                return o2.getAge().compareTo(o1.getAge());
            }
        });
        System.out.println(list2);

        List<Student> list3 = new ArrayList<>(Arrays.asList(
                new Student("a1", 44),
                new Student("b1", 55),
                new Student("b1", 33)
        ));
        Collections.sort(list3);
        // [name is b3 age is 55, name is a3 age is 77, name is b3 age is 99]
        System.out.println(list3);

    }


    class Employer {
        private String name;
        private Integer age;

        public Employer(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        //重载了Object类里的toString方法，使之可以按照我们要求的格式打印
        @Override
        public String toString() {
            return "name is " + name + " age is " + age;
        }
    }

    class Student implements Comparable<Student> {
        private String name;
        private Integer age;

        public Student(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        //重载了Object类里的toString方法，使之可以按照我们要求的格式打印
        @Override
        public String toString() {
            return "name is " + name + " age is " + age;
        }

        //重载了Comparable接口里的compareTo方法来实现具体的比较
        @Override
        public int compareTo(Student a) {//第二个参数（右）
            return this.age.compareTo(a.getAge());
        }

    }

    @Test
    public void test2() {
        List chars = new ArrayList(Arrays.asList("w", "o", "r", "l", "d"));
        Collections.shuffle(chars);     // 改变的集合本身的元素，返回void
        System.out.println(chars);
    }

    @Test
    public void test3() {
        List chars = new ArrayList(Arrays.asList("w", "o", "r", "l", "d"));
        Collections.sort(chars);
        int l = Collections.binarySearch(chars, "l");
        System.out.println(l);
        int m = Collections.binarySearch(chars, "m");
        System.out.println(m);
    }

    @Test
    public void test4() {
        List chars = new ArrayList(Arrays.asList("w", "o", "r", "l", "d"));
        int l = Collections.binarySearch(chars, "l");
        System.out.println(l);
        int m = Collections.binarySearch(chars, "m");
        System.out.println(m);
    }

    @Test
    public void test5() {
        List chars = new ArrayList(Arrays.asList("w", "o", "r", "l", "d"));

        String max = (String) Collections.max(chars);
        System.out.println(max);
        String min = (String) Collections.min(chars);
        System.out.println(min);

        String max1 = Collections.max(chars, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
        System.out.println(max1);
        String min1 = Collections.min(chars, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
        System.out.println(min1);
    }

    @Test
    public void test6() {
        List<String> list = Arrays.asList("one two three four five six siven".split(" "));

        System.out.println(list);
        List subList = Arrays.asList("three four five six".split(" "));
        System.out.println(subList);
        System.out.println(Collections.indexOfSubList(list, subList));
    }

    @Test
    public void test7() {
        List list = Arrays.asList("one two three four five six siven one two three four five six siven".split(" "));

        System.out.println(list);
        List subList = Arrays.asList("three four five six".split(" "));
        System.out.println(subList);
        System.out.println(Collections.lastIndexOfSubList(list, subList));
    }

    @Test
    public void test8(){
        List list = Arrays.asList("old one two three four old five six siven old".split(" "));
        System.out.println(list);
        System.out.println(Collections.replaceAll(list, "old", "new"));
        System.out.println(list);
    }

    @Test
    public void test9() {
        List list = Arrays.asList("one two three four five six siven".split(" "));
        System.out.println(list);
        Collections.reverse(list);
        System.out.println(list);
    }


    @Test
    public void test10() {
        List list = Arrays.asList("one two three four five six siven".split(" "));
        System.out.println(list);
        Collections.rotate(list, 2);
        System.out.println(list);
    }

    /**
     * 将集合n中的元素全部复制到m中,并且覆盖相应索引的元素
     * （从0开始覆盖，后面的元素向后移）
     */
    @Test
    public void test11() {
        List m = Arrays.asList("one two three four five six siven".split(" "));
        System.out.println(m);
        List n = Arrays.asList("我是 复制 过来的哈".split(" "));
        System.out.println(n);
        Collections.copy(m, n);
        System.out.println(m);
    }

    @Test
    public void test12() {
        List m = Arrays.asList("one two three four five six siven".split(" "));
        System.out.println(m);
        Collections.swap(m, 2, 3);
        System.out.println(m);
    }

    @Test
    public void test13() {
        List m = Arrays.asList("one two three four five six siven".split(" "));
        System.out.println(m);
        Collections.fill(m, "haha12345");
        System.out.println(m);
    }

    /**
     * 此方法返回的list不允许修改，因为它返回的是AbstractList的一个简单实现类
     */
    @Test
    public void nCopies() {

        List<String> haha = Collections.nCopies(5, "haha");

        System.out.println(haha.getClass());
        System.out.println(haha);

    }

}