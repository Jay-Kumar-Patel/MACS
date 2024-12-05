public class CRC {

    public String zeros(int length) {
        StringBuilder zeroString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            zeroString.append('0');
        }
        return zeroString.toString();
    }

    public String xor(String a, String b) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < b.length(); i++) {
            result.append(a.charAt(i) == b.charAt(i) ? '0' : '1');
        }
        return result.toString();
    }

    public boolean checkMessage(String receivedMessage, String generator) {
        String remainder = binaryDivision(receivedMessage, generator);
        return remainder.equals(zeros(generator.length() - 1));
    }

    public String binaryDivision(String dividend, String divisor) {
        
        int dropDownVal = divisor.length();
        String currStr = dividend.substring(0, dropDownVal);
    
        while (dropDownVal < dividend.length()) {
            if (currStr.charAt(0) == '1') {
                currStr = xor(divisor, currStr) + dividend.charAt(dropDownVal);
            } else {
                currStr = xor(zeros(divisor.length()), currStr) + dividend.charAt(dropDownVal);
            }
            
            dropDownVal += 1;
            currStr = currStr.substring(1);
        }
    
        if (currStr.charAt(0) == '1') {
            currStr = xor(divisor, currStr);
        } else {
            currStr = xor(zeros(divisor.length()), currStr);
        }

        return currStr.substring(1);
    }


    public String generateCRC(String message, String generator) {
        int genLen = generator.length();
        String paddedMessage = message + zeros(genLen - 1);
        String remainder = binaryDivision(paddedMessage, generator);
        return message + remainder;
    }
    
}
