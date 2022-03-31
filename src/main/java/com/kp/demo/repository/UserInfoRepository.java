package com.kp.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kp.demo.entity.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
	List<UserInfo> findByEmailId(String email);
}