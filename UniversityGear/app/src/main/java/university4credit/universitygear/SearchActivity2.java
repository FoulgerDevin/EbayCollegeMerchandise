package university4credit.universitygear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

public class SearchActivity2 extends Activity {
    ToggleButton button1,button2,button3;
    Button mbutton, mbutton2;
    EditText edit;
    int temp1, temp2, temp3;
    String schoolname = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        button1 = (ToggleButton)findViewById(R.id.toggleButton);
        button2 = (ToggleButton)findViewById(R.id.toggleButton2);
        button3 = (ToggleButton)findViewById(R.id.toggleButton3);
        mbutton = (Button)findViewById(R.id.nextbutton);
        mbutton2 = (Button)findViewById(R.id.nextbutton2);
        edit = (EditText)findViewById(R.id.editText1);

        mbutton2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                schoolname = "";
                if(button1.isChecked()){
                    schoolname+=button1.getText().toString();
                    schoolname+=" ";
                }
                if(button2.isChecked()){
                    schoolname+=button2.getText().toString();
                    schoolname+=" ";
                }
                if(button3.isChecked()){
                    schoolname+=button3.getText().toString();
                    schoolname+=" ";
                }
                Intent intent = new Intent(v.getContext(),SearchActivity.class);
                Log.d("schoolname", schoolname);
                intent.putExtra("asdf",schoolname);
                startActivity(intent);
            }
        });
        mbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),SearchActivity.class);
                Log.d("schoolname2", edit.getText().toString());
                intent.putExtra("asdf",edit.getText().toString());
                startActivity(intent);
            }
        });

    }

}
