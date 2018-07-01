package CST8284_Assignment2;


import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class PlotMyAddress {
	
	/****Fields used to store persons and addresses******/
	 static Scanner input; static Formatter output; //in and out 
	static ArrayList<Person> persons = new ArrayList<>();
	static ArrayList<Address> addresses = new ArrayList<>();
	/**
	 * First step: converting （assignment 1） xx.txt=>xx.cvs
	 * 
	 * @param targetFilePath
	 *            for xx.txt file
	 * @param destFilePath
	 *            for xx.cvs file
	 * @throws IOException
	 */
	private static void convertFile(String targetFilePath, String destFilePath) throws IOException {
		 Person person; Address address; // declare references  for objects
		 String[] records; // used to store lines for input
		 String[] names; String[] streetInfo; String[] cityProv; // three kinds info for each element of records
		 String nextLine; //cursor stands for current line
		 
		 // set up input and output
		input = new Scanner(Paths.get(targetFilePath)); output = new Formatter(destFilePath);
		
		//loop the targetFile
		while (input.hasNext()) {
			nextLine = input.nextLine();

			while (nextLine != null) {
				records = new String[4];// including post code

				for (int i = 0; i < 4; i++) {
					records[i] = nextLine;
					if (input.hasNext())
						nextLine = input.nextLine();
				}

				names = records[0].split("\\s*(\\s|,)\\s*");
				streetInfo = records[1].split("\\s+");
				cityProv = records[2].split("\\s*(\\s|,)\\s*");

				person = new Person();
				for(String s: names) System.out.println(s);
				person.setFirstName(names[0]);
				person.setLastName(names[1]);

				if (names[1].equalsIgnoreCase("and")) {
					person.setLastName(names[3]);
				}

				if (names.length == 2) {
					person.setSpouseFirstName("");
					person.setSpouseLastName("");
				} else {
					person.setSpouseFirstName(names[2]);
					person.setSpouseLastName(names[3]);
				}
				// save current person into persons
				persons.add(person);

				address = new Address();
				address.setStreetNumber(streetInfo[0]);
				address.setStreetName(streetInfo[1]);
				address.setStreetType(streetInfo[2]);

				if (streetInfo.length == 3) {
					address.setStreetOrientation("");
				} else {
					address.setStreetOrientation(streetInfo[3]);
				}

				address.setCityName(cityProv[0]);
				address.setProvince(cityProv[1]);

				// save current address into addresses
				addresses.add(address);
				// records[3] is postal code, ignore
				
				
				//output into destFile
				output.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s%n", person.getFirstName(), person.getLastName(),
						person.getSpouseFirstName(), person.getSpouseLastName(), address.getStreetNumber(),
						address.getStreetName(), address.getStreetType(), address.getStreetOrientation(),
						address.getCityName(), address.getProvince());
				
				// skip  escaped lines
				if (nextLine.length() == 0 && input.hasNext())
					nextLine = input.nextLine();
				// check the end of file
				if (!input.hasNext()) {
					break;
				}
			}
		}
		input.close();
		output.close();
	}

/**
 * to build latlng.cvs by using saved ArrayLists(persons, addresses)
 * @param persons ArrayList for those people in file
 * @param addresses  ArrayList for those addresses in file
 * @param path3 for xx.cvs file
 * @throws IOException
 */
	private static void outputForLatLngFile(ArrayList<Person> persons, ArrayList<Address> addresses, String path3)
			throws IOException {
		output = new Formatter(path3);
		// header
		output.format("%s,%s,%s,%s,%s,%s%n", "Latitude", "Longitude", "Name", "Icon", "IconScale", "IconAltitude");
		for (int i = 0; i < persons.size(); i++) {
			
			output.format("%s,%s,%s,%s,%s,%s%n", addresses.get(i).getLat(), addresses.get(i).getLng(),
					persons.get(i).toString(), 111, 1, 1);
		}
		output.close();
	}
	public static void main(String[] args) {
		String path1 = "C:/CST8284/input/InputAddresses.txt";
		String path2 = "C:/CST8284/input/OutputAddresses.csv";
		String path3 = "C:/CST8284/output/LatLong.csv";
		try {
			convertFile(path1, path2);
			outputForLatLngFile(persons, addresses, path3);
		} catch (IOException | NoSuchElementException | IllegalStateException e) {
			System.out.println(e);
		} finally {
			
		}

	}
}