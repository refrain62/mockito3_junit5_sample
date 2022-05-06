package mockito3_junit5_sample;
/**
 * テスト対象のクラス
 */
public class Client
{
    private Worker worker = new Worker();

    // 引数あり・戻り値ありメソッドを呼び出す
    public int callAriari(int x)
    {
        // 2倍にした値を Woker#ariari に渡す
        return this.worker.ariari( x * 2 );
    }

    // 引数なし・戻り値なしメソッドを呼び出す
    public int callNasinasi(int x) 
    {
        // Worker#nasinasi を呼び出す
        this.worker.nasinasi();

        // 2倍にした値を返す
        return x * 2;
    }
}
