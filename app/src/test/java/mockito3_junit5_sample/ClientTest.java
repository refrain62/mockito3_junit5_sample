package mockito3_junit5_sample;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

// Mockito による JUnit5 エクステンション
@ExtendWith( MockitoExtension.class )
public class ClientTest {
    // モックを注入するオブジェクト
    @InjectMocks
    private Client client = new Client();

    // モック化するオブジェクト
    @Mock
    private Worker mockedWorker;

    @Test
    public void 引数あり_戻り値あり() {
        // モックの振る舞い設定：Woker#ariari に 2 が渡されたときに 6 を返す
        doReturn( 6 ).when( mockedWorker ).ariari( 2 );

        // テスト：Client#callAriari に 1 を渡すと モックの Woker#ariari に 2 を渡して 6 が返ってくる
        assertEquals( 6, client.callAriari( 1 ) );
    }

    @Test
    public void 引数なし_戻り値なし() {
        // モックの振る舞い設定：Woker#nasinasi を呼び出したときに何もしない
        doNothing().when( mockedWorker ).nasinasi();

        // テスト：Client#callNasinasi に 1 を渡すとモックの Woker#nasinasi を実行して 2 が返ってくる
        assertEquals( 2,  client.callNasinasi( 1 ) );
    }

    @Test
    public void 例外発生() {
        // モックの振る舞い設定：Woker#ariari に 4 が渡されたときに例外を投げる
        doThrow( new IllegalArgumentException("モック例外") ).when( mockedWorker ).ariari( 4 );

        // テスト：Client#callAriari に 2 を渡すと モックの Woker#ariari に 4 を渡して 例外発生
        IllegalArgumentException e = assertThrows( IllegalArgumentException.class, () -> client.callAriari( 2 ) );
        
        // テスト：例外に想定したメッセージが含まれている
        assertEquals( "モック例外", e.getMessage() );
    }

    @Test
    public void 順次呼び出し() {
        // モックの振る舞い設定：Woker#ariari に 6 が渡されたときに 例外を２回投げた後 18 を返す
        doThrow( new IllegalArgumentException("モック例外1回目") )
        .doThrow( new IllegalArgumentException("モック例外2回目") )
        .doReturn( 18 )
        .when( mockedWorker )
        .ariari( 6 );

        // テスト：Client#callAriari に 3 を渡すとモックの Worker#ariari に 6 を渡して例外が投げられる
        IllegalArgumentException e1 = assertThrows( IllegalArgumentException.class, () -> client.callAriari( 3 ) );
        assertEquals( "モック例外1回目", e1.getMessage() );

        // テスト：Client#callAriari に 3 を渡すとモックの Worker#ariari に 6 を渡して例外が投げられる
        IllegalArgumentException e2 = assertThrows( IllegalArgumentException.class, () -> client.callAriari( 3 ) );
        assertEquals( "モック例外2回目", e2.getMessage() );

        // テスト：Client#callAriari に 3 を渡すとモックの Worker#ariari に 6 を渡して 18 が返ってくる
        assertEquals( 18, client.callAriari( 3 ) );
    }
}