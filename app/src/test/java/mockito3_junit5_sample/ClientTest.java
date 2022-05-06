package mockito3_junit5_sample;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

// @Orderアノテーションでテスト順番を指定
@TestMethodOrder( MethodOrderer.OrderAnnotation.class )
// Mockito による JUnit5 エクステンション
@ExtendWith( MockitoExtension.class )
// Please remove unnecessary stubbings or use 'lenient' strictness. More info:javadoc for UnnecessaryStubbingException class. の回避用
@MockitoSettings( strictness = Strictness.LENIENT )
// initMocksメソッドは非推奨で クラスレベルのアノテーションを付与する（デフォルトPER_METHOD）
@TestInstance( Lifecycle.PER_METHOD )
public class ClientTest
{
    // テスト対象のクラスに対してつけるアノテーション
    @InjectMocks
    private Client client = new Client();

    // テスト対象で使用するクラスに対してつけるアノテーション
    @Mock
    private Worker mockedWorker;

    @BeforeAll
    static void initAll() {
        System.out.print("initAll()");
    }

    @BeforeEach
    void init() {
        System.out.print("init()");
    }

    @Test
    @Order( 2 )
    @DisplayName("引数あり_戻り値あり のテスト")
    public void 引数あり_戻り値あり() {
        // モックの振る舞い設定：Woker#ariari に 2 が渡されたときに 6 を返す
        doReturn( 6 ).when( mockedWorker ).ariari( 2 );

        // テスト：Client#callAriari に 1 を渡すと モックの Woker#ariari に 2 を渡して 6 が返ってくる
        assertEquals( 6, client.callAriari( 1 ) );
    }

    @Test
    @Order( 3 )
    @DisplayName("引数なし_戻り値なし のテスト")
    public void 引数なし_戻り値なし() {
        // モックの振る舞い設定：Woker#nasinasi を呼び出したときに何もしない
        doNothing().when( mockedWorker ).nasinasi();

        // テスト：Client#callNasinasi に 1 を渡すとモックの Woker#nasinasi を実行して 2 が返ってくる
        assertEquals( 2,  client.callNasinasi( 1 ) );
    }

    @Test
    @Order( 1 )
    @DisplayName("例外発生 のテスト")
    public void 例外発生() {
        // モックの振る舞い設定：Woker#ariari に 4 が渡されたときに例外を投げる
        doThrow( new IllegalArgumentException("モック例外") ).when( mockedWorker ).ariari( 4 );

        // テスト：Client#callAriari に 2 を渡すと モックの Woker#ariari に 4 を渡して 例外発生
        IllegalArgumentException e = assertThrows( IllegalArgumentException.class, () -> client.callAriari( 2 ) );
        
        // テスト：例外に想定したメッセージが含まれている
        assertEquals( "モック例外", e.getMessage() );
    }

    @Test
    @Order( 4 )
    @DisplayName("順次呼び出し のテスト")
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
