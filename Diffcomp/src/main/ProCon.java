package main;

public class ProCon implements Comparable<ProCon>{
	protected int pro;
	protected int con;
	protected int value;
	
	protected String name;
	
	protected static int proWeight = 1;			//Weighting factors for pro and con
	protected static int conWeight = 1;	
	
	public ProCon(String name, int pro, int con) {
		this.pro = pro;
		this.con = con;
		this.name = name;
		value = (pro * proWeight) - (con * conWeight);
	}
	
	public ProCon(String name){
		this(name, 0, 0);
	}

	@Override
	public int compareTo(ProCon pc) {
		return pc.value - this.value;
	}
	
	/**
	 * Set the pro value and recalculate total value.
	 * @param pro new value for pro
	 */
	public void setPro(int pro){
		this.pro = pro;
		this.value = (pro * proWeight) - (con * conWeight);
	}
	
	/**
	 * Set the weighting factor for the pro value
	 * @param w the weighting factor
	 */
	public void setProWeighting(int w){
		proWeight = w;
	}
	
	public int getPro(){
		return pro;
	}
	
	/**
	 * Get the weighting factor for the pro value
	 * @return the weighting factor
	 */
	public int getProWeighting(){
		return proWeight;
	}
	
	/**
	 * Increase pro value and update total value
	 */
	public void increasePro(int amount){
		pro += amount;
		value = (pro * proWeight) - (con * conWeight);
	}
	
	/**
	 * Decrease pro value and update total value
	 */
	public void decreasePro(int amount){
		pro -= amount;
		value = (pro * proWeight) - (con * conWeight);
	}
	
	/**
	 * Set the con value and recalculate total value.
	 * @param con new value for con
	 */
	public void setCon(int con){
		this.con = con;
		this.value = (pro * proWeight) - (con * conWeight);
	}
	
	/**
	 * Set the weighting factor for the con value
	 * @param w the weighting factor
	 */
	public void setConWeighting(int w){
		conWeight = w;
	}
	
	public int getCon(){
		return con;
	}
	
	/**
	 * Get the weighting factor for the con value
	 * @return the weighting factor
	 */
	public int getConWeighting(){
		return conWeight;
	}
	
	/**
	 * Increase con value and update total value
	 */
	public void increaseCon(int amount){
		con += amount;
		value = (pro * proWeight) - (con * conWeight);
	}
	
	/**
	 * Decrease con value and update total value
	 */
	public void decreaseCon(int amount){
		con -= amount;
		value = (pro * proWeight) - (con * conWeight);
	}
	
	
	/**
	 * Get total value, calculated using pro, con and the respective weighting factors.
	 * @return total value
	 */
	public int getValue(){
		value = (pro * proWeight) - (con * conWeight);
		return value;
	}
	
}
