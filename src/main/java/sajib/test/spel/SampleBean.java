package sajib.test.spel;

import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.stereotype.Component;

@Component("sampleBean")
public class SampleBean {
	private String property = "String property";
	private ArrayList<Integer> arrayList = new ArrayList<Integer>();
	private HashMap<String, String> hashMap = new HashMap<String, String>();

	public SampleBean() {
		arrayList.add(36);
		arrayList.add(45);
		arrayList.add(98);

		hashMap.put("key 1", "value 1");
		hashMap.put("key 2", "value 2");
		hashMap.put("key 3", "value 3");
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public ArrayList<Integer> getArrayList() {
		return arrayList;
	}

	public void setArrayList(ArrayList<Integer> arrayList) {
		this.arrayList = arrayList;
	}

	public HashMap<String, String> getHashMap() {
		return hashMap;
	}

	public void setHashMap(HashMap<String, String> hashMap) {
		this.hashMap = hashMap;
	}

}