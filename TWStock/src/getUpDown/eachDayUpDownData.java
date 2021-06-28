package getUpDown;

import java.text.ParseException;


public class eachDayUpDownData {
	public String date;
	public float open;
	public float high;
	public float low;
	public float close;
	public float amount;
	public int y;
	public int m;
	public int d;
	eachDayUpDownData(String rawInput) throws ParseException {
		 loadData(rawInput);
	}
	void loadData(String rawInput) throws ParseException {
		rawInput=rawInput.replace(",", "");
		String [] input= rawInput.split(" ");
		this.open=Float.parseFloat(input[1]);
		this.high=Float.parseFloat(input[2]);
		this.low=Float.parseFloat(input[3]);
		this.close=Float.parseFloat(input[4]);
		this.amount=Float.parseFloat(input[7]);
		this.date=input[0];
		String[] date = input[0].split("/");
		d=Integer.parseInt(date[2]);
		m=Integer.parseInt(date[1]);
		y=Integer.parseInt(date[0]);
	}
	
	@Override
    public String toString()
    {
         return this.date+","+this.open+","+this.high+","+this.low+","+this.close+","+this.amount;
    }

}
