package aplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import aplication.exception.FileFormatException;

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

                switch (array[0]) {
                    case "switch-fabric:":
                        fileResponse.setSwitchFabric(array[1]);
                        break;
                    case "input:":
                        fileResponse.addInputPort(array[1], array[2], array[3], array[4]);
                        break;
                    case "output:":
                        fileResponse.addOutputPort(array[1], array[2], array[3], array[4], array[5]);
                        break;
                    default:
                        throw new FileFormatException("Formato do arquivo");
                        //System.out.println("File with wrong formatting");
                        //wrongFormat = true;
                }
            }

            specificationFile.close();
            fileResponse.checkSumFowardProbability();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro de leitura do txt");
        }
        return fileResponse;
    }

    public static BufferedWriter createLogFile(String name) {
        try {
            Path path = Paths.get("./logs/"+ name + ".txt");
            if (Files.exists(path)){
                FileWriter fileWriter = new FileWriter("./logs/" + name + ".txt", true);
                BufferedWriter out = new BufferedWriter(fileWriter);
                return out;
            } else {
                FileWriter fileWriter = new FileWriter("./logs/" + name + ".txt");
                BufferedWriter out = new BufferedWriter(fileWriter);
                return out;
            }


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
