import java.util.List;

public class TitleModel {
    private int id;
    private String title;
    private List<String> match;
    private List<String> positiveMatch;
    private List<String> negativeMatch;
    private String score;
    private Type polarity;

    public TitleModel(){}

    public TitleModel(int id, String title, List<String> match, List<String> positiveMatch, List<String> negativeMatch, String score, Type polarity) {
        this.id = id;
        this.title = title;
        this.match = match;
        this.positiveMatch = positiveMatch;
        this.negativeMatch = negativeMatch;
        this.score = score;
        this.polarity = polarity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getMatch() {
        return match;
    }

    public void setMatch(List<String> match) {
        this.match = match;
    }

    public List<String> getPositiveMatch() {
        return positiveMatch;
    }

    public void setPositiveMatch(List<String> positiveMatch) {
        this.positiveMatch = positiveMatch;
    }

    public List<String> getNegativeMatch() {
        return negativeMatch;
    }

    public void setNegativeMatch(List<String> negativeMatch) {
        this.negativeMatch = negativeMatch;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Type getPolarity() {
        return polarity;
    }

    public void setPolarity(Type polarity) {
        this.polarity = polarity;
    }

    @Override
    public String toString() {
        return "TitleModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", match=" + match +
                ", positiveMatch=" + positiveMatch +
                ", negativeMatch=" + negativeMatch +
                ", score='" + score + '\'' +
                ", polarity='" + polarity + '\'' +
                '}';
    }
}
