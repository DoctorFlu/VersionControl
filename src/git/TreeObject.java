package git;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.*;
public class TreeObject {
	private HashMap<String, String> shas = new HashMap<String, String>();
	private ArrayList<String> privStrings;
	
	public TreeObject (ArrayList<String> strings) throws IOException {
		privStrings = new ArrayList<String>();
		for (int i = 0; i < strings.size(); i++) {
			privStrings.add(strings.get(i) + " : " + SHA1.encryptThisString(strings.get(i)));
		}
		File objectsFolder = new File ("test/objects");
		objectsFolder.mkdir();
		StringBuilder masterStringBuilder = new StringBuilder();
		for (int i = 0; i < privStrings.size(); i++) {
			masterStringBuilder.append (privStrings.get(i) + "\n");
		}
		String masterString = masterStringBuilder.toString ();
		String masterStringSHA = SHA1.encryptThisString(masterString);
		File SHAFile = new File ("test/objects/" + masterStringSHA);
		FileWriter SHAFileWriter = new FileWriter (SHAFile);
		SHAFileWriter.write(masterString);
		SHAFileWriter.close();
	}
	
	public void delete(String fileName) throws IOException {
		File file = new File("test/" + fileName);
		file.delete();
		replaceSelected(fileName, " *deleted* " + fileName, getStringLine("test/index.txt", fileName));	
	}
	
	public void replaceSelected(String fileName, String newLineContent, int lineToBeEdited) {
		ChangeLineInFile changeFile = new ChangeLineInFile();
	    changeFile.changeALineInATextFile(fileName, newLineContent, lineToBeEdited);
	}
	
	public int getStringLine(String fileName, String line) throws IOException {
		LineNumberReader reader = new LineNumberReader(new FileReader(fileName));
		int num = 0;
		reader.setLineNumber(num);
		while((reader.readLine() != line)) {
			num++;
		}
		return reader.getLineNumber();
	}
}
