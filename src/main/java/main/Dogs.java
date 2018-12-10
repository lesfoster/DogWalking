package main;

import java.util.Hashtable;
import java.util.Vector;

import sort.Sort;

public class Dogs
{
	public Hashtable dogsHT = new Hashtable();
	public Vector dogsV	= null;
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

	}
	
	public void update ()
	{
		dogsV = retrieveVectorFromHT();
	}
	
	public Vector sort ()
	{
		dogsV = Sort.sort(retrieveVectorFromHT());
		
		return (dogsV);
	}
	
	public void addDog (Dog dog)
	{
		dogsHT.put(dog.getId(), dog);
	}
	
	public Dog getDog (String key)
	{
		return ( (Dog) dogsHT.get (key));
	}
	
	public Vector retrieveVectorFromHT ()
	{
		return (new Vector (dogsHT.values()));
	}

	public Vector getDogsV() {
		return dogsV;
	}

	public void setDogsV(Vector dogsV) {
		this.dogsV = dogsV;
	}
}
