// Programmed by:
// Haw Guan Ong 
// Danny Sum Khar Chen
// Shiivain A/L Thangiah 

import java.util.Scanner;

class Assignment_final {
    public static void main(String[] args) {

        int option;

        Scanner input = new Scanner(System.in); // Create Scanner object to get user input

        System.out.println("Please input your DNA sequence (press Enter TWICE to proceed):");

        // Create StringBuilder (mutable String) object
        StringBuilder userInputBuilder = new StringBuilder();

        String line;
        while (!(line = input.nextLine()).isEmpty()) {
            userInputBuilder.append(line);
        }

        // Accepts user input as uppercase String
        String userInput = userInputBuilder.toString().toUpperCase();

        // Converts userInput from a String to a Char array
        char[] DNAbase = userInput.toCharArray();

        // Checks if the user input contains invalid character (Not A, T, C, or G)
        // If invalid characters exist, set isValid to false
        boolean isValid = true;
        for (char c : DNAbase) {
            System.out.println(c);

            if (c != 'A' && c != 'T' && c != 'C' && c != 'G') {
                isValid = false;
                break;
            }
        }

        // Notifies the user whether the input sequence is valid or invalid
        if (!isValid) {
            System.out.println("The DNA sequence contains invalid characters. Please input a valid DNA sequence.");
            // Ask for input again
            userInputBuilder = new StringBuilder();
            while (!(line = input.nextLine()).isEmpty()) {
                userInputBuilder.append(line);
            }
            userInput = userInputBuilder.toString().toUpperCase();
            DNAbase = userInput.toCharArray();

            // Check the validity of the new input
            isValid = true;
            for (char c : DNAbase) {
                System.out.println(c);

                if (c != 'A' && c != 'T' && c != 'C' && c != 'G') {
                    isValid = false;
                    break;
                }
            }
        }

        // Notifies the user whether the input sequence is valid or invalid
        if (isValid) {
            System.out.println("The DNA sequence is valid.");
        } else {
            System.out.println("The DNA sequence contains invalid characters.");
        }

        // Prompts the user for the orientation of the sequence
        System.out.println("\nWhat is the orientation of the sequence: ");
        System.out.println("Press [1] to select 5' to 3' or [2] to select 3' to 5'");

        // Accepts user input for orientation of the input sequence
        option = input.nextInt();

        // Validates the user's orientation choice
        while (option != 1 && option != 2) {
            System.out.println("Invalid input. Please select either [1] or [2].");
            option = input.nextInt();
        }

        // Declare and initialize an empty String rnaSequence
        String rnaSequence = "";

        // Call method transcribeToRNA based on the orientation of the input sequence
        // and initialize the return value to String rnaSequence
        switch (option) {
            case 1:
                System.out.println("5' to 3' is coding strand");
                rnaSequence = transcribeToRNA(userInput);
                System.out.println("\nTranscription using coding strand: " + rnaSequence);
                break;

            case 2:
                // Calls method templateStrand() within transcribeToRNA method
                System.out.println("3' to 5' is template strand");
                rnaSequence = transcribeToRNA(templateStrand(userInput));
                System.out.println("\nTranscription using template strand: " + rnaSequence);
                break;
        }

        // Call method translateRNA on String rnaSequence, and initialize the
        // output to String fullProteinSequence
        String fullProteinSequence = translateRNA(rnaSequence);
        System.out.println("\nFull Translation: " + fullProteinSequence);

        // Call method translateCDS on String rnaSequence
        String cdsProteinSequence = translateCDS(rnaSequence);

        // If cdsProteinSequence is not empty
        if (!cdsProteinSequence.isEmpty()) {
            System.out.println("\nCDS Translation (from first Methionine to Stop): " + cdsProteinSequence);
            System.out.println("It is a CDS (coding sequence).");
        } else {
            System.out.println("\nNo Methionine-Stop CDS found in the sequence.");
        }

        input.close(); // Close input object for garbage collection
    }

    // Method to transcribe input sequence to RNA by replacing thymine (T) with uracil (U)
    private static String transcribeToRNA(String dnaSequence) {
        return dnaSequence.replace('T', 'U');
    }

    private static String templateStrand(String dnaSequence) {

        // Instantiate StringBuilder object named template
        StringBuilder template = new StringBuilder();

        // Converts the parameter dnaSequence to a char array
        // Iterate through every character in the array and changing each base to its
        // corresponding partner (A to U, T to A, G to C, C to G)
        for (char c : dnaSequence.toCharArray()) {
            switch (c) {
                case 'A':
                    template.append('U');
                    break;
                case 'T':
                    template.append('A');
                    break;
                case 'G':
                    template.append('C');
                    break;
                case 'C':
                    template.append('G');
                    break;
            }
        }
        // Returns StringBuilder object template as a String using toString() method
        return template.toString();
    }

    // Method translates RNA sequence
    private static String translateRNA(String rnaSequence) {
        StringBuilder protein = new StringBuilder();
        for (int i = 0; i < rnaSequence.length() - 2; i += 3) {
            String codon = rnaSequence.substring(i, i + 3);
            String aminoAcid = getAminoAcid(codon);
            protein.append(aminoAcid).append(" ");
        }
        // Returns protein as a String using toString() method
        return protein.toString();
    }

    /**
     * Method checks the sequence for coding sequences by identifying the start and stop codons
     * The method iterates through the sequence in increments of 3 letters and looks for the 
     * "AUG" sequence and changes the value of startCodonFound to true.
     * If start codon is found, the method getAminoAcid is called on the sequence and looks for
     * the "Stop" codon and changes the value of stopCodonFound to true.
     */
    private static String translateCDS(String rnaSequence) {
        StringBuilder protein = new StringBuilder();
        boolean startCodonFound = false;
        boolean stopCodonFound = false;
        for (int i = 0; i < rnaSequence.length() - 2; i += 3) {
            String codon = rnaSequence.substring(i, i + 3);
            if (codon.equals("AUG")) {
                startCodonFound = true;
            }
            if (startCodonFound) {
                String aminoAcid = getAminoAcid(codon);
                protein.append(aminoAcid).append(" ");
                if (aminoAcid.equals("Stop")) {
                    stopCodonFound = true;
                    break;
                }
            }
        }

        // Reset the protein sequence if no stop codon was found
        if (!stopCodonFound) {
            protein = new StringBuilder();
        }

        return protein.toString();
    }

    /**
     * getAminoAcid method translates three letters sequence into corresponding
     * amino acids
     */
    private static String getAminoAcid(String codon) {
        switch (codon) {
            case "AAA":
            case "AAG":
                return "Lysine";
            case "AAC":
            case "AAU":
                return "Asparagine";
            case "ACA":
            case "ACC":
            case "ACG":
            case "ACU":
                return "Threonine";
            case "AGA":
            case "AGG":
                return "Arginine";
            case "AGC":
            case "AGU":
                return "Serine";
            case "AUA":
            case "AUC":
            case "AUU":
                return "Isoleucine";
            case "CAA":
            case "CAG":
                return "Glutamine";
            case "CAC":
            case "CAU":
                return "Histidine";
            case "CCA":
            case "CCC":
            case "CCG":
            case "CCU":
                return "Proline";
            case "CGA":
            case "CGC":
            case "CGG":
            case "CGU":
                return "Arginine";
            case "CUA":
            case "CUC":
            case "CUG":
            case "CUU":
                return "Leucine";
            case "GAA":
            case "GAG":
                return "Glutamic Acid";
            case "GAC":
            case "GAU":
                return "Aspartic Acid";
            case "GCA":
            case "GCC":
            case "GCG":
            case "GCU":
                return "Alanine";
            case "GGA":
            case "GGC":
            case "GGG":
            case "GGU":
                return "Glycine";
            case "GUA":
            case "GUC":
            case "GUG":
            case "GUU":
                return "Valine";
            case "UAA":
            case "UAG":
            case "UGA":
                return "Stop";
            case "UAC":
            case "UAU":
                return "Tyrosine";
            case "UCA":
            case "UCC":
            case "UCG":
            case "UCU":
                return "Serine";
            case "UGG":
                return "Tryptophan";
            case "UGC":
            case "UGU":
                return "Cysteine";
            case "UUC":
            case "UUU":
                return "Phenylalanine";
            case "AUG":
                return "Methionine";
            case "UUA":
            case "UUG":
                return "Leucine";
            default:
                return "Unknown";
        }
    }
}
