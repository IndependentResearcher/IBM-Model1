/**
 * File : IBMMODEL1.java
 * Author : Arpit Sharma
 * Functionality : This class is an implementation of IBM language translation model 1
 */

package ibmModel1;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class IBMMODEL1 {
	
	private String corpusFileGer = "./short.de";
	private String corpusFileEng = "./short.en";
	private String testFileloc = "./testWords";
	
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		IBMMODEL1 ibm = new IBMMODEL1();

		if(args.length!=0){
			ibm.corpusFileGer = args[0];
			ibm.corpusFileEng = args[1];
			ibm.testFileloc = args[2];
		}
		
//		Start recording time
		final long startTime = System.currentTimeMillis();

//		Call the model() function to train and fing the probabilities of translations
		ibm.model();
		
//		End recording time and report it
		final long endTime = System.currentTimeMillis();
		System.out.println("Total execution time: " + (double)(endTime - startTime)/1000 + " seconds");
	}
	
	
	/**
	 * Function to train using IBM model1 algorithm using "corpusFileGer" as foreign language training corpus file and
	 * "corpusFileEng" as English language training corpus file. 
	 * And finally printing the top five most probable translations of the words given in test file "testFileloc"
	 * The 5 most probable word translations are arranged in ascending order of probabilities.
	 */
	private void model(){
		try{
			String gerline = "";
			String engline = "";
			int numberOfGerWords = 0;
			BufferedReader germanFileTemp = new BufferedReader(new FileReader(corpusFileGer));
			while((gerline=germanFileTemp.readLine())!=null){
				ArrayList<String> gerWords = new ArrayList<String>();
				for(String tempGer : gerline.split(" ")){
					if(!tempGer.equalsIgnoreCase(",") && !tempGer.equalsIgnoreCase(".") && 
							!tempGer.equalsIgnoreCase(";")&& !tempGer.equalsIgnoreCase("?")
							&& !tempGer.equalsIgnoreCase("\"") && !tempGer.equalsIgnoreCase("\'")){ 
						if(!gerWords.contains(tempGer)){
							gerWords.add(tempGer);
							numberOfGerWords++;
						}
					}
				}
			}
			germanFileTemp.close();
				
			HashMap<String,Double> probsT = new HashMap<String,Double>();
			BufferedReader germanFile = new BufferedReader(new FileReader(corpusFileGer));
			BufferedReader englishFile = new BufferedReader(new FileReader(corpusFileEng));

			while((gerline=germanFile.readLine())!=null){
				ArrayList<String> gerWords = new ArrayList<String>();
				ArrayList<String> engWords = new ArrayList<String>();

				for(String tempEng : gerline.split(" ")){
					if(!tempEng.equalsIgnoreCase(",") && !tempEng.equalsIgnoreCase(".") && 
							!tempEng.equalsIgnoreCase(";")&& !tempEng.equalsIgnoreCase("?")
							&& !tempEng.equalsIgnoreCase("\"") && !tempEng.equalsIgnoreCase("\'")){ 
						gerWords.add(tempEng);
					}
				}

				engline = englishFile.readLine();
				for(String tempEng : engline.split(" ")){
					if(!tempEng.equalsIgnoreCase(",") && !tempEng.equalsIgnoreCase(".") && 
							!tempEng.equalsIgnoreCase(";")&& !tempEng.equalsIgnoreCase("?")
							&& !tempEng.equalsIgnoreCase("\"") && !tempEng.equalsIgnoreCase("\'")){ 
						engWords.add(tempEng);
					}
				}
				engWords.add("NULL");
				for(int i=0;i<gerWords.size();++i){
					for(int j=0;j<engWords.size();++j){
						String temp = gerWords.get(i) + "|"+engWords.get(j);
						probsT.put(temp, (double)1/numberOfGerWords);
					}
				}
			}
			germanFile.close();
			englishFile.close();
//			System.out.println("Size of probs :-" + probs.size());

//			Starting EM here:::::::::::
			for(int iter=0;iter<10;++iter){
//				Initializing Count(f|e)::::::::;
				HashMap<String,Double> count = new HashMap<String,Double>();
				HashMap<String,Double> total = new HashMap<String,Double>();

				BufferedReader germanFile1 = new BufferedReader(new FileReader(corpusFileGer));
				BufferedReader englishFile1 = new BufferedReader(new FileReader(corpusFileEng));
				gerline = "";
				engline = "";
				while((gerline=germanFile1.readLine())!=null){
					ArrayList<String> gerWords = new ArrayList<String>();
					ArrayList<String> engWords = new ArrayList<String>();

					for(String tempEng : gerline.split(" ")){
						if(!tempEng.equalsIgnoreCase(",") && !tempEng.equalsIgnoreCase(".") && 
								!tempEng.equalsIgnoreCase(";")&& !tempEng.equalsIgnoreCase("?")
								&& !tempEng.equalsIgnoreCase("\"") && !tempEng.equalsIgnoreCase("\'")){ 
							gerWords.add(tempEng);
						}
					}

					engline = englishFile1.readLine();
					for(String tempEng : engline.split(" ")){
						if(!tempEng.equalsIgnoreCase(",") && !tempEng.equalsIgnoreCase(".") && 
								!tempEng.equalsIgnoreCase(";")&& !tempEng.equalsIgnoreCase("?")
								&& !tempEng.equalsIgnoreCase("\"") && !tempEng.equalsIgnoreCase("\'")){ 
							engWords.add(tempEng);
						}
					}
					engWords.add("NULL");
					for(int i=0;i<gerWords.size();++i){
						for(int j=0;j<engWords.size();++j){
							String temp = gerWords.get(i) + "|"+engWords.get(j);
							count.put(temp, 0.0);
							total.put(engWords.get(j), 0.0);
						}
					}
				}
				germanFile1.close();
				englishFile1.close();




				BufferedReader germanFile2 = new BufferedReader(new FileReader(corpusFileGer));
				BufferedReader englishFile2 = new BufferedReader(new FileReader(corpusFileEng));
				while((gerline=germanFile2.readLine())!=null){
					engline = englishFile2.readLine();

					ArrayList<String> gerWords = new ArrayList<String>();
					for(String temp : gerline.split(" ")){
						if(!temp.equalsIgnoreCase(",") && !temp.equalsIgnoreCase(".") && 
								!temp.equalsIgnoreCase(";")&& !temp.equalsIgnoreCase("?")
								&& !temp.equalsIgnoreCase("\"") && !temp.equalsIgnoreCase("\'")){ 
							gerWords.add(temp);
						}
					}

					ArrayList<String> engWords = new ArrayList<String>();
					for(String temp : engline.split(" ")){
						if(!temp.equalsIgnoreCase(",") && !temp.equalsIgnoreCase(".") && 
								!temp.equalsIgnoreCase(";")&& !temp.equalsIgnoreCase("?")
								&& !temp.equalsIgnoreCase("\"") && !temp.equalsIgnoreCase("\'")){ 
							engWords.add(temp);
						}
					}
					engWords.add("NULL");

					HashMap<String,Double> sumTotalGer = new HashMap<String,Double>();
					for(int i=0;i<gerWords.size();++i){
						sumTotalGer.put(gerWords.get(i), 0.0);
						for(int j=0;j<engWords.size();++j){
							String tempKey = gerWords.get(i)+"|"+engWords.get(j);
							double tempVal = probsT.get(tempKey);
							sumTotalGer.put(gerWords.get(i),sumTotalGer.get(gerWords.get(i))+tempVal);
						}
					}

					for(int i=0;i<gerWords.size();++i){
						for(int j=0;j<engWords.size();++j){
							String tempKey = gerWords.get(i)+"|"+engWords.get(j);
							double tempValT = probsT.get(tempKey);
							double tempValC = count.get(tempKey);
							double tempValTOTAL = total.get(engWords.get(j));
							double tempNew = tempValT/sumTotalGer.get(gerWords.get(i));
							count.put(tempKey, tempValC+tempNew);
							total.put(engWords.get(j), tempValTOTAL+tempNew);
						}
					}
				}
				germanFile2.close();
				englishFile2.close();
				
				for(String key : count.keySet()){
					String temp = key.substring(key.indexOf("|")+1,key.length());
					double tmp = total.get(temp);
					double tempVal = count.get(key)/tmp;
					probsT.put(key, tempVal);
				}
			}
			
			ArrayList<String> testArray	= new ArrayList<String>();
			BufferedReader testFile = new BufferedReader(new FileReader(testFileloc));
			String line = "";
			while((line=testFile.readLine())!=null){
				testArray.add(line.trim());
			}
			testFile.close();
			
			HashMap<String,HashMap<String,Double>> maps = new HashMap<String,HashMap<String,Double>>();
			for(int i=0;i<testArray.size();++i){
				HashMap<String,Double> temp = new HashMap<String,Double>();
				maps.put(testArray.get(i), temp);
			}

			for(String k : probsT.keySet()){
				if(testArray.contains(k.substring(k.indexOf('|')+1, k.length()))){
					String german = k.substring(0,k.indexOf('|'));
					double probValue = probsT.get(k);
					HashMap<String,Double> temp2 = maps.get(k.substring(k.indexOf('|')+1, k.length()));
					temp2.put(german, probValue);
					maps.put(k.substring(k.indexOf('|')+1, k.length()), temp2);
				}
			}
			
			IBMMODEL1 ib = new IBMMODEL1();
			for(String k : maps.keySet()){
				HashMap<String,Double> te = maps.get(k);
				HashMap<String,Double> newMap = ib.sortByValue(te);
				maps.put(k, newMap);
			}
			
			for(String k : maps.keySet()){
				System.out.println("For "+k);
				HashMap<String,Double> te = maps.get(k);
				int counter = 0;
				System.out.println("TRANSLATION\tPROBABILITY");
				for(String ke : te.keySet()){
					if (counter >= te.size()-5){
					System.out.println(ke + "  \t " + te.get(ke));
					}
					counter++;
				}
				System.out.println("\n");
			}
		}catch(IOException e){e.printStackTrace();}
	}
	
	/**
	 * Function to sort a HashMap according to values
	 * @param map
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private HashMap<String,Double> sortByValue(Map<String,Double> map) {
	     List list = new LinkedList(map.entrySet());
	     Collections.sort(list, new Comparator() {
	          public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o1)).getValue())
	              .compareTo(((Map.Entry) (o2)).getValue());
	          }
	     });

	    HashMap result = new LinkedHashMap<String,Double>();
	    for (Iterator it = list.iterator(); it.hasNext();) {
	        Map.Entry entry = (Map.Entry)it.next();
	        result.put(entry.getKey(), entry.getValue());
	    }
	    return result;
	} 
}
