package git;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
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

	public void delete(String fileName) throws IOException {
		replaceSelected(fileName, " *deleted* " + fileName, getStringLine("test/Index.txt", fileName));	
	}
	
	public void edit(String fileName) throws IOException {
		replaceSelected(fileName, " *edited* " + fileName, getStringLine("test/Index.txt", fileName));
	}
	
	public int getStringLine(String fileName, String line) throws IOException {
		LineNumberReader reader = new LineNumberReader(new FileReader(fileName));
		int num = 0;
		reader.setLineNumber(num);
		while((reader.readLine() != line)) {
			num++;
		}
		reader.close();
		return reader.getLineNumber();
	}
	
	public void replaceSelected(String fileName, String newLineContent, int lineToBeEdited) {
		ChangeLineInFile changeFile = new ChangeLineInFile();
	    changeFile.changeALineInATextFile(fileName, newLineContent, lineToBeEdited);
	}
	
}
