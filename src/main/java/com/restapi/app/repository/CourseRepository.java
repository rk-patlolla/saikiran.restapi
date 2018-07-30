package com.restapi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.restapi.app.pojo.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
