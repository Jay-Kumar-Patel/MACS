public class BitStuffing {
    
    public String HexToBinary(String hexValue){

        StringBuilder stringBuilder = new StringBuilder();

        for(int i=0;i<hexValue.length();i++){

            char val = hexValue.charAt(i);

            switch (val) {
                case '0': 
                    stringBuilder.append("0000"); 
                    break;
                case '1': 
                    stringBuilder.append("0001"); 
                    break;
                case '2': 
                    stringBuilder.append("0010"); 
                    break;
                case '3': 
                    stringBuilder.append("0011"); 
                    break;
                case '4': 
                    stringBuilder.append("0100"); 
                    break;
                case '5': 
                    stringBuilder.append("0101"); 
                    break;
                case '6': 
                    stringBuilder.append("0110"); 
                    break;
                case '7': 
                    stringBuilder.append("0111"); 
                    break;
                case '8': 
                    stringBuilder.append("1000"); 
                    break;
                case '9': 
                    stringBuilder.append("1001"); 
                    break;
                case 'A':
                    stringBuilder.append("1010");
                    break;
                case 'B':
                    stringBuilder.append("1011");
                    break;
                case 'C':
                    stringBuilder.append("1100");
                    break;
                case 'D':
                    stringBuilder.append("1101");
                    break;
                case 'E':
                    stringBuilder.append("1110");
                    break;
                case 'F':
                    stringBuilder.append("1111");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid hex character: " + val);
            }

        }
        return stringBuilder.toString();
    }

    public String stuffing(String binaryValue) {
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
    
        for (int i = 0; i < binaryValue.length(); i++) {
            char currentChar = binaryValue.charAt(i);
            stringBuilder.append(currentChar);
    
            if (currentChar == '1') {
                count++;
            } else {
                count = 0;
            }
    
            if (count == 5) {
                stringBuilder.append('0');
                count = 0;
            }
        }
    
        return stringBuilder.toString();
    }    

    public String unStuffing(String binaryValue) {
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
    
        for (int i = 0; i < binaryValue.length(); i++) {
            char currentChar = binaryValue.charAt(i);
            
            if (currentChar == '1') {
                count++;
                stringBuilder.append(currentChar);
                
                if (count == 5) {
                    if (i + 1 < binaryValue.length() && binaryValue.charAt(i + 1) == '0') {
                        i++;
                    }
                    count = 0;
                }
            } else {
                count = 0;
                stringBuilder.append(currentChar);
            }
        }
    
        return stringBuilder.toString();
    }      


    public String BinaryToHex(String binaryValue){

        StringBuilder stringBuilder = new StringBuilder();

        int lastIndex = 0;

        for(int i=0;i<binaryValue.length();i+=4){

            if(i+3 >= binaryValue.length()){
                for(int p=lastIndex;p<binaryValue.length();p++){
                    stringBuilder.append(binaryValue.charAt(p));
                }
                break;
            }

            Character firstValue = binaryValue.charAt(i);
            Character secondValue = binaryValue.charAt(i+1);
            Character thirdValue = binaryValue.charAt(i+2);
            Character fourthValue = binaryValue.charAt(i+3);

            lastIndex = i+3;

            String currStr = firstValue + "" + secondValue + "" + thirdValue + "" + fourthValue;

            switch (currStr) {
                case "0000":
                    stringBuilder.append('0');
                    break;
                case "0001":
                    stringBuilder.append('1');
                    break;
                case "0010":
                    stringBuilder.append('2');
                    break;
                case "0011":
                    stringBuilder.append('3');
                    break;
                case "0100":
                    stringBuilder.append('4');
                    break;
                case "0101":
                    stringBuilder.append('5');
                    break;
                case "0110":
                    stringBuilder.append('6');
                    break;
                case "0111":
                    stringBuilder.append('7');
                    break;
                case "1000":
                    stringBuilder.append('8');
                    break;
                case "1001":
                    stringBuilder.append('9');
                    break;
                case "1010":
                    stringBuilder.append('A');
                    break;
                case "1011":
                    stringBuilder.append('B');
                    break;
                case "1100":
                    stringBuilder.append('C');
                    break;
                case "1101":
                    stringBuilder.append('D');
                    break;
                case "1110":
                    stringBuilder.append('E');
                    break;
                case "1111":
                    stringBuilder.append('F');
                    break;    
                default:
                    throw new IllegalArgumentException("Invalid binary string: " + currStr);
            }
        }

        return stringBuilder.toString();
    }

}
