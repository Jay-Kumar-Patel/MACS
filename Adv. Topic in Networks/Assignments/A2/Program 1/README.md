# Programming Exercise 1

- **Student Name**: Jay Sanjaybhai Patel
- **Banner ID**: B00982253

## 1. Overview

This Python program allows users to input a binary string and generates three different waveforms based on the following encoding schemes:

- **Unipolar Encoding**: Encodes '1' as +5V and '0' as 0V.
- **NRZ (Non-Return to Zero) Encoding**: Encodes '1' as +5V and '0' as -5V.
- **Manchester Encoding**: Each bit is represented by two transitions (low-to-high for '1' and high-to-low for '0').

The program plots these waveforms using the `matplotlib` library's `step()` function, simulating time-based signal transitions.

## 2. Programming Language

The program is written in **Python** and utilizes the **Matplotlib** library for plotting the waveforms.

## 3. How to Run the Program

### Pre-requisites

Before running the program, ensure that you have the following:

- **Python 3.x** installed on your system.
- The **Matplotlib** library. You can install it using pip with the following command:
  
  ```bash
  pip install matplotlib
  ```

### Steps to Run

- Download the source code file (P1.py).
- Open your terminal or command prompt and navigate to the directory where P1.py is saved.
- Run the program using the following command:

```bash
python P1.py
```
- The program will prompt you to enter a binary string (e.g., 001101000).
- After entering the binary string, the program will generate and display the waveforms: