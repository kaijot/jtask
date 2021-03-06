package net.kistowski.jTask.task;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class TaskController {

    private final TaskService taskService;
    TaskDto taskDto;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping("/")
    public String showIndexPage(Model model) {
        Task task = new Task();
        model.addAttribute("tasks", taskService.getAllTasks());
        model.addAttribute("outdatedtasks", taskService.getOutdatedTasks());
        model.addAttribute("newtask", task);

        return "index";
    }

    @RequestMapping(value = "/addtask", method = RequestMethod.POST)
    public String addTask(@ModelAttribute("newtask") @Valid TaskDto taskDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.toString());
            model.addAttribute("tasks", taskService.getAllTasks());

            model.addAttribute("newtask", taskDto);
            return "index";
        } else {
            ModelMapper modelMapper = new ModelMapper();
            Task task = modelMapper.map(taskDto, Task.class);
            model.addAttribute("newTask", taskDto);
            taskService.addTask(task);
            return "redirect:/";
        }


    }

    @RequestMapping(value = "/deletetask/{id}", method = RequestMethod.DELETE)
    public String removeTask(@PathVariable int id) {
        taskService.deleteTask(id);
        return "redirect:/";
    }

    @RequestMapping("/showtasks")
    public String showTasks(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        return "tasks";
    }

    @RequestMapping(value = "/changestatus/{id}", method = RequestMethod.PUT)
    public String changeStatus(@PathVariable int id) {
        taskService.changeStatus(id);
        return "redirect:/";
    }

    @RequestMapping("/edittask/{id}")
    public String editTask(@PathVariable int id, Model model) {
        Optional task = taskService.getTask(id);
        if (task.isPresent()) {
            model.addAttribute("taskToEdit", task);
            return "edittask";
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/doEditTask", method = RequestMethod.PUT)
    public String doEditTask(@ModelAttribute Task task) {
        taskService.editTask(task);
        return "redirect:/";

    }


}
