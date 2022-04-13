package com.example.users.usersRepository.util;

import com.example.users.usersRepository.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileWorker {
    public static List<User> readFile(File file) throws IOException {
        java.io.FileReader fileReader = new java.io.FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<User> usersList = new ArrayList<>();
        while (bufferedReader.ready()) {
            String line = bufferedReader.readLine();
            String[] split = line.split(";");
            usersList.add(new User(
                    Integer.parseInt(split[0]),
                    split[1],
                    split[2]
            ) {
            });
        }
        bufferedReader.close();
        fileReader.close();
        return usersList;
    }
}
