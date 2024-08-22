import java.util.ArrayList;
import java.util.List;

public class Calculations {

    // Method to calculate sentiments for given data based on positive and negative words
    public List<TitleModel> calculateSentiments(List<String[]> data, List<String> negativeWords, List<String> positiveWords) {
        List<TitleModel> response = new ArrayList<>();
        int index = 0;

        for (String[] curr : data) {
            int positive = 0;
            int negative = 0;
            List<String> curPositiveWords = new ArrayList<>();
            List<String> curNegativeWords = new ArrayList<>();

            index++;

            for (String str : curr) {
                // Check if the word is positive or negative and update counts and lists accordingly
                if (positiveWords.contains(str.trim().toLowerCase())) {
                    positive++;
                    curPositiveWords.add(str);
                } else if (negativeWords.contains(str.trim().toLowerCase())) {
                    negative++;
                    curNegativeWords.add(str);
                }
            }

            // Create a TitleModel object with the calculated sentiments
            TitleModel titleModel = createModel(index, curr, positive, negative, curPositiveWords, curNegativeWords);

            response.add(titleModel); // Add the model to the response list
        }

        return response; // Return the list of TitleModel objects
    }

    // Method to create a TitleModel object based on the given parameters
    private TitleModel createModel(int index, String[] title, int positive, int negative, List<String> curPositiveWords, List<String> curNegativeWords){
        TitleModel titleModel = new TitleModel();
        titleModel.setId(index);
        titleModel.setTitle(convertArrayToString(title));
        titleModel.setNegativeMatch(curNegativeWords);
        titleModel.setPositiveMatch(curPositiveWords);

        // Determine the polarity based on positive and negative word counts
        Type polarity = findPolarity(positive, negative);

        // Set the polarity, match words, and score in the TitleModel object
        if (polarity.equals(Type.POSITIVE)) {
            titleModel.setPolarity(Type.POSITIVE);
            titleModel.setMatch(curPositiveWords);
            titleModel.setScore("+" + positive);
        } else if (polarity.equals(Type.NEGATIVE)) {
            titleModel.setPolarity(Type.NEGATIVE);
            titleModel.setMatch(curNegativeWords);
            titleModel.setScore("-" + negative);
        } else {
            titleModel.setPolarity(Type.NEUTRAL);
            List<String> neutralList = new ArrayList<>();
            neutralList.addAll(curNegativeWords);
            neutralList.addAll(curPositiveWords);
            titleModel.setMatch(neutralList);
            titleModel.setScore("0");
        }

        return titleModel; // Return the created TitleModel object
    }

    // Method to convert an array of strings into a single concatenated string
    private String convertArrayToString(String[] titleArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : titleArray) {
            stringBuilder.append(str);
        }
        return stringBuilder.toString(); // Return the concatenated string
    }

    // Method to determine the polarity based on counts of positive and negative words
    private Type findPolarity(int positive, int negative) {
        if (positive > negative) {
            return Type.POSITIVE;
        } else if (negative > positive) {
            return Type.NEGATIVE;
        } else {
            return Type.NEUTRAL;
        }
    }
}