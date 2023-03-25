package my.edu.utar.individualassignment7;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        TextView tvCM = findViewById(R.id.tvContextMenu);
        registerForContextMenu(tvCM);

    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,v,menuInfo);
        if(v.getId() == R.id.tvContextMenu){
            getMenuInflater().inflate(R.menu.main,menu);
            menu.add(0,1,0,"Easy");
            menu.add(0,2,0,"Medium");
            menu.add(0,3,0,"Hard");
            menu.setHeaderIcon(android.R.drawable.ic_media_play).setHeaderTitle("Settings");
        }
    }

    public boolean onContextItemSelected(MenuItem item){
        super.onContextItemSelected(item);
        TextView tv = (TextView)findViewById(R.id.tvResult);
        switch(item.getItemId()){
            case 1: tv.setText("You have choosen Easy"); break;
            case 2: tv.setText("You have choosen Medium"); break;
            case 3: tv.setText("You have choosen Hard"); break;};
        return true;
    }
}