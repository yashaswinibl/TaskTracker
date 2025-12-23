import java.io.*;  //filereader,bufferreader

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

class Task{
      public int id;
      public String description;
      public String status;
      private Timestamp createdAt;
      private Timestamp updatedAt;

      public Task(int id, String description, String status) {
          this.id = id;
          this.description = description;
          this.status = status;
          this.createdAt = Timestamp.from(Instant.now());
          this.updatedAt = Timestamp.from(Instant.now());

      }
@Override
      public String toString(){
          return id + "-" + description + "-" + status + "-" + createdAt + "-" + updatedAt;
      }

  }
public class TaskManager {

        private static final String FILE="Task.json";

    public static void main(String[] args){
        if(args.length<1){   // more than one argument thats is define add,up..
            System.out.println("Usage: Java Task Manager [add|update|delete|mark|list] ...");
            return;
        }
        String command=args[0];
        List<Task> tasks=loadTasks();

        switch (command){
            case "add":
                addTask(tasks,args[1]);
                break;
            case "update":
                    updateTask(tasks, Integer.parseInt(args[1]),args[2]);
                    break;
            case "delete":
                deleteTask(tasks, Integer.parseInt(args[1]));
                break;
            case "mark":
                markTask(tasks, Integer.parseInt(args[1]), args[2]);
                break;
            case "list":
                if (args.length == 2) listTasks(tasks, args[1]);
                else listTasks(tasks, null);  //list all
                break;
            default:
                System.out.println("Unknown command.");
        }
        saveTasks(tasks);
    }
    //load task
    private static List<Task> loadTasks(){
        List<Task> tasks=new ArrayList<>();
        try{
            File file = new File(FILE);
            if(!file.exists()){
                return tasks;
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                // Simple parsing: id|description|status
                String[] parts = line.split("\\|");
                tasks.add(new Task(Integer.parseInt(parts[0]), parts[1], parts[2]));
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error loading tasks: " + e.getMessage());

        }
        return tasks;
    }

    //save task
    private static void saveTasks(List<Task> tasks) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE));
            for (Task t : tasks) {
                bw.write(t.id + "|" + t.description + "|" + t.status);
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    //add task
    private static void addTask(List<Task> tasks, String description){
        int id=tasks.size()+1;
        tasks.add(new Task(id, description, "not done"));
        System.out.println("Task added: " + description);
    }
    //update
    private static void updateTask(List<Task> tasks, int id, String newDescription){
      for(Task t : tasks){
          if(t.id==id){
              t.description=newDescription;
              System.out.println("New Task updated");
              return;
          }
      }
        System.out.println("Task not found");
    }
    //delet task
    public static void deleteTask(List<Task> tasks, int id){
        tasks.removeIf(t -> t.id==id);
        System.out.println("Task deleted");

    }
    //mark task
    private static void markTask(List<Task> tasks, int id, String status) {
        for (Task t : tasks) {
            if (t.id == id) {
                t.status = status;
                System.out.println("Task " + id + " marked as " + status);
                return;
            }
        }
        System.out.println("Task not found.");
    }

    private static void listTasks(List<Task> tasks, String filterStatus) {
        for (Task t : tasks) {
            if (filterStatus == null || t.status.equals(filterStatus)) {
                System.out.println(t);
            }
        }
    }
}

