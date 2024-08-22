package example;

import java.util.List;

public class SNS {

    private String topicName;
    private String topicArn;

    public SNS(){}

    public SNS(String topicName, String topicArn) {
        this.topicName = topicName;
        this.topicArn = topicArn;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicArn() {
        return topicArn;
    }

    public void setTopicArn(String topicArn) {
        this.topicArn = topicArn;
    }

    @Override
    public String toString() {
        return "SNS{" +
                "topicName='" + topicName + '\'' +
                ", topicArn='" + topicArn + '\'' +
                '}';
    }
}
