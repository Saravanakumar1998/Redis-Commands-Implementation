import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Transactiontask {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		MyLinkedHashMap<String, String> map = new MyLinkedHashMap<String, String>();
		ArrayList<MyLinkedHashMap<String, String>> list = new ArrayList<>();
		Methods ms = new Methods();
		System.out.println("Enter : ");
		String str = br.readLine();
		while (ms.equals(str, "exit") == false) {
			try {
				if (str.startsWith("set")) {
					String[] parts = str.split(" ");
					String key = parts[1];
					String val = parts[2];
					map.set(key, val);
				} else if (ms.myStartWith(str, "get")) {
					String[] parts = str.split(" ");
					String key = parts[1];
					System.out.println(map.get(key));
				} else if (ms.myStartWith(str, "unset")) {
					String[] parts = str.split(" ");
					String key = parts[1];
					map.unset(key);
				} else if (ms.myStartWith(str, "count")) {
					String[] parts = str.split(" ");
					String val = parts[1];
					map.count(val);
				} else if (ms.myStartWith(str, "begin")) {
					list.add(map);
					map = new MyLinkedHashMap();
					int i = list.size() - 1;
					while (i >= 0) {
						MyLinkedHashMap<String, String> temp = list.get(i);
						for (int index = 0; index < temp.size(); index++) {
							map.set((String) temp.Keyset().toArray()[index], (String) temp.Valueset().toArray()[index]);
						}
						break;
					}
				} else if (ms.myStartWith(str, "commit")) {
					int i = list.size() - 1;
					while (i >= 0) {
						MyLinkedHashMap<String, String> temp = list.get(i);
						for (int index = 0; index < map.size(); index++) {
							temp.set((String) map.Keyset().toArray()[index], (String) map.Valueset().toArray()[index]);
						}
						break;
					}
				} else if (ms.myStartWith(str, "rollback")) {
						int i = 0;
						while (i < map.size()) {
							map.unset((String) map.Keyset().toArray()[i]);
						}
						map = list.get(list.size() - 1);
						list.remove(list.size() - 1);
					
				} else if (ms.myStartWith(str, "update")) {
					String[] parts = str.split(" ");
					String key = parts[1];
					String val = parts[2];
					map.update(key, val);
				} else if (ms.myStartWith(str, "display")) {
					map.display();
				}
				else if (ms.myStartWith(str, "flushall")) {
					map.flushall();
				}
				else if (ms.myStartWith(str, "lrange")) {
					String[] parts = str.split(" ");
					String start = parts[1];
					String end = parts[2];
					map.lrange(start, end);
				}
				else {
					System.out.println("follow correct command");
				}
			}
			catch(IndexOutOfBoundsException e) {
	            System.out.println("Index out of bounds. Array length: " + map.size());
	        }
			
			str = br.readLine();
		}
	}
}