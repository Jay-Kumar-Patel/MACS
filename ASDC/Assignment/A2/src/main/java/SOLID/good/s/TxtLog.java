package SOLID.good.s;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.sql.Timestamp;

public class TxtLog implements Log{
    @Override
    public boolean writeLog(String moduleName, String message) {
        try (FileWriter writer = new FileWriter(Paths.get(System.getProperty("user.home"), "Desktop", "log.txt").toString(), true)){

            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.write("[" + String.valueOf(new Timestamp(System.currentTimeMillis())) + "] [" + moduleName + "] [" + message + "]");

            bufferedWriter.newLine();

            bufferedWriter.close();

            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
