package aplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    /*
     * {
     * "switchFabric: 50",
     * listaInput: [
     * input: [
     * A_ 10 100 10
     * ]
     * input: [
     * B_ 10 150 15
     * ]
     * ],
     * listaOutput: [
     * output: C_ 20 30 25 20
     * output: D_ 20 30 25 20
     * output: E_ 25 40 50 30
     * ],
     * }
     * 
     * List<>
     * 
     * 
     */

    public static FileResponse readpecificationFile(String name) {

        FileResponse fileResponse = new FileResponse();

        try {

            FileReader fileReader = new FileReader("./resources/" + name + ".txt");

            String line;

            BufferedReader specificationFile = new BufferedReader(fileReader);

            while ((line = specificationFile.readLine()) != null) {
                String[] array = line.split(" ");
                List<String> listAux = new ArrayList<>();

                switch (array[0]) {
                    case "switch-fabric:":
                        fileResponse.setSwithFabric(Integer.parseInt(array[1]));
                        break;
                    case "input:":
                        for (int i = 1; i < array.length; i++) {
                            listAux.add(array[i]);
                        }
                        fileResponse.addInputList(listAux);
                        break;
                    case "output:":
                        for (int i = 1; i < array.length; i++) {
                            listAux.add(array[i]);
                        }
                        fileResponse.addOutputList(listAux);
                        break;
                    default:
                        System.out.println("File with wrong formatting");
                        break;
                }
            }

            specificationFile.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro de leitura do txt");
        }
        return fileResponse;
    }

    public static BufferedWriter createLogFile(String name) {
        try {
            FileWriter fileWriter = new FileWriter("src/logs/" + name + ".txt");

            BufferedWriter out = new BufferedWriter(fileWriter);
            return out;

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao inicializar arquivo de log : " + name);
        }

        return null;
    }

    public static void closeLogFile(BufferedWriter file) {
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao fechar arquivo de log");
        }
    }

    public static void writeLog(BufferedWriter log, String info) {
        try {
            log.write(info + "\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro de escrita no arquivo de log");
        }
    }
}
