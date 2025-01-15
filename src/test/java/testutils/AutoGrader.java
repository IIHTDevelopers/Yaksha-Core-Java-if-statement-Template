package testutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.IfStmt;

public class AutoGrader {

	// Test if the code uses only 'if' statements and not 'if-else'
	public boolean testIfStatementsOnly(String filePath) throws IOException {
		System.out.println("Starting testIfStatementsOnly with file: " + filePath);

		File participantFile = new File(filePath); // Path to participant's file
		if (!participantFile.exists()) {
			System.out.println("File does not exist at path: " + filePath);
			return false;
		}

		FileInputStream fileInputStream = new FileInputStream(participantFile);
		JavaParser javaParser = new JavaParser();
		CompilationUnit cu;
		try {
			cu = javaParser.parse(fileInputStream).getResult()
					.orElseThrow(() -> new IOException("Failed to parse the Java file"));
		} catch (IOException e) {
			System.out.println("Error parsing the file: " + e.getMessage());
			throw e;
		}

		System.out.println("Parsed the Java file successfully.");

		boolean hasIfStatements = false;
		boolean hasIfElseStatements = false;

		// Log the parsed if statements (to see what JavaParser captures)
		System.out.println("------ If Statements ------");
		for (IfStmt ifStmt : cu.findAll(IfStmt.class)) {
			System.out.println("If Statement found: " + ifStmt);
			hasIfStatements = true;
			if (ifStmt.getElseStmt().isPresent()) {
				hasIfElseStatements = true;
			}
		}

		// Check if only 'if' statements are used
		System.out.println("Has 'if' statements: " + hasIfStatements);
		System.out.println("Has 'if-else' statements: " + hasIfElseStatements);

		boolean result = hasIfStatements && !hasIfElseStatements;
		System.out.println("Test result: " + result);

		return result;
	}
}
