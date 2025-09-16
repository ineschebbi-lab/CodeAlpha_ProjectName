import java.util.ArrayList;
import java.util.Scanner;

class Student{
    String name;
    double grade;
    public Student(String name,double grade ){
        this.name=name;
        this.grade=grade;
    }
}
public class Main{
    public static void main(String[] args){
        ArrayList<Student> students=new ArrayList<>();
        Scanner scanner=new Scanner(System.in);
        System.out.println("enter students(type 'exit' to finish):");
        while(true){
            System.out.print("enter student name:");
            String name=scanner.nextLine();
            if (name.equalsIgnoreCase("exit")){
                break;
            }
            System.out.print("enter grade for " + name + ": ");
            double grade;
            try{
                grade = Double.parseDouble(scanner.nextLine());
            }catch (NumberFormatException e){
                System.out.println("invalid grade please enter a number.");
                continue;
            }
            students.add(new Student(name,grade));
        }
        System.out.println("\nList of students:");
        for(Student s : students){
            System.out.println(s.name +":"+s.grade);
        }
        if(!students.isEmpty()) {
            double sum = 0;
            double max = students.get(0).grade;
            double min = students.get(0).grade;
            String maxStudent = students.get(0).name;
            String minStudent = students.get(0).name;
            for (Student s : students) {
                sum = sum + s.grade;
                if (s.grade > max) {
                    max = s.grade;
                    maxStudent = s.name;
                }
                if (s.grade < min) {
                    min = s.grade;
                    minStudent = s.name;
                }


            }
            double average = sum / students.size();
            System.out.println("\n===summary report===");
            System.out.printf("Average grade: %.2f%n", average);
            System.out.printf("Max grade: %.2f (%s)%n", max, maxStudent);
            System.out.printf("Min grade: %.2f (%s)%n", min, minStudent);
        }else{
            System.out.println("No students were entered");
        }

        scanner.close();
    }

} 
Add Task1 Main.java
