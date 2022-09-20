package git;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
	public Commit(String pTree, String summary,String author, Commit parent) throws FileNotFoundException
	{
		if (parent == null)
		{
			isHead = true;
		}
		else
			isHead = false;
		child = null;
		this.pTree = pTree;
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
	public static void main(String[] args) throws FileNotFoundException {
		
		Commit c1 = new Commit ("someTree", "first commit", "JBao", null);
		Commit c2 = new Commit ("somTree2", "WEEEEE commit", "JBao", c1);
		System.out.println("first commit child is" + c1.childSha1Hash);
		Commit c3 = new Commit ("someTree3", "good mesure", "JBAO", c2);
		
		
	}

}
