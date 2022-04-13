package com.example.users.services;

import com.example.users.userInterface.UI;
import com.example.users.usersRepository.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.PreDestroy;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

@Service
@Profile("dbReader")
public class DataBaseService implements UserService {
    private final List<User> usersList = new ArrayList<>();
    private final List<Integer> idsDelete = new ArrayList<>();
    private static Connection connection;

    @Autowired
    private Scanner scanner;

    static {
        try {
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/users", "postgres", "Knoxville1488!ps");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private UI userInterface;

    @Override
    public void enter() {
        userInterface.screenGreetings();
        collectUsers();
        userInterface.screenPrintList();
        print();

        boolean work = true;
        while (work) {
            userInterface.startWorkScreen();
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    userInterface.addUserScreen(usersList, connection);
                    break;
                case 2:
                    userInterface.updateUserScreen(usersList, connection);
                    break;
                case 3:
                    userInterface.deleteUserScreen(usersList, idsDelete);
                    break;
                case 4:
                    print();
                    break;
                case 0:
                    work = false;
                    break;
            }
        }
        deleteFromCollection();
    }

    private void deleteFromCollection() {
        if (idsDelete.isEmpty()) {
            System.out.println("Пользователь не просил удалять никого из списка");
        } else {
            for (int i = 0; i < idsDelete.size(); i++) {
                for (User user : usersList) {
                    if (Objects.equals(user.getId(), idsDelete.get(i))) {
                        usersList.remove(user);
                        break;
                    }
                }
            }
            System.out.println("Все Юзеры с id удалены из коллекции");
        }
        System.out.println("Список из коллекции: ");
        print();
    }

    @Override
    public void print() {
        System.out.println(usersList);
    }

    @Override
    public List<User> collectUsers() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
        }

        try {
            if (!ObjectUtils.isEmpty(connection)) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    usersList.
                            add(new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usersList;
    }


    @PreDestroy
    public void delete() {
        for (int i = 0; i < idsDelete.size(); i++) {
            try {
                if (!ObjectUtils.isEmpty(connection)) {
                    PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Users WHERE id = ?");
                    preparedStatement.setInt(1, idsDelete.get(i));
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            Statement statement = connection.createStatement();
            {

                String selectSql = "SELECT * FROM Users";
                ResultSet resultSet = statement.executeQuery(selectSql);

                System.out.println("Список из бд: ");
                while (resultSet.next()) {
                    System.out.print(resultSet.getInt("id") + " ");
                    System.out.print(resultSet.getString("login") + " ");
                    System.out.println(resultSet.getString("email") + " ");
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}