# Programming Exercise 2

- **Student Name**: Jay Sanjaybhai Patel
- **Banner ID**: B00982253

## 1. Overview

This program performs **bit stuffing** and **bit unstuffing** for a hexadecimal input value. The program converts the hexadecimal input to binary, applies bit stuffing, reverses it using bit unstuffing, and converts the result back to hexadecimal.

The main steps involved are:
1. Convert Hexadecimal to Binary.
2. Perform Bit Stuffing.
3. Perform Bit Unstuffing.
4. Convert Binary back to Hexadecimal.

## 2. Working of Program

- **Hexadecimal to Binary Conversion**: Each hexadecimal character is converted to its corresponding 4-bit binary representation.
- **Bit Stuffing**: After every sequence of five consecutive '1's in the binary string, a '0' is added.
- **Bit Unstuffing**: Removes the extra '0' added during bit stuffing.
- **Binary to Hexadecimal Conversion**: After unstuffing, the binary value is converted back to its hexadecimal form.

## 3. Programming Language

The program is written in **Java**.

## 4. How to Run the Program

### Pre-requisites

Ensure you have **Java** installed on your system.

### Steps to Run

1. Download the `P2.java` and `BitStuffing.java` files.
2. Open your terminal or command prompt and navigate to the directory where the files are saved.
3. Compile both Java files:

   ```bash
   javac P2.java BitStuffing.java
   ```
4. Run the program using the following command:

    ```bash
    java P2
    ```
5. Enter a hexadecimal string (e.g., 1A3B).

6. The program will then display the following steps:

- Input: Your hexadecimal input.
- Conversion to binary: The binary representation of the input.
- After bit stuffing: The binary string after applying bit stuffing.
- After bit unstuffing: The binary string after reversing the bit stuffing.
- Output: The final hexadecimal string after converting the unstuffed binary back to hexadecimal.
