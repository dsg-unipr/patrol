package it.unipr.ce.dsg.p2pgame.util;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class CheckOutput {
	
	private  PrintStream output = null;
	
	
	public CheckOutput(String file)
	{
		try {
			output = new PrintStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void print_msg(String id,String msg){
		
		this.output.print(id + " : "+ msg+ "\n");
		System.out.println("lenght: "+ msg.length()+"  " +  id + " : "+ msg+ "\n");
		
	}

}
