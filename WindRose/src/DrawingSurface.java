
import processing.core.PApplet;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class DrawingSurface extends PApplet {
	private WindRose wr;
	
	public DrawingSurface() {
		String s = JOptionPane.showInputDialog("Pick a start date (enter in mm/dd/yyyy format):");
		String e = JOptionPane.showInputDialog("Pick an end date (enter in mm/dd/yyyy format) or leave blank to use current date:");
		ArrayList<WindVector> wv = DataLoader.loadData(parseDate(s), parseDate(e));
		wr = new WindRose(wv);
		double[][] r = wr.relativeFrequencies();
		for(double[] d : r) System.out.println(Arrays.toString(d));
	}

	private long parseDate(String date) {
		if(date == null || date.equals("")) return System.currentTimeMillis();
		String[] s = date.split("/");
		Calendar d = Calendar.getInstance();
		d.set(Integer.parseInt(s[2]), Integer.parseInt(s[0]) - 1, Integer.parseInt(s[1]), 0, 0, 0);
		return d.getTimeInMillis();
	}
	
	// The statements in the setup() function 
	// execute once when the program begins
	public void setup() {

	}
	
	// The statements in draw() are executed until the 
	// program is stopped. Each statement is executed in 
	// sequence and after the last line is read, the first 
	// line is executed again.
	public void draw() { 
		background(255);   // Clear the screen with a white background
		wr.draw(this, width / 3, height / 2, width * 0.2f);
	}
}