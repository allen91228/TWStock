package companyList;
public class companyListData {
	int[] ID;
	String[] name;
	int length;
	companyListData(int[] ID, String[] name){
		if(ID.length==name.length) {

			length=name.length;
			this.ID=ID;
			this.name=name;
		}
	}
	public String getName(int ID){
		for(int i =0;i<length;i++) {
			if(this.ID[i]==ID) {
				return name[i];
			}
		}
		return null; 
	}
}
