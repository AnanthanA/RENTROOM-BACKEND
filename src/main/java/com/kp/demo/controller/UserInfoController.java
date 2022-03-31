package com.kp.demo.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kp.demo.entity.UserInfo;
import com.kp.demo.model.LoginModel;
import com.kp.demo.repository.UserInfoRepository;
import com.kp.demo.util.MD5Util;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserInfoController {
	@Autowired
	UserInfoRepository userInfoRepository;

	@GetMapping("/{id}")
	public ResponseEntity<UserInfo> getUserInfoById(@PathVariable("id") Integer id) {

		Optional<UserInfo> userInfo = userInfoRepository.findById(id);
		if (userInfo.isPresent()) {
			return new ResponseEntity<>(userInfo.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/list")
	public ResponseEntity<List<UserInfo>> getAllUserInfo() {
		try {
			List<UserInfo> userInfoList = userInfoRepository.findAll();
			if (userInfoList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(userInfoList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("check/email/{emailId}")
	public ResponseEntity<UserInfo> isEmailExists(@PathVariable("emailId") String emailId) {
		List<UserInfo> userInfo = userInfoRepository.findByEmailId(emailId);
		if (userInfo.isEmpty())
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/signup")
	public ResponseEntity<UserInfo> signUp(@RequestBody UserInfo model) {
		try {
			model.setPassword(MD5Util.getMD5Hash(model.getPassword()));
			UserInfo userInfo = userInfoRepository.save(model);
			return new ResponseEntity<>(userInfo, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<UserInfo> logIn(@RequestBody LoginModel model) {
		List<UserInfo> userInfoList = userInfoRepository.findByEmailId(model.getEmailId());
		if (userInfoList.isEmpty())
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		final int defaultIndex = 0;
		UserInfo userInfo = userInfoList.get(defaultIndex);
		if (userInfo.getPassword().equals(MD5Util.getMD5Hash(model.getPassword())))
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<UserInfo> updateUserInfo(@PathVariable("id") Integer id, @RequestBody UserInfo model) {
		Optional<UserInfo> existingBook = userInfoRepository.findById(id);
		if (existingBook.isPresent()) {
			UserInfo userInfo = existingBook.get();
			userInfo.setEmailId(model.getEmailId());
			// userInfo.setPassword(model.getPassword());
			userInfo.setMobileNumber(model.getMobileNumber());
			userInfo.setType(model.getType());
			userInfo.setSellerName(model.getSellerName());
			userInfo.setHotelName(model.getHotelName());
			userInfo.setHotelAddress(model.getHotelAddress());
			userInfo.setHotelImageUrl(model.getHotelImageUrl());
			return new ResponseEntity<>(userInfoRepository.save(userInfo), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deleteUserInfo(@PathVariable("id") Integer id) {
		try {
			userInfoRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
