package com.example.exam;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> taskList;
    private List<Task> filteredList;
    private Button addTaskButton, deleteTaskButton, editTaskButton, filterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des vues
        recyclerView = findViewById(R.id.recyclerView);
        addTaskButton = findViewById(R.id.addTaskButton);
        deleteTaskButton = findViewById(R.id.deleteTaskButton);
        editTaskButton = findViewById(R.id.editTaskButton);
        filterButton = findViewById(R.id.filterButton);

        // Initialisation de la liste des tâches et de l'adaptateur
        taskList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new TaskAdapter(filteredList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Gestion de l'événement du bouton pour ajouter une tâche
        addTaskButton.setOnClickListener(v -> showAddTaskDialog());

        // Gestion de l'événement du bouton pour supprimer une tâche
        deleteTaskButton.setOnClickListener(v -> showDeleteTaskDialog());

        // Gestion de l'événement du bouton pour modifier une tâche
        editTaskButton.setOnClickListener(v -> showEditTaskDialog());

        // Gestion de l'événement du bouton de filtrage
        filterButton.setOnClickListener(v -> showFilterDialog());
    }

    // Méthode pour afficher une boîte de dialogue afin d'ajouter une tâche
    private void showAddTaskDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText titleEditText = new EditText(this);
        final EditText descriptionEditText = new EditText(this);
        final EditText dueDateEditText = new EditText(this);

        titleEditText.setHint("Titre de la tâche");
        descriptionEditText.setHint("Description de la tâche");
        dueDateEditText.setHint("Date d'échéance");

        layout.addView(titleEditText);
        layout.addView(descriptionEditText);
        layout.addView(dueDateEditText);

        new AlertDialog.Builder(this)
                .setTitle("Ajouter une tâche")
                .setMessage("Entrez les détails de la tâche")
                .setView(layout)
                .setPositiveButton("Ajouter", (dialog, which) -> {
                    String title = titleEditText.getText().toString();
                    String description = descriptionEditText.getText().toString();
                    String dueDate = dueDateEditText.getText().toString();

                    Task task = new Task(title, description, dueDate);
                    taskList.add(task);
                    filteredList.add(task); // Ajouter la tâche dans la liste filtrée
                    adapter.notifyItemInserted(filteredList.size() - 1);
                    Toast.makeText(MainActivity.this, "Tâche ajoutée", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    // Méthode pour afficher une boîte de dialogue afin de supprimer une tâche
    private void showDeleteTaskDialog() {
        // Demander à l'utilisateur de sélectionner une tâche à supprimer
        new AlertDialog.Builder(this)
                .setTitle("Supprimer une tâche")
                .setMessage("Voulez-vous vraiment supprimer cette tâche ?")
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    if (!taskList.isEmpty()) {
                        // Supprimer la dernière tâche
                        taskList.remove(taskList.size() - 1);
                        filteredList.remove(filteredList.size() - 1); // Supprimer également de la liste filtrée
                        adapter.notifyItemRemoved(filteredList.size());
                        Toast.makeText(MainActivity.this, "Tâche supprimée", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Aucune tâche à supprimer", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    // Méthode pour afficher une boîte de dialogue pour modifier une tâche
    private void showEditTaskDialog() {
        if (!taskList.isEmpty()) {
            Task task = taskList.get(taskList.size() - 1);  // Modifier la dernière tâche

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText titleEditText = new EditText(this);
            titleEditText.setText(task.getTitle());
            final EditText descriptionEditText = new EditText(this);
            descriptionEditText.setText(task.getDescription());
            final EditText dueDateEditText = new EditText(this);
            dueDateEditText.setText(task.getDueDate());

            layout.addView(titleEditText);
            layout.addView(descriptionEditText);
            layout.addView(dueDateEditText);

            new AlertDialog.Builder(this)
                    .setTitle("Modifier la tâche")
                    .setMessage("Modifiez les détails de la tâche")
                    .setView(layout)
                    .setPositiveButton("Modifier", (dialog, which) -> {
                        task.setTitle(titleEditText.getText().toString());
                        task.setDescription(descriptionEditText.getText().toString());
                        task.setDueDate(dueDateEditText.getText().toString());

                        adapter.notifyItemChanged(taskList.indexOf(task));
                        Toast.makeText(MainActivity.this, "Tâche modifiée", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        } else {
            Toast.makeText(MainActivity.this, "Aucune tâche à modifier", Toast.LENGTH_SHORT).show();
        }
    }

    // Méthode pour afficher la boîte de dialogue de filtrage
    private void showFilterDialog() {
        final String[] filterOptions = {"Toutes", "Terminées", "Non terminées"};
        new AlertDialog.Builder(this)
                .setTitle("Filtrer les tâches")
                .setItems(filterOptions, (dialog, which) -> {
                    switch (which) {
                        case 0: // "Toutes"
                            filteredList.clear();
                            filteredList.addAll(taskList);
                            break;
                        case 1: // "Terminées"
                            filteredList.clear();
                            for (Task task : taskList) {
                                if (task.isCompleted()) {
                                    filteredList.add(task);
                                }
                            }
                            break;
                        case 2: // "Non terminées"
                            filteredList.clear();
                            for (Task task : taskList) {
                                if (!task.isCompleted()) {
                                    filteredList.add(task);
                                }
                            }
                            break;
                    }
                    adapter.notifyDataSetChanged();
                })
                .show();
    }
}
