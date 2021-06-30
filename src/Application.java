import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("enter source file name");
        String sourceFileName = scanner.nextLine();

        System.out.println("enter new file name");
        String newFileName = scanner.nextLine();

        FileInputStream fileInputStream = new FileInputStream(sourceFileName);
        BufferedReader fileBufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

        String fileString;
        int fileStringNumber = 1;

        StringBuilder newFileText = new StringBuilder();

        while ((fileString = fileBufferedReader.readLine()) != null) {

            if (fileString.toLowerCase(Locale.ROOT).contains("nstr(")) {

                int start = fileString.indexOf("\"") + 1;
                int stop = fileString.indexOf("\"", start);
                if (stop == -1)
                    stop = fileString.length();

                fileString = fileString.substring(start, stop);

                try {
                    HashMap<String, String> strMap = transformStringToMap(fileString);

                    for (String key : strMap.keySet()) {
                        String value = strMap.get(key);
                        newFileText.append(fileStringNumber + ": ");
                        newFileText.append(key + " : ");
                        newFileText.append(value);
                        newFileText.append("\n");
                    }
                    newFileText.append("\n");
                } catch (Exception e) {
                    newFileText.append(fileStringNumber + "error");
                    newFileText.append("\n");
                }
            }
            fileStringNumber++;
        }

        System.out.println(newFileText.toString());
        fileInputStream.close();

        PrintStream out = new PrintStream(newFileName);
        out.println(newFileText.toString());
        out.flush();
        out.close();

    }

    private static HashMap transformStringToMap(String initialString) {

        String[] messagesWithCode = initialString.split(";");

        HashMap<String, String> messagesWithCodeMap = new HashMap<>();

        for (String messageWithCode : messagesWithCode) {

            String[] messageWithCodeArray = messageWithCode.split("=");

            String code = messageWithCodeArray[0].trim();
            String message = messageWithCodeArray[1].trim();
            message = message.substring(1, message.length() - 1);

            messagesWithCodeMap.put(code, message);
        }
        return messagesWithCodeMap;
    }
}
