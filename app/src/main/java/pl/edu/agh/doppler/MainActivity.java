package pl.edu.agh.doppler;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import pl.edu.agh.doppler.engine.Doppler;


public class MainActivity extends ActionBarActivity {

    private Doppler doppler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = getApplicationContext();

        doppler = Doppler.getDoppler();
        doppler.start();

        doppler.setGestureListener(new Doppler.OnGestureListener() {
            @Override
            public void onPush() {
                Toast.makeText(context, R.string.push, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPull() {
                Toast.makeText(context, R.string.pull, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTap() {
                Toast.makeText(context, R.string.tap, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDoubleTap() {
                Toast.makeText(context, R.string.double_tap, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothing() {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        doppler.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        doppler.start();
    }
}
