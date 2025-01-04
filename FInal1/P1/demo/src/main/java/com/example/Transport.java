package com.example;

class Transport {
    String vehicleID;
    String route;
    int capacity;
    String status; //active|inactive
    
    public String toString(){
        return "Vehicle ID: "+vehicleID+",\n "+"Vehicletype:"+this.getClass().getSimpleName()+"\tthe state:"+status;
    }
    
}



class Bus extends Transport implements WiFiEnabled{

    Boolean hasWiFi;
    Bus(String vehicleID, String route, int capacity, String status, Boolean hasWiFi){
        super.vehicleID = vehicleID;
        super.route = route;
        super.capacity = capacity;
        super.status = status;
        this.hasWiFi = hasWiFi;
    }
    
    public Boolean isWiFiAvailable(){
        return hasWiFi;
    }

}

class Train extends Transport{
    
    int numberOfCarriages;
    String fuelType;            //electric|diesel
    Train(String vehicleID, String route, int capacity, String status, int numberOfCarriages, String fuelType){
        super.vehicleID = vehicleID;
        super.route = route;
        super.capacity = capacity;
        super.status = status;
        this.numberOfCarriages = numberOfCarriages;
        this.fuelType = fuelType;
    }
}

class Metro extends Transport{
    
    Boolean isUnderground;
    Metro(String vehicleID, String route, int capacity, String status, Boolean isUnderground){
        super.vehicleID = vehicleID;
        super.route = route;
        super.capacity = capacity;
        super.status = status;
        this.isUnderground = isUnderground;
    }
}


