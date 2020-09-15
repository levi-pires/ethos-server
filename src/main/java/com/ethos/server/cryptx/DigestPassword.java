package com.ethos.server.cryptx;

import com.ethos.server.App;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONObject;

/*
Esta classe foi criada com intuito único de providenciar
o hash code das senhas dos usuários
*/
public class DigestPassword {
  /*Foi escolhido o JSON
	para armazenar as regras do binário*/
  private JSONObject jsonobject;

  public DigestPassword() {
    /*Lê o binary.json e o transfere para 
		um JSONObject*/
    jsonobject = new JSONObject(App.getBinary());
  }

  public final String digest(String arg) {
    SHA3.DigestSHA3 digestsha3 = new SHA3.Digest512();

    //Digere a string e a transforma em binário
    for (int i = 0; i < 16; i++) {
      arg = Hex.toHexString(digestsha3.digest(arg.getBytes()));

      arg += String.valueOf(i);

      //Método utilizado para traduzir a string para binário
      arg =
        arg
          .replaceAll("a", jsonobject.getString("a"))
          .replaceAll("b", jsonobject.getString("b"))
          .replaceAll("c", jsonobject.getString("c"))
          .replaceAll("d", jsonobject.getString("d"))
          .replaceAll("e", jsonobject.getString("e"))
          .replaceAll("f", jsonobject.getString("f"))
          .replaceAll("2", jsonobject.getString("2"))
          .replaceAll("3", jsonobject.getString("3"))
          .replaceAll("4", jsonobject.getString("4"))
          .replaceAll("5", jsonobject.getString("5"))
          .replaceAll("6", jsonobject.getString("6"))
          .replaceAll("7", jsonobject.getString("7"))
          .replaceAll("8", jsonobject.getString("8"))
          .replaceAll("9", jsonobject.getString("9"));
    }

    //Cospe uma versão digerida do resultado do loop
    return Hex.toHexString(digestsha3.digest(arg.getBytes()));
  }
}
