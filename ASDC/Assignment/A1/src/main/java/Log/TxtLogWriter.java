package Log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.sql.Timestamp;

public class TxtLogWriter implements LogWriter{

    private static TxtLogWriter txtLogWriter;

    //File Path Where we have to log this Information.
    private String logFilePath = Paths.get(System.getProperty("user.home"), "Desktop", "log.txt").toString();

    public void setFilePath(String logFilePath){
        this.logFilePath = logFilePath;
    }

    //Singleton Approach to Create Object of this class
    public static TxtLogWriter getInstance()
    {
        if (txtLogWriter == null) {
            //Use Synchronized, so that in case of multithreading or multiple user create object at the same time
            // only one thread goes inside this block.
            synchronized (TxtLogWriter.class) {
                if (txtLogWriter == null){
                    //Create Onne time Object
                    txtLogWriter = new TxtLogWriter();
                }
            }
        }

        return txtLogWriter;
    }

    /**
     * Method to Write Log in .txt File located at desktop.
     *
     * @param type       : Type of Message (Success, Failure, Information, Warning)
     * @param module     : Module from which this message is belong to.
     * @param logMessage : Message which we want to write.
     */
    @Override
    public boolean Write(String type, String module, String logMessage) {

        // Use this Try-Catch block ensures that the FileWriter is properly closed after its use.
        try (FileWriter writer = new FileWriter(logFilePath, true)){

            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.write("[" + String.valueOf(new Timestamp(System.currentTimeMillis())) + "] [" + type + "] [" + module + "] [" + logMessage + "]");

            // Adds a new line after writing the log message.
            bufferedWriter.newLine();

            bufferedWriter.close();

            return true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
