package it.unipr.ce.dsg.p2pgame.network.test.multi;

import it.unipr.ce.dsg.p2pgame.util.SHA1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * This class is used for generate NWB (NetWorkBench) file of network and
 * relative Python script for draw peer on a ring.
 *
 * @param args the file which contains the name list of other file with finger table
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class DrawChordForNWB {


	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String fileConfig = "configNWBFile.txt";
		ArrayList<String> inputFile = new ArrayList<String>();
		HashMap<String, NodeInfo> infos = new HashMap<String, NodeInfo>();

		final String fileOut = "chordNWB.nwb";
		final String fileScript = "chordNWB.py";

		if (args.length < 1 ) {
			System.out.println("USE: fileConfig ");
			System.exit(1);
		}
		else {

			fileConfig = args[0].trim();
		}

		//Load the name of input file
		try {
			FileReader reader = new FileReader(fileConfig);
			BufferedReader input = new BufferedReader(reader);

			String line;
			try {
				while ( (line = input.readLine()) != null )
				{
					inputFile.add(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//load all info from input files
		for (int i = 0; i < inputFile.size(); i++){
			//new input file
			FileReader readerFile = null;
			try {
				readerFile = new FileReader(inputFile.get(i));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			BufferedReader in = new BufferedReader(readerFile);

			//read info from all input file
			String fileLine;
			try {
				NodeInfo nodeInfo = new NodeInfo();

				nodeInfo.setPeerId(in.readLine().trim());

				fileLine = in.readLine().trim();
				if ( fileLine.compareTo("NULL") == 0 )
					nodeInfo.setSuccessorId(null);
				else
					nodeInfo.setSuccessorId(fileLine);

				fileLine = in.readLine().trim();
				if ( fileLine.compareTo("NULL") == 0 )
					nodeInfo.setPredecessorId(null);
				else
					nodeInfo.setPredecessorId(fileLine);
				//nodeInfo.setPredecessorId(in.readLine().trim());

				ArrayList<String> finger = new ArrayList<String>();
				while ( (fileLine = in.readLine()) != null )
				{
					finger.add(fileLine);
					System.out.println(nodeInfo.getPeerId() + " -> " + fileLine);
				}

				nodeInfo.setFingerEntry(finger);

				infos.put(nodeInfo.getPeerId(), nodeInfo);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		//write info for NWB
		FileOutputStream fileNWB = null;
		try {
			fileNWB = new FileOutputStream(fileOut);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    PrintStream outNWB = new PrintStream(fileNWB);
	    outNWB.println("##### Chord test on cluster Script for nwb:");
	    outNWB.println("#NWB Data for Streaming P2P\n *Nodes " + infos.size() + "\n id*int label*string color*string ");

	    Set<String> key_set = infos.keySet();
		Iterator<String> iter = key_set.iterator();
		HashMap<String,Integer> app = new HashMap<String,Integer>();

		int i = 0;
		while (iter.hasNext()){
			String key = iter.next();
			outNWB.println((i+1) + " \"" + key + "\" \"blue\" ");
			app.put(key, i+1);
			i++;
		}

		outNWB.println("*DirectedEdges\n source*int target*int directed*string color*string");

		ArrayList<String> unordered_key = new ArrayList<String>();

		key_set = infos.keySet();
		iter = key_set.iterator();
		//while(iter.hasNext()){
		for (int q = 0; q < infos.size(); q++){

			String key = iter.next();

			unordered_key.add(key);

			if ( infos.get(key).getPredecessorId().compareTo("NULL") != 0 )
				outNWB.println( Integer.toString(app.get(infos.get(key).getPredecessorId())) + " " + Integer.toString(app.get(key)) + " \"true\" \"green\" " );

			if ( infos.get(key).getSuccessorId().compareTo("NULL") != 0 )
				outNWB.println( Integer.toString(app.get(infos.get(key).getSuccessorId())) + " " + Integer.toString(app.get(key)) + " \"true\" \"green\" " );


			//System.out.println("key " + key + " Finger size: " + infos.get(key).getFingerEntry().size());
			for (int k = 0; k < infos.get(key).getFingerEntry().size(); k++) {

				//app.get(Integer.toString( ( (JXTARendezvousSuperPeer) n).RPV.get(j).JXTAID ) )  + " " +
				//app.get(Integer.toString(n.JXTAID)) + " \"true\" \"blue\" "
				//System.out.println("for " + key + " " + app.get(key) + " " + app.get(infos.get(key).getFingerEntry().get(k)));
				System.out.println("infos " + infos.get(key).getFingerEntry().get(k) + "  Primo membro " + app.get(infos.get(key).getFingerEntry().get(k)) + "  Secondo membro " + app.get(key));
				outNWB.println( Integer.toString(app.get(infos.get(key).getFingerEntry().get(k))) + " " + Integer.toString(app.get(key)) + " \"true\" \"green\" " );

			}
		}


		System.out.println("UNORDERED PEER ID:");
		for (int s = 0; s < unordered_key.size(); s++){
			System.out.println(unordered_key.get(s));
		}


		ArrayList<String> ordered_key = new ArrayList<String>();

		BigInteger maxDist = (BigInteger.valueOf(2L).pow(160)).subtract(BigInteger.valueOf(1L));


		ordered_key.add(unordered_key.get(0));

		System.out.println("Ordering...");

		while( (unordered_key.size() -1) > 0 ){
			BigInteger diff = (BigInteger.valueOf(2L).pow(160)).subtract(BigInteger.valueOf(1L));
			//System.out.println("unordered size " + unordered_key.size() + " ordered size " + ordered_key.size());
			//System.out.println("Try to get " + (ordered_key.size()-1));
			String peerId = ordered_key.get(ordered_key.size()-1);
			//System.out.println(" for peer " + peerId);
			unordered_key.remove(peerId);

			BigInteger peerPos = SHA1.convertFromStringToBig(peerId);

			String successor = null;
			for (int t=0; t < unordered_key.size(); t++){

				String key = unordered_key.get(t);
				BigInteger onTab = SHA1.convertFromStringToBig(key);

				if ( onTab.compareTo(peerPos) > 0  &&  diff.compareTo(onTab.subtract(peerPos)) > 0 ){

					diff = onTab.subtract(peerPos);

					successor = key;
				}
				else if (onTab.compareTo(peerPos) <= 0 && diff.compareTo((onTab.subtract(peerPos)).add(maxDist)) > 0 ) {
					diff = (onTab.subtract(peerPos)).add(maxDist);

					successor = key;
				}

			}

			ordered_key.add(successor);

//			System.out.println("ALL ORDERED");
//			for (int k = 0; k < ordered_key.size(); k++){
//				System.out.println(k + "  " + ordered_key.get(k));
//			}
//
//			System.out.println("POST - size " + ordered_key.size());
		}

		System.out.println("ORDERED PEER ID:");
		for (int l=0; l < ordered_key.size(); l++){
			System.out.println(ordered_key.get(l));
		}



		//create Pyton script for NWB
		FileOutputStream fileScriptNWB = null;
		try {
			fileScriptNWB = new FileOutputStream(fileScript);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    PrintStream outScriptNWB = new PrintStream(fileScriptNWB);
		int ray = 1000;
		int x_center = 100;
		int y_center = 100;
		double x[] = new double[infos.size()];
		double y[] = new double[infos.size()];

		//for (int p = 0; p < infos.size(); p++)
		for (int p = 0; p < ordered_key.size(); p++)
		{
			x[p] = x_center + ray * Math.cos((p*6.283185307*(360.0/(double)infos.size()))/360.0);
			y[p] = y_center + ray * Math.sin((p*6.283185307*(360.0/(double)infos.size()))/360.0);
			//outScriptNWB.println("for n in g.nodes: " + "n"+(p+1)+".x=" + x[p] +";" + "n"+(p+1)+".y=" + y[p] +";");
			//outScriptNWB.println("for n in g.nodes: " + "n"+app.get(ordered_key.get(p+1))+".x=" + x[app.get(ordered_key.get(p))] +";" + "n"+app.get(ordered_key.get((p+1)))+".y=" + y[app.get(ordered_key.get(p))] +";");
		}
		for (int p = 0; p < ordered_key.size(); p++)
		{
			//outScriptNWB.println("for n in g.nodes: " + "n"+(p+1)+".x=" + x[app.get(ordered_key.get(p))-1] +";" + "n"+(p+1)+".y=" + y[app.get(ordered_key.get(p))-1] +";");
			outScriptNWB.println("for n in g.nodes: " + "n"+app.get(ordered_key.get(p))+".x=" + x[p] +";" + "n"+app.get(ordered_key.get(p))+".y=" + y[p] +";");

		}
	}

}
