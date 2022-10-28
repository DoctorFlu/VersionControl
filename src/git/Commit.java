package git;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Commit {
    String pTree;
	String summary;
	String author;
	String date;
	Commit parent ;
	Commit child;
	String sha1Hash;
	String parentSha1Hash;
	String childSha1Hash;
	boolean isHead;
	ArrayList<String> list = new ArrayList<String>();
	File HEAD = new File("test/HEAD");
	public Commit(String summary,String author, String parentHash) throws IOException
	{
		sha1Hash = encryptThisString(getSubsetFileContents());
		parentSha1Hash = parentHash;
		if (parentHash == null)
		{
			isHead = true;
		}
		else {
			isHead = false;
		}
		this.summary = summary;
		this.author = author;
		this.date = getDate();
		TreeObject tree = new TreeObject(getBlobTreeList());
		clearIndex();
		
		
		writeCommitFile();
		File index = new File ("test/Index.txt");
		BufferedReader br = new BufferedReader(new FileReader(index)); 
		ArrayList <String> toDelete = new ArrayList <String> ();
		String line = br.readLine();
		String hasDeleted = "false";
		
		if (hasDeleted.equals("false")) {
			this.addParent();
		}
		else {
			File treeF = new File ("test/" + pTree);
			this.delete(toDelete, treeF);
		}

		TreeObject tree1 = new TreeObject (list);
		//write to the current file: 
		writeToFile();
		FileWriter wr = new FileWriter(HEAD);
		wr.flush();
		wr.append("Test/Objects/" + this.getCommitHash ());
		wr.close();
		FileWriter writer = new FileWriter(index);
		writer.flush();
	}

	
	public String getSubsetFileContents()
	{
		return summary+ "\n" + date + "\n" + author + "\n" + parentSha1Hash;
	}
	
	public String getCommitHash()
	{
		sha1Hash = encryptThisString(getSubsetFileContents());
		return sha1Hash;
	}
	public void writeCommitFile() throws FileNotFoundException
	{
//		if (parentSha1Hash != null)
//		{
////			setParent();
//		}
//		if (child == null)
//		{
//			childSha1Hash = null;
//		}
//		else {
//			childSha1Hash = "test/objects/" + child.getCommitHash();
//		}
		
		PrintWriter pw = new PrintWriter("test/objects/" + sha1Hash + "\n");
//		System.out.println(sha1Hash);
		pw.append("objects/test/da39a3ee5e6b4b0d3255bfef95601890afd80709" + "\n");
		pw.append("test/objects/" + parentSha1Hash + "\n");
//		System.out.println(parentSha1Hash);
		pw.append("test/objects/" + childSha1Hash + "\n");
		System.out.println(childSha1Hash);
		pw.append(author + "\n");
		pw.append(date + "\n");
		pw.append(summary + "\n");
		pw.close();
	}
//	private void setParent () throws FileNotFoundException{
//			parent.setChild(this);	
////			parent.writeCommitFile();
//	}
	
	public void setChild (String child1Hash) throws FileNotFoundException {
		childSha1Hash = child1Hash;
		PrintWriter pw = new PrintWriter("test/objects/" + sha1Hash + "\n");
//		System.out.println(sha1Hash);
		pw.append("objects/test/da39a3ee5e6b4b0d3255bfef95601890afd80709" + "\n");
		pw.append("test/objects/" + parentSha1Hash + "\n");
//		System.out.println(parentSha1Hash);
		pw.append("test/objects/" + childSha1Hash + "\n");
		System.out.println(childSha1Hash);
		pw.append(author + "\n");
		pw.append(date + "\n");
		pw.append(summary + "\n");
		pw.close();
//		System.out.println(childSha1Hash);
	//	System.out.println("i ran");
	}
	
	public static String encryptThisString(String input)
	{
		try {	         
			MessageDigest md = MessageDigest.getInstance("SHA-1");	      
			byte[] messageDigest = md.digest(input.getBytes());	     
			BigInteger no = new BigInteger(1, messageDigest);         
			String hashtext = no.toString(16); 
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}	       
			return hashtext;
		}	 
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	//gets the date duh
	public String getDate()
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		return df.format(new Date());
		
	}
	public static void main(String[] args) throws IOException {
		
		Commit c1 = new Commit ("first commit", "JBao", null);
		Commit c2 = new Commit ("WEEEEE commit", "JBao", c1.getCommitHash());
		c1.setChild(c2.getCommitHash());
		Commit c3 = new Commit ("good mesure", "JBAO", c2.getCommitHash());
		c2.setChild(c3.getCommitHash());
		Commit c4 =  new Commit("yes", "JBAO", c3.getCommitHash());
		c3.setChild(c4.getCommitHash());
//		System.out.println("first commit child is " + c4.getCommitHash());
		
	}
	public ArrayList<String> getBlobTreeList() throws IOException {
		BufferedReader reader;
		ArrayList<String> blobTreeList = new ArrayList<String>();
		reader = new BufferedReader(new FileReader("test/index.txt"));
		String line = reader.readLine();
		while (line != null) {
			blobTreeList.add(line);
			line = reader.readLine();
		}
		reader.close();
		if(parent != null) {
			blobTreeList.add("tree " + parent.pTree);
		}
		return blobTreeList;
	}
	
	public void clearIndex() throws FileNotFoundException {
		PrintWriter writer = new PrintWriter("test/index.txt");
		writer.print("");
		writer.close();
	}
	
	public void delete (ArrayList<String> list, File tree) throws IOException {
		String pTree = "";
		BufferedReader reader = new BufferedReader(new FileReader(tree));
		String line = reader.readLine();
		while (line != null) {
			if (line.contains("tree : ")) {
				pTree = line;
			} else if (list.size() == 0) {
				list.add(line);
			} else {
				boolean notFound = true;
				for (int i = 0; i < list.size(); i++) {
					if (line.substring(48).equals(list.get(i))) {
						list.remove(i);
						i--;
						notFound = false;
					}
				}
				if (notFound == true) {
					list.add(line);
				}
			}
			line = reader.readLine();
		}
		if (!list.isEmpty()) {
			File newF = new File ("Test/objects/" + pTree.substring(7));
			this.delete(list, newF);
		}
		else {
			list.add(pTree);
		}
	}
	
	private void addParent () throws IOException {
		if (parent != null) {
			File parentF = new File ("Test/Objects/"+ parent);
			BufferedReader br = new BufferedReader(new FileReader(parentF)); 
			String line = br.readLine();
			br.close ();
			
			list.add("tree : " + line.substring(8));
		}
		
	}
	private File writeToFile() throws IOException {
		File f = new File("test/objects/" + parentSha1Hash);
		FileWriter writer = new FileWriter(f);
		writer.append("objects/" + pTree + "\n");
		if (parent!=null) {
			writer.append("objects/" + parent + "\n");	
		}
		else {
			writer.append("\n");
		}
		writer.append("\n");
		writer.append(author + "\n");
		writer.append(date + "\n");
		writer.append(summary);
		writer.close();
		return f;
	}

}
