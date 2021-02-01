import java.util.Date;

public class Person {

    private String name;
    private String gender;
    private Date birthday;

    public Person(String name, Date birthday, String gender){
        this.name =  name;
        this.birthday = birthday;
        this.gender = gender;
    }

    public String getName(){return name;}

    public String getGender(){return gender;}

    public Date getBirthday(){return birthday;}

    public void setName(String name){this.name = name;}

    public void setGender(String gender){
        if(gender.equals("man") || gender.equals("woman")){
            this.gender = gender;
        } else {
            System.out.println("invalid gender");
        }
    }

    public void setAge(Date birthday){
            this.birthday = birthday;
    }

    public void print(){
        Date result = new Date();
        System.out.printf(name + " " + birthday + " " + gender + " " + (result.getYear() - birthday.getYear()) + "age\n");
    }


}
