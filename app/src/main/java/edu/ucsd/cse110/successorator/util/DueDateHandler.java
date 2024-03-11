//package edu.ucsd.cse110.successorator.util;
//
//import java.time.LocalDate;
//import java.util.Date;
//
//import edu.ucsd.cse110.successorator.lib.domain.Task;
//
//public class DueDateHandler implements IDueDateHandler{
//    private LocalDate dueDate;
//
//    public DueDateHandler(){}
//
//    public void setDueDate(LocalDate dueDate) {
//        this.dueDate = dueDate;
//    }
//    public LocalDate getDueDate() {
//        return this.dueDate;
//    }
//
//    public void moveTaskToToday(Task task) {
//        task.setDue(LocalDate.now());
//    }
//
//    public void moveTaskToTomorrow(Task task) {
//        task.setDue(LocalDate.now().plusDays(1));
//    }
//}
