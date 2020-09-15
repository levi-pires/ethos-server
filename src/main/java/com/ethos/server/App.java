package com.ethos.server;

import com.ethos.server.exception.CatchExceptions;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.springframework.boot.SpringApplication;
//
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
  private static final String binary = new App()
  .getStringInFile("/json/binary.json");
  private static final String passwd = new App().getStringInFile("/etc/passwd");

  public static String getBinary() {
    return binary;
  }

  public static String getPasswd() {
    return passwd;
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  private String getStringInFile(String path) {
    System.out.println("\n... " + path + " ...\n");
    BufferedReader bFile = null;
    String readFile = null;
    String cpath = null;

    try {
      cpath = new File(".").getCanonicalPath();
    } catch (IOException e) {
      new CatchExceptions().printErrX(e);
    }

    try {
      bFile = new BufferedReader(new FileReader(cpath + path));
    } catch (FileNotFoundException e) {
      new CatchExceptions().printErrX(e);
    }

    try {
      while (bFile.ready()) {
        readFile += bFile.readLine();
      }
      bFile.close();
      readFile = readFile.replaceAll("null", "");
    } catch (IOException e) {
      new CatchExceptions().printErrX(e);
    }

    return readFile;
  }
}
