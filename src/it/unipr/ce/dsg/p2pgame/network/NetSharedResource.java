package it.unipr.ce.dsg.p2pgame.network;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Shared resource among all thread of a peer.
 * Can store information about PeerId and NetPeerInfo
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class NetSharedResource {

	private int readers = 0;
	private int writers = 0;
	private int writeRequests = 0;
	
	public synchronized void lockRead() throws InterruptedException{
		while (writers > 0 || writeRequests > 0) {
			wait();	
		}
		readers++;
	}
	
	public synchronized void unlockedRead() {
		readers--;
		notifyAll();
	}
	
	public synchronized void lockWrite() throws InterruptedException{
		writeRequests++;
		while(readers > 0 || writers > 0){
			wait();
		}
		writeRequests--;
		writers++;
	}
	
	public synchronized void unlockWriter() throws InterruptedException{
		writers--;
		notifyAll();
	}
	
	
	
	/**
	 * Store peer's informations
	 */
	private ConcurrentHashMap<String, NetPeerInfo> peersInfo;

	/**
	 * Store peer's identifier
	 */
	private ConcurrentHashMap<String, String> peersId;

	/**
	 *
	 * Constructor for shared resource. Initialize all.
	 *
	 */
	public NetSharedResource() {
		super();

		this.peersInfo = new ConcurrentHashMap<String, NetPeerInfo>();
		this.peersId = new ConcurrentHashMap<String, String>();
	}


	/**
	 *
	 * Get all shared information about peer
	 *
	 * @return the HashMap with all peer info
	 * @throws InterruptedException 
	 *
	 */
	public ConcurrentHashMap<String, NetPeerInfo> getPeersInfo() throws InterruptedException {
		
		lockRead();
		ConcurrentHashMap<String, NetPeerInfo> pI = new ConcurrentHashMap<String, NetPeerInfo>(peersInfo);
		unlockedRead();
		return pI;
		//return peersInfo;
		
		
	}


	/**
	 *
	 * Get all shared identifier about peer
	 *
	 * @return the HashMap with all peer identifier
	 * @throws InterruptedException 
	 *
	 */
	public ConcurrentHashMap<String, String> getPeersId() throws InterruptedException {
		
		lockRead();
		ConcurrentHashMap<String, String> pI = new ConcurrentHashMap<String, String>(peersId);
		unlockedRead();
		return pI;
		//return peersId;
	}

	/**
	 *
	 * Get identifier information saved for 'owner'
	 *
	 * @param owner the identifier of cache owner
	 * @return the peer identifier saved
	 * @throws InterruptedException 
	 *
	 */
	public String getIdFor(String owner) throws InterruptedException{
		
		lockRead();
		String id = new String(this.peersId.get(owner));
		unlockedRead();
		return id;
		
		//return this.peersId.get(owner);
	}

	/**
	 *
	 * Get peer information saved for 'owner'
	 *
	 * @param owner the identifier of cache owner
	 * @return the peer information saved
	 * @throws InterruptedException 
	 *
	 */
	public NetPeerInfo getInfoFor(String owner) throws InterruptedException{
		
		lockRead();
		NetPeerInfo info = new NetPeerInfo(this.peersInfo.get(owner));
		unlockedRead();
		return info;
		
		//System.out.println("############NET SHARED REOUSRCE GETINFOFOR#############à");
		//return this.peersInfo.get(owner);
	}

	/**
	 *
	 * Save new identifier and information on 'owner' cache
	 *
	 * @param owner the owner of cache to update
	 * @param npi the information about peer to save
	 * @param pId the identifier about peer to save
	 * @throws InterruptedException 
	 *
	 */
	public synchronized void saveInfo(String owner, NetPeerInfo npi, String pId) throws InterruptedException{

		
		lockWrite();
		
		if (this.peersInfo.containsKey(owner)){
			this.peersInfo.remove(owner);
			this.peersId.remove(pId);
		}

		this.peersInfo.put(owner, npi);
		this.peersId.put(owner, pId);
		
		
		unlockWriter();
		
	/*	if(!this.peersInfo.containsKey(owner))
		{
			this.peersInfo.put(owner, npi);
			this.peersId.put(owner, pId);
			
			
		}
*/
	}
	
	
	
	//not protected against Read\Write concurrency
	public void printPeersInfo()
	{
		Set<String> key_set=peersInfo.keySet();
		Iterator<String> iterator=key_set.iterator();
		int i=0; 
		while(iterator.hasNext())
		{
			
			String key=iterator.next();
			NetPeerInfo info=peersInfo.get(key);
			System.out.println("PEER INFO "+i+" "+key +" : "+ info.getIpAddress()+" "+info.getPortNumber() );
			i++;
		}
	}


}
