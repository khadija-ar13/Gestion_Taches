package com.example.exam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflation de la vue de chaque item
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        // Mise à jour des vues avec les informations de la tâche
        holder.title.setText(task.getTitle());
        holder.description.setText(task.getDescription());
        holder.dueDate.setText(task.getDueDate());

        // Affichage du statut de la tâche (Terminée ou Non terminée)
        if (task.isCompleted()) {
            holder.status.setText("Terminée");
        } else {
            holder.status.setText("Non terminée");
        }

        // Mise à jour de l'état de la CheckBox
        holder.checkBox.setChecked(task.isCompleted());

        // Listener pour changer le statut de la tâche lorsque la CheckBox est cochée/décochée
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            // Mise à jour du statut dans l'affichage
            notifyItemChanged(position);
            Toast.makeText(buttonView.getContext(), "Tâche mise à jour", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description, dueDate, status; // Ajout du TextView pour afficher le statut
        public CheckBox checkBox;

        public TaskViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskTitle);
            description = itemView.findViewById(R.id.taskDescription);
            dueDate = itemView.findViewById(R.id.taskDueDate);
            checkBox = itemView.findViewById(R.id.taskCheckBox);
            status = itemView.findViewById(R.id.taskStatus); // TextView pour afficher le statut
        }
    }
}
