import java.io.File;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Manager {

    private String EXCEL_SHEET_FILE_PATH;
    private String GDC_FILE_PATH;

    private List<String> tweetDatabaseConfig;
    private List<String> userDatabaseConfig;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");

    public Manager(){
        tweetDatabaseConfig = new ArrayList<>();
        userDatabaseConfig = new ArrayList<>();
    }

    public void getGDCInfo(){

        File file = new File(GDC_FILE_PATH);

        try (Scanner scanner = new Scanner(file)){
            while (scanner.hasNextLine()){
                String currLine = scanner.nextLine();

                if (currLine.startsWith("#")){
                    String fragmentName = currLine.substring(1);

                    if (fragmentName.equals("Tweet")){

                        String[] configurations = scanner.nextLine().split("#");
                        tweetDatabaseConfig.add(configurations[0]);
                        tweetDatabaseConfig.add(configurations[1]);
                        tweetDatabaseConfig.add(configurations[2]);

                    }
                    else if(fragmentName.equals("User")){

                        String[] configurations = scanner.nextLine().split("#");
                        userDatabaseConfig.add(configurations[0]);
                        userDatabaseConfig.add(configurations[1]);
                        userDatabaseConfig.add(configurations[2]);
                    }
                    else {
                        //Do Nothing
                    }
                }
            }
        }
        catch (FileNotFoundException exception){
            System.out.println(exception.getMessage());
        }
    }

    public void performTransaction(List<String> quries){
        DistributedTranscation distributedTranscation = new DistributedTranscation(tweetDatabaseConfig, userDatabaseConfig);
        distributedTranscation.executeInsertTransaction(quries);
    }

    public void insertDataFromExcel(){
        InsertExcelData insertExcelData = new InsertExcelData(EXCEL_SHEET_FILE_PATH, tweetDatabaseConfig, userDatabaseConfig);
        insertExcelData.readData();
        insertExcelData.insertDataTweet();
        insertExcelData.insertDataUser();
    }


    public String getEXCEL_SHEET_FILE_PATH() {
        return EXCEL_SHEET_FILE_PATH;
    }

    public void setEXCEL_SHEET_FILE_PATH(String EXCEL_SHEET_FILE_PATH) {
        this.EXCEL_SHEET_FILE_PATH = EXCEL_SHEET_FILE_PATH;
    }

    public String getGDC_FILE_PATH() {
        return GDC_FILE_PATH;
    }

    public void setGDC_FILE_PATH(String GDC_FILE_PATH) {
        this.GDC_FILE_PATH = GDC_FILE_PATH;
    }

}
