package hello.core;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
public class HelloLombok {

    private String name;
    private int age;

    public static void main(String[] args) {
        HelloLombok helloLombok = new HelloLombok();
        HelloLombok.setName("asdf");

        String name = helloLombok.getName();
        System.out.println("name = " + name);
    }
}