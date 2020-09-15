package com.ethos.server.exception;

//Lida com erros. Não use diretamente no Spring
public class CatchExceptions {

  //Print Error And Exit
  public void printErrX(Object obj) {
    System.out.println(obj.toString());
    System.exit(1);
  }
}
