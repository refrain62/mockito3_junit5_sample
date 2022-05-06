package mockito3_junit5_sample;
/**
 * モック化対象となるクラス
 */
public class Worker
{
    // 引数あり・戻り値ありメソッド
    public int ariari(int x) {
        throw new IllegalStateException("環境依存で発生するみたいな例外");
    }

    // 引数なし・戻り値なしメソッド
    public void nasinasi() {
        throw new IllegalStateException("環境依存で発生するみたいな例外");
    }
}
