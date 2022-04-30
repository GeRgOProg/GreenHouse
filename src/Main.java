


//2021/22/2 Szoftvermodellezés és tesztelés

import java.util.ArrayList;
import java.util.List;


//Csapat: 32
//Keresztúri Gergő, Kozma Henrietta, Balla Patrik, Molnár Ákos

//Green House projekt
public class Main {

    public static void main(String[] args) {
        System.out.println("Üdvözlöm az okos üvegház-kezelő alkalmazásban!");
        System.out.println("Loader modul elindítása és adatok betöltése...");
       Loader loader = new Loader("greenhouses.json");
       GreenHouseList greenhouseList = loader.loadGreenHouses();
       List<Greenhouse> greenhouses = greenhouseList.getGreenhouseList();
        Monitor monitorService = new Monitor();
        Controller controller = new Controller();
        System.out.println("Üvegházak adatai:");
        for(int i = 0; i < greenhouses.size(); i++)
        {
            greenhouses.get(i).print();
            System.out.println();
        }
        System.out.println("Szenzorok állapotának lekérdezése és parancsok végrehajtása:");
        for(int i = 0; i < greenhouses.size(); i++)
        {
            SensorData data = monitorService.getSensorData(greenhouses.get(i).ghId);
            controller.control(data, greenhouses.get(i));
        }
        System.out.println("Program vége - Viszontlátásra!");
    }
    
}
