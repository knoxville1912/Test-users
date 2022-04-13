package com.example.users.userInterface;

import com.example.users.usersRepository.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

@Component
public class UI {

    @Autowired
    private Scanner scanner;

    public void screenGreetings() {
        System.out.println("Это приложение может считывать файл, выводить список этого файла и производить его обновление\n");
        System.out.println("Сначала мы считываем файл\n");
    }

    public void screenPrintList() {
        System.out.println("Выведем лист Юзеров");
    }

    public void startWorkScreen() {
        System.out.println("\n" +
                "Выберете пункт: \n" +
                "1. Добавить Юзера в лист \n" +
                "2. Обновить данные Юзера в листе \n" +
                "3. Удалить Юзера по id \n" +
                "4. Вывести список Юзеров \n" +
                "0. Закончить работу \n");
    }

    public void updateUserScreen(List<User> usersList) {
        System.out.println("Выберете по какому id обновить данные: \n");
        int idUpdate = scanner.nextInt();
        System.out.println("Выберете какие обновить данные: \n" +
                "1. логин \n" +
                "2. имейл \n");
        int choiceUpdate = scanner.nextInt();
        switch (choiceUpdate) {
            case 1:
                for (User user : usersList) {
                    if (user.getId() == idUpdate) {
                        System.out.println("Введите логин на который хотите поменять: \n");
                        String login = scanner.next();
                        user.setLogin(login);
                        System.out.println("Логин изменён\n");
                        break;
                    }
                }
                break;
            case 2:
                for (User user : usersList) {
                    if (user.getId() == idUpdate) {
                        System.out.println("Введите имейл на который хотите поменять: \n");
                        String email = scanner.next();
                        user.setEmail(email);
                        System.out.println("Имейл изменён\n");
                        break;
                    }
                }
                break;
        }
    }

    public void updateUserScreen(List<User> usersList, Connection connection) {
        System.out.println("Выберете по какому id обновить данные: \n");
        int idUpdate = scanner.nextInt();
        System.out.println("Выберете какие обновить данные: \n" +
                "1. логин \n" +
                "2. имейл \n");
        int choiceUpdate = scanner.nextInt();
        switch (choiceUpdate) {
            case 1:
                for (User user : usersList) {
                    if (user.getId() == idUpdate) {
                        System.out.println("Введите логин на который хотите поменять: \n");
                        String login = scanner.next();
                        user.setLogin(login);
                        try {
                            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Users SET login=? WHERE id=?");
                            preparedStatement.setString(1, login);
                            preparedStatement.setInt(2, idUpdate);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Логин изменён\n");
                        break;
                    }
                }
                break;
            case 2:
                for (User user : usersList) {
                    if (user.getId() == idUpdate) {
                        System.out.println("Введите имейл на который хотите поменять: \n");
                        String email = scanner.next();
                        user.setEmail(email);
                        try {
                            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Users SET email=? WHERE id=?");
                            preparedStatement.setString(1, email);
                            preparedStatement.setInt(2, idUpdate);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Имейл изменён\n");
                        break;
                    }
                }
                break;
        }
    }

    public void addUserScreen(List<User> usersList, int lastId) {
        System.out.println("Введите логин Юзера, которого хотите добавить: \n");
        String login = scanner.next();
        System.out.println("Введите email Юзера, которого хотите добавить: \n");
        String email = scanner.next();
        usersList.add(new User(++lastId, login, email));
        System.out.println("Вы добавили Юзера\n");
    }

    public void addUserScreen(List<User> usersList, Connection connection) {
        System.out.println("Введите логин Юзера, которого хотите добавить: \n");
        String login = scanner.next();
        System.out.println("Введите email Юзера, которого хотите добавить: \n");
        String email = scanner.next();
        Integer id = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Users (login, email) VALUES (?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            while (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        usersList.add(new User(id, login, email));
        System.out.println("Вы добавили Юзера\n");
    }

    public void deleteUserScreen(List<User> usersList, List<Integer> idsDelete) {
        System.out.println("Введите id Юзера, которое хотите удалить: \n");
        int idDelete = scanner.nextInt();
        boolean isThereAnID = false;
        for (User user : usersList) {
            if (user.getId() == idDelete) {
                isThereAnID = true;
                break;
            }
        }
        if (isThereAnID) {
            idsDelete.add(idDelete);
            System.out.println("Данные будут удалены по завершении программы \n");
        } else {
            System.out.println("Такого id не существует \n");
        }
    }

}
