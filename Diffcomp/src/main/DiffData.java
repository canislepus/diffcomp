package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DiffData {

	private HashMap<String, ProCon> data;

	public DiffData() {
		data = new HashMap<String, ProCon>(127);
	}
	
	/**
	 * Get the data as a sorted ProCon array.
	 * @return sorted data
	 */
	public List<ProCon> getSortedList(){
		List<ProCon> sorted = data.values().stream().sorted().collect(Collectors.toList());
		return sorted;
	}
	
	/**
	 * Get the data as a sorted String array (less detailed though).
	 * Format: name value
	 * @return sorted data
	 */
	public List<String> getSortedStringList(){
		List<String> sorted = data.values().stream().sorted().map( val -> (val.name + "     " + val.getValue()) ).collect(Collectors.toList());
		
		return sorted;
	}
	/**
	 * Returns wether or not the data set already contains key.
	 * @param key the key to look for
	 * @return whether or not key already exists in the data set
	 */
	public boolean contains(String key){
		return data.containsKey(key);
	}
	
	/**
	 * Add a ProCon element to the data set.
	 * @param pc element to be added
	 */
	public void add(ProCon pc){
		data.put(pc.name, pc);
	}
	
	/**
	 * Add amount to the pro value of the ProCon at key.
	 * @param key
	 * @param amount
	 */
	public void pro(String key, int amount){
		data.get(key).increasePro(amount);
	}
	
	/**
	 * Add amount to the con value of the ProCon at key.
	 * @param key
	 * @param amount
	 */
	public void con(String key, int amount){
		data.get(key).increaseCon(amount);
	}
	
	/**
	 * Remove a ProCon element from the data set.
	 * @param key key of the element to be removed
	 */
	public void remove(String key){
		data.remove(key);
	}

	/**
	 * Clears the data map.
	 */
	public void clear() {
		data.clear();
	}

	/**
	 * Loads data from a data file (binary; written by saveToFile(String path))
	 * into the object's data map. The map is not cleared beforehand to allow
	 * loading multiple files into one data set. Use clear() if you want to
	 * clear the map.
	 * 
	 * @param path
	 *            The path to the data file
	 * @throws IOException
	 *             see DataInputStream
	 */
	public void loadFromFile(String path) throws IOException {
		DataInputStream in = new DataInputStream(new FileInputStream(path));
		while (in.available() > 8) {
			String key = in.readUTF();
			if(data.containsKey(key)){								//If the entry already exists, simply add the data.
				data.get(key).increasePro(in.readInt());
				data.get(key).increaseCon(in.readInt());
			}else{
			data.put(key, new ProCon(key, in.readInt(), in.readInt()));
			}
		}
		in.close();
	}

	/**
	 * Save data from the data map to a file in binary form.
	 * @param path The path to the file to save to
	 * @throws IOException
	 */
	public void saveToFile(String path) throws IOException{
		DataOutputStream out = new DataOutputStream(new FileOutputStream(path));
		for(String key : data.keySet()){
			out.writeUTF(key);
			ProCon pc = data.get(key);
			out.writeInt(pc.pro);
			out.writeInt(pc.con);
		}
		out.close();
	}

}
