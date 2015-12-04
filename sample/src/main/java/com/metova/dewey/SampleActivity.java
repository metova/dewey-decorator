package com.metova.dewey;

import com.metova.deweydecoration.DeweyItemDecoration;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DeweyItemDecoration decoration = new DeweyItemDecoration(this, R.layout.dewey_item);
        recyclerView.addItemDecoration(decoration);

        recyclerView.setAdapter(new SampleAdapter(this, getDummyPersonData()));
    }

    private List<Person> getDummyPersonData() {
        List<Person> personList = Arrays.asList(
                new Person("Jason", "Robinson"),
                new Person("Jack", "Sparrow"),
                new Person("George", "Washington"),
                new Person("Richard", "Stevenson"),
                new Person("Bernie", "Sanders"),
                new Person("Sean", "Connery"),
                new Person("Freddy", "Mercury"),
                new Person("Adam", "West"),
                new Person("Mickey", "Mouse"),
                new Person("John", "Cena"),
                new Person("George", "Costanza"),
                new Person("Ricky", "Cervantes"),
                new Person("Robin", "Skouteris"),
                new Person("Jake", "Wharton"),
                new Person("Miley", "Cyrus"),
                new Person("Jules", "Winnfield"),
                new Person("Dave", "McAllister"),
                new Person("Andrew", "Owsley"),
                new Person("Ron", "Burgundy"),
                new Person("Jennifer", "Lawrence"),
                new Person("Rhonda", "Rousey"),
                new Person("Serena", "Williams"),
                new Person("Taylor", "Swift"),
                new Person("Emma", "Watson"),
                new Person("Michelle", "Obama")
        );

        Collections.sort(personList);
        return personList;
    }
}
