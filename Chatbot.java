import java.util.*;
import java.io.*;

public class Chatbot{
    private static String filename = "./WARC201709_wid.txt";
    private static int vSize = 4699;
    private static ArrayList<Integer> readCorpus(){
        ArrayList<Integer> corpus = new ArrayList<Integer>();
        try{
            File f = new File(filename);
            Scanner sc = new Scanner(f);
            while(sc.hasNext()){
                if(sc.hasNextInt()){
                    int i = sc.nextInt();
                    corpus.add(i);
                }
                else{
                    sc.next();
                }
            }
        }
        catch(FileNotFoundException ex){
            System.out.println("File Not Found.");
        }
        return corpus;
    }

    public static int[][] conditionProb(int h, ArrayList<Integer> corpus){
        int[][] countMatrix = new int[4700][2];
        for (int i = 0; i < countMatrix.length; i++) {
            countMatrix[i][0] = i;
            countMatrix[i][1] = 0;
        }
        for (int u = 0; u <= vSize; u++) {
            for (int i = 0; i < corpus.size() - 1; i++) {
                if(h == corpus.get(i) && u == corpus.get(i+1)){
                    countMatrix[u][1]++;
                }
            }
        }
        return countMatrix;
    }

    public static int[][] conditionProb(int h1, int h2, ArrayList<Integer> corpus){
        int[][] countMatrix = new int[4700][2];
        for (int i = 0; i < countMatrix.length; i++) {
            countMatrix[i][0] = i;
            countMatrix[i][1] = 0;
        }
        for (int u = 0; u <= vSize; u++) {
            for (int i = 0; i < corpus.size() - 1; i++) {
                if(h1 == corpus.get(i) && h2 == corpus.get(i + 1) && u == corpus.get(i+2)){
                    countMatrix[u][1]++;
                }
            }
        }
        return countMatrix;
    }

    public static void unigram(List<double[]> segList, ArrayList<Integer> corpus){
        double sumProb = 0;
        for (int i = 0; i <= vSize; i++) {
            int countI = 0;
            for (Integer indice: corpus
                    ) {
                if(i == indice){
                    countI++;
                }
            }
            double[] segment = {i, sumProb, sumProb + countI/(double)corpus.size()};
            if(segment[1] != segment[2])
            {
                segList.add(segment);
                sumProb += countI/(double)corpus.size();
            }
        }
    }

    public static int findInterval(List<double[]> segList, double r){
        int w = 0;
        for (int i = 0; i < segList.size(); i++) {
            if(r == 0){
                w = (int)segList.get(0)[0];
            }
            else{
                if(r > segList.get(i)[1] && r <= segList.get(i)[2]){
                    w = (int)segList.get(i)[0];
                }
            }
        }
        return w;
    }

    public static List<double[]> addToSeglist(List<double[]> segList, int[][] countMatrix, int count){
        double sumProb = 0;
        for (int i = 0; i < vSize; i++) {
            double left = sumProb;
            sumProb = sumProb + (double)countMatrix[i][1] / (double)count;
            if(left != sumProb) {
                segList.add(new double[]{i, left, sumProb});
            }
        }
        return segList;
    }

    public static void printSegList(List<double[]> segList, double r){
        if(r == 0){
            System.out.println((int)segList.get(0)[0]);
            System.out.println(String.format("%.7f", segList.get(0)[1]));
            System.out.println(String.format("%.7f", segList.get(0)[2]));
        }
        else {
            for (double[] seg : segList
                    ) {
                if (r > seg[1] && r <= seg[2]) {
                    System.out.println((int) seg[0]);
                    System.out.println(String.format("%.7f", seg[1]));
                    System.out.println(String.format("%.7f", seg[2]));
                }
            }
        }
    }

    static public void main(String[] args){
        ArrayList<Integer> corpus = readCorpus();
        int flag = Integer.valueOf(args[0]);
        if(flag == 100){
            int w = Integer.valueOf(args[1]);
            int count = 0;
            //TODO count occurence of w
            for (Integer indice:corpus
                    ) {
                if(indice == w){
                    count++;
                }
            }
            System.out.println(count);
            System.out.println(String.format("%.7f",count/(double)corpus.size()));
        }
        else if(flag == 200){
            //TODO generate
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            double r = (double)n1 / (double)n2;
            List<double[]> segList = new ArrayList<>();
            unigram(segList,corpus);
            printSegList(segList,r);

        }
        else if(flag == 300){
            int h = Integer.valueOf(args[1]);
            int w = Integer.valueOf(args[2]);
            //TODO
            int[][] countMatrix = conditionProb(h,corpus);
            //output
            int count = 0;
            for (int i = 0; i < countMatrix.length; i++) {
                count += countMatrix[i][1];
            }
            System.out.println(countMatrix[w][1]);
            System.out.println(count);
            System.out.println(String.format("%.7f", (double)countMatrix[w][1]/(double) count));
        }
        else if(flag == 400){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int h = Integer.valueOf(args[3]);
            //TODO
            double r = (double) n1 / (double) n2;
            ArrayList<double[]> segList = new ArrayList<>();
            int[][] countMatrix = conditionProb(h,corpus);
            int count = 0;
            for (int i = 0; i < countMatrix.length; i++) {
                count += countMatrix[i][1];
            }
            addToSeglist(segList,countMatrix,count);
            printSegList(segList,r);

        }
        else if(flag == 500){
            int h1 = Integer.valueOf(args[1]);
            int h2 = Integer.valueOf(args[2]);
            int w = Integer.valueOf(args[3]);
            int count = 0;

            //TODO
            int[][] countMatrix = conditionProb(h1,h2,corpus);
            for (int i = 0; i < countMatrix.length; i++) {
                count += countMatrix[i][1];
            }

            System.out.println(countMatrix[w][1]);
            System.out.println(count);
            if(count == 0)
                System.out.println("undefined");
            else
                System.out.println(String.format("%.7f", countMatrix[w][1]/(double)count));
        }
        else if(flag == 600){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int h1 = Integer.valueOf(args[3]);
            int h2 = Integer.valueOf(args[4]);
            //TODO
            int count = 0;
            double r = (double) n1 / (double) n2;
            int[][] countMatrix = conditionProb(h1,h2,corpus);
            for (int i = 0; i < countMatrix.length; i++) {
                count += countMatrix[i][1];
            }
            if(count == 0){
                System.out.println("undefined");
            }
            else {
                ArrayList<double[]> segList = new ArrayList<>();
                addToSeglist(segList,countMatrix,count);
                printSegList(segList, r);
            }
        }
        else if(flag == 700){
            int seed = Integer.valueOf(args[1]);
            int t = Integer.valueOf(args[2]);
            int h1=0,h2=0;

            Random rng = new Random();
            if (seed != -1) rng.setSeed(seed);

            if(t == 0){
                // TODO Generate first word using r
                double r = rng.nextDouble();
                if(h1 == 9 || h1 == 10 || h1 == 12){
                    return;
                }
                ArrayList<double[]> segList = new ArrayList<>();
                unigram(segList, corpus);
                h1 = findInterval(segList,r);
                System.out.println(h1);
                // TODO Generate second word using r
                r = rng.nextDouble();
                int[][] countMatrix = conditionProb(h1, corpus);
                segList = new ArrayList<>();
                int count = 0;
                for (int i = 0; i < countMatrix.length; i++) {
                    count += countMatrix[i][1];
                }
                addToSeglist(segList,countMatrix,count);
                h2 = findInterval(segList,r);
                System.out.println(h2);
            }
            else if(t == 1){
                h1 = Integer.valueOf(args[3]);
                // TODO Generate second word using r
                double r = rng.nextDouble();
                int[][] countMatrix = conditionProb(h1, corpus);
                List<double[]>segList = new ArrayList<>();
                int count = 0;
                for (int i = 0; i < countMatrix.length; i++) {
                    count += countMatrix[i][1];
                }
                addToSeglist(segList,countMatrix,count);
                h2 = findInterval(segList,r);
                System.out.println(h2);
            }
            else if(t == 2){
                h1 = Integer.valueOf(args[3]);
                h2 = Integer.valueOf(args[4]);
            }

            while(h2 != 9 && h2 != 10 && h2 != 12){
                double r = rng.nextDouble();
                int w  = 0;
                // TODO Generate new word using h1,h2
                int[][] countMatrix = conditionProb(h1,h2,corpus);
                List<double[]> segList = new ArrayList<>();
                int count = 0;
                for (int i = 0; i < countMatrix.length; i++) {
                    count += countMatrix[i][1];
                }
                if(count == 0){
                    countMatrix = conditionProb(h2,corpus);
                    for (int i = 0; i < countMatrix.length; i++) {
                        count += countMatrix[i][1];
                    }
                }
                addToSeglist(segList, countMatrix, count);
                w = findInterval(segList,r);
                System.out.println(w);
                h1 = h2;
                h2 = w;
            }
        }

        return;
    }
}
