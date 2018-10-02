import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankList {
	// read the record file to display rank list
	
	static class Record implements Comparable<Record> {
		String user;
		int time;
		int step;
		
		Record(String user, int time, int step) {
			this.user = user;
			this.time = time;
			this.step = step;
		}

		@Override
		public int compareTo(Record rhs) {
			if(step != rhs.step)
				return step - rhs.step;
			if(time != rhs.time)
				return time - rhs.time;
			return user.compareTo(rhs.user);
		}
		
		@Override
		public String toString() {
			return user + " " + step + " steps, " + time + "s";
		}
	}
	
	public static void addRecord(String user, int time, int step) {
		StringBuffer newRecord = new StringBuffer();
		File file = new File("rank.txt");
		confirmFile(file);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			boolean found = false;
			
			while ((tempString = reader.readLine()) != null) {
				newRecord.append(tempString);
				if (tempString.contains(user)) {
					found = true;
					newRecord.append("##" + time + "##" + step);
				}
				newRecord.append("\r\n");
			}
			if(!found) {
				newRecord.append(user + "##" + time + "##" + step + "\r\n");
			}
			
			FileWriter fw = new FileWriter("rank.txt");
			fw.write(newRecord.toString());
			fw.flush();
			fw.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public static String readRank() {
		StringBuffer rankstr = new StringBuffer();
		File file = new File("rank.txt");
		confirmFile(file);
		List<Record> list = new ArrayList<Record>();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				String str[] = tempString.split("##");
				for(int i = 1; i < str.length; i += 2) {
					list.add(new Record(str[0], Integer.parseInt(str[i]), 
							Integer.parseInt(str[i+1])));
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		Collections.sort(list);
		int rank = 1;
		for (Record entry : list) {
			rankstr.append("Rank " + rank + ": " + entry + "\n");
			rank++;
			if(rank > 10)
				break;
		}
		return rankstr.toString();
	}
	
	private static void confirmFile(File fileName) {
		try {
			if (!fileName.exists()) {
				fileName.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
