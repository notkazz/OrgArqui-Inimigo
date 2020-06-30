import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Processor {
    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    // Class atributes
    HashMap<String, Integer> registers; // Init
    HashMap<String, Integer> labelAddress; // Init
    HashMap<String, Integer> dataAddress; //Init
    String[] commandMemory = new String[200]; // MC
    String[] dataMemory = new String[200]; 
    Integer[] memory = new Integer[200]; // MC
    String pc; // Sim
    String commandLine; // Sim
    int commandCount = 0;
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
        Scanner interactable = new Scanner(System.in);
        for (int i = 0; i < commandMemory.length; i++) {
            if (commandMemory[i] != null) {
                String[] command = commandMemory[i].split(" ", 2);
                switch (command[0]) {
                    case "xor": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        str = interactable.nextLine();
                        commandLine = commandMemory[i];
                        pcPos = i+1;
                        pc = commandMemory[pcPos];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        reg3 = param[2];
                        rVal1 = registers.get(reg2);
                        rVal2 = registers.get(reg3);
                        aux = binToIntI(intBinXor(rVal1, rVal2));
                        registers.put(reg1, aux);
                        updateSummaryLog();
                        newLine();
                        updateInterface();
                        break;

                    case "lui": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        str = interactable.nextLine();
                        commandLine = commandMemory[i];
                        pcPos = i+1;
                        pc = commandMemory[pcPos];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        registers.put(reg1, Integer.parseInt(reg2));
                        updateSummaryLog();
                        newLine();
                        updateInterface();
                        break;

                    case "addu": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        str = interactable.nextLine();
                        commandLine = commandMemory[i];
                        pcPos = i+1;
                        pc = commandMemory[pcPos];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        reg3 = param[2];
                        rVal1 = registers.get(reg2);
                        rVal2 = registers.get(reg3);
                        aux = binToIntI(intBinAdd(rVal1, rVal2));
                        registers.put(reg1, aux);
                        updateSummaryLog();
                        newLine();
                        updateInterface();
                        break;

                    case "addiu": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        str = interactable.nextLine();
                        commandLine = commandMemory[i];    
                        pcPos = i+1;
                        pc = commandMemory[pcPos];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        reg3 = param[2];
                        rVal1 = registers.get(reg2);
                        rVal2 = Integer.parseInt(reg3);
                        aux = rVal1 + rVal2;
                        registers.put(reg1, aux);
                        updateSummaryLog();
                        newLine();
                        updateInterface();
                        break;

                    case "lw": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        str = interactable.nextLine();
                        commandLine = commandMemory[i];    
                        pcPos = i+1;
                        pc = commandMemory[pcPos];
                        if(commandLine.contains("(")){
                            param = command[1].split(", ");
                            reg1 = param[0];
                            reg2 = param[1].substring(0, 1);
                            reg3 = param[1].substring(2, param[1].length()-1);
                            aux = (Integer.parseInt(reg2)/4);
                            registers.put(reg1, memory[aux]);
                        }
                        else {
                            param = command[1].split(", ");
                            reg1 = param[0];
                            reg2 = param[1];
                            aux = dataAddress.get(reg2);
                            memory[aux] = registers.get(reg1);
                        }
                        updateSummaryLog();
                        newLine();
                        updateInterface();
                        break;

                    case "sw": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        str = interactable.nextLine();
                        commandLine = commandMemory[i];    
                        pcPos = i+1;
                        pc = commandMemory[pcPos];
                        if(commandLine.contains("(")){
                            param = command[1].split(", ");
                            reg1 = param[0];
                            reg2 = param[1].substring(0, 1);
                            reg3 = param[1].substring(2, param[1].length()-1);
                            aux = (Integer.parseInt(reg2)/4) + registers.get(reg3);
                            memory[aux] = registers.get(reg1);
                        }
                        else {
                            param = command[1].split(", ");
                            reg1 = param[0];
                            reg2 = param[1];
                            aux = dataAddress.get(reg2);
                            memory[aux] = registers.get(reg1);
                        }
                        updateSummaryLog();
                        newLine();
                        updateInterface();
                        break;

                    case "beq": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        str = interactable.nextLine();
                        commandLine = commandMemory[i];    
                        pcPos = i+1;
                        pc = commandMemory[pcPos];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        reg3 = param[2];
                        rVal1 = registers.get(reg1);
                        rVal2 = registers.get(reg2);
                        if(rVal1==rVal2){
                            aux = labelAddress.get(reg3);
                            i = aux;
                        }
                        updateSummaryLog();
                        newLine();
                        updateInterface();
                        break;

                    case "bne": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        str = interactable.nextLine();
                        commandLine = commandMemory[i];    
                        pcPos = i+1;
                        pc = commandMemory[pcPos];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        reg3 = param[2];
                        rVal1 = registers.get(reg1);
                        rVal2 = registers.get(reg2);
                        if(rVal1!=rVal2){
                            aux = labelAddress.get(reg3);
                            i = aux;
                        }
                        updateSummaryLog();
                        newLine();
                        updateInterface();
                        break;

                    case "slt": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        str = interactable.nextLine();
                        commandLine = commandMemory[i];    
                        pcPos = i+1;
                        pc = commandMemory[pcPos];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        reg3 = param[2];
                        rVal1 = registers.get(reg2);
                        rVal2 = registers.get(reg3);
                        aux = lessThan(rVal1,rVal2);
                        registers.put(reg1, aux);
                        updateSummaryLog();
                        newLine();
                        updateInterface();
                        break;

                    case "ori": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        str = interactable.nextLine();
                        commandLine = commandMemory[i];    
                        pcPos = i+1;
                        pc = commandMemory[pcPos];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        reg3 = param[2];
                        rVal1 = registers.get(reg2);
                        rVal2 = Integer.parseInt(reg3);
                        aux = binToIntI(intBinOri(rVal1, rVal2));
                        registers.put(reg1, aux);
                        updateSummaryLog();
                        newLine();
                        updateInterface();
                        break;

                    case "and": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        str = interactable.nextLine();   
                        commandLine = commandMemory[i];    
                        pcPos = i+1;
                        pc = commandMemory[pcPos];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        reg3 = param[2];
                        rVal1 = registers.get(reg2);
                        rVal2 = registers.get(reg3);
                        aux = binToIntI(intBinAnd(rVal1, rVal2));
                        registers.put(reg1, aux);
                        updateSummaryLog();
                        newLine();
                        updateInterface();
                        break;

                    case "andi": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        str = interactable.nextLine();
                        commandLine = commandMemory[i];    
                        pcPos = i+1;
                        pc = commandMemory[pcPos];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        reg3 = param[2];
                        rVal1 = registers.get(reg2);
                        rVal2 = Integer.parseInt(reg3);
                        aux = binToIntI(intBinAnd(rVal1, rVal2));
                        registers.put(reg1, aux);
                        updateSummaryLog();
                        newLine();
                        updateInterface();
                        break;

                    case "srl": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        str = interactable.nextLine();    
                        commandLine = commandMemory[i];    
                        pcPos = i+1;
                        pc = commandMemory[pcPos];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        reg3 = param[2];
                        rVal1 = registers.get(reg2);
                        rVal2 = Integer.parseInt(reg3);
                        aux = rVal1>>rVal2;
                        registers.put(reg1, aux);
                        updateSummaryLog();
                        newLine();
                        updateInterface();
                        break;

                    case "sll": //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                        str = interactable.nextLine();    
                        commandLine = commandMemory[i];    
                        pcPos = i+1;
                        pc = commandMemory[pcPos];
                        param = command[1].split(", ");
                        reg1 = param[0];
                        reg2 = param[1];
                        reg3 = param[2];
                        rVal1 = registers.get(reg2);
                        rVal2 = Integer.parseInt(reg3);
                        aux = rVal2<<rVal1;
                        registers.put(reg1, aux);
                        updateSummaryLog();
                        newLine();
                        updateInterface();
                        break;

                    default:
                        break;
                }
            }
        }
        System.out.println("-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-");
        System.out.println("-=-=-=-=-=-=--=-=-=-=-=-=-=-End-=-=-=-=-=-=-=--=-=-=-=-=-=-");
        System.out.println("-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-");
        interactable.close();
        /*System.out.println(commandCount);
        for(String s: commandMemory){
            if(s!=null) System.out.println(s);
        }
        */
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
    public String hexSumAddress(int displc){
        Integer hex1 = Integer.parseInt("400000", 16);
        Integer hex2 = displc*(Integer.parseInt("4",16));
        hex1 = hex1 + hex2;
        String hexStr = Integer.toHexString(hex1);
        while(hexStr.length()<8){
            hexStr = "0"+hexStr;
        }
        return hexStr;
    }
    public String hexSumAddressPC(int displc){
        Integer hex1 = Integer.parseInt("0", 16);
        Integer hex2 = displc*(Integer.parseInt("4",16));
        hex1 = hex1 + hex2;
        String hexStr = Integer.toHexString(hex1);
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
    public String intBinXor(int bin1, int bin2){
        String binS1 = intToBinS(bin1);
        String binS2 = intToBinS(bin2);
        String xor = "";
        int tam = 0;
        if(binS1.length() > binS2.length()){
            int dif = binS1.length() - binS2.length();
            for(int i = 0; i<dif;i++){
                binS2 = "0" + binS2;
            }
            tam = binS1.length();
        }
        else if(binS2.length() > binS1.length()){
            int dif = binS1.length() - binS2.length();
            for(int i = 0; i<dif;i++){
                binS1 = "0" + binS1;
            }
            tam = binS2.length();
        } 
        else { tam = binS1.length();}
        for(int i = 0; i < tam; i++){
            char b = binS1.charAt(i);
            char b2 = binS2.charAt(i);
            if(b!=b2){
                xor = xor + "1";
            }
            else if(b==b2){
                xor = xor + "0";
            }
        }
        return xor;
    }
    public String intBinOri(int bin1, int bin2){
        String binS1 = intToBinS(bin1);
        String binS2 = intToBinS(bin2);
        String ori = "";
        int tam = 0;
        if(binS1.length() > binS2.length()){
            int dif = binS1.length() - binS2.length();
            for(int i = 0; i<dif;i++){
                binS2 = "0" + binS2;
            }
            tam = binS1.length();
        }
        else if(binS2.length() > binS1.length()){
            int dif = binS2.length() - binS1.length();
            for(int i = 0; i<dif; i++){
                binS1 = "0" + binS1;
            }
            tam = binS2.length();
        } 
        else { tam = binS1.length();}
        for(int i = 0; i < tam; i++){
            char b = binS1.charAt(i);
            char b2 = binS2.charAt(i);
            if(b== '0' && b2 == '0'){
                ori = ori + "0";
            }
            else if(b == '1' || b2 == '1'){
                ori = ori + "1";
            }
        }
        return ori;
    }
    public String intBinAnd(int bin1, int bin2){
        String binS1 = intToBinS(bin1);
        String binS2 = intToBinS(bin2);
        String and = "";
        while(binS1.length()<32){
            binS1 = "0"+binS1;
        }
        while(binS2.length()<32){
            binS2 = "0"+binS2;
        }
        for(int i = 0; i < binS1.length(); i++){
            char b1 = binS1.charAt(i);
            char b2 = binS2.charAt(i);
            if(b1=='1' && b2=='1'){
               and = and + "1";
            }
            else {
               and = and + "0";  
            }
        }
        return and;
    }
    public int lessThan(int num1,int num2){
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
    // Interface & Log Controll - I&L
    public void updateSummaryLog(){
        try (FileWriter writer = new FileWriter("summaryLog.txt", true)){
            writer.write("\n-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");
            writer.write("-=-Instruction|" + commandLine +"|\n");
            writer.write("-=-=-=-=-=-=PC|" + pc + "| PCVal: 0x"+hexSumAddressPC(pcPos-1)+"\n");
            for(String reg : registers.keySet()){
                writer.write(" Reg|"+reg+" => 0x"+binaryToHex8(intToBinS(registers.get(reg)))+"\n");
            }
            for(int h=0; h<memory.length; h++){
                if(memory[h]!=null){
                    writer.write(" Mem|"+hexSumAddress(h)+" => "+memory[h]);
                }
            }
            writer.close();
        }
        catch(IOException e){
            System.out.println("Morreu");
        }    
    }
    public void updateInterface() {
        newLine();
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        System.out.println("-=-Instruction|" + commandLine +"|");
        System.out.println("-=-=-=-=-=-=PC|" + pc + "| PCVal: 0x"+hexSumAddressPC(pcPos-1));
        for(String reg : registers.keySet()){
            System.out.println(" Reg|"+reg+" => 0x"+binaryToHex8(intToBinS(registers.get(reg))));
        }
        for(int h=0; h<memory.length; h++){
            if(memory[h]!=null){
                System.out.println(" Mem|0x"+hexSumAddress(h)+" => "+memory[h]);
            }
        }
    }
    int pcPos;
    public static void newLine() {  
        System.out.println(""); 
    }




    //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    //Initialize Registers & Memory - Init
    public void initMemory(File file){
        initCommandMemory(file);
        initLabelsAddress();
        initRegisters();
        initData(file);
    }
    public void initCommandMemory(File file){
        try(BufferedReader buffReader = new BufferedReader(new FileReader(file))){     
            int memoryPos = 0;
            String line;
            Pattern pat = Pattern.compile("\\.");
            while((line = buffReader.readLine()) != null){
                Matcher mat = pat.matcher(line);
                if(line.contains(".data:")){
                    break;
                }
                if(!mat.find()){
                    commandMemory[memoryPos] = line;
                    memoryPos++;
                    if(!line.equals("")){
                        commandCount++;
                    }
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
    public void initData(File file){
        try(BufferedReader buffReader = new BufferedReader(new FileReader(file))){
            dataAddress = new HashMap<String, Integer>();
            String line;
            int count = 0;
            int displc = commandCount - labelAddress.size();
            Boolean permission = false;
            while((line = buffReader.readLine()) != null){
                if(permission){
                    count++;
                    String[] dataArray = line.split(" ");
                    memory[displc+count] = Integer.parseInt(dataArray[2]);
                    String label = dataArray[0].substring(0,dataArray[0].length()-1);
                    int pos = displc+count;
                    dataAddress.put(label, pos); 
                }
                if(line.contains(".data:")){
                    permission = true;
                }
            }
        }
        catch(IOException except){
            System.out.println("MORREU não?!Indeed");;
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