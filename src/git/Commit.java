package git;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
	public Commit(String summary,String author, Commit parent) throws IOException
	{
		if (parent == null)
		{
			isHead = true;
		}
		else
			isHead = false;
		child = null;
		this.summary = summary;
		this.author = author;
		this.date = getDate();
		
		this.parent = parent;
	
	
		if (this.parent == null)
		{
			parentSha1Hash = null;
		}
		else {
	
			parentSha1Hash = "test/objects/" + parent.getCommitHash();
			setParent();
		}
		TreeObject tree = new TreeObject(getBlobTreeList());
		clearIndex();
		//generates the sha1 based on contents of commit
		sha1Hash = encryptThisString(getSubsetFileContents());
		
		
		writeCommitFile();
		
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
		if (child == null)
		{
			childSha1Hash = null;
		}
		else {
			childSha1Hash = "test/objects/" + child.getCommitHash();
			System.out.println("doasfjsOK" +  child);
		}
		PrintWriter pw = new PrintWriter("test/objects/" + sha1Hash);
		pw.append(pTree + "\n");
		pw.append(parentSha1Hash + "\n");
		pw.append(childSha1Hash + "\n");
		pw.append(author + "\n");
		pw.append(date + "\n");
		pw.append(summary);
		pw.close();
	}
	private void setParent () throws FileNotFoundException{
			parent.setChild(this);	
			parent.writeCommitFile();
	
	}
	
	private void setChild (Commit child) {
		this.child = child;
	//	System.out.println("i ran");
	}
	
	
	
	
	//sha 1 creator
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
		// For specifying wrong message digest algorithms
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
		Commit c2 = new Commit ("WEEEEE commit", "JBao", c1);
		System.out.println("first commit child is" + c1.childSha1Hash);
		Commit c3 = new Commit ("good mesure", "JBAO", c2);
		Commit c4 =  new Commit("yes", "JBAO", c3);
		
		
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
}
