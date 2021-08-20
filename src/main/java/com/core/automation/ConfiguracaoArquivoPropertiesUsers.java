package com.core.automation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.openqa.selenium.remote.SessionId;

public class ConfiguracaoArquivoPropertiesUsers {

    static Map<SessionId, String> usuarioLogado = new HashMap<SessionId, String>();

    public synchronized static Properties getProp() throws IOException {
        Properties props = new Properties();
        FileInputStream file = new FileInputStream(".\\target\\classes\\config.properties");
        props.load(file);
        return props;
    }

    public synchronized static String lerArquivo(String nomeDoArquivo) throws InterruptedException {
        String arquivoEsperado = null;
        Path caminho = Paths.get(System.getProperty("user.dir") + "\\arquivos\\xml\\arquivosEsperados\\" + nomeDoArquivo);
        try {
            byte[] texto = Files.readAllBytes(caminho);
            arquivoEsperado = new String(texto);
            arquivoEsperado = new String(arquivoEsperado.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // System.out.println(arquivoEsperado);
        } catch (Exception ignored) {
        }
        Thread.sleep(1000);
        return arquivoEsperado;
    }

    public synchronized static void liberarUser(String file, SessionId session) throws IOException {
        File arq = new File(file);
        if (arq.exists()) {
            List<String> linhas = Files.readAllLines(Paths.get(file));
            ArrayList<String> us = new ArrayList<String>();
            for (String lin : linhas) {
                if (lin.split(";")[1].equals(session.toString())) {
                    us.add(lin.split(";")[0] + ";" + "ND");
                } else {
                    us.add(lin);
                }
            }
            FileWriter apagar = new FileWriter(arq, true);
            apagar.close();

            FileWriter fw = new FileWriter(arq);
            BufferedWriter bw = new BufferedWriter(fw);
            System.out.println("                ## Usuários ##                ");
            for (String u : us) {
                bw.write(u);
                System.out.println(u);
                bw.newLine();
            }

            bw.close();
            fw.close();
        }
    }

    public synchronized static String verificarSessionId(String file, SessionId session) throws IOException {
        List<String> linhas = Files.readAllLines(Paths.get(file));
        String userLogin = null;
        boolean flagIdSessionPresent = false;

        for (String lin : linhas) {
            if (lin.split(";")[1].equals(session.toString())) {
                userLogin = lin.split(";")[0];
                flagIdSessionPresent = true;
                break;
            }
        }
        if (!flagIdSessionPresent) {
            userLogin = inserirSessionControleDeUsuario(file, session);
        }

        return userLogin;
    }

    public synchronized static String inserirSessionControleDeUsuario(String file, SessionId session) throws IOException {
        File arq = new File(file);
        List<String> linhas = Files.readAllLines(Paths.get(file));
        ArrayList<String> us = new ArrayList<String>();
        String userLogin = null;
        boolean flagIdSessionPresent = false;

        while (userLogin == null) {
            for (String lin : linhas) {
                if (lin.split(";")[1].equals("ND") && !flagIdSessionPresent) {
                    userLogin = lin.split(";")[0];
                    us.add(lin.split(";")[0] + ";" + session);
                    flagIdSessionPresent = true;
                } else {
                    us.add(lin);
                }
            }
            FileWriter apagar = new FileWriter(arq, true);
            apagar.close();
            FileWriter fw = new FileWriter(arq);
            BufferedWriter bw = new BufferedWriter(fw);
            for (String u : us) {
                if (!u.split(";")[1].equals("ND")) {
                    System.out.println(u);
                }
                bw.write(u);
                bw.newLine();
            }
            bw.close();
            fw.close();
        }

        return userLogin;
    }
}