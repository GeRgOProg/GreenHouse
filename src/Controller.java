import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;

//Controller implementációja
//Beavatkozások eldöntésére
public class Controller {
    private Driver driver;

    public Controller()
    {
        driver = new Driver();

    }

    private double controlHelper(int param) //Specifikációban megadott táblázat segédfüggvénye
    {
        double returnVal = 0.0;
        switch (param)
        {
            case 0:
            {
                returnVal = 0;
            }break;

            case 20:
            {
                returnVal = 17.3;
            }break;

            case 21:
            {
                returnVal = 18.5;
            }break;

            case 22:
            {
                returnVal =19.7;
            }break;

            case 23:
            {
                returnVal = 20.9;
            }break;

            case 24:
            {
                returnVal = 22.1;
            }break;

            case 25:
            {
                returnVal = 23.3;
            }break;

            case 26:
            {
                returnVal = 24.7;
            }break;

            case 27:
            {
                returnVal =  26.1;
            }break;

            case 28:
            {
                returnVal = 27.5;
            }break;

            case 29:
            {
                returnVal = 28.9;
            }break;

            case 30:
            {
                returnVal = 30.3;
            }break;

            case 31:
            {
                returnVal = 31.9;
            }break;

            case 32:
            {
                returnVal = 33.5;
            }break;

            case 33:
            {
                returnVal = 35.1;
            }break;

            case 34:
            {
                returnVal = 36.7;
            }break;

            case 35:
            {
                returnVal = 38.3;
            }break;

            default:
            {
                returnVal = 0.0;
            }break;
        }
        return returnVal;
    }

    public void control(SensorData data, Greenhouse gh)
    {


        System.out.println(gh.ghId + " üvegház kezelése...");
        if(data.boiler_on)
            System.out.println("Kazánrendszer éppen más utasítást végez el! Üres parancs küldése a felhőnek...");
        if(data.sprinkler_on)
            System.out.println("Locsolórendszer éppen más utasítást végez el! Üres parancs küldése a felhőnek...");
        double sprinklerValue = 0;
        double boilerValue = 0;
        if((data.temperature_act - gh.temperature_min >= 5) || (data.humidity_act - gh.humidity_min >= 20))
        {
            if((data.temperature_act - gh.temperature_min >= 5))
            {
                System.out.println("Hibát észleltünk a kazán rendszerében! Naplózás...");
                try {
                    PrintWriter writer = new PrintWriter("Errors.txt");
                    writer.println("BoilerError - " + data.temperature_act + "C temperature");
                    writer.flush();
                    writer.close();
                } catch (FileNotFoundException e) {
                    System.out.println("Fájl nem található: Errors.txt");
                }

            }

            if(data.humidity_act - gh.humidity_min >= 20)
            {
                System.out.println("Hibát észleltünk az locsolórendszerben! Naplózás...");
                try {
                    PrintWriter writer = new PrintWriter("Errors.txt");
                    writer.println("SprinklerError - " + data.humidity_act + "% humidity");
                    writer.flush();
                    writer.close();
                } catch (FileNotFoundException e) {
                    System.out.println("Fájl nem található: Errors.txt");
                }
            }
        }
        else {
            if (data.boiler_on || data.sprinkler_on) //Valamelyik eszköz éppen utasítást végez el
            {
                boilerValue = 0;
                sprinklerValue = 0;
            } else if (data.boiler_on == false && data.sprinkler_on == false) //Egyik eszköz sem foglalt éppen
            {

                if (data.temperature_act < gh.temperature_min) //Hőemelés szükséges
                {

                    boilerValue = (gh.temperature_opt - data.temperature_act);
                    double humidityOptPercentage = 0;
                    double humidityPercentage = 0;
                    System.out.println("Számolások elvégzése...");
                    humidityPercentage = (controlHelper((int) Math.round(data.temperature_act)) * (data.humidity_act/100)); //16.31
                    humidityOptPercentage = (controlHelper(Math.round(gh.temperature_opt)) * 0.6);
                    sprinklerValue = ((humidityOptPercentage - humidityPercentage)) / 0.01;
                    sprinklerValue = (sprinklerValue * gh.volume) / 1000;
                } else //Hőemelés nem szükséges
                {
                    boilerValue = 0;
                    sprinklerValue = 0;
                }
                System.out.println("Parancs fordítása...");
                //Parancs átadása a drivernek
                int cmdStatus = driver.sendCommand(gh, data.token, boilerValue, sprinklerValue);

                switch (cmdStatus)
                {
                    case 100:
                    {
                        System.out.println("A parancs végrehajtásra került!");

                    }break;

                    case 101:
                    {
                        System.out.println("Hibás kalkuláció!");
                    }break;

                    case 102:
                    {
                        System.out.println("Parancs került kiküldésre egy éppen parancsot végrehajtó eszköznek!");
                    }break;

                    case 103:
                    {
                        System.out.println("Hibás parancs került kiküldésre a kazánnak!");
                    }break;

                    case 104:
                    {
                        System.out.println("Hibás parancs került kiküldésre a locsolónak!");
                    }break;

                    case 105:
                    {
                        System.out.println("Az üzenetben lévő token nem érvényes!");
                    }break;

                    case 106:
                    {
                        System.out.println("Az üzenetben szereplő üvegház nem található!");
                    }break;

                    case 107:
                    {
                        System.out.println("Általános üzenet feldolgozási hiba!");
                    }break;

                    default:
                    {
                        System.out.println("Hiba - parancs nem feldolgozható!");
                    }break;
                }

            }
        }
        System.out.println();
    }
}
