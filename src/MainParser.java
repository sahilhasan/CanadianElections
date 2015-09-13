import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class MainParser {

	Set<String> endResult = new TreeSet<String>();
	static String[] rawData;

	public static void main(String[] args) {
		MainParser parse = new MainParser();
		parse.parse();
		try {
			parse.riseAndFall();
		} catch (IOException e) {

			e.printStackTrace();
		}
		 //parse.interpretBestWin();
		 
	}

	int pCount = 0;
	static Person[] ppl = new Person[38778]; // TODO: Autonomous length does not
												// come with purchase also check
		                               // HeapSort
	ArrayList<String> partyName = new ArrayList<String>();

	public void parse() {
		String file = "HFER_e.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {

				rawData = line.split(cvsSplitBy);
				Person p = new Person(rawData[0], rawData[2], rawData[3],
						rawData[4], rawData[5], rawData[6], rawData[7],
						rawData[8], rawData[9], rawData[10], rawData[11],
						rawData[12]);
				if (!partyName.contains(rawData[9].toLowerCase()))
				{
					partyName.add(rawData[9].toLowerCase());
				}
				String dateToBeSorted = p.date;
				int pNumber;
				if (!(p.pNumber.length() > 3)) {
					pNumber = Integer.parseInt(p.pNumber);
				} else {
					pNumber = -1;
				}
				int tempYear;
				int tempMonth;
				int tempDay;
				String[] dateStorage;
				if (pNumber > 36) // hardcode, highly customized to retarded
									// date formats, saves dates in YYYY/MM/DD format no matter format present in .csv
				{
					dateStorage = dateToBeSorted.split("/");
					tempYear = Integer.parseInt(dateStorage[2]); // gets double
																	// digit
																	// year
					tempYear += 2000;
					tempMonth = Integer.parseInt(dateStorage[0]);
					tempDay = Integer.parseInt(dateStorage[1]);
					p.setDate(tempYear, tempMonth, tempDay);
				} else if (pNumber == 36) // 1900s to 2000s split occurred
											// during this parliament
				{
					dateStorage = dateToBeSorted.split("/");
					tempYear = Integer.parseInt(dateStorage[2]);
					if (tempYear > 50) {
						tempYear += 1900;
					} else {
						tempYear += 2000;
					}
					tempMonth = Integer.parseInt(dateStorage[0]);
					tempDay = Integer.parseInt(dateStorage[1]);
					p.setDate(tempYear, tempMonth, tempDay);
				} else if (pNumber > 9 && pNumber < 36) {
					dateStorage = dateToBeSorted.split("/");
					tempYear = Integer.parseInt(dateStorage[2]);
					tempYear += 1900;
					tempMonth = Integer.parseInt(dateStorage[0]);
					tempDay = Integer.parseInt(dateStorage[1]);
					p.setDate(tempYear, tempMonth, tempDay);
				} else if (pNumber == 9) // THEY CHANGED FORMAT IN THE MIDDLE OF
											// THIS PARLIAMENT GDI
				{
					dateStorage = dateToBeSorted.split("/");
					if (dateStorage[0].length() > dateStorage[1].length()) {
						tempYear = Integer.parseInt(dateStorage[0]);
						tempMonth = Integer.parseInt(dateStorage[1]);
						tempDay = Integer.parseInt(dateStorage[2]);
					} else {
						tempYear = Integer.parseInt(dateStorage[2]) + 1900;
						tempMonth = Integer.parseInt(dateStorage[0]);
						tempDay = Integer.parseInt(dateStorage[1]);
					}
					p.setDate(tempYear, tempMonth, tempDay);

				} else if (pNumber <= 8 && pNumber >= 1) // and again in this
															// one
				{
					if (dateToBeSorted.charAt(4) == '-') {
						dateStorage = dateToBeSorted.split("-");
					} else {
						dateStorage = dateToBeSorted.split("/");
					}
					tempYear = Integer.parseInt(dateStorage[0]);
					tempMonth = Integer.parseInt(dateStorage[1]);
					tempDay = Integer.parseInt(dateStorage[2]);
					p.setDate(tempYear, tempMonth, tempDay);

				}

				ppl[pCount] = p;
				pCount++;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void getAllDates()  //quick tester
	{
		for (int count = 10000; count < 25460; count ++) {
			Person p = ppl[count];
			System.out.println(p.date);
		}
	}

	public static String capitalize(String givenString) // Prettify
	{
		try {
			String[] arr = givenString.split(" ");
			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < arr.length; i++) {
				sb.append(Character.toUpperCase(arr[i].charAt(0)))
						.append(arr[i].substring(1)).append(" ");
			}
			return sb.toString().trim();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Find only the best chance of winning by job type
	 */
	public void interpretBestWin() {
		ArrayList<String> jobNames = new ArrayList<String>();
		HashMap<String, Person> output = new HashMap<String, Person>();
		PriorityQueue<Person> sorted = heapSort(7, ppl);
		while (sorted.peek() != null) {
			Person p = sorted.poll();
			String occ = capitalize(p.occupation);

			if (!jobNames.contains(occ) && occ != "") 
			{
				jobNames.add(occ); // Add specific job names to iterative
									// list...will be used to go through hash
									// table
				output.put(occ, p);
			}
			// if the job is already there compare the voting percentage they
			// have and replace if larger
			else {
				try {
					if (Double.parseDouble(p.vPer) > Double.parseDouble(output
							.get(occ).vPer)) {
						output.put(occ, p);
					}
				} catch (Exception e) {

				}
			}
		}
		

		File fout;
		PrintWriter out = null;
		try
		{
			fout = new File("out.txt");
			out = new PrintWriter(fout);
		}
		catch(FileNotFoundException e)
		{
			
		}
		
		
		for (String s : jobNames) {
			
			 out.println(s + "," + output.get(s).vPer + "," + output.get(s).date.substring(0, 4) + "," +
			 capitalize(output.get(s).party) + "," +
			 capitalize(output.get(s).riding) + "?");
		}
	}
	
	public ArrayList<ArrayList<Person>> diffByParliament() //Data Structures are interesting...
	{
		ArrayList<ArrayList<Person>> split = new ArrayList<ArrayList<Person>>(); //look it works 
		PriorityQueue<Person> sorted = heapSort(9, ppl);
		for (int i = 1; i <= 40; i ++) //iterate through all parliaments
		{
			String s = i + "";

			Person p = sorted.poll();
			ArrayList<Person> oneParli = new ArrayList<Person>(); //storage for single parliament
			while (p != null && p.pNumber.equals(s)) //make sure person obj isnt empty and it is correct parliamentary number
			{	
				oneParli.add(p);
				p = sorted.poll();
			}
			split.add(i - 1, oneParli);
			
		}
		return split;
	}
	
	public void riseAndFall() throws IOException
	{
		ArrayList<ArrayList<Person>> org = diffByParliament(); //each one is a single parliament
		FileWriter fout;
		BufferedWriter out = null;
		try
		{
			fout = new FileWriter("analysis.csv",false); //output file
			out = new BufferedWriter(fout);
			out.write("Parliament Number,");
			for (String s : partyName)
			{
				out.write(s + " - elected,");
				out.write(s + " - running,");
			}
			out.write('\n');
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
	} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	
		
		int[][] countElec = new int[org.size()][partyName.size()];
		int[][] countRun = new int[org.size()][partyName.size()];
		int i = 0;
		//now limited to a single parliament 
		for (ArrayList<Person> p : org)
		{
			for(Person x : p)
			{
				int j = partyName.indexOf((x.party.trim().toLowerCase()));
				
				countRun[i][j]++;
				if (x.elected.trim().equals("1"))
				{
					countElec[i][j] ++;
				}
			}
			i++;
		}
			
		for(int k = 0; k < org.size(); k++)
		{
			out.write(k + 1 + ",");
			for(int l = 0;l<partyName.size();l++)
			{
				out.write(countElec[k][l] + ",");
				out.write(countRun[k][l] + ",");
			}
			out.newLine();
		}
		out.close();


	}
	

	



	public PriorityQueue<Person> heapSort(int method, Person[] p) {
		Person.setSortMethod(method);

		PriorityQueue<Person> temp = new PriorityQueue<Person>();
		for (int i = 0; i < p.length && p[i] != null; i++) {
			temp.add(p[i]);
//			System.out.println(i);

		}

		return temp;

	}

}
