
public class Person implements Comparable
{
	
	static int sortmethod;
	public String date, province, riding, last, first, gender, occupation, party,pNumber, rawV, vPer, elected;
	public Person(String date, String pNumber, String province, String riding, String last, String first, String gender, String occupation, String party, String rawV, String vPer, String elected)
	{
		this.date = date.toLowerCase();
		this.province = province.toLowerCase();
		this.riding = riding.toLowerCase();
		this.last = last.toLowerCase();
		this.first = first.toLowerCase();
		this.gender = gender.toLowerCase();
		this.occupation = occupation.toLowerCase();
		this.party = party.toLowerCase();
		this.pNumber = pNumber.toLowerCase();
		this.rawV = rawV.toLowerCase();
		this.vPer = vPer.toLowerCase();
		this.elected = elected.toLowerCase();
			sortmethod = 1;	
	}
	
	
	public void setDate(int year, int month, int day)
	{
		this.date = year + "/" + month + "/" + day;
	}
	
	
	@Override
	public int compareTo(Object o) 
	{
			
		Person other = null;
		
		try
		{
			other = (Person)(o);
		}
		catch(ClassCastException exc)
		{
			System.out.println("Other object was not a Person Object, incorrect comparison");
			exc.printStackTrace();
		}
			
		switch(sortmethod)
		{
		 case 1:return(date.compareTo(other.date));
		 case 2:return(province.compareTo(other.province));
		 case 3:return(riding.compareTo(other.riding));
		 case 4:return(last.compareTo(other.last));
		 case 5:return(first.compareTo(other.first));
		 case 6:return(gender.compareTo(other.gender));
		 case 7:return(occupation.compareTo(other.occupation));
		 case 8:return(party.compareTo(other.party));
		 case 9:return(Integer.parseInt(pNumber)-Integer.parseInt(other.pNumber));
		 case 10:return(Integer.parseInt(rawV)-Integer.parseInt(other.rawV));
		 case 11:return (int) (Double.parseDouble(vPer)-Double.parseDouble(other.vPer));
		 case 12: return (Integer.parseInt(elected)-Integer.parseInt(other.elected));
		 default:System.out.println("Invalid sortmethod number: Recheck setSortMethod() for a parameter check");
		 return 0;
		}
	}
	
	public static boolean setSortMethod(int x)
	{
		if(x>0 && x<13)
		{
			sortmethod = x;
			return true;
		}
		
		System.out.println("setSortMethod was passed an invalid parameter"); 	
		return false;
	}
}
