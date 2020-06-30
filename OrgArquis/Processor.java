import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Processor {
    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    // Class atributes
    HashMap<String, Integer> registers; // Init
    HashMap<String, Integer> labelAddress; // Init
    String[] commandMemory = new String[1000]; // MC
    Integer[] memory = new Integer[1000]; // MC
    String pc; // Sim
    String commandLine; // Sim


    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    public Processor(File file) throws IOException {
        initMemory(file);
        File summary = new File("summary.txt");
        summary.delete();
    }


    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    // Simulation method - Sim
    public void process() {
        String reg1, reg2, reg3, address, str;
        Integer rVal1, rVal2, aux;
        String[] param;
        for (int i = 0; i < commandMemory.length; i++) {
            if (commandMemory[i] != null) {
                String[] command = commandMemory[i].split(" ", 2);
                switch (command[0]) {
                    case "xor": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        commandLine = commandMemory[i];
                        pc = commandMemory[i+1];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        reg3 = param[2];
                        rVal1 = registers.get(reg2);
                        rVal2 = registers.get(reg3);
                        aux = binToIntI(intBinXor(rVal1, rVal2));
                        registers.put(reg1, aux);
                        updateSummary();
                        break;

                    case "lui": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        commandLine = commandMemory[i];
                        pc = commandMemory[i+1];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        registers.put(reg1, Integer.parseInt(reg2));
                        updateSummary();
                        break;

                    case "addu": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        commandLine = commandMemory[i];
                        pc = commandMemory[i+1];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        reg3 = param[2];
                        rVal1 = registers.get(reg2);
                        rVal2 = registers.get(reg3);
                        aux = binToIntI(intBinAdd(rVal1, rVal2));
                        registers.put(reg1, aux);
                        updateSummary();
                        break;

                    case "addiu": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        commandLine = commandMemory[i];    
                        pc = commandMemory[i+1];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        reg3 = param[2];
                        rVal1 = registers.get(reg2);
                        rVal2 = Integer.parseInt(reg3);
                        aux = rVal1 + rVal2;
                        registers.put(reg1, aux);
                        updateSummary();
                        break;

                    case "lw": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        commandLine = commandMemory[i];    
                        pc = commandMemory[i+1];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1].substring(0, 1);
                        reg3 = param[1].substring(2, param[1].length()-1);
                        aux = (Integer.parseInt(reg2)/4);
                        registers.put(reg1, memory[aux]);
                        updateSummary();
                        break;

                    case "sw": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        commandLine = commandMemory[i];    
                        pc = commandMemory[i+1];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1].substring(0, 1);
                        reg3 = param[1].substring(2, param[1].length()-1);
                        aux = (Integer.parseInt(reg2)/4) + registers.get(reg3);
                        memory[aux] = registers.get(reg1);
                        updateSummary();
                        break;

                    case "beq": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                                //BEQ $s0, $s1, L2              #desvia para L2 se $s0 = $s1
                        commandLine = commandMemory[i];    
                        pc = commandMemory[i+1];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        reg3 = param[2];
                        rVal1 = registers.get(reg1);
                        rVal2 = registers.get(reg2);
                        if(rVal1==rVal2){

                            //desvia pra reg1
                        }
                        updateSummary();
                        break;

                    case "bne": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                                //BEQ $s0, $s1, L2              #desvia para L2 se $s0 = $s1
                        commandLine = commandMemory[i];    
                        pc = commandMemory[i+1];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        reg3 = param[2];
                        rVal1 = registers.get(reg1);
                        rVal2 = registers.get(reg2);
                        if(rVal1!=rVal2){

                            //desvia pra reg1
                        }
                        updateSummary();
                        break;

                    case "slt": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        commandLine = commandMemory[i];    
                        pc = commandMemory[i+1];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        reg3 = param[2];
                        rVal1 = registers.get(reg2);
                        rVal2 = registers.get(reg3);
                        aux = lessThan(rVal1,rVal2);
                        registers.put(reg1, aux);
                        
                        updateSummary();
                        break;

                    case "ori": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        commandLine = commandMemory[i];    
                        pc = commandMemory[i+1];

                        updateSummary();
                        break;

                    case "and": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        commandLine = commandMemory[i];    
                        pc = commandMemory[i+1];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        reg3 = param[2];
                        rVal1 = registers.get(reg2);
                        rVal2 = registers.get(reg3);
                        
                        updateSummary();
                        break;

                    case "andi": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        commandLine = commandMemory[i];    
                        pc = commandMemory[i+1];

                        updateSummary();
                        break;

                    case "srl": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        commandLine = commandMemory[i];    
                        pc = commandMemory[i+1];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        reg3 = param[2];
                        rVal1 = registers.get(reg2);
                        rVal2 = Integer.parseInt(reg3);
                        aux = rVal1>>rVal2;
                        registers.put(reg1, aux);

                        updateSummary();
                        break;

                    case "sll": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        commandLine = commandMemory[i];    
                        pc = commandMemory[i+1];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        reg3 = param[2];
                        rVal1 = registers.get(reg2);
                        rVal2 = Integer.parseInt(reg3);
                        aux = rVal2<<rVal1;
                        registers.put(reg1, aux);

                        updateSummary();
                        break;

                    default:
                        break;
                }
            }
        }
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        System.out.println("-=-=-=-=-=-=-=-End-=-=-=-=-=-=-=-");
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
    }


    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    // Converters - Conv
    public String intToBinS(int val) {
        String bin = Integer.toBinaryString(val);
        return bin;
    }
    public int binToIntI(String str) {
        int val = Integer.parseInt(str, 2);
        return val;
    }
    public String binaryToHex8(String str){
        int decimal = Integer.parseInt(str,2);
        String hexStr = Integer.toString(decimal,16);
        while(hexStr.length()<8){
            hexStr = "0"+hexStr;
        }
        return hexStr;
    }


    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    // Operations - Oper
    public String binAdd(String bin1, String bin2) {
        int bin1Int = Integer.parseInt(bin1, 2);
        int bin2Int = Integer.parseInt(bin2, 2);
        int sum = bin1Int + bin2Int;
        return Integer.toBinaryString(sum);
    }
    public String intBinAdd(int bin1, int bin2) {
        String sum = binAdd(intToBinS(bin1), intToBinS(bin2));
        return sum;
    }
    public int lessThan(int num1, int num2){
        if(num1<num2){
            return 1;
        }
        else{
            return 0;
        }
        
    }


    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    // Memory Control - MC
    public void saveInMemory(int address, int value) {
        address = (address / 4);
        memory[address] = value;
    }
    public int loadFromMemory(int address) {
        address = (address / 4);
        int value = memory[address];
        return value;
    }


    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    // Summary Controll - SM
    public void updateSummary() {
        try (FileWriter writer = new FileWriter("summary.txt", true)){
            writer.write("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");
            writer.write("-=-Instruction|" + commandLine + "|\n");
            writer.write("-=-=-=-=-=-=PC|" + pc + "|\n");
            for(String reg : registers.keySet()){
                writer.write(" Reg|"+reg+" => 0x"+binaryToHex8(intToBinS(registers.get(reg)))+"\n");
            }
            for(int h=0; h<memory.length; h++){
                if(memory[h]!=null){
                    writer.write(" Mem|"+h+" => "+memory[h]);
                }
            }
            writer.close();
        }
        catch(IOException e){
            System.out.println("Morreu");
        }    
    }


    //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    //Initialize Registers & Memory - Init
    public void initMemory(File file){
        initCommandMemory(file);
        initLabelsAddress();
        initRegisters();
    }
    public void initCommandMemory(File file){
        try(BufferedReader buffReader = new BufferedReader(new FileReader(file))){     
            int memoryPos = 0;
            String line;
            Pattern pat = Pattern.compile("\\.");
            while((line = buffReader.readLine()) != null){
                Matcher mat = pat.matcher(line);
                if(!mat.find()){
                    commandMemory[memoryPos] = line;
                    memoryPos++;
                }
            }
        }
        catch(IOException except){
            System.out.println("MORREU não?!Indeed");;
        }
    }
    public void initLabelsAddress(){
        labelAddress = new HashMap<String, Integer>();
        Pattern pat = Pattern.compile("(.*):");
        String line;
        for(int i=0; i<commandMemory.length; i++){
            if(commandMemory[i]!=null){
                line = commandMemory[i];
                Matcher mat = pat.matcher(line);
                if(mat.find()){
                    labelAddress.put(mat.group(1), i);
                }
            }    
        }
    }
    public void initRegisters(){
        registers = new HashMap<String, Integer>(30);
        registers.put("$zero", 0); registers.put("$at", 0);  registers.put("$v0", 0);
        registers.put("$v1", 0);  registers.put("$a0", 0);  registers.put("$a1", 0);
        registers.put("$a2", 0);  registers.put("$a3", 0);  registers.put("$t0", 0);
        registers.put("$t1", 0);  registers.put("$t2", 0);  registers.put("$t3", 0);
        registers.put("$t4", 0);  registers.put("$t5", 0);  registers.put("$t6", 0);
        registers.put("$t7", 0);  registers.put("$s0", 0);  registers.put("$s1", 0);
        registers.put("$s2", 0);  registers.put("$s3", 0);  registers.put("$s4", 0);
        registers.put("$s5", 0);  registers.put("$s6", 0);  registers.put("$s7", 0);
        registers.put("$t8", 0);  registers.put("$t9", 0);  registers.put("$k0", 0);
        registers.put("$k1", 0);  registers.put("$gp", 0);  registers.put("$sp", 0);
        registers.put("$fp", 0);  registers.put("$ra", 0);
    }
}