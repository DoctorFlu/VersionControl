package git;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class Index {
	
	HashMap<String,String> blobList = new HashMap<String,String>();
	public static void main(String[] args) throws IOException  {
	
		Index git = new Index();
		git.init();
		git.add("foo.txt");
		git.add("bar.txt");
		git.add("foobar.txt");
		git.remove("foobar.txt");
	}
	public Index()
	{
		
	}
	public void init() throws FileNotFoundException {
		File f = new File ("Index.txt");
		PrintWriter pw = new PrintWriter("test/" + f);
		pw.append("");
		pw.close();
		File d = new File("test/objects");
		d.mkdir();
	}
	private void writeIndex() throws FileNotFoundException {
		PrintWriter pw = new PrintWriter("test/Index.txt");
		for (String listKey : blobList.keySet())
		{
			pw.append("Blob " + listKey + " : " + blobList.get(listKey) + "\n");
			
		}
		pw.close();
	}
	public void add(String fileName) throws IOException{
		Blob newBlob = new Blob("test/" + fileName);
		blobList.put(fileName, newBlob.getFileName());
		writeIndex();
		
		}
	public void remove(String fileName) throws IOException{

		File fileToRemove = new File("test/objects/" + blobList.get(fileName));
		fileToRemove.delete();
		blobList.remove(fileName);
		writeIndex();
		
	}

	
	
	
}
