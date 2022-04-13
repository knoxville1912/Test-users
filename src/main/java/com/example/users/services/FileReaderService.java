package com.example.users.services;

import com.example.users.userInterface.UI;
import com.example.users.usersRepository.User;
import com.example.users.usersRepository.util.FileWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@Profile("fileReader")
public class FileReaderService implements UserService {
    private List<User> usersList = new ArrayList<>();
    private int lastId = Integer.MIN_VALUE;
    private final List<Integer> idsDelete = new ArrayList<>();
    @Autowired
    private Scanner scanner;
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
                    userInterface.addUserScreen(usersList, countLastID());
                    break;
                case 2:
                    userInterface.updateUserScreen(usersList);
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
    }

    private int countLastID() {
        for (User user : usersList) {
            if (lastId < user.getId()) {
                lastId = user.getId();
            }
        }
        return lastId;
    }

    @Override
    public void print() {
        System.out.println(usersList);
    }

    @Override
    public List<User> collectUsers() {
        File directory = new File("C:\\Users\\Danko\\IdeaProjects\\users\\files");
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
                try {
                    File file = files[i];
                    usersList = FileWorker.readFile(file);
                } catch (IOException e) {
                    System.err.println("Issue with file");
                }
            }
        }
        return usersList;
    }

    @PreDestroy
    public void delete() {
        if (idsDelete.isEmpty()) {
            System.out.println("Пользователь не просил удалять никого из списка");
        } else {
            for (int i = 0; i < idsDelete.size(); i++) {
                Iterator<User> iterator = usersList.iterator();
                while (iterator.hasNext()) {
                    User next = iterator.next();
                    if (Objects.equals(next.getId(), idsDelete.get(i))) {
                        iterator.remove();
                    }
                }
                System.out.println("Все Юзеры с id удалены");
            }
            print();
        }
    }
}
