package com.restapi.app.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.restapi.app.pojo.Student;
import com.restapi.app.repository.StudentRepository;
import com.restapi.app.service.StudentService;
@Service
public class CustomDetails implements UserDetailsService {
	@Autowired
	StudentRepository studentRes;
	@Autowired
	StudentService studentService;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String uservailability = studentService.checkMobileNoForReg(username);
		UserDetails userDetails = null;
		if (uservailability != null) {
			Student st = studentRes.findUserByMobileNo(username);
			List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
			GrantedAuthority authority = new SimpleGrantedAuthority(st.getUserrole());
			grantList.add(authority);
		 userDetails = (UserDetails) new User(st.getMobileNo(), st.getStudentpassword(), grantList);
		}
		else {
			
			 throw new UsernameNotFoundException(
		                "Could not find user with this number " + username);
		}
		return userDetails;

	}

}
