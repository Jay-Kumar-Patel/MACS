import java.util.ArrayList;
import java.util.List;

public class DriverClass {
    public static void main(String[] args) {

        Manager manager = new Manager();
        manager.setEXCEL_SHEET_FILE_PATH("sample.csv");
        manager.setGDC_FILE_PATH("gdc.txt");
        manager.getGDCInfo();

        List<String> quries = new ArrayList<>();
        quries.add("Insert into twitterdata values (1,http://twitter.com,01 Jul 2024 19:03:00,demo content,tweet,twitter for iPhone" +
                ",100,400,Monastery Lane (CANADA),Gujarati,2,jay,jay411,demo bio,Verified,https://profilephoto,Protected,500,400,31 Jun 2024 14:52:12,705");

        manager.performTransaction(quries);
    }
}
