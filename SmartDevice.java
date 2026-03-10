public class SmartDevice {
   
    String name;
    boolean isOn;


    public SmartDevice(String name) {
        this.name = name;
        this.isOn = false;
    }

    // Method (Behavior)
    public void togglePower() {
        isOn = !isOn;
        String status = isOn ? "ON" : "OFF";
        System.out.println(name + " is now " + status);
    }

    public static void main(String[] args) {
        // Creating an instance (Object) of the class
        SmartDevice livingRoomLight = new SmartDevice("Living Room Light");
        
        livingRoomLight.togglePower(); 
        livingRoomLight.togglePower(); 
    }
}