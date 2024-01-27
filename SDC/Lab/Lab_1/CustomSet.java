import java.util.ArrayList;

public class CustomSet {


    ArrayList<Object> list;

    
    //Default Constructor (Initialize our arraylist which I used to implement Set)
    public CustomSet(){
        list = new ArrayList<>();
    }


    //The "put" method will insert the element into our set. This is a generic method, 
    //so we first need to check if the list is empty. If it is, we can directly append the element. 
    //However, if the list contains any elements, we must check the data type of the element we want 
    //to insert against the data type of the elements already present in the list. If they are the same, 
    //then we can insert the element; otherwise, we should return false.
     
    public boolean put(Object obj){

        if(obj==null || obj.toString().trim().equals(""))
        {
            return false;
        }

        if(list.size() == 0){
            list.add(obj);
            return true;
        }
        else{
            if (list.get(0).getClass().equals(obj.getClass())) {
                list.add(obj);
                return true;
            }
        }

        return false;
    }






    //Return the size of the list
    public int size(){
        return list.size();
    }






    
    //This method is also generic; it only checks whether the element we have is present in our set or not.
    //If either list is empty or the data type of the element we have is not the same as the element present in the set in both conditions, just return false."
    public boolean contains(Object obj){

        if(list.size() == 0 || !list.get(0).getClass().equals(obj.getClass()))
        {
            return false;
        }

        for(Object currObj : list){
            if (currObj == obj) {
                return true;
            }
        }
        return false;
    }




    
    //This method returns the entire set data structure so we can check all the elements at once.
    public String get(){
        return String.valueOf(list);
    }
    
}
