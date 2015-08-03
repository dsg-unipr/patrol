package it.unipr.ce.dsg.p2pgame.platform.test;

import it.unipr.ce.dsg.p2pgame.platform.GamePeer;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceEvolve;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceMobile;
import it.unipr.ce.dsg.p2pgame.util.SHA1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class GamePeerTest {

	public static void main(String[] args) throws IOException, NumberFormatException, InterruptedException {

		System.out.println("GAME PEER");

		String threadId = new Long(Thread.currentThread().getId()).toString();

		Random ran = new Random();
		int port = ran.nextInt(9999);
		int gamePort = port+2;//ran.nextInt(9999); //occorrono porte consecutive

		//TODO: modificare World per avere dei valori con la granularita' desiderata!!!
		Random rand = new Random();
		double a = Math.round(( rand.nextDouble()*(10 - 0) + 0 )*10);
		System.out.println("a " + a/10);



		int unL = ran.nextInt(16);
		int pwdL = ran.nextInt(16);
		System.out.println("username( " + unL + " ): " + BigInteger.valueOf(unL).toString(16) );
		System.out.println("password( " + pwdL + " ): " + BigInteger.valueOf(pwdL).toString(16) );

		GamePeer gp = new GamePeer(port, port-1, 160, "", "localhost", 1235, gamePort, gamePort-1, "localhost", 2222, 4000,1000,64000,2000);
		//GamePeer gp = new GamePeer(port, port-1, 4, "", "localhost", 1235, gamePort, gamePort-1, "localhost", 2222);

		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		while (true) {

			String input = console.readLine();

			String[] info = input.split(":");
			if(info[0] != null){
				if (info[0].trim().contentEquals("REG")){
					gp.registerOnServer(BigInteger.valueOf(unL).toString(16), BigInteger.valueOf(pwdL).toString(16));
				} else if (info[0].trim().contentEquals("LOG")){
					gp.loginOnServer(BigInteger.valueOf(unL).toString(16), BigInteger.valueOf(pwdL).toString(16));
				} else if (info[0].trim().contentEquals("LOGOUT")){
					gp.logoutOnServer(threadId);
				} else if (info[0].trim().contentEquals("START")){
					gp.startGame(0,10,0,10,0,10, 1,2, 1);
				} else if (info[0].trim().contentEquals("MOVE")){
					System.out.println("Player position: " + gp.getPlayer().getPosX() + ", " + gp.getPlayer().getPosY() + ", " + gp.getPlayer().getPosZ());
					//gp.movePlayer(Double.parseDouble(info[1].trim()), Double.parseDouble(info[2].trim()), Double.parseDouble(info[3].trim()));
					gp.movePlayer(Double.parseDouble(info[1].trim()), Double.parseDouble(info[2].trim()), Double.parseDouble(info[3].trim()), threadId);
					System.out.println("New Player position: " + gp.getPlayer().getPosX() + ", " + gp.getPlayer().getPosY() + ", " + gp.getPlayer().getPosZ());
				} else if (info[0].trim().contentEquals("PRINTRES")){
					gp.printRespPlayer();
				} else if (info[0].trim().contentEquals("PUBRES")){
					//gp.publishPosition();
					gp.publishPosition(threadId);
				}

				else if (info[0].trim().contentEquals("CREATEEVO")){
					GameResourceEvolve evo = new GameResourceEvolve("aaa", info[1].trim(), 10, 5000, 1);
					gp.addToMyResource(evo);
					System.out.println("Risorsa evolvibile creata");
				}
				else if (info[0].trim().contentEquals("CREATEMOB")){
					gp.createMobileResource("mobile",1);
				} else if (info[0].trim().contentEquals("MOVEMOB")){
					String id = "";
					try {
						id = SHA1.convertToHex(SHA1.calculateSHA1("mobile"));
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					}
					GameResourceMobile res = gp.getMyMobileResourceFromId(id);
					System.out.println("Mobile resource " + id + " position: " + res.getX() + ", " + res.getY() + ", " + res.getZ());
					//gp.moveResourceMobile(id, Double.parseDouble(info[1].trim()), Double.parseDouble(info[2].trim()), Double.parseDouble(info[3].trim()));
					gp.moveResourceMobile(id, Double.parseDouble(info[1].trim()), Double.parseDouble(info[2].trim()), Double.parseDouble(info[3].trim()), threadId);
					res = gp.getMyMobileResourceFromId(id);
					System.out.println("NEW Mobile resource " + id + " position: " + res.getX() + ", " + res.getY() + ", " + res.getZ());
				} else if (info[0].trim().contentEquals("PRINTMOB")){
					gp.printRespResources();
				} else if (info[0].trim().contentEquals("PUBMOB")){
					//gp.publishResourceMobile();
					gp.publishResourceMobile(threadId);
				} else if (info[0].trim().contentEquals("PRINTMY")){
					gp.printMyResource();
				}

				else if (info[0].trim().contentEquals("PRINTVIS")){
					gp.printVision();
				} else if (info[0].trim().contentEquals("FINDVIS")){
					try {
						System.out.println("Find for vision: " + Double.parseDouble(info[1].trim()) + ", " + Double.parseDouble(info[2].trim()) + ", " + Double.parseDouble(info[3].trim()));
						//String position = SHA1.convertToHex(SHA1.calculateSHA1(Double.parseDouble(info[1].trim()) + "+" + Double.parseDouble(info[2].trim()) + "+" + Double.parseDouble(info[3].trim())));
						String pos = new Double(Double.parseDouble(info[1].trim())).toString() + new Double(Double.parseDouble(info[2].trim())).toString() + new Double(Double.parseDouble(info[3].trim())).toString();
						String position = SHA1.convertToHex(SHA1.calculateSHA1(pos));
						System.out.println("Key to find " + position);
						Object resp = gp.requestResource(position, Double.parseDouble(info[1].trim()), Double.parseDouble(info[2].trim()), Double.parseDouble(info[3].trim()), new Long(Thread.currentThread().getId()).toString());
						if (resp != null)
							System.out.println("RICEVUTO QUALCOSA!!!!");
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					}
				} else if (info[0].trim().contentEquals("PRINT")){
					gp.printAllInfo();
				} else if (info[0].trim().contentEquals("FIX")){
					gp.fixFinger(threadId);
				} else if (info[0].trim().contentEquals("EXIT"))
					System.exit(0);

				else
					System.out.println("generic messsage");
			}

		}
	}
}
