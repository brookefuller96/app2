
package baseline;
public class itemgettersetter {
    private String SerialNumber;
    private String Name;
    private Double Value;



    public String getSerialNumber(){
        return SerialNumber;
    }
    public void setSerialNumber(String SerialNumber){
        this.SerialNumber=SerialNumber;
    }

    public String getName(){
        return Name;
    }
    public void setName(String Name){
        this.Name=Name;
    }


    public void setValue(Double Value){
        this.Value=Value;
    }
    public Double getValue(){
        return Value;
    }

    public itemgettersetter(String SerialNumber, String Name, double Value){
        this.setSerialNumber(SerialNumber);
        this.setName(Name);
        this.setValue(Value);
    }
    @Override
    public String toString(){
        //returns the input from text box to readable string for serial number and name
        return this.getSerialNumber()+ "  " + this.getName() + " "+this.getValue();

    }
}
