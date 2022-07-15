package com.example.todolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static ArrayList<Task> tasks;
    private ListView listView;
    private static DataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DataBase(this);
        tasks=db.getData();

        listView= (ListView) findViewById(R.id.lista);
        myAdapter a = new myAdapter(this,(R.layout.lista_layout),tasks);
        listView.setAdapter(a);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int ida = tasks.get(position).getId();
                db.delete(ida);
                tasks.remove(position);
                myAdapter a = new myAdapter(MainActivity.this,(R.layout.lista_layout),tasks);
                listView.setAdapter(a);
                Button b = (Button) findViewById(R.id.btn);

                if(tasks.size() <=6){
                    b.setAlpha(1.0f);
                }
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog d = new Dialog(MainActivity.this);
                d.setCancelable(false);
                d.setContentView(R.layout.dialog);
                d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                EditText nameF =(EditText) d.findViewById(R.id.nema);
                nameF.setText(tasks.get(position).getName());
                EditText dateF =(EditText) d.findViewById(R.id.deta);
                dateF.setText(tasks.get(position).getDate().getDate());
                Button add = (Button) d.findViewById(R.id.seta);
                add.setText("Update");
                Button cancel=(Button) d.findViewById(R.id.cancela);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(nameF.getText().length()>0 && dateF.getText().length()>0){
                            String name= nameF.getText().toString();

                            if (isValidDate(dateF.getText().toString())){
                                Date a = getDate(dateF.getText().toString());
                                Task w;
                                if (tasks.size()>0){
                                    w = new Task(tasks.get(tasks.size()-1).getId()+1,name , a,false);
                                }else {
                                    w = new Task(0,name , a,false);
                                }
                                if (w!=null){
                                    int id = tasks.get(position).getId();
                                    db.update(id,w);
                                    tasks.set(position,w);
                                    myAdapter a2 = new myAdapter(MainActivity.this,(R.layout.lista_layout),tasks);
                                    listView.setAdapter(a2);
                                    d.dismiss();
                                }
                            }else {
                                Toast.makeText(MainActivity.this,"Date is not valid !!!",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(MainActivity.this,"Fill The Blanks Please",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                d.show();
            }
        });
        actions();
    }
    private void actions(){
        btn_action();
    }
    public static DataBase getDb(){
        return db;
    }
    private void btn_action(){
        Button b = (Button) findViewById(R.id.btn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog d = new Dialog(MainActivity.this);
                d.setCancelable(false);
                d.setContentView(R.layout.dialog);
                d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                EditText nameF =(EditText) d.findViewById(R.id.nema);
                EditText dateF =(EditText) d.findViewById(R.id.deta);
                Button add = (Button) d.findViewById(R.id.seta);
                Button cancel=(Button) d.findViewById(R.id.cancela);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(nameF.getText().length()>0 && dateF.getText().length()>0){
                            String name= nameF.getText().toString();

                            if (isValidDate(dateF.getText().toString())){
                                Date a = getDate(dateF.getText().toString());
                                Task w;
                                if (tasks.size()>0){
                                     w = new Task(tasks.get(tasks.size()-1).getId()+1,name , a,false);
                                }else {
                                     w = new Task(1,name , a,false);
                                }
                                if (w!=null){
                                    add(w);
                                    d.dismiss();
                                }
                            }else {
                                Toast.makeText(MainActivity.this,"Date is not valid !!!",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(MainActivity.this,"Fill The Blanks Please",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                d.show();
            }
        });
    }
    private Date getDate(String a){
        String day="",month="",year="",S_counter="";
        int counter=0;
        for (int i =0 ; i<a.length();i++){
            if (a.charAt(i) != '/'){
                S_counter=S_counter+a.charAt(i);
            }
            if (a.charAt(i) == '/'){
                counter++;
                if (counter == 1){
                    day=S_counter;
                    S_counter="";
                }else if(counter ==2){
                    month=S_counter;
                    S_counter="";
                    for (int y =i+1 ; y<a.length();y++){
                        S_counter=S_counter+a.charAt(y);
                    }
                    year=S_counter;
                    break;
                }
            }

        }
        return new Date(Integer.parseInt(day),Integer.parseInt(month),Integer.parseInt(year));
    }
    private boolean isValidDate(String a){
      String day="",month="",year="",S_counter="";
      boolean b=false,c,d=false;
      int counter=0;
      for (int i =0 ; i<a.length();i++){
            if (a.charAt(i) != '/'){
                S_counter=S_counter+a.charAt(i);
            }
          if (a.charAt(i) == '/'){
              counter++;
              if (counter == 1){
                  day=S_counter;
                  S_counter="";
              }else if(counter ==2){
                  month=S_counter;
                  S_counter="";
                  b=true;
                  for (int y =i+1 ; y<a.length();y++){
                      S_counter=S_counter+a.charAt(y);
                  }
                  year=S_counter;
                  break;
              }
          }

      }

      c=((day.length() >=1 && day.length()<=2) && isNumber(day)) &&(month.length() >=1 && month.length()<=2) && isNumber(month)&&
        (year.length() ==4 ) && isNumber(day);
      if (c){
          int dayI=Integer.parseInt(day);
          int monthI=Integer.parseInt(month);
          int yearI = Integer.parseInt(year);
          d=(dayI>0 && dayI<31) && (monthI>0 && monthI<13) && (yearI>2021);
      }

      return b && c && d;
    }
    private boolean isNumber(String a){
        boolean t = true;
        for (int i =0 ; i<a.length();i++){
            if(!isNumericChar(a.charAt(i))){
                t=false;
                break;
            }
        }
        return t;
    }
    private boolean isNumericChar(char a){
        return a>='0' && a<='9';
    }
    private void add(Task t){
        Button button = (Button) findViewById(R.id.btn);
        db.insert(t);
        tasks.add(t);
        myAdapter a = new myAdapter(MainActivity.this,(R.layout.lista_layout),tasks);
        listView.setAdapter(a);
        if (tasks.size() > 6){
            button.setAlpha(0.5f);
        }
    }
    public static  ArrayList<Task> getItems(){
        return tasks;
    }
}
class myAdapter extends ArrayAdapter<Task> {
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    private static class ViewHolder {
        TextView name;
        TextView date;
        TextView no_or_yes;
        RelativeLayout r;
    }

    public myAdapter(Context context, int resource, ArrayList<Task> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        Date date = getItem(position).getDate();
        boolean Done = getItem(position).isDone();

        Task task = new Task(name, date);
        task.setDone(Done);
        final View result;

        ViewHolder holder;


        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name_here);
            holder.date = (TextView) convertView.findViewById(R.id.date_here);
            holder.no_or_yes = (TextView) convertView.findViewById(R.id.txta_here);
            holder.r=(RelativeLayout) convertView.findViewById(R.id.father_here);


            result = convertView;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        holder.name.setText(String.valueOf(task.getName()));
        holder.date.setText(task.getDate().getDate());
        if (MainActivity.getItems().get(position).isDone()){
            holder.no_or_yes.setText("✔");
        }else {
            holder.no_or_yes.setText("    ");
        }

        holder.r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.no_or_yes.getText().toString().equals("✔")){
                    holder.no_or_yes.setText("    ");
                    Task wr= MainActivity.getItems().get(position);
                    MainActivity.getDb().updateStatues(wr.getId(),wr);
                    MainActivity.getItems().get(position).setDone(false);
                }else {
                    holder.no_or_yes.setText("✔");
                    Task wr= MainActivity.getItems().get(position);
                    MainActivity.getDb().updateStatues(wr.getId(),wr);
                    MainActivity.getItems().get(position).setDone(true);
                }
            }
        });

        return convertView;
    }
}