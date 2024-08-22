package example;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SetTopicAttributesRequest;
import software.amazon.awssdk.services.sns.model.SetTopicAttributesResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

public class SNSService {

    private final SnsClient snsClient;

    public SNSService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    public void setTopicDisplayName(String topicArn, String displayName) {
        try {
            SetTopicAttributesRequest request = SetTopicAttributesRequest.builder()
                    .topicArn(topicArn)
                    .attributeName("DisplayName")
                    .attributeValue(displayName)
                    .build();

            SetTopicAttributesResponse result = snsClient.setTopicAttributes(request);
            System.out.println("DisplayName set successfully. RequestId: " + result.responseMetadata().requestId());

        } catch (SnsException e) {
            System.err.println("Error setting topic attributes: " + e.awsErrorDetails().errorMessage());
        }
    }

}
