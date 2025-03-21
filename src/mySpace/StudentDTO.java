package mySpace;

import lombok.Data;

@Data
public class StudentDTO {
    private String name;
    private int age;

    public StudentDTO(String name,int age){
        this.name =name;
        this.age = age;
    }


}
