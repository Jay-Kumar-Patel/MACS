public class Lab_1 {

    public static void main(String[] args) {
        

        //Create Object of set
        CustomSet set = new CustomSet();


        //Print Set
        System.out.println(set.get());


        //Insertion of set
        set.put(14);
        set.put(54216);
        set.put("Jay");
        set.put(true);
        set.put(10.24537);
        set.put(100);


        //Print Set
        System.out.println(set.get());


        //Check the element is present or not
        System.out.println(set.contains(100));
        System.out.println(set.contains("J"));
        System.out.println(set.contains(77));
        System.out.println(set.contains("Dal"));
        System.out.println(set.contains(false));


        //Print Set
        System.out.println(set.get());


        //Size of set
        System.out.println(set.size());
    }

    
}
