public class LinkedListTest {

	public static void main(String[] args) {
		LinkedList theList = new LinkedList();

		theList.append(null);
		theList.append("");
		theList.append( "world" );
		theList.append("!!");
		theList.append("Hi");
		theList.append(null);
		theList.append(null);

		theList.print();
		
		if (theList.find(null)) {
			System.out.println("Found hello");
		} else {
			System.out.println("Missing hello");
		}
	}
}



