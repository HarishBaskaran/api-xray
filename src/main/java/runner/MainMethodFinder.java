package runner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.github.javaparser.ast.body.MethodDeclaration;

import config.ConfigReader;
import config.FindDifference;
import utils.FileOperations;

public class MainMethodFinder {

	private static File masterFolder;
	private static File bundleFolder;
	static String branch = "VCTRS-73819\\";
	static String branchBase = System.getProperty("user.dir") + "\\BundleVersions\\";
	String masterBase = System.getProperty("user.dir") + "\\BundleVersions\\pms\\";

	public static void main(String[] args) throws Exception {

		MainMethodFinder main = new MainMethodFinder();

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		System.out.println(dtf.format(now));

//		main.startWriting("account").xray("account");
//		main.startWriting("comment").xray("comment");
//		main.startWriting("profile").xray("profile");
		main.startWriting("property").xray("property");
//		main.startWriting("rate").xray("rate");
//		main.startWriting("relay").xray("relay");
//		main.startWriting("reservation").xray("reservation");
//		main.startWriting("report").xray("report");
//		main.startWriting("servicerequest").xray("servicerequest");
//		main.startWriting("integration").xray("integration");
//		main.startWriting("payment").xray("payment");

	}

	public MainMethodFinder() {
		ConfigReader.fileClean("./trace.txt");
		ConfigReader.fileClean("./result.txt");
		ConfigReader.fileClean("./src/main/java/files/positiveDifference.txt");
		ConfigReader.fileClean("./src/main/java/files/negativeDifference.txt");
		ConfigReader.fileClean("./methods.txt");
		ConfigReader.fileClean("./innerMethods.txt");
	}

	private MainMethodFinder startWriting(String service) {

		ConfigReader.fileClean("./src/main/java/files/positiveDifference.txt");
		ConfigReader.fileClean("./src/main/java/files/negativeDifference.txt");

		writeFiles(false, new File("./src/main/java/files/paths/search-paths.txt"),
				branchBase + branch + service + "service");
		writeFiles(false, new File("./src/main/java/files/paths/search-bundle.txt"),
				branchBase + branch + service + "service");
		writeFiles(false, new File("./src/main/java/files/paths/search-master.txt"), masterBase + service + "service");

		writeFiles(false, new File("./src/main/java/files/paths/search-interface.txt"),
				branchBase + branch + service + "interface");

		writeFiles(true, new File("./result.txt"),
				"\n--------------------------------------\n" + service + "\n--------------------------------------\n");

		writeFiles(true, new File("./methods.txt"),
				"\n--------------------------------------\n" + service + "\n--------------------------------------\n");

		writeFiles(true, new File("./innerMethods.txt"),
				"\n--------------------------------------\n" + service + "\n--------------------------------------\n");

		System.out.println(
				"\n--------------------------------------\n" + service + "\n--------------------------------------");

		return this;

	}
	
	private MainMethodFinder startWritingPmscommon(String service) {

		ConfigReader.fileClean("./src/main/java/files/positiveDifference.txt");
		ConfigReader.fileClean("./src/main/java/files/negativeDifference.txt");

		writeFiles(false, new File("./src/main/java/files/paths/search-paths.txt"),
				branchBase + branch + service );
		writeFiles(false, new File("./src/main/java/files/paths/search-bundle.txt"),
				branchBase + branch + service );
		writeFiles(false, new File("./src/main/java/files/paths/search-master.txt"), masterBase + service );

		

		writeFiles(true, new File("./result.txt"),
				"\n--------------------------------------\n" + service + "\n--------------------------------------\n");

		writeFiles(true, new File("./methods.txt"),
				"\n--------------------------------------\n" + service + "\n--------------------------------------\n");

		writeFiles(true, new File("./innerMethods.txt"),
				"\n--------------------------------------\n" + service + "\n--------------------------------------\n");

		System.out.println(
				"\n--------------------------------------\n" + service + "\n--------------------------------------");

		return this;

	}


	Map<MethodDeclaration, String> result = new HashMap<MethodDeclaration, String>();

	private void xray(String api) throws Exception {

		masterFolder = ConfigReader.getMasterFolders();
		bundleFolder = ConfigReader.getBundleFolders();

		ConfigReader.fileClean("./src/main/java/files/occurence.txt");
		System.out.println("started Checking for changes");

		boolean bool = new FindDifference(masterFolder, bundleFolder).findBundleDiff();

		if (bool) {

//			result.putAll(Executor4.runner());
			result.putAll(Executor5.runner());
		} else
			System.out.println("No changes found");

		String s = "\n----------------------------------------------------\n";

		result.entrySet().forEach(res -> {
			FileOperations.writeFiles(new File("./result.txt"), res.getKey() + s, true);
			FileOperations.writeFiles(new File("./methods.txt"), res.getKey().getNameAsString() + "\n", true);

		});
		

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		System.out.println(dtf.format(now));
	}

	private void writeFiles(boolean bool, File file, String s) {

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file, bool);

			fileOutputStream.write(s.getBytes(), 0, s.length());

		} catch (IOException e1) {
			System.out.println(e1);
		}

	}
}
