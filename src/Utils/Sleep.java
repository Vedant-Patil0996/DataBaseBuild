package Utils;

public class Sleep {
    public Sleep()
    {

    }

    public static void Sleep(long ms)
    {
        try{
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
