
//It is the interface which will be implemented by main class (WorkingSetTree).
public interface Searchable {

    public boolean add( String key );

    public boolean find( String key );

    public boolean remove( String key );

    public int size();

    public int levelOf( String key );

    public String[] serialize();

    public boolean rebuild( String[] keys );
    
}