package com.wasup.mytasks.repository;

import com.wasup.mytasks.model.entity.Task;
import com.wasup.mytasks.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserId(User user);
    List<Task> findByUserIdOrderByDateDesc(User user);

}
