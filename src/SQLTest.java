import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;



public class SQLTest {

    public static final String DB_URL = "jdbc:h2:/C:/Projects/SQLTest/DateBase/Test";
    public static final String DB_Driver = "org.h2.Driver";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL)){
            Class.forName(DB_Driver); //Проверяем наличие JDBC драйвера для работы с БД
            System.out.println("connection success");
            switch (args[0]){
                case "1":
                    createTable(connection);
                    break;
                case "2":
                    addPerson(connection, args[1], args[2], args[3]);
                    System.out.println("Person add");
                    break;
                case "3":
                    filterFirst(connection);
                    break;
                case "4":
                    randomAdd(connection);
                    System.out.println("Randomize end");
                    break;
                case "5":
                    filterSecond(connection);
                    break;
                default: System.out.println("invalid command");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // обработка ошибки  Class.forName
            System.out.println("JDBC driver not found");
        } catch (SQLException e) {
            e.printStackTrace(); // обработка ошибок  DriverManager.getConnection
            System.out.println("SQL ERROR");
        }
    }

    public static void createTable(Connection connection) throws  SQLException{
        Statement statement = connection.createStatement();  // Создаем statement для выполнения sql-команд
        statement.execute("CREATE TABLE IF NOT EXISTS Persons(" +
                "name VARCHAR(200) NOT NULL," +
                "birthday DATE,"+
                "gender VARCHAR(10) CHECK(gender IN ('woman', 'man')))"); // Выполняем statement - sql команду
        statement.close();
        System.out.println("Table create");
    }

    public static void addPerson(Connection connection, String name, String date, String gender) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO Persons VALUES ('" + name + "', TO_DATE('" + date + "', 'dd.MM.yyyy'), '" + gender + "')"); // Выполняем statement - sql команду
        statement.close();
    }

    public static void randomAdd(Connection connection) throws SQLException{
        Statement statement = connection.createStatement();
        statement.execute("TRUNCATE TABLE Persons"); // Выполняем statement - sql команду
        statement.close();
        for (int i = 0;i < 1000000;i++){
            Random rand = new Random();
            String gender = rand.nextBoolean() ? "man":"woman";
            String name = nameGen(true);
            Random  rnd;
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            Date    dt;
            long    ms;
            ms = -946771200000L + (Math.abs(rand.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
            dt = new Date(ms);
            String date = (df.format(dt));
            addPerson(connection, name, date, gender);
        }

        for (int i = 0;i < 100;i++){
            Random rand = new Random();
            String gender = rand.nextBoolean() ? "man":"woman";
            String name = nameGen(false);
            Random  rnd;
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            Date    dt;
            long    ms;
            ms = -946771200000L + (Math.abs(rand.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
            dt = new Date(ms);
            String date = (df.format(dt));
            addPerson(connection, name, date, gender);
        }

    }

    public static void filterFirst(Connection connection) throws  SQLException{
        PreparedStatement selectStatement = connection.prepareStatement("SELECT DISTINCT *  FROM Persons ORDER BY name");
        ResultSet rs = selectStatement.executeQuery();
        ArrayList<Person> personList = new ArrayList<>();
        while (rs.next()){
            String name = rs.getString("name");
            Date age = rs.getDate("birthday");
            String gender = rs.getString("gender");
            Person person = new Person(name, age, gender);
            personList.add(person);
        }
        for (Person person : personList){
            person.print();
        }
        rs.close();
        selectStatement.close();
    }

    public static void filterSecond(Connection connection) throws  SQLException{
        Date first = new Date();
        PreparedStatement selectStatement = connection.prepareStatement("SELECT *  FROM Persons WHERE gender = 'man' AND name LIKE 'F%' ");
        Date second = new Date();
        ResultSet rs = selectStatement.executeQuery();
        ArrayList<Person> personList = new ArrayList<>();
        while (rs.next()){
            String name = rs.getString("name");
            Date age = rs.getDate("birthday");
            String gender = rs.getString("gender");
            Person person = new Person(name, age, gender);
            personList.add(person);
        }
        for (Person person : personList){
            person.print();
        }
        System.out.println("run time:" + (second.getTime() - first.getTime()) + " ms");
        rs.close();
        selectStatement.close();
    }

    public static String nameGen(boolean flag) {

        int size = (int)(Math.random() * 10 + 5);
        char[] name = new char[size];
        if (flag) {
            name[0] = (char) (Math.random() * ('Z' - 'A') + 'A');
            for (int i = 1; i < size; i++) {
                name[i] = (char) (Math.random() * ('z' - 'a') + 'a');
            }
        } else {
            name[0] = 'F';
            for (int i = 1; i < size; i++) {
                name[i] = (char) (Math.random() * ('z' - 'a') + 'a');
            }
        }
        return String.valueOf(name);
    }

}
