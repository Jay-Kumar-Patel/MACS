import matplotlib.pyplot as plt

#User Input
def get_binary_input():
    userInputString = input("Enter the binary string: ")
    print("Binary String:", userInputString)
    return userInputString



#Calculations for Unipolar Waveform
def cal_unipolar_waveform(binary_string):
    unipolarWaveForm = [5 if bit == '1' else 0 for bit in binary_string]
    unipolarWaveForm.append(5 if binary_string[-1] == '1' else 0)
    return unipolarWaveForm



#Calculations for NRZ Waveform
def cal_nrz_waveform(binary_string):
    nrzWaveForm = [5 if bit == '1' else -5 for bit in binary_string]
    nrzWaveForm.append(5 if binary_string[-1] == '1' else -5)
    return nrzWaveForm



#Calculations for Manchester Waveform
def cal_manchester_waveform(binary_string):
    manchesterWaveForm = []

    if binary_string[0] == '1':
        manchesterWaveForm.append(5)
        manchesterWaveForm.append(-5)
    
    for bit in binary_string:
        if bit == '1':
            # Low to high transition
            manchesterWaveForm.append(-5)
            manchesterWaveForm.append(5)
        else:
            # High to low transition
            manchesterWaveForm.append(5)
            manchesterWaveForm.append(-5)
            
    manchesterWaveForm.append(5 if manchesterWaveForm[-1] == 5 else -5)
    return manchesterWaveForm



def plot_waveforms(unipolar_waveform, nrz_waveform, manchester_waveform):
    
    plt.figure(figsize=(10, 8))

    # Plot the unipolar waveform
    plt.subplot(3, 1, 1)
    plt.step(range(len(unipolar_waveform)), unipolar_waveform, where='post', color='b', label='Unipolar waveform')
    plt.ylim(-7, 7)
    plt.title('Unipolar Waveform (1 = +5V, 0 = 0V)')
    plt.xlabel('Bit Index')
    plt.ylabel('Voltage (V)')
    plt.grid(True)
    plt.legend()

    # Plot the NRZ waveform
    plt.subplot(3, 1, 2)
    plt.step(range(len(nrz_waveform)), nrz_waveform, where='post', color='r', label='NRZ waveform')
    plt.ylim(-7, 7)
    plt.title('NRZ Waveform (1 = +5V, 0 = -5V)')
    plt.xlabel('Bit Index')
    plt.ylabel('Voltage (V)')
    plt.grid(True)
    plt.legend()

    # Plot the Manchester waveform
    plt.subplot(3, 1, 3)
    plt.step(range(len(manchester_waveform)), manchester_waveform, where='post', color='g', label='Manchester Encoding')
    plt.ylim(-7, 7)
    plt.title('Manchester Encoding Waveform')
    plt.xlabel('Bit Index (with transitions)')
    plt.ylabel('Voltage (V)')
    plt.grid(True)
    plt.legend()

    # Show the plots
    plt.tight_layout()
    plt.show()



#Main function to run the program.
def main():

    #User Input
    binary_string = get_binary_input()

    #Calculation for the Waveforms
    unipolar_waveform = cal_unipolar_waveform(binary_string)
    nrz_waveform = cal_nrz_waveform(binary_string)
    manchester_waveform = cal_manchester_waveform(binary_string)

    #Draw Wavedforms
    plot_waveforms(unipolar_waveform, nrz_waveform, manchester_waveform)



if __name__ == "__main__":
    main()