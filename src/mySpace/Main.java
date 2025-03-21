package mySpace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<StudentDTO> student_list = new ArrayList<>(10);

        student_list.add(new StudentDTO("jhf",3));
        student_list.add(new StudentDTO("dlwjdtn",2));
        student_list.add(new StudentDTO("dlwjdtn",1));

        ServiceStudent s1 = new ServiceStudent();
        s1.listprint(student_list);




    }
}
