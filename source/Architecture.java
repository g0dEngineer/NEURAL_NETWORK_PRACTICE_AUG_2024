import java.util.ArrayList;

public class Architecture extends ArrayList <Integer>
{
    //features
    private String structure;
    
    //constructor
    public Architecture ( String description )
    {
        this.structure = description;
        
        String [ ] descriptionComponents = description.split ( "," );
        
        for ( int dCI = 0; dCI < descriptionComponents.length; dCI ++ )
            add ( Integer.parseInt ( descriptionComponents [ dCI ] ) );
    }
    
    public String getDescription ( )
    {
        return structure;
    }
}