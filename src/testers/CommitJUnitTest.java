package testers;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import git.Commit;

class CommitJUnitTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() throws IOException {
		Commit parent = new Commit("first commit","JBAO",null);		
		Commit child = new Commit("WEEEEE commit","JBAO", parent);
		parent.setChild(child);
		Commit grandChild = new Commit("yes","JBAO", child);
		child.setChild(grandChild);
		Commit greatGrandChild = new Commit("good mesure", "JBAO", grandChild);
		grandChild.setChild(greatGrandChild);
		
		File commitFile = new File("test/objects/69c39c201703df9a6985250cbc56e51338cdd43");
		File childFile = new File("test/objects/163210369adabb79a4678b6e212b35fbcacd0d11");
		File grandChildFile = new File("test/objects/a481eea41ba06192c957ce04a96712d8b494a3f0");
		File greatGrandChildFile = new File("");
		
		assertTrue(commitFile.exists());
		assertTrue(childFile.exists());
		assertTrue(grandChildFile.exists());
		assertTrue(greatGrandChildFile.exists());
		
		commitFile.delete();
		childFile.delete();
		grandChildFile.delete();
		greatGrandChildFile.delete();
		
	}

}
