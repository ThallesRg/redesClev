package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import application.exception.FileFormatException;

/**
* A classe FileHandler é responsável por gerenciar os arquivos da pasta 'logs' e 'input'
* 
* @author Luiz Felipe Triques Moraes
* @author Renata Rona Garib
* @author Thalles Raphael Guimarães
* 
*/
public class FileHandler {

    /**
     * Método responsável por receber o nome de um arquivo, encontra-lo na pasta 'input' e passar os dados para a classe FileResponse trata-la
     * @param name variável contendo o nome do arquivo a ser utilizado
     * @return o método retorna um dado do tipo FileResponse com o tratamento já correto
     */
    public static FileResponse readpecificationFile(String name) {

        FileResponse fileResponse = new FileResponse();

        try {

            FileReader fileReader = new FileReader("./input/" + name + ".txt");

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

    /**
     * Método responsável por criar arquivos de log através de seu nome, permitindo inserção de novos elementos caso o arquivo já exista
     * @param name variável contendo o nome do log a ser criado
     * @throws IOException para o caso de não conseguir criar um log corretamente
     * @return um dado do tipo BufferedWriter
     */
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

    /**
     * Método responsável por fechar o arquivo aberto previamente
     * @param file variável contendo o BufferedWriter do arquivo a ser fechado
     * @throws IOException para o caso de não conseguir fechar o log corretamente
     */
    public static void closeLogFile(BufferedWriter file) {
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao fechar arquivo de log");
        }
    }

    /**
     * Método responsável por escrever determinada informação num determinado arquivo
     * @param log variável contendo o BufferedWriter do arquivo a ser escrito
     * @param info variável contendo o dado a ser inserido no arquivo
     * @throws IOException para o caso de não conseguir escrever o log corretamente
     */
    public static void writeLog(BufferedWriter log, String info) {
        try {
            log.write(info + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro de escrita no arquivo de log");
        }
    }
}
