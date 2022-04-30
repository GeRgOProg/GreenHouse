

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
//Driver implementáció
//Vezérlésekre és fordításokra
public class Driver implements IDriver {

    @Override
    public int sendCommand(Greenhouse gh, String token, double boilerValue, double sprinklerValue) {

        if (token != null) {
            String boilerCommand = "";
            String sprinklerCommand = "";
            String ghId = gh.ghId;
            if (boilerValue != 0)
                boilerCommand = "bup" + Math.round(boilerValue) + "c";
            if (sprinklerValue != 0)
                sprinklerCommand = "son" + Math.round(sprinklerValue) + "l";
            Command cmd = new Command(ghId, boilerCommand, sprinklerCommand);
            if(boilerCommand != "")
                System.out.println("Parancs definiálása a kazán számára - " + boilerCommand);
            if(sprinklerCommand != "")
                System.out.println("Parancs definiálása a locsoló számára - " + sprinklerCommand);
            System.out.println("Parancs elküldése a felhőnek...");
            return cmd.send(token);
        }
        return 105;
    }
}
