package com.restapi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.restapi.app.pojo.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

	Student findUserByMobileNo(String mobileNo);

}
